package dllhell.familybudget.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import dllhell.familybudget.App
import dllhell.familybudget.R
import dllhell.familybudget.presentation.addexpense.AddExpensePresenter
import dllhell.familybudget.presentation.addexpense.AddExpenseView
import kotlinx.android.synthetic.main.fragment_add_expense.*
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : MvpAppCompatFragment(), AddExpenseView {

    private var sum: Double = 0.0
    private var date: Date = Date()

    @InjectPresenter
    lateinit var presenter: AddExpensePresenter

    @ProvidePresenter
    internal fun providePresenter(): AddExpensePresenter {
        return App.appComponent.addExpensePresenterProvider().get()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSave.setOnClickListener {
            presenter.onSaveClick(date, sum, category_spinner.selectedItem.toString())
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                onDateChanged()
            }
        }
        etDate.addTextChangedListener(watcher)
        etTime.addTextChangedListener(watcher)
        etSum.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                try {
                    sum = java.lang.Double.parseDouble(etSum.text.toString())
                    tilSum.error = null
                } catch (e: Exception) {
                    tilSum.error = "Invalid sum"
                }

            }
        })

        if (arguments != null) {
            sum = arguments.getDouble(AddExpenseFragment.ARG_SUM)
            date = arguments.getSerializable(AddExpenseFragment.ARG_DATE) as Date

            etSum.setText(String.format(Locale.US, "%.2f", sum))
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            etDate.setText(dateFormat.format(date))

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            etTime.setText(timeFormat.format(date))
        }
    }

    private fun onDateChanged() {
        try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.US)
            date = dateFormat.parse(etDate.text.toString() + " " + etTime.text.toString())
            tilDate.error = null
        } catch (e: Exception) {
            tilDate.error = "Invalid date"
        }

    }

    override fun onStart() {
        super.onStart()

        activity.setTitle(R.string.title_add_expense)
    }

    companion object {

        val ARG_DATE = "ARG_DATE"
        val ARG_SUM = "ARG_SUM"
    }
}
