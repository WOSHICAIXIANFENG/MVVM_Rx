package com.cai.demo.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cai.demo.R
import com.cai.demo.model.Museum
import kotlinx.android.synthetic.main.item_museum.view.museum_img
import kotlinx.android.synthetic.main.item_museum.view.museum_name
import kotlinx.android.synthetic.main.item_museum_img.view.mu_img

class MuseumAdapter(private var museums: MutableList<Museum>) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

  private companion object {
    private const val ITEM_IMG = 0
    private const val ITEM_TEXT_IMG = 1
  }

  private var museumListFull: List<Museum> = museums

  override fun getItemViewType(position: Int): Int {
    return if (position % 2 == 0) ITEM_TEXT_IMG else ITEM_IMG
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      ITEM_IMG -> {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_museum_img, parent, false)
        MViewHolder1(v)
      }
      ITEM_TEXT_IMG -> {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_museum, parent, false)
        MViewHolder2(v)
      }
      else -> {
        throw IllegalStateException("Unknown Item type")
      }
    }
  }

  override fun getItemCount(): Int {
    return museums.size
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val museum = museums[position]
    when (getItemViewType(position)) {
      ITEM_IMG -> {
        Glide.with((holder as MViewHolder1).img.context).load(museum.imgUrl).into(holder.img)
      }
      ITEM_TEXT_IMG -> {
        val viewHolder2 = holder as MViewHolder2
        Glide.with(viewHolder2.itemView).load(museum.imgUrl).into(viewHolder2.img)
        viewHolder2.name.text = museum.name
      }
    }
  }

  override fun getFilter(): Filter {
    return myFilter
  }

  private val myFilter = object : Filter() {
    override fun performFiltering(constraint: CharSequence?): FilterResults {
      return if (constraint == null || constraint.isEmpty()) {
        FilterResults().apply {
          values = museumListFull
        }
      } else {
        FilterResults().apply {
          values = museumListFull.filter { it.name.contains(constraint.toString(), true) }
        }
      }
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
      results?.values?.let {
        museums.clear()
        museums.addAll(it as List<Museum>)
        notifyDataSetChanged() //!!!
      }
    }
  }

  fun update(newData: List<Museum>) {
    museums.clear()
    museums.addAll(newData)
    museumListFull = museums.toList()
    notifyDataSetChanged()
  }

  private class MViewHolder1(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.mu_img
  }

  private class MViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
    val img: ImageView = view.museum_img
    val name: TextView = view.museum_name
  }
}