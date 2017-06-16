package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import timber.log.Timber;


public class EditTextFragment extends Fragment implements fragmentStateListener {


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.wrapper_answer_edit_text)
    TextInputLayout textInputLayout;

    private String userSelectedAnswer = "";
    private String question;
    private String hint;
    private int position;
    private onAnswerSelectedListener listener;


    public EditTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edittext, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        return rootView;
    }

    public void prepareQuestionAndAnswer(String question, String hint, int position) {
        this.question = question;
        this.hint = hint;
        this.position = position;

        Timber.i("Preparing question with question \' %s \' at postion %s", question, position);
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(question);
        textInputLayout.getEditText().setHint(hint);
    }

    private void getAnswer(final int pos) {
        userSelectedAnswer = textInputLayout.getEditText().getText().toString();
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {
        String questionName = "q" + pos;
        try {
            listener.onAnswerSelected(questionName, userSelectedAnswer);
        } catch (ClassCastException cce) {

            Timber.e(cce.toString());

        }

        Timber.i("Question: %s Answer: %s", question, userSelectedAnswer);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onAnswerSelectedListener) {
            listener = (onAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onAnswerSelectedListener");
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

    private void setErrorMessage(String message) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(message);
        textInputLayout.requestFocus();
    }
}
