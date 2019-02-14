package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guna.libmultispinner.MultiSelectionSpinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;
import timber.log.Timber;


public class MultiSelectSpinnerFragment extends Fragment implements fragmentStateListener, MultiSelectionSpinner.OnMultipleItemsSelectedListener, onPageVisibleListener {


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;


    @BindView(R.id.multi_select_spinner_answer_options)
    MultiSelectionSpinner multiSelectionSpinner;
    @BindView(R.id.wrapper_multi_select_other)
    TextInputLayout wrapperMultiSelectOther;

    private String userSelectedAnswer = "";


    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private QuestionAnswer multiselectSpinnerQuestion;


    public MultiSelectSpinnerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_multi_select_spinner, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        return rootView;
    }



    public void prepareQuestionAndAnswer(QuestionAnswer multiselectSpinnerQuestion) {
        this.multiselectSpinnerQuestion = multiselectSpinnerQuestion;
        Timber.i("Preparing question with question \' %s \' at postion %s", multiselectSpinnerQuestion.getQuestion(), multiselectSpinnerQuestion.getOrder());
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(multiselectSpinnerQuestion.getQuestion());
        multiSelectionSpinner.setTitle(multiselectSpinnerQuestion.getQuestion());
        multiSelectionSpinner.setItems(multiselectSpinnerQuestion.getDropOptions());
        multiSelectionSpinner.setListener(this);

    }


    public void selectOptions(int[] options) {
        multiSelectionSpinner.setSelection(options);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        multiSelectionSpinner.setListener(null);
    }

    private void getAnswer(final int pos) {
        //selectedStrings methods reads the answer

//        String otherAnswer = wrapperMultiSelectOther.getEditText().getText().toString().trim();
//
//        if (otherAnswer.length() > 0) {
//            userSelectedAnswer.replace(getString(R.string.msg_other), wrapperMultiSelectOther.getEditText().getText());
//        }


        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {


        try {
            multiselectSpinnerQuestion.setAnswer(userSelectedAnswer);
            listener.onAnswerSelected(multiselectSpinnerQuestion);

        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", multiselectSpinnerQuestion.getQuestion(), multiselectSpinnerQuestion.getAnswer());
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

    @Override
    public void fragmentStateChange(int state, int fragmentPositionInViewPager) {


        if (fragmentPositionInViewPager -1  == multiselectSpinnerQuestion.getOrder()) {
            getAnswer(multiselectSpinnerQuestion.getOrder());
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {


        try {
            userSelectedAnswer = new JSONArray(strings).toString();
            handleOtherInDropDown(new JSONArray(strings));
        } catch (JSONException e) {
            ToastUtils.showLongSafe(e.toString());
            e.printStackTrace();
        }
    }

    private void handleOtherInDropDown(JSONArray jsonArray) throws JSONException {


        for (int i = 0; i < jsonArray.length(); i++) {
            String j = jsonArray.getString(i);
            if (j.equalsIgnoreCase(getString(R.string.msg_other))) {
                showOtherInputView();
            }
        }
    }

    private void showOtherInputView() {
        wrapperMultiSelectOther.setVisibility(View.VISIBLE);
        wrapperMultiSelectOther.requestFocus();


    }


    @Override
    public void fragmentIsVisible() {
        allowViewPagerSwipeListener.stopViewpagerScroll(false);
    }


}
