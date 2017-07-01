package dllhell.familybudget.presentation.edit;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.presentation.BasePresenter;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class EditExpensePresenter extends BasePresenter<EditExpenseView> {

    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public EditExpensePresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }
}
