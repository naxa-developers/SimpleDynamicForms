package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import timber.log.Timber;


public class AutoCompleteTextFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {


    @BindView(R.id.answer_edit_text)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;


    private String userSelectedAnswer = "";
    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private boolean shouldStopWipe = false;
    private boolean validationRequired;

    private QuestionAnswer questionAnswer;


    public AutoCompleteTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_autoedittext, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();


        if (validationRequired) {
            stopViewPagerSwipe();
            setRulesForValidation();
        }

        return rootView;
    }

    public void prepareQuestionAndAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;

        Timber.i("Preparing question with question \' %s \' at postion %s", questionAnswer.getQuestion(), questionAnswer.getOrder());
    }



    public void setQuestionAndAnswers() {
        tvQuestion.setText(questionAnswer.getQuestion());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, questionAnswer.getDropOptions());
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

    }


    public void stopViewPagerSwipe() {
        shouldStopWipe = true;
    }


    private void getAnswer(final int pos) {
        userSelectedAnswer = autoCompleteTextView.getText().toString();
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {

        try {
            listener.onAnswerSelected(questionAnswer);
            listener.shoudStopSwipe(shouldStopWipe);
        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", questionAnswer.getQuestion(), questionAnswer.getAnswer());
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


        if (fragmentPositionInViewPager -1 == questionAnswer.getOrder()) {
            getAnswer(questionAnswer.getOrder());
        }
    }


    private void setRulesForValidation() {


        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (autoCompleteTextView.getText().toString().trim().length() == 0) {
                    stopViewPagerSwipe();
                    setErrorMessage("This Field Cannot be empty");
                } else {


                    shouldAllowSwipe();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void setErrorMessage(String message) {

        autoCompleteTextView.setError(message);
        autoCompleteTextView.requestFocus();
    }

    private void shouldAllowSwipe() {

        shouldStopWipe = false;
        listener.shoudStopSwipe(shouldStopWipe);
    }

    @Override
    public void fragmentIsVisible() {

        allowViewPagerSwipeListener.stopViewpagerScroll(shouldStopWipe);
    }
}
