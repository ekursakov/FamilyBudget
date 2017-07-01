package dllhell.familybudget.presentation.addexpense;

import com.arellomobile.mvp.MvpView;

import java.util.Date;

public interface AddExpenseView extends MvpView {
    void SetSum(long sum);

    void SetDate(Date date);
}
