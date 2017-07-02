package dllhell.familybudget.presentation.scanner

import android.os.Bundle

import com.arellomobile.mvp.InjectViewState

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.inject.Inject

import dllhell.familybudget.data.DataRepository
import dllhell.familybudget.presentation.BasePresenter
import dllhell.familybudget.ui.fragment.AddExpenseFragment
import dllhell.familybudget.ui.navigation.Screens
import ru.terrakok.cicerone.Router

@InjectViewState
class ScannerPresenter @Inject
constructor(private val router: Router, private val dataRepository: DataRepository) : BasePresenter<ScannerView>() {

    /**
     * Decodes String to inner fields date and sum.

     * @param text String like this "t=20170701T085100&s=169.00&fn=8710000100627004&i=75&fp=1563831204&n=1"
     */
    fun onBarcodeScan(text: String) {
        var date: Date? = null
        var sum: Double? = null
        val infoArray = text.split(REGEX_RULE.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in infoArray.indices) {
            if (date != null && sum != null) break
            if (date == null && infoArray[i].substring(0, 2) == DATE_START_PATTERN) {
                val dateInString = infoArray[i].substring(2)
                val formatter = SimpleDateFormat(DATE_PATTERN)
                try {
                    date = formatter.parse(dateInString)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            } else if (sum == null && infoArray[i].substring(0, 2) == SUM_START_PATTERN) {
                val str = infoArray[i].substring(2)
                sum = java.lang.Double.parseDouble(str)
            }
        }
        if (date != null || sum != null) {
            val bundle = Bundle()
            bundle.putDouble(AddExpenseFragment.ARG_SUM, sum!!)
            bundle.putSerializable(AddExpenseFragment.ARG_DATE, date)
            router.navigateTo(Screens.ADD_EXPENSE, bundle)
        } else {
            viewState.setStatusText("Can't recognize QR code data, please use manual mode or retry")
        }
    }

    fun onSpeechKitCalled(text: String) {
        if (speechKitManualAddKeywordsDetected(text)) {
            val bundle = Bundle()
            val sum = speechKitDetectSum(text)
            if (sum != null && sum.isEmpty()) {
                bundle.putDouble(AddExpenseFragment.ARG_SUM, 0.0)
            } else {
                try {
                    bundle.putDouble(AddExpenseFragment.ARG_SUM, java.lang.Double.valueOf(sum)!!)
                } catch (e: NumberFormatException) {
                    bundle.putDouble(AddExpenseFragment.ARG_SUM, 0.0)
                }

            }
            bundle.putSerializable(AddExpenseFragment.ARG_DATE, Date(System.currentTimeMillis()))
            router.navigateTo(Screens.ADD_EXPENSE, bundle)
        } else {
            viewState.setStatusText("Can't recognize your speech, please use manual mode or retry")
        }
    }

    private fun speechKitManualAddKeywordsDetected(text: String): Boolean {
        return text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_1)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_2)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_3)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_4)
    }

    private fun speechKitDetectSum(text: String): String? {
        val pattern = Pattern.compile(SPEECH_KIT_SUM_PATTERN)
        val matcher = pattern.matcher(text)
        if (matcher.find()) {
            val sum = matcher.group(0)
            return sum.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
        } else {
            return ""
        }
    }

    companion object {

        private val REGEX_RULE = "&"
        private val DATE_PATTERN = "yyyyMMdd'T'HHmmss"
        private val DATE_START_PATTERN = "t="
        private val SUM_START_PATTERN = "s="

        private val SPEECH_KIT_MANUAL_ADD_PATTERN_1 = "добавь"
        private val SPEECH_KIT_MANUAL_ADD_PATTERN_2 = "добавить"
        private val SPEECH_KIT_MANUAL_ADD_PATTERN_3 = "вручн"
        private val SPEECH_KIT_MANUAL_ADD_PATTERN_4 = "вручную"
        private val SPEECH_KIT_SUM_PATTERN = "\\d+ [р]"
    }
}
