package dllhell.familybudget.ui.fragment.history

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.Query
import dllhell.familybudget.R
import dllhell.familybudget.data.models.Expense
import kotlinx.android.synthetic.main.item_expense.view.*
import java.text.SimpleDateFormat
import java.util.*

internal class HistoryAdapter(
        query: Query
) : FirebaseRecyclerAdapter<Expense, HistoryAdapter.ViewHolder>(Expense::class.java, R.layout.item_expense, ViewHolder::class.java, query) {

    companion object {
        private val DATE_FORMAT = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault())
    }

    override fun populateViewHolder(viewHolder: ViewHolder, model: Expense, position: Int) {
        val ref = getRef(position)

        val expenseKey = ref.key
        viewHolder.itemView.setOnClickListener({

        })

        viewHolder.bind(model)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.tvDate
        val titleTextView: TextView = itemView.tvTitle
        val sumTextView: TextView = itemView.tvSum

        fun bind(expense: Expense) {
            dateTextView.text = DATE_FORMAT.format(expense.date)
            titleTextView.text = expense.category
            sumTextView.text = String.format("%.2f", expense.sum)
        }
    }
}