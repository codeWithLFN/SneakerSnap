package com.example.sneakersnap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SneakerAdapter(private val context: Context, private val sneakers: MutableList<Sneaker>) : BaseAdapter() {
    override fun getCount(): Int {
        return sneakers.size
    }

    override fun getItem(position: Int): Any {
        return sneakers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.sneaker_item, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val sneaker = sneakers[position]
        holder.bind(sneaker)

        return view
    }

    inner class ViewHolder(view: View) {
        private val nameTextView: TextView = view.findViewById(R.id.txtSneakerName)
        private val brandTextView: TextView = view.findViewById(R.id.txtSneakerBrand)

        fun bind(sneaker: Sneaker) {
            nameTextView.text = sneaker.name
            brandTextView.text = sneaker.brand
        }
    }

    fun updateSneakers(sneakers: List<Sneaker>) {
        this.sneakers.clear()
        this.sneakers.addAll(sneakers)
        notifyDataSetChanged()
    }
}
