package com.example.ilerimobilfinalapp.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ilerimobilfinalapp.R

class AppAdapter(
    private val movieList: List<MovieEntity>,
    private val cardClick: (MovieEntity) -> Unit
) : RecyclerView.Adapter<AppAdapter.ViewHolder>(), Filterable {
    private var list = listOf<MovieEntity>()

    init {
        list = movieList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false),
            cardClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View, private val cardClick: (MovieEntity) -> Unit) :
        RecyclerView.ViewHolder(view) {

        fun bindData(movie: MovieEntity) {
            with(movie) {
                Glide
                    .with(itemView.context)
                    .load(movie.poster)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(itemView.findViewById(R.id.image))

                itemView.findViewById<TextView>(R.id.title).text = "${movie.id}: ${movie.name}"
                itemView.findViewById<TextView>(R.id.body).text = movie.playtime

                itemView.setOnClickListener { cardClick(this) }
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()

                if (charSearch.isEmpty()) {
                    list = movieList
                } else {
                    val result = ArrayList<MovieEntity>()
                    for (item in movieList) {
                        if (item.name.contains(charSearch)) {
                            result.add(item)
                        }
                    }
                    list = result
                }

                return FilterResults().apply { values = list }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                list = results?.values as List<MovieEntity>
                notifyDataSetChanged()
            }
        }
    }

    fun sort(type: Int) {
        when (type) {
            0 -> list = list.sortedBy { it.name }
            1 -> list = list.sortedByDescending { it.name }
            2 -> list = list.sortedBy { it.id }
            3 -> list = list.sortedByDescending { it.id }
        }
        notifyDataSetChanged()
    }
}