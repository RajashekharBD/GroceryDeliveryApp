package com.example.grocery.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery.R
import com.example.grocery.databinding.FragmentCartBinding
import com.example.grocery.ui.cart.adapter.CartAdapter
import com.example.grocery.ui.cart.viewmodel.CartViewModel

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CartViewModel by viewModels()

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()
        setupSwipeToDelete()
        setupCheckoutButton()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupCheckoutButton() {
        binding.checkoutButton.setOnClickListener {
            val cartItems = viewModel.cartItems.value
            if (cartItems.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChange = { cartItem, newQuantity ->
                viewModel.updateQuantity(cartItem, newQuantity)
            },
            onRemoveClick = { cartItem ->
                viewModel.removeFromCart(cartItem)
            }
        )
        binding.cartRecyclerView.adapter = cartAdapter
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            cartAdapter.submitList(cartItems)
            updateCartSummary()
        }
    }

    private fun updateCartSummary() {
        val cartItems = viewModel.cartItems.value ?: return
        
        val totalItems = cartItems.sumOf { it.quantity }
        val totalAmount = cartItems.sumOf { it.quantity * it.productPrice }
        val savings = totalAmount * 0.15
        val deliveryFee = if (totalAmount >= 500) 0.0 else 40.0
        finalAmount = totalAmount - savings + deliveryFee

        binding.totalItemsTextView.text = "$totalItems Items"
        binding.totalAmountTextView.text = "₹${"%.2f".format(totalAmount)}"
        binding.savingsTextView.text = "₹${"%.2f".format(savings)}"
        binding.deliveryFeeTextView.text = "₹${"%.2f".format(deliveryFee)}"
        binding.finalAmountTextView.text = "₹${"%.2f".format(finalAmount)}"
    }

    private var finalAmount = 0.0

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val cartItem = cartAdapter.currentList[position]
                viewModel.removeFromCart(cartItem)
                Toast.makeText(requireContext(), "Item removed from cart", Toast.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.cartRecyclerView)
    }
}