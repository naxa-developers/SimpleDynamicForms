package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.Dump;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.listeners.onPageVisibleListener;
import np.com.naxa.simpledynamicforms.form.listeners.shouldAllowViewPagerSwipeListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import timber.log.Timber;


public class SpinnerFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {

    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.spinner_answer_options)
    Spinner optionsSpinner;

    private String userSelectedAnswer = "";
    private QuestionAnswer questionAnswer;

    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;

    public SpinnerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spinner, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        return rootView;
    }


    public void setQuestionAndAnswers() {
        tvQuestion.setText(questionAnswer.getQuestion());
        optionsSpinner.setPrompt(questionAnswer.getQuestion());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, questionAnswer.getDropOptions());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(dataAdapter);


        boolean hasAnswer = !TextUtils.isEmpty(questionAnswer.getAnswer());
        if (hasAnswer){
            int positionOfAnswer = dataAdapter.getPosition(questionAnswer.getAnswer());
            optionsSpinner.setSelection(positionOfAnswer);
        }

    }

    private void getAnswer(final int pos) {
        userSelectedAnswer = optionsSpinner.getSelectedItem().toString();
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {

        try {

            questionAnswer.setAnswer(userSelectedAnswer);
            listener.onAnswerSelected(questionAnswer);



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

        if (fragmentPositionInViewPager - 1 == questionAnswer.getOrder()) {
            getAnswer(questionAnswer.getOrder());
        }
    }

    @Override
    public void fragmentIsVisible() {
        allowViewPagerSwipeListener.stopViewpagerScroll(false);
    }

    public void prepareQuestionAndAnswer(QuestionAnswer singleDropdown) {
        this.questionAnswer = singleDropdown;

        Timber.i("Preparing question with question \' %s \' at postion %s", questionAnswer.getQuestion(), questionAnswer.getOrder());

    }
}
