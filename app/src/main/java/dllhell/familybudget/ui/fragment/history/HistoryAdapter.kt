package dllhell.familybudget.ui.fragment.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import dllhell.familybudget.R
import dllhell.familybudget.data.models.Expense
import kotlinx.android.synthetic.main.item_expense.view.*
import java.text.SimpleDateFormat
import java.util.*

internal class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault())
    }

    private var items = emptyList<Expense>()

    fun setItems(items: List<Expense>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_expense, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.tvDate
        val titleTextView: TextView = itemView.tvTitle
        val sumTextView: TextView = itemView.tvSum

        fun bind(expense: Expense) {
            dateTextView.text = DATE_FORMAT.format(expense.date)
            titleTextView.text = "TODO"
            sumTextView.text = expense.sum.toString()
        }
    }
}