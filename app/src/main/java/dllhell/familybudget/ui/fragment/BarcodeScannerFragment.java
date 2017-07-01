package dllhell.familybudget.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import dllhell.familybudget.R;

public class BarcodeScannerFragment extends Fragment {

    private static final String TAG = BarcodeScannerFragment.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 99;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        checkCameraPermission();
        // Inflate the layout for this fragment
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
            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            //Added preview of scanned barcode
            ImageView imageView = (ImageView) getView().findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
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


    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(barcodeCallback);
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
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CAMERA);
                            }
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
