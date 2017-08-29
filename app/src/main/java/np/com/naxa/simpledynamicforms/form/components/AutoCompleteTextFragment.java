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
import timber.log.Timber;


public class AutoCompleteTextFragment extends Fragment implements fragmentStateListener, onPageVisibleListener {


    @BindView(R.id.answer_edit_text)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;


        String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear"};



        private String userSelectedAnswer = "";
    private String question;
    private String hint;
    private int inputType;
    private int position;
    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private boolean shouldStopWipe = false;
    private boolean validationRequired;
    private ArrayList<String> options;


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

    public void prepareQuestionAndAnswer(String question, String hint, ArrayList<String> options, int inputType, int position) {
        this.question = question;
        this.options = options;
        this.hint = hint;
        this.position = position;
        this.position = position;
        Timber.i("Preparing question with question \' %s \' at postion %s", question, position);
    }



    public void setQuestionAndAnswers() {
        tvQuestion.setText(question);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, options);
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
            listener.onAnswerSelected(StringFormatter.replaceStringWithUnderScore(question), userSelectedAnswer);
            listener.shoudStopSwipe(shouldStopWipe);
        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s QuestionAnswer: %s", question, userSelectedAnswer);
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

        Timber.d("Asking Fragment At Postion %s for answer for the question ", fragmentPositionInViewPager);

        Boolean doFragmentIdMatch = fragmentPositionInViewPager == position;

        Timber.d(" %s and %s are the same ? %s \n question: %s", fragmentPositionInViewPager, position, doFragmentIdMatch.toString(), question);

        if (fragmentPositionInViewPager == position) {
            getAnswer(position);
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
