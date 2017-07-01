package dllhell.familybudget.presentation.addexpense;

import com.arellomobile.mvp.InjectViewState;

import java.util.Date;
import java.util.Set;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.presentation.BasePresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class AddExpensePresenter extends BasePresenter<AddExpenseView> {

    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public AddExpensePresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }

    public void SetSum(double sum) {
        this.getViewState().SetSum(sum);
    }

    public void SetDate(Date date) {
        this.getViewState().SetDate(date);
    }
}
