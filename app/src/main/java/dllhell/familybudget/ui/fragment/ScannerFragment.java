package dllhell.familybudget.ui.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

import dllhell.familybudget.App;
import dllhell.familybudget.R;
import dllhell.familybudget.presentation.scanner.ScannerPresenter;
import dllhell.familybudget.presentation.scanner.ScannerView;
import ru.yandex.speechkit.Recognizer;
import ru.yandex.speechkit.SpeechKit;
import ru.yandex.speechkit.gui.RecognizerActivity;

public class ScannerFragment extends MvpAppCompatFragment implements ScannerView, View.OnClickListener {

    private static final String API_KEY = "2b81bde9-b1f3-4590-8d6c-6021db5c621b";
    private static final int SPEECH_KIT_REQUEST_CODE = 31;
    private static final int PERMISSIONS_REQUEST_CAMERA = 99;

    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;
    private FloatingActionButton speechKitFab;

    @InjectPresenter
    ScannerPresenter presenter;

    @ProvidePresenter
    ScannerPresenter providePresenter() {
        return App.getAppComponent().scannerPresenterProvider().get();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechKit.getInstance().configure(getContext(), API_KEY);
    }

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
        speechKitFab = (FloatingActionButton) view.findViewById(R.id.fab_speech_kit);
        speechKitFab.setOnClickListener(this);
    }

    private BarcodeCallback barcodeCallback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            presenter.onBarcodeScan(result.getText());

            lastText = result.getText();
            beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle(R.string.app_name);
    }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_speech_kit:
                /// To start recognition create an Intent with required extras.
                Intent intent = new Intent(getActivity(), RecognizerActivity.class);
                // Specify the model for better results.
                intent.putExtra(RecognizerActivity.EXTRA_MODEL, Recognizer.Model.QUERIES);
                // Specify the language.
                intent.putExtra(RecognizerActivity.EXTRA_LANGUAGE, Recognizer.Language.RUSSIAN);
                // To get recognition results use startActivityForResult(),
                // also don't forget to override onActivityResult().
                startActivityForResult(intent, SPEECH_KIT_REQUEST_CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (requestCode == SPEECH_KIT_REQUEST_CODE) {
            if (resultCode == RecognizerActivity.RESULT_OK && data != null) {
                final String result = data.getStringExtra(RecognizerActivity.EXTRA_RESULT);
                presenter.onSpeechKitCalled(result);
            } else if (resultCode == RecognizerActivity.RESULT_ERROR) {
                String error = ((ru.yandex.speechkit.Error) data.getSerializableExtra(RecognizerActivity.EXTRA_ERROR)).getString();
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setStatusText(String text) {
        barcodeView.setStatusText(text);
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
                                    PERMISSIONS_REQUEST_CAMERA);
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            }
            return false;
        } else {
            return true;
        }
    }
}
