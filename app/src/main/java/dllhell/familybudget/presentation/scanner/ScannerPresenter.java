package dllhell.familybudget.presentation.scanner;

import android.os.Bundle;

import com.arellomobile.mvp.InjectViewState;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dllhell.familybudget.data.DataRepository;
import dllhell.familybudget.presentation.BasePresenter;
import dllhell.familybudget.ui.fragment.AddExpenseFragment;
import dllhell.familybudget.ui.navigation.Screens;
import ru.terrakok.cicerone.Router;

@InjectViewState
public class ScannerPresenter extends BasePresenter<ScannerView> {

    private final Router router;
    private final DataRepository dataRepository;

    private static final String REGEX_RULE = "&";
    private static final String DATE_PATTERN = "yyyyMMdd'T'HHmmss";
    private static final String DATE_START_PATTERN = "t=";
    private static final String SUM_START_PATTERN = "s=";

    private static final String SPEECH_KIT_MANUAL_ADD_PATTERN_1 = "добавь";
    private static final String SPEECH_KIT_MANUAL_ADD_PATTERN_2 = "добавить";
    private static final String SPEECH_KIT_MANUAL_ADD_PATTERN_3 = "вручн";
    private static final String SPEECH_KIT_MANUAL_ADD_PATTERN_4 = "вручную";
    private static final String SPEECH_KIT_SUM_PATTERN = "\\d+ [р]";

    @Inject
    public ScannerPresenter(Router router, DataRepository dataRepository) {
        this.router = router;
        this.dataRepository = dataRepository;
    }

    /**
     * Decodes String to inner fields date and sum.
     *
     * @param text String like this "t=20170701T085100&s=169.00&fn=8710000100627004&i=75&fp=1563831204&n=1"
     */
    public void onBarcodeScan(String text) {
        Date date = null;
        Double sum = null;
        final String[] infoArray = text.split(REGEX_RULE);
        for (int i = 0; i < infoArray.length; i++) {
            if (date != null && sum != null) break;
            if (date == null && infoArray[i].substring(0, 2).equals(DATE_START_PATTERN)) {
                String dateInString = infoArray[i].substring(2);
                DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
                try {
                    date = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (sum == null && infoArray[i].substring(0, 2).equals(SUM_START_PATTERN)) {
                String str = infoArray[i].substring(2);
                sum = Double.parseDouble(str);
            }
        }
        if (date != null || sum != null) {
            Bundle bundle = new Bundle();
            bundle.putDouble(AddExpenseFragment.ARG_SUM, sum);
            bundle.putSerializable(AddExpenseFragment.ARG_DATE, date);
            router.navigateTo(Screens.ADD_EXPENSE, bundle);
        } else {
            getViewState().setStatusText("Can't recognize QR code data, please use manual mode or retry");
        }
    }

    public void onSpeechKitCalled(String text) {
        if (speechKitManualAddKeywordsDetected(text)) {
            Bundle bundle = new Bundle();
            String sum = speechKitDetectSum(text);
            if (sum != null && sum.isEmpty()) {
                bundle.putDouble(AddExpenseFragment.ARG_SUM, 0);
            } else {
                try {
                    bundle.putDouble(AddExpenseFragment.ARG_SUM, Double.valueOf(sum));
                } catch (NumberFormatException e) {
                    bundle.putDouble(AddExpenseFragment.ARG_SUM, 0);
                }
            }
            bundle.putSerializable(AddExpenseFragment.ARG_DATE, new Date(System.currentTimeMillis()));
            router.navigateTo(Screens.ADD_EXPENSE, bundle);
        } else {
            getViewState().setStatusText("Can't recognize your speech, please use manual mode or retry");
        }
    }

    private boolean speechKitManualAddKeywordsDetected(String text) {
        return text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_1)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_2)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_3)
                || text.contains(SPEECH_KIT_MANUAL_ADD_PATTERN_4);
    }

    private String speechKitDetectSum(String text) {
        Pattern pattern = Pattern.compile(SPEECH_KIT_SUM_PATTERN);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String sum = matcher.group(0);
            return sum.split(" ")[0];
        } else {
            return "";
        }
    }
}
