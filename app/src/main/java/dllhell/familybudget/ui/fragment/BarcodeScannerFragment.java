package dllhell.familybudget.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import dllhell.familybudget.R;

public class BarcodeScannerFragment extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;
    private static final String REGEX_RULE = "&";
    private static final String DATE_PATTERN = "yyyyMMdd'T'HHmmss";
    private static final String DATE_START_PATTERN = "t=";
    private static final String SUM_START_PATTERN = "s=";

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private Date date = null;
    private String sum = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkCameraPermission();
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barcodeView = (DecoratedBarcodeView) view.findViewById(R.id.barcode_scanner);
        barcodeView.decodeContinuous(barcodeCallback);
        beepManager = new BeepManager(getActivity());
    }

    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            beepManager.playBeepSoundAndVibrate();

            if (decodeQRCodeInfo(result.getText())) {
                // TODO: 7/1/17 pass params to fragment EditExp.
            } else {
                barcodeView.setStatusText("Can't recognize QR code data, please use manual mode or retry");
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    /**
     * Decodes String to inner fields date and sum.
     * @param info String like this "t=20170701T085100&s=169.00&fn=8710000100627004&i=75&fp=1563831204&n=1"
     * @return True - if decode successful, False - if not.
     */
    private boolean decodeQRCodeInfo(final String info) {
        final String[] infoArray = info.split(REGEX_RULE);
        for (int i = 0; i < infoArray.length; i++) {
            if (date != null && !sum.isEmpty()) break;
            if (date == null && infoArray[i].substring(0, 2).equals(DATE_START_PATTERN)) {
                String dateInString = infoArray[i].substring(2);
                DateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
                try {
                    date = formatter.parse(dateInString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (sum.isEmpty() && infoArray[i].substring(0, 2).equals(SUM_START_PATTERN)) {
                sum = infoArray[i].substring(2);
            }
        }
        return date != null || !sum.isEmpty();
    }

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getContext())
                        .setTitle("Camera permission")
                        .setMessage("Please grant access to camera to scan barcodes")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            // Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_CAMERA);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        } else {
            return true;
        }
    }
}
