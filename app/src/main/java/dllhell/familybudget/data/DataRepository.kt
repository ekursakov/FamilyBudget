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
import javax.inject.Inject

class DataRepository @Inject constructor() {
    fun getExpenses(): Observable<List<Expense>> {
        return Observable.create { emitter ->
            val author = FirebaseAuth.getInstance().currentUser!!.uid
            val reference = FirebaseDatabase.getInstance().getReference("expenses/")

            val listener = object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    emitter.onError(error.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    emitter.onNext(snapshot.children.map { it.getValue(Expense::class.java)!! })
                }
            }
            reference.addListenerForSingleValueEvent(listener)

            emitter.setDisposable(Disposables.fromAction { reference.removeEventListener(listener) })
        }
    }
}
