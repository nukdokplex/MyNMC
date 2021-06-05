package ru.nukdotcom.mynmc.helpers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.models.news.NewsElement
import java.lang.Exception

class NewsRecyclerViewAdapter(private val news: ArrayList<NewsElement>): RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var date: TextView = itemView.findViewById(R.id.date)
        var title: TextView = itemView.findViewById(R.id.title)
        var excerpt: TextView = itemView.findViewById(R.id.excerpt)
        var button: Button = itemView.findViewById(R.id.viewPostButton)

        public fun bind(item: NewsElement){
            if (item.time != null) date.text = item.time else date.text = "N/A"
            if (item.title != null) title.text = item.title else title.text = "N/A"
            if (item.excerpt != null) excerpt.text = item.excerpt else excerpt.text = "N/A"

            button.setOnClickListener {
                try{
                    var url: String = "http://nmt.edu.ru/"
                    if (item.url != null) url = item.url
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://nmt.edu.ru"+url))
                    itemView.context.startActivity(browserIntent)
                }
                catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(itemView.context, "Произошла ошибка при попытке открыть веб-страницу. Возможно, на Вашем устройстве не установлен браузер.", Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(news[position])
    }

    override fun getItemCount(): Int {
        return news.count()
    }

    fun addItems(items: List<NewsElement>){
        news.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems(){
        news.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: NewsElement){
        news.add(item)
        notifyDataSetChanged()
    }

    fun setItems(items:ArrayList<NewsElement>){
        news.clear()
        news.addAll(items)
        notifyDataSetChanged()
    }
}