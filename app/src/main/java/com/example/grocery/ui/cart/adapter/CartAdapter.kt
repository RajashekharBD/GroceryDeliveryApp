package com.example.grocery.ui.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.databinding.ItemCartBinding
import com.example.grocery.model.CartItem

class CartAdapter(
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemoveClick: (CartItem) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CartItem>() {
            override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem.productId == newItem.productId
            }

            override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            Glide.with(binding.productImageView)
                .load(cartItem.productImageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.productImageView)

            binding.productNameTextView.text = cartItem.productName
            binding.quantityTextView.text = cartItem.quantity.toString()
            binding.priceTextView.text = "₹${"%.2f".format(cartItem.productPrice)}"

            binding.increaseButton.setOnClickListener {
                onQuantityChange(cartItem, cartItem.quantity + 1)
            }

            binding.decreaseButton.setOnClickListener {
                if (cartItem.quantity > 1) {
                    onQuantityChange(cartItem, cartItem.quantity - 1)
                }
            }

            binding.removeButton.setOnClickListener {
                onRemoveClick(cartItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}