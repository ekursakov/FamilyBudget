package dllhell.familybudget.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dllhell.familybudget.data.models.Expense
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import java.util.*
import javax.inject.Inject

class DataRepository @Inject constructor() {
    fun getExpenses(): Observable<List<Expense>> {
        return Observable.create { emitter ->
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val reference = FirebaseDatabase.getInstance().getReference("expenses/$uid")

            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onNext(snapshot.children.map { child ->
                        Expense(child.child("date").getValue(Date::class.java)!!, child.child("sum").getValue(Long::class.java)!!)
                    })
                }
            }
            reference.addListenerForSingleValueEvent(listener)

            emitter.setDisposable(Disposables.fromAction { reference.removeEventListener(listener) })
        }
    }

    fun addExpense(expense: Expense): Completable {
        return Completable.create { emitter ->
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            val reference = FirebaseDatabase.getInstance().getReference("expenses/$uid")
            reference.push().setValue(expense.toMap()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(task.exception!!)
                }
            }
        }
    }
}
