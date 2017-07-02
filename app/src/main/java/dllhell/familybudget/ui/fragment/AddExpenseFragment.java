package dllhell.familybudget.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

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
    private Spinner categorySpinner;

    private double sum;
    private Date date;

    @InjectPresenter
    AddExpensePresenter presenter;
    private TextInputLayout dateTextInputLayout;
    private TextInputLayout sumTextInputLayout;

    @ProvidePresenter
    AddExpensePresenter providePresenter() {
        return App.getAppComponent().addExpensePresenterProvider().get();
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
        dateTextInputLayout = (TextInputLayout) view.findViewById(R.id.tilDate);
        sumTextInputLayout = (TextInputLayout) view.findViewById(R.id.tilSum);
        dateEditText = (EditText) view.findViewById(R.id.etDate);
        timeEditText = (EditText) view.findViewById(R.id.etTime);
        sumEditText = (EditText) view.findViewById(R.id.etSum);
        categorySpinner = (Spinner) view.findViewById(R.id.category_spinner);

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            presenter.onSaveClick(date, sum, categorySpinner.getSelectedItem().toString());
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onDateChanged();
            }
        };
        dateEditText.addTextChangedListener(watcher);
        timeEditText.addTextChangedListener(watcher);
        sumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    sum = Double.parseDouble(sumEditText.getText().toString());
                    sumTextInputLayout.setError(null);
                } catch (Exception e) {
                    sumTextInputLayout.setError("Invalid sum");
                }
            }
        });

        if (getArguments() != null) {
            sum = getArguments().getDouble(AddExpenseFragment.ARG_SUM);
            date = (Date) getArguments().getSerializable(AddExpenseFragment.ARG_DATE);

            sumEditText.setText(String.format(Locale.US, "%.2f", sum));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            dateEditText.setText(dateFormat.format(date));

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            timeEditText.setText(timeFormat.format(date));
        }
    }

    private void onDateChanged() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            date = dateFormat.parse(dateEditText.getText().toString() + " " + timeEditText.getText().toString());
            dateTextInputLayout.setError(null);
        } catch (Exception e) {
            dateTextInputLayout.setError("Invalid date");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.title_add_expense);
    }
}
