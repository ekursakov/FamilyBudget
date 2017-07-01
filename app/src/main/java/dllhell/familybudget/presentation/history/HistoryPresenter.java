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

    @Override
    protected void onFirstViewAttach() {
        loadItems();
    }

    public void retry() {
        getViewState().hideFatalError();
        loadItems();
    }

    private void loadItems() {
        getViewState().setLoading(true);

        destroyOnDispose(dataRepository.getExpenses()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    getViewState().setItems(items);
                    getViewState().setLoading(false);
                }, e -> {
                    Timber.w(e);
                    getViewState().setLoading(false);
                    getViewState().setItems(null);
                    getViewState().showFatalError("Ошибка загрузки.");
                }));
    }
}

