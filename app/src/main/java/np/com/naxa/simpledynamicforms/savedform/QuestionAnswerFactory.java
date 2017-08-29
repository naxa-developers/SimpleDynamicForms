package np.com.naxa.simpledynamicforms.savedform;

import android.support.annotation.NonNull;

/**
 * Created on 8/29/17
 * by nishon.tan@gmail.com
 */

public class QuestionAnswerFactory {

    public static String QUESTION_TYPE_TEXT = "TEXT";


    public static QuestionAnswer getEditTextQuestion(@NonNull int order,@NonNull String question, @NonNull String hint,@NonNull int InputType, @NonNull boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QUESTION_TYPE_TEXT);
        questionAnswer.setQuestion(question);
        questionAnswer.setHint(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setInputType(InputType);
        questionAnswer.setOrder(order);

        return questionAnswer;
    }
}
