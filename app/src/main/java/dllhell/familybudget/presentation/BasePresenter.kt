package dllhell.familybudget.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : MvpView> : MvpPresenter<View>() {
    private val onDestroyDisposables = CompositeDisposable()

    fun destroyOnDispose(disposable: Disposable) {
        onDestroyDisposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroyDisposables.dispose()
    }
}
