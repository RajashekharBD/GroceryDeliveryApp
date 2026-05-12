package com.example.grocery.ui.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.data.CartDatabase
import com.example.grocery.databinding.FragmentCheckoutBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        val paymentModes = arrayOf("Cash on Delivery", "Online Payment")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, paymentModes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.paymentModeSpinner.adapter = adapter

        loadCartSummary()

        binding.placeOrderButton.setOnClickListener {
            val address = binding.addressEditText.text.toString().trim()
            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    CartDatabase.getInstance(requireContext()).cartDao().deleteAllCartItems()
                }
            }
            findNavController().navigate(R.id.action_checkoutFragment_to_successFragment)
        }
    }

    private fun loadCartSummary() {
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) {
                CartDatabase.getInstance(requireContext()).cartDao().getAllCartItems()
            }
            val totalItems = items.sumOf { it.quantity }
            val totalAmount = items.sumOf { it.quantity * it.productPrice }
            val fee = if (totalAmount >= 500) 0.0 else 40.0

            binding.cartItemsTextView.text = "Items: $totalItems"
            binding.checkoutTotalTextView.text = "Total: ₹${"%.2f".format(totalAmount)}"
            binding.checkoutDeliveryFeeTextView.text = "Delivery: ₹${"%.2f".format(fee)}"
            binding.checkoutFinalAmountTextView.text = "Amount Payable: ₹${"%.2f".format(totalAmount + fee)}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}