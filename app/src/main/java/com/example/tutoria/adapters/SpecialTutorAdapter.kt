package com.example.tutoria.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tutoria.data.SpecialTutor
import com.example.tutoria.databinding.SpecialRvItemBinding

class SpecialTutorAdapter: RecyclerView.Adapter<SpecialTutorAdapter.SpecialTutorViewHolder>() {
    inner class SpecialTutorViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(specialTutor: SpecialTutor) {
            binding.apply {
                Glide.with(itemView).load(specialTutor.image).into(imageSpecialRvItem)
                tvSpecialProductName.text = specialTutor.name
                tvSpecialProductPrice.text = specialTutor.price.toString()
                btnAddToCart
            }
        }
    }
    private val diffCallback = object : DiffUtil.ItemCallback<SpecialTutor>() {
            override fun areItemsTheSame(oldItem: SpecialTutor, newItem: SpecialTutor): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SpecialTutor, newItem: SpecialTutor): Boolean {
                return oldItem == newItem
            }
        }
    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialTutorViewHolder {
        return SpecialTutorViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialTutorViewHolder, position: Int) {
        val specialTutor = differ.currentList[position]
        holder.bind(specialTutor)
    }
}