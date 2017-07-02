package dllhell.familybudget.presentation.addexpense

import com.arellomobile.mvp.InjectViewState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dllhell.familybudget.data.models.Expense
import dllhell.familybudget.presentation.BasePresenter
import ru.terrakok.cicerone.Router
import java.util.*
import javax.inject.Inject

@InjectViewState
class AddExpensePresenter @Inject
constructor(private val router: Router) : BasePresenter<AddExpenseView>() {

    fun onSaveClick(date: Date, sum: Double, category: String) {
        val database = FirebaseDatabase.getInstance()

        val post = Expense("", date, sum, category, "")
        database.reference.child("expenses")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .push()
                .setValue(post)
                .addOnSuccessListener { router.exit() }
                .addOnFailureListener { router.showSystemMessage("Error") }
    }
}
