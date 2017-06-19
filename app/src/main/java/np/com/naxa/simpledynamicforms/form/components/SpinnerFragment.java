package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import np.com.naxa.simpledynamicforms.R;
import np.com.naxa.simpledynamicforms.form.listeners.fragmentStateListener;
import np.com.naxa.simpledynamicforms.form.listeners.onAnswerSelectedListener;
import np.com.naxa.simpledynamicforms.form.utils.StringFormatter;
import timber.log.Timber;


public class SpinnerFragment extends Fragment implements fragmentStateListener {


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.spinner_answer_options)
    Spinner optionsSpinner;

    private String userSelectedAnswer = "";
    private String question;
    private ArrayList<String> options;
    private int position;
    private onAnswerSelectedListener listener;


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

    public void prepareQuestionAndAnswer(String question, ArrayList<String> options, int position) {
        this.question = question;
        this.options = options;
        this.position = position;

        Timber.i("Preparing question with question \' %s \' at postion %s", question, position);
    }

    public void setQuestionAndAnswers() {
        tvQuestion.setText(question);
        optionsSpinner.setPrompt(question);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(dataAdapter);
    }

    private void getAnswer(final int pos) {
        userSelectedAnswer = optionsSpinner.getSelectedItem().toString();
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {

        try {
            listener.onAnswerSelected(StringFormatter.replaceStringWithUnderScore(question), userSelectedAnswer);
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
}
