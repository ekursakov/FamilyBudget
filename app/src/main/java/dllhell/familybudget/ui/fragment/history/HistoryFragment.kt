package dllhell.familybudget.ui.fragment.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.data.models.Expense
import dllhell.familybudget.presentation.history.HistoryPresenter
import dllhell.familybudget.presentation.history.HistoryView
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : MvpAppCompatFragment(), HistoryView {

    private val adapter = HistoryAdapter()

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

        rvExpenses.adapter = adapter;
        rvExpenses.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    override fun setLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun setItems(items: List<Expense>?) {
        if (items != null) {
            rvExpenses.visibility = View.VISIBLE
            adapter.setItems(items)
        } else {
            rvExpenses.visibility = View.GONE
        }
    }

    override fun showFatalError(message: String) {
        errorView.visibility = View.VISIBLE

        tvErrorMessage.text = message
    }

    override fun hideFatalError() {
        errorView.visibility = View.GONE
    }
}