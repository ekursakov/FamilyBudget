package dllhell.familybudget.presentation.scanner

import com.arellomobile.mvp.MvpView

interface ScannerView : MvpView {

    fun setStatusText(text: String)
}
