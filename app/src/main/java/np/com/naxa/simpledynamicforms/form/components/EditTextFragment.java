package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.formhost.FormEntryActivity;


public class EditTextFragment extends Fragment implements FormEntryActivity.fragmentStateListener {

    private String userSelectedAnswer = "";
    private EditText etAnswer;
    private onAnswerSelectedListener listener;
    private String question;
    private TextView tvQuestion;
    private String hint;
    private int position;


    public EditTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_edittext, container, false);
        initUI(rootview);
        return rootview;

    }

    private void initUI(View rootview) {
        tvQuestion = (TextView) rootview.findViewById(R.id.tv_question_edit_text);
        etAnswer = (EditText) rootview.findViewById(R.id.tv_answer_edit_text);
        setQuestionAndAnswers();
    }

    public void prepareQuestionAndAnswer(String question, String hint, int position) {
        this.question = question;
        this.hint = hint;
        this.position = position;

        Timber.i("Preparing question with question \' %s \' at postion %s", question, position);
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(question);
        etAnswer.setHint(hint);
    }

    private void getUserAnswer(final int pos) {
        userSelectedAnswer = etAnswer.getText().toString();
        sendAnswerToActvitiy(pos);
    }

    private void sendAnswerToActvitiy(int pos) {
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
            getUserAnswer(position);
        }
    }
}
