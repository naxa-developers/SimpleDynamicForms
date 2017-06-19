package np.com.naxa.simpledynamicforms.form.listeners;

public interface onAnswerSelectedListener {
    void onAnswerSelected(String question, String answer);
    void shoudStopSwipe(boolean shouldStopSwipe);
}
