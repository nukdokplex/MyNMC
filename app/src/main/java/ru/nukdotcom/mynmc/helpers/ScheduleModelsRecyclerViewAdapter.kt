package ru.nukdotcom.mynmc.helpers

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.nukdotcom.mynmc.R
import ru.nukdotcom.mynmc.models.news.NewsElement
import ru.nukdotcom.mynmc.models.schedule_model.Model
import java.lang.Exception

class ScheduleModelsRecyclerViewAdapter(private val models: ArrayList<Model>, public val model_type: String): RecyclerView.Adapter<ScheduleModelsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val button: Button = itemView.findViewById(R.id.gotoModelScheduleButton)

        @SuppressLint("SetTextI18n")
        public fun bind(item: Model){
            if (item.name == null || item.id == null){
                button.visibility = View.GONE
                return
            }
            button.visibility = View.VISIBLE
            button.text = "${item.name} <${item.id}>"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.schedule_model_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(models[position])
    }

    override fun getItemCount(): Int {
        return models.count()
    }

    fun addItems(items: List<Model>){
        models.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems(){
        models.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: Model){
        models.add(item)
        notifyDataSetChanged()
    }

    fun setItems(items:ArrayList<Model>){
        models.clear()
        models.addAll(items)
        notifyDataSetChanged()
    }
}