package com.example.epfcinit

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EPFCListAdapter(var dataset:MutableList<EPFCData>): RecyclerView.Adapter<EPFCListAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var cNameView:TextView = itemView.findViewById(R.id.nameTextView)
        var cNumView:TextView = itemView.findViewById(R.id.numberTextView)
        var dateView:TextView = itemView.findViewById(R.id.dateTextView)
        val kwhView:TextView = itemView.findViewById(R.id.stringValue1)
        val leadView:TextView = itemView.findViewById(R.id.stringValue2)
        val lagView:TextView = itemView.findViewById(R.id.stringValue3)

//        init {
//            cNameView = view.findViewById(R.id.nameTextView)
//            cNumView = view.findViewById(R.id.numberTextView)
//            dateView = view.findViewById(R.id.dateTextView)
//            kwhView = view.findViewById(R.id.stringValue1)
//            leadView = view.findViewById(R.id.stringValue2)
//            lagView = view.findViewById(R.id.stringValue3)
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EPFCListAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.epfc_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EPFCListAdapter.ViewHolder, position: Int) {
        val currItem = dataset[position]
        Log.d(currItem.toString(), currItem.toString())
        holder.cNameView.text = currItem.custName.toString()
        holder.cNumView.text = currItem.custNum.toString()
        holder.dateView.text = currItem.date.toString()
        holder.kwhView.text = currItem.kwh.toString()
        holder.leadView.text = currItem.lead.toString()
        holder.lagView.text = currItem.lag.toString()
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(newDataset: MutableList<EPFCData>){
        dataset = newDataset
        notifyDataSetChanged()
    }
}