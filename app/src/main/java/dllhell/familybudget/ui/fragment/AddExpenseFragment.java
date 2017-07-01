package dllhell.familybudget.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dllhell.familybudget.App;
import dllhell.familybudget.R;
import dllhell.familybudget.presentation.addexpense.AddExpensePresenter;
import dllhell.familybudget.presentation.addexpense.AddExpenseView;

public class AddExpenseFragment extends MvpAppCompatFragment implements AddExpenseView {

    public static final String ARG_DATE = "ARG_DATE";
    public static final String ARG_SUM = "ARG_SUM";

    private EditText dateEditText;
    private EditText timeEditText;
    private EditText sumEditText;
    private EditText locationEditText;


    @InjectPresenter
    AddExpensePresenter presenter;

    @ProvidePresenter
    AddExpensePresenter providePresenter() {
        AddExpensePresenter presenter = App.getAppComponent().addExpensePresenterProvider().get();
        Bundle arguments = this.getArguments();

        double sum = arguments.getDouble(AddExpenseFragment.ARG_SUM);
        presenter.SetSum(sum);

        Date date = (Date)arguments.getSerializable(AddExpenseFragment.ARG_DATE);
        presenter.SetDate(date);

        return presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateEditText = (EditText) view.findViewById(R.id.date_edit_text);
        timeEditText = (EditText) view.findViewById(R.id.time_edit_text);
        sumEditText = (EditText) view.findViewById(R.id.sum_edit_text);
        locationEditText = (EditText) view.findViewById(R.id.location_edit_text);
    }

    @Override
    public void SetSum(double sum) {
        sumEditText.setText(String.valueOf(sum));
    }

    @Override
    public void SetDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        dateEditText.setText(dateFormat.format(date));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        timeEditText.setText(timeFormat.format(date));
    }
}
