package np.com.naxa.simpledynamicforms.form.listeners;

import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;

public interface onAnswerSelectedListener {
    void onAnswerSelected(String question, String answer);
    void shoudStopSwipe(boolean shouldStopSwipe);

    void onAnswerSelected(QuestionAnswer questionAnswer);
}
