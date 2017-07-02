package dllhell.familybudget.ui.fragment.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.presentation.history.HistoryPresenter
import dllhell.familybudget.presentation.history.HistoryView
import kotlinx.android.synthetic.main.fragment_history.*


class HistoryFragment : MvpAppCompatFragment(), HistoryView {

    private val adapter = HistoryAdapter(getQuery())

    private fun getQuery(): Query {
        return FirebaseDatabase.getInstance().reference.child("expenses")
                .child(FirebaseAuth.getInstance().currentUser?.uid)
    }

    @InjectPresenter
    lateinit var presenter: HistoryPresenter

    @ProvidePresenter
    internal fun providePresenter(): HistoryPresenter {
        return App.getAppComponent().historyPresenterProvider().get()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvExpenses.adapter = adapter
        rvExpenses.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }
}