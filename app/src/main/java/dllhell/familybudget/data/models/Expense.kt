package dllhell.familybudget.data.models

import java.util.*

data class Expense(val date: Date, val sum: Long) {

    fun toMap(): Map<String, Any> {
        return mapOf("date" to date, "sum" to sum)
    }
}
