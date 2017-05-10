package in.exun.campusbox.fragments.EventAdd;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.fenchtose.nocropper.CropperCallback;
import com.fenchtose.nocropper.CropperView;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.ByteArrayOutputStream;

import in.exun.campusbox.R;
import in.exun.campusbox.activity.AddEvent;
import in.exun.campusbox.helper.BitmapUtils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ayush on 01/05/17.
 */

public class Poster extends Fragment implements BlockingStep {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_READ_PERMISSION = 22;
    private static final int REQUEST_GALLERY = 21;

    private View rootView;
    private CropperView imgPoster;

    private Bitmap mBitmap;
    private boolean isSnappedToCenter = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_poster, container, false);

        imgPoster = (CropperView) rootView.findViewById(R.id.img_poster);

        ImageView btnPickImage = (ImageView) rootView.findViewById(R.id.btn_pick_image);
        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGalleryIntent();
            }
        });

        ImageView btnSnap = (ImageView) rootView.findViewById(R.id.btn_snap);
        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snapImage();
            }
        });

        return rootView;
    }

    private void loadNewImage(String filePath) {
        Log.i(TAG, "load image: " + filePath);
        mBitmap = BitmapFactory.decodeFile(filePath);
        Log.i(TAG, "bitmap: " + mBitmap.getWidth() + " " + mBitmap.getHeight());

        int maxP = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        float scale1280 = (float) maxP / 2560;

        if (imgPoster.getWidth() != 0) {
            imgPoster.setMaxZoom(imgPoster.getWidth() * 2 / 1280f);
        } else {

            ViewTreeObserver vto = imgPoster.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imgPoster.getViewTreeObserver().removeOnPreDrawListener(this);
                    imgPoster.setMaxZoom(imgPoster.getWidth() * 2 / 1280f);
                    return true;
                }
            });

        }
        imgPoster.setMinZoom(0.5f);

        mBitmap = Bitmap.createScaledBitmap(mBitmap, (int) (mBitmap.getWidth() / scale1280),
                (int) (mBitmap.getHeight() / scale1280), true);
        imgPoster.setImageBitmap(mBitmap);

    }

    private void snapImage() {
        if (isSnappedToCenter) {
            imgPoster.cropToCenter();
        } else {
            imgPoster.fitToCenter();
        }

        isSnappedToCenter = !isSnappedToCenter;
    }

    private void startGalleryIntent() {

        Log.d(TAG, "startGalleryIntent: ");

        if (!hasGalleryPermission()) {
            askForGalleryPermission();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    private boolean hasGalleryPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askForGalleryPermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE_READ_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent resultIntent) {
        super.onActivityResult(requestCode, responseCode, resultIntent);

        if (responseCode == RESULT_OK) {
            String absPath = BitmapUtils.getFilePathFromUri(getActivity(), resultIntent.getData());
            loadNewImage(absPath);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: " + requestCode);
        if (requestCode == REQUEST_CODE_READ_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryIntent();
                return;
            }
        }

        Toast.makeText(getActivity(), "Gallery permission not granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
    }

    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback callback) {

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        imgPoster.getCroppedBitmapAsync(new CropperCallback() {
            @Override
            public void onCropped(Bitmap bitmap) {

                if (bitmap != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] b = baos.toByteArray();
                    ((AddEvent) getActivity()).stringImage = "data:image/png;base64," + Base64.encodeToString(b, Base64.URL_SAFE);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((AddEvent)getActivity()).addEvent();
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}
