package np.com.naxa.simpledynamicforms.savedform;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created on 8/29/17
 * by nishon.tan@gmail.com
 */

public class QuestionFactory {



    public final class QuestionType {
        public final static String TEXT = "TEXT";
        public final static String DATETIME = "DATE_TIME";
        public final static String DROPDOWN_SINGLE = "DROP_DOWN";
        public final static String AUTO_COMPLETE_TEXT = "AUTO_COMPLETE_TEXT";
        public final static String LOCATION = "LOCATION";
        public static final String PHOTO = "PHOTO";
        public static final String DROPDOWN_WITH_OTHER = "DROPDOWN_WITH_OTHER";
        public static final String DROPDOWN_MULTI_SELECT = "DROPDOWN_MULTI_SELECT";

    }


    public static QuestionAnswer getText(int order, @NonNull String question, @NonNull String hint, @NonNull String answer, @NonNull int InputType, @NonNull boolean isRequired) {
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

    public static QuestionAnswer getDateTime(int order, @NonNull String question, @NonNull String answer, boolean isRequired) {

        //todo loading date in edit is wrong
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.DATETIME);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);

        return questionAnswer;
    }

    public static QuestionAnswer getSpinner(int order, @NonNull String question, @NonNull String answer, @NonNull ArrayList<String> dropOptions, boolean isRequired) {


        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.DROPDOWN_SINGLE);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);
        questionAnswer.setDropOptions(dropOptions);

        return questionAnswer;
    }

    public static QuestionAnswer getAutoCompleteText(int order, String question, String hint, String answer, ArrayList<String> options, int inputType, boolean isRequired) {
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

    public static QuestionAnswer getLocation(int order, String question, String answer, boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.LOCATION);
        questionAnswer.setQuestion(question);

        questionAnswer.setRequired(isRequired);

        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);


        return questionAnswer;
    }


    public static QuestionAnswer getPhoto(int order, String question, String answer, Boolean isRequired) {

        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.PHOTO);
        questionAnswer.setQuestion(question);

        questionAnswer.setRequired(isRequired);

        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);


        return questionAnswer;
    }


    public static QuestionAnswer getSpinnerWithOther(int order, String question, String answer, ArrayList<String> dropDownOptions, Boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.DROPDOWN_SINGLE);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);
        questionAnswer.setDropOptions(dropDownOptions);

        return questionAnswer;

    }


    public static QuestionAnswer getSpinnerMultiSelect(int order, String question, String answer, ArrayList<String> dropDownOptions, Boolean isRequired) {
        QuestionAnswer questionAnswer = new QuestionAnswer();

        questionAnswer.setQuestionType(QuestionType.DROPDOWN_MULTI_SELECT);
        questionAnswer.setQuestion(question);
        questionAnswer.setRequired(isRequired);
        questionAnswer.setOrder(order);
        questionAnswer.setAnswer(answer);
        questionAnswer.setDropOptions(dropDownOptions);

        return questionAnswer;
    }



}
