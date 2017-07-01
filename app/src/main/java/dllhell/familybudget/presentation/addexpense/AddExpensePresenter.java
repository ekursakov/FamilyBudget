package dllhell.familybudget.presentation.addexpense;

import com.arellomobile.mvp.InjectViewState;

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
}
