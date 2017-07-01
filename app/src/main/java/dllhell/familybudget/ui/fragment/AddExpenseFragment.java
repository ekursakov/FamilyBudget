package dllhell.familybudget.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dllhell.familybudget.R;

public class AddExpenseFragment extends Fragment {
    
    public static final String ARG_DATE = "ARG_DATE";
    public static final String ARG_SUM = "ARG_SUM";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        takeArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_expense, container, false);
    }
    
    private void takeArguments() {
        // TODO: 7/1/17  
    }
}
