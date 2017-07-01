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
    private double sum;
    private Date date;

    @Inject
    public AddExpensePresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }

    public void setSum(double sum) {
        this.sum = sum;
        this.getViewState().SetSum(this.sum);
    }

    public void setDate(Date date) {
        this.date = date;
        this.getViewState().SetDate(this.date);
    }

    public void onEditClick() {

    }

    public void onNewScanningClick() {
        this.router.exit();
    }
}
