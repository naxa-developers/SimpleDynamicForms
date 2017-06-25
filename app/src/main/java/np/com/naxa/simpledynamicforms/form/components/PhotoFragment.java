package np.com.naxa.simpledynamicforms.form.components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.uitils.DialogFactory;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class PhotoFragment extends Fragment implements fragmentStateListener,onPageVisibleListener {


    private static final int REQUEST_IMAGE_FROM_GALLERY = 101;
    private static final int REQUEST_READ_EXTERNAL_STORAGE = 201;
    private static final int REQUEST_IMAGE_CAPTURE = 102;
    public static final int REQUEST_MULTIPLE_PERMISSION = 103;


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;
    @BindView(R.id.fragment_photo_btn_open_camera)
    Button btnOpenCamera;
    @BindView(R.id.fragment_photo_btn_open_gallery)
    Button btnOpenGallery;
    @BindView(R.id.fragment_photo_iv_image_preview)
    ImageView ivImagePreview;


    private String question;
    private String hint;
    private int position;
    private onAnswerSelectedListener listener;
    private String mCurrentPhotoPath;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;


    public PhotoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        return rootView;
    }

    public void prepareQuestionAndAnswer(String question, int position) {
        this.question = question;
        this.position = position;

        Timber.i("Preparing question with question \' %s \' at postion %s", question, position);
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(question);

    }

    private void getAnswer(final int pos) {

        sendAnswerToActivity(pos);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onAnswerSelectedListener) {
            listener = (onAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onAnswerSelectedListener");
        }

        if (context instanceof shouldAllowViewPagerSwipeListener) {
            allowViewPagerSwipeListener = (shouldAllowViewPagerSwipeListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement shouldAllowViewPagerSwipeListener");
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof onAnswerSelectedListener) {
            listener = (onAnswerSelectedListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement onAnswerSelectedListener");
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof shouldAllowViewPagerSwipeListener) {
            allowViewPagerSwipeListener = (shouldAllowViewPagerSwipeListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement shouldAllowViewPagerSwipeListener");
        }
    }

    private void sendAnswerToActivity(int pos) {

        try {
            listener.onAnswerSelected(StringFormatter.replaceStringWithUnderScore(question), mCurrentPhotoPath);
        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s Answer: %s", question, mCurrentPhotoPath);
    }

    @OnClick(R.id.fragment_photo_btn_open_gallery)
    public void openGallery() {

        if (hasReadExternalStoragePermission()) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_IMAGE_FROM_GALLERY);
        }
    }

    @OnClick(R.id.fragment_photo_btn_open_camera)
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (hasPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE)
                && takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "np.com.naxa.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }


        } else {
            DialogFactory.createGenericErrorDialog(getActivity(), "No Supported Camera App found.");
        }
    }

    private boolean hasReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                return false;
            }
            return true;
        }
        return true;
    }

    private boolean hasPermissions(String permission[], int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ArrayList<String> notAvaliablePermList = new ArrayList<>();

            for (String currentPermission : permission) {
                int permissionCheck = checkSelfPermission(getActivity(), currentPermission);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    notAvaliablePermList.add(currentPermission);
                }
            }
            if (notAvaliablePermList.size() >= 1) {
                requestPermissions(notAvaliablePermList.toArray(new String[notAvaliablePermList.size()]), requestCode);
                return false;

            }
        }
        return true;
    }

    @Override
    public void fragmentStateChange(int state, int fragmentPositionInViewPager) {

        Timber.d("Asking Fragment At Postion %s for answer for the question ", fragmentPositionInViewPager);

        Boolean doFragmentIdMatch = fragmentPositionInViewPager == position;

        Timber.d(" %s and %s are the same ? %s \n question: %s", fragmentPositionInViewPager, position, doFragmentIdMatch.toString(), question);

        if (fragmentPositionInViewPager == position) {
            getAnswer(position);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Boolean b = data != null;

        Timber.e("Request Code %s ,ResultCode %s, Data %s", requestCode, resultCode, b.toString());

        if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && null != data) {
            try {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mCurrentPhotoPath = cursor.getString(columnIndex);
                cursor.close();
                //todo fix cursor leak
                ivImagePreview.setImageBitmap(BitmapFactory.decodeFile(mCurrentPhotoPath));

            } catch (Exception e) {
                DialogFactory.createGenericErrorDialog(getActivity(), e.toString());
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                setPic();
            } catch (Exception e) {
                e.printStackTrace();
                Timber.e(e.toString());

            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SDF_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void setPic() throws Exception {

        // Get the dimensions of the View
        int targetW = ivImagePreview.getWidth();
        int targetH = ivImagePreview.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);


        ivImagePreview.setImageBitmap(bitmap);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();

                } else {
                    DialogFactory.createMessageDialog(getActivity(), "Permission Needed", "Sto" +
                            "" +
                            "rage permission is needed to select photos. ").show();
                }
                break;
            case REQUEST_MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();

                } else {
                    DialogFactory.createMessageDialog(getActivity(), "Permission Needed", "Camera and Storage permission is needed to take photos. ").show();
                }

        }
    }


    @Override
    public void fragmentIsVisible() {
        allowViewPagerSwipeListener.stopViewpagerScroll(false);
    }
}
