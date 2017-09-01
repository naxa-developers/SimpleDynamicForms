package np.com.naxa.simpledynamicforms.form.components;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import np.com.naxa.simpledynamicforms.uitils.CalendarUtils;
import np.com.naxa.simpledynamicforms.uitils.TimeUtils;
import timber.log.Timber;


public class DateTimeFragment extends Fragment implements fragmentStateListener, SingleDateAndTimePicker.Listener, onPageVisibleListener {


    @BindView(R.id.tv_question_edit_text)
    TextView tvQuestion;

    @BindView(R.id.fragment_date_time_picker)
    SingleDateAndTimePicker singleDateAndTimePicker;

    private String userSelectedAnswer = "";
    private onAnswerSelectedListener listener;
    private shouldAllowViewPagerSwipeListener allowViewPagerSwipeListener;
    private QuestionAnswer questionAnswer;


    public DateTimeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_date_time_t, container, false);
        ButterKnife.bind(this, rootView);
        setQuestionAndAnswers();
        singleDateAndTimePicker.setListener(this);

        return rootView;
    }


    public void prepareQuestionAndAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;


        Timber.i("Preparing question with question \' %s \' at postion %s", questionAnswer.getQuestion(), questionAnswer.getOrder());
    }


    public void setQuestionAndAnswers() {
        tvQuestion.setText(questionAnswer.getQuestion());

        Date date = TimeUtils.string2Date(questionAnswer.getAnswer());
        Calendar calendar = CalendarUtils.toCalendar(date);

        singleDateAndTimePicker.selectDate(calendar);
    }


    private void getAnswer(final int pos) {
        sendAnswerToActivity(pos);
    }

    private void sendAnswerToActivity(int pos) {

        try {

            questionAnswer.setAnswer(userSelectedAnswer);

            Dump.object("DateTime", questionAnswer);

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


        //making sure the fragment only reports to activity when it is it's turn

        //  index of fragmentPositionInViewPager begins at 1
        // index of order begins at 0

        // substract to match

        if (fragmentPositionInViewPager - 1 == questionAnswer.getOrder()) {
            getAnswer(questionAnswer.getOrder());
        }
    }


    @Override
    public void onDateChanged(String displayed, Date date) {
        userSelectedAnswer = displayed;
    }


    @Override
    public void fragmentIsVisible() {
        allowViewPagerSwipeListener.stopViewpagerScroll(false);
    }
}
