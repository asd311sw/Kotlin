package com.example.aop_part3_chapter02

import android.annotation.SuppressLint
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class QuotesPagerAdapter(
    val quotes: List<Quote>,
    val isNameRevealed:Boolean
) : RecyclerView.Adapter<QuotesPagerAdapter.QuoteViewHolder>() {
    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val quoteTextView = itemView.findViewById<TextView>(R.id.quoteTextView)
        val nameTextView = itemView.findViewById<TextView>(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.ltem_quote, parent, false)
        return QuoteViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val actualPosition = position % quotes.size

        holder.quoteTextView.text = "\"${quotes[actualPosition].quote}\""

        if(isNameRevealed) {
            holder.nameTextView.text = "-${quotes[actualPosition].name}-"
            holder.nameTextView.isVisible = true
        }
        else
            holder.nameTextView.isVisible = false
    }



    override fun getItemCount() = Int.MAX_VALUE
}