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
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswerFactory;
import np.com.naxa.simpledynamicforms.uitils.ToastUtils;
import timber.log.Timber;


public class EditTextFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.wrapper_answer_edit_text)
    TextInputLayout textInputLayout;

    private String userSelectedAnswer = "";
    private QuestionAnswer questionAnswer;
    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private boolean shouldStopWipe = false;


    public EditTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edittext, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();

        if (questionAnswer.isRequired()) {
            stopViewPagerSwipe();
            setRulesForValidation();
        }

        return rootView;
    }

    public void prepareQuestionAndAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;


        Timber.i("Preparing question with question \' %s \' at postion %s", questionAnswer.getQuestion(), questionAnswer.getOrder());
    }


    public void setMaxCounter(@NonNull int counterMaxLength) {
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(counterMaxLength);
    }


    public void stopViewPagerSwipe() {
        shouldStopWipe = true;
    }


    public void setQuestionAndAnswers() {
        tvQuestion.setText(questionAnswer.getQuestion());
        textInputLayout.getEditText().setHint(questionAnswer.getHint());
        textInputLayout.getEditText().setInputType(questionAnswer.getInputType());
        textInputLayout.getEditText().setText(questionAnswer.getAnswer());
    }

    private void getAnswer(final int pos) {
        userSelectedAnswer = textInputLayout.getEditText().getText().toString();
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {


        try {

            questionAnswer.setAnswer(userSelectedAnswer);

            listener.onAnswerSelected(questionAnswer);
            listener.shoudStopSwipe(shouldStopWipe);

        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", questionAnswer.getQuestion(), userSelectedAnswer);
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

        //making sure the fragment only reports to activity when it is it's turn

        //  index of fragmentPositionInViewPager begins at 1
        // index of order begins at 0

        // substract to match

        if (fragmentPositionInViewPager - 1 == questionAnswer.getOrder()) {
            getAnswer(questionAnswer.getOrder());
        }
    }

    private void setErrorMessage(String message) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(message);
        textInputLayout.requestFocus();
    }


    private void setRulesForValidation() {


        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (textInputLayout.getEditText().getText().toString().trim().length() == 0) {
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

    private void shouldAllowSwipe() {

        shouldStopWipe = false;
        listener.shoudStopSwipe(shouldStopWipe);
    }

    @Override
    public void fragmentIsVisible() {

        allowViewPagerSwipeListener.stopViewpagerScroll(shouldStopWipe);
    }
}
