package com.example.grocery.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.grocery.R
import com.example.grocery.databinding.FragmentSuccessBinding

class SuccessFragment : Fragment() {

    private var _binding: FragmentSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Generate a random order ID for demonstration
        val orderId = "ORD" + System.currentTimeMillis().toString().substring(6)
        val deliveryTime = "Today, 6:00 PM - 8:00 PM"

        binding.orderIdTextView.text = "Order ID: $orderId"
        binding.deliveryTimeTextView.text = "Expected delivery: $deliveryTime"

        binding.continueShoppingButton.setOnClickListener {
            // Navigate to home fragment
            findNavController().navigate(R.id.action_successFragment_to_homeFragment)
        }

        binding.trackOrderButton.setOnClickListener {
            Toast.makeText(requireContext(), "Order tracking feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}