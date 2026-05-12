package com.example.grocery.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.grocery.R
import com.example.grocery.databinding.ItemProductBinding
import com.example.grocery.model.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.productNameTextView.text = product.name
            binding.priceTextView.text = "₹${product.price}"
            binding.originalPriceTextView.text = "₹${product.originalPrice}"
            binding.originalPriceTextView.paintFlags = binding.originalPriceTextView.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            binding.discountBadgeTextView.text = "${product.discount}% OFF"
            binding.ratingBar.rating = product.rating
            binding.stockStatusTextView.text = if (product.isInStock) "In Stock" else "Out of Stock"
            binding.stockStatusTextView.setTextColor(
                if (product.isInStock) 
                    android.graphics.Color.GREEN 
                else 
                    android.graphics.Color.RED
            )

            Glide.with(binding.productImageView)
                .load(product.imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.productImageView)

            binding.root.setOnClickListener {
                onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}