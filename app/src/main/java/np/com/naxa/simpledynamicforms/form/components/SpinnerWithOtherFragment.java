package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import np.com.naxa.simpledynamicforms.uitils.SpinnerHelper;
import timber.log.Timber;


public class SpinnerWithOtherFragment extends Fragment implements fragmentStateListener, AdapterView.OnItemSelectedListener, onPageVisibleListener {

    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    SpinnerHelper spinner;

    @BindView(R.id.wrapper_spinner_answer_other)
    TextInputLayout textInputLayout;

    private String userSelectedAnswer = "";
    private onAnswerSelectedListener listener;
    private boolean shouldGetValueFromOther;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private QuestionAnswer spinnerOtherQuestion;
    private ArrayAdapter<String> dataAdapter;


    public SpinnerWithOtherFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spinner, container, false);
        spinner = new SpinnerHelper(rootView.findViewById(R.id.spinner_answer_options));
        setSpinnerSelectionWithoutCallingListener((Spinner) rootView.findViewById(R.id.spinner_answer_options));
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();


        return rootView;
    }


    public void prepareQuestionAndAnswer(QuestionAnswer spinnerOtherQuestion) {
        this.spinnerOtherQuestion = spinnerOtherQuestion;

        Timber.i("Preparing question with question \' %s \' at postion %s", spinnerOtherQuestion.getQuestion(), spinnerOtherQuestion.getOrder());
    }


    private void showTextInputLayout() {
        textInputLayout.setVisibility(View.VISIBLE);
        textInputLayout.requestFocus();
    }

    private void hideTextInputLayout() {
        textInputLayout.setVisibility(View.GONE);
        textInputLayout.requestFocus();
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(spinnerOtherQuestion.getQuestion());
        // spinner.getSpinner().setPrompt(spinnerOtherQuestion.getQuestion());

        setOptionsSpinner();
        setAnswerIfPresent();

    }


    private void setOptionsSpinner() {
        dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerOtherQuestion.getDropOptions());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOtherQuestion.getDropOptions().add("Other");
        spinner.setAdapter(dataAdapter);
    }

    private void setAnswerIfPresent() {
        boolean hasAnswer = !TextUtils.isEmpty(spinnerOtherQuestion.getAnswer());


        if (hasAnswer) {
            int positionOfAnswer = dataAdapter.getPosition(spinnerOtherQuestion.getAnswer());

            spinner.setSelection(positionOfAnswer);

            boolean otherWasSelected = positionOfAnswer == -1;


            if (otherWasSelected) {
                showTextInputLayout();
                textInputLayout.getEditText().setText(spinnerOtherQuestion.getAnswer());
            }
        }

    }

    private void getAnswer(final int pos) {

        if (shouldGetValueFromOther) {
            userSelectedAnswer = textInputLayout.getEditText().getText().toString();
        } else {
            userSelectedAnswer = spinner.getSelectedItem().toString();
        }
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {

        try {

            spinnerOtherQuestion.setAnswer(userSelectedAnswer);

            listener.onAnswerSelected(spinnerOtherQuestion);


        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", spinnerOtherQuestion.getQuestion(), spinnerOtherQuestion.getAnswer());
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


        if (fragmentPositionInViewPager - 1 == spinnerOtherQuestion.getOrder()) {
            getAnswer(spinnerOtherQuestion.getOrder());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Log.d("NIRVANA", position + " ");

        if (position == spinnerOtherQuestion.getDropOptions().size() - 1) {
            setGetValueFromOther();
            showTextInputLayout();
        } else {
            hideTextInputLayout();
            setGetValueFromSpinner();
        }
    }

    private void setGetValueFromOther() {
        shouldGetValueFromOther = true;
    }

    private void setGetValueFromSpinner() {
        shouldGetValueFromOther = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void fragmentIsVisible() {
        allowViewPagerSwipeListener.stopViewpagerScroll(false);
    }


    /**
     * https://stackoverflow.com/a/13528576/4179914
     * stops spinner at automatially selecting to index 0
     * at listner set up
     */
    private void setSpinnerSelectionWithoutCallingListener(final Spinner spinner) {
        spinner.setOnItemSelectedListener(null);
        spinner.post(new Runnable() {
            @Override
            public void run() {

                spinner.post(new Runnable() {
                    @Override
                    public void run() {
                        spinner.setOnItemSelectedListener(SpinnerWithOtherFragment.this);
                    }
                });
            }
        });
    }

}
