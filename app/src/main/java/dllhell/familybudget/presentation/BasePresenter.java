package dllhell.familybudget.presentation;

import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter<View extends MvpView> extends MvpPresenter<View> {
    private final CompositeDisposable onDestroyDisposables = new CompositeDisposable();

    public void destroyOnDispose(Disposable disposable) {
        onDestroyDisposables.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDestroyDisposables.dispose();
    }
}
