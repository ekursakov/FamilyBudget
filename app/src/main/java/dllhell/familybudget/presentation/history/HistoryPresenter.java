package dllhell.familybudget.presentation.history;

import com.arellomobile.mvp.InjectViewState;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.presentation.BasePresenter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

@InjectViewState
public class HistoryPresenter extends BasePresenter<HistoryView> {
    private final Router router;
    private final DataRepository dataRepository;

    @Inject
    public HistoryPresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }
}

