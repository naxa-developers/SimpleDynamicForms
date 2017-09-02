package np.com.naxa.simpledynamicforms.form.components;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.helpers.GeoPointActivity;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import np.com.naxa.simpledynamicforms.uitils.DialogFactory;
import np.com.naxa.simpledynamicforms.uitils.SpanUtils;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;
import static np.com.naxa.simpledynamicforms.form.components.PhotoFragment.REQUEST_MULTIPLE_PERMISSION;

/**
 * Created by Nishon Tandukar on 20 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class LocationFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {
    public static final int GEOPOINT_RESULT_CODE = 1994;
    public static final String LOCATION_RESULT = "LOCATION_RESULT";
    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.tv_preview_location)
    TextView tvLocationPreview;

    @BindView(R.id.btn_location_fragment_get_location)
    Button btnGetLocation;


    private String location;
    private QuestionAnswer locationQuestion;

    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        return rootView;

    }


    public void prepareQuestionAndAnswer(QuestionAnswer locationQuestion) {
        this.locationQuestion = locationQuestion;
        Timber.i("Preparing question with question \' %s \' at postion %s", locationQuestion.getQuestion(), locationQuestion.getOrder());
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(locationQuestion.getQuestion());

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
            locationQuestion.setAnswer(location);

            listener.onAnswerSelected(locationQuestion);

        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", locationQuestion.getQuestion(), locationQuestion.getAnswer());
    }


    @OnClick(R.id.btn_location_fragment_get_location)
    public void tryToGetLocation() {

        if (hasPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_MULTIPLE_PERMISSION)) {
            Intent toGeoPointActivity = new Intent(getActivity().getApplicationContext(), GeoPointActivity.class);
            startActivityForResult(toGeoPointActivity, GEOPOINT_RESULT_CODE);
        }


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GEOPOINT_RESULT_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        location = data.getStringExtra(LOCATION_RESULT);
                        showLocationPreview();
                        break;
                }
                break;
        }
    }


    private void showLocationPreview() {
        String s = "Location Recorded \n" + location;
        SpanUtils spanUtils = new SpanUtils();
        String boldText = spanUtils.makeSectionOfTextBold(s, "Location Recorded").toString();
        tvLocationPreview.setText(boldText);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MULTIPLE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tryToGetLocation();

                } else {
                    DialogFactory.createMessageDialog(getActivity(), "Permission Needed", "Location permission is needed to record locaiton. ").show();
                }

        }
    }

    @Override
    public void fragmentStateChange(int state, int fragmentPositionInViewPager) {

        if (fragmentPositionInViewPager - 1 == locationQuestion.getOrder()) {
            getAnswer(locationQuestion.getOrder());
        }
    }

    @Override
    public void fragmentIsVisible() {

    }


}
