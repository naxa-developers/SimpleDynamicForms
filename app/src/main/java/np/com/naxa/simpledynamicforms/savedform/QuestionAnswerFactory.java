package np.com.naxa.simpledynamicforms.savedform;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created on 8/29/17
 * by nishon.tan@gmail.com
 */

public class QuestionAnswerFactory {


    public final class QuestionType {
        public final static String TEXT = "TEXT";
        public final static String DATETIME = "DATE_TIME";
        public final static String SINGLE_DROPDOWN = "DROP_DOWN";
        public final static String AUTO_COMPLETE_TEXT = "AUTO_COMPLETE_TEXT";
    }


    public static QuestionAnswer getEditTextQuestion(int order, @NonNull String question, @NonNull String hint, @NonNull String answer, @NonNull int InputType, @NonNull boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.TEXT);
        questionAnswer.setQuestion(question);
        questionAnswer.setHint(hint);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setInputType(InputType);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);

        return questionAnswer;
    }

    public static QuestionAnswer getDateTimeQuestion(int order, @NonNull String question, @NonNull String answer, boolean isRequired) {

        //todo loading date in edit is wrong
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.DATETIME);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);

        return questionAnswer;
    }

    public static QuestionAnswer getSpinnerQuestion(int order, @NonNull String question, @NonNull String answer, @NonNull ArrayList<String> dropOptions, boolean isRequired) {


        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.SINGLE_DROPDOWN);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);
        questionAnswer.setDropOptions(dropOptions);

        return questionAnswer;
    }

    public static QuestionAnswer getAutoCompleteTextQuestion(int order, String question, String hint, String answer, ArrayList<String> options, int inputType, boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.AUTO_COMPLETE_TEXT);
        questionAnswer.setQuestion(question);
        questionAnswer.setHint(hint);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setInputType(inputType);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);
        questionAnswer.setDropOptions(options);

        return questionAnswer;
    }
}
