package np.com.naxa.simpledynamicforms.savedform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 8/28/17
 * by nishon.tan@gmail.com
 */

public class Form {


    private List<QuestionAnswer> questionAnswers;
    private String formName;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Form() {
        this.questionAnswers = new ArrayList<>();
    }


    public List<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(ArrayList<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }


    public List<QuestionAnswer> addQuestionAnswer(QuestionAnswer questionAnswer, List<QuestionAnswer> questionAnswers) {


        questionAnswers.add(questionAnswer);
        return questionAnswers;
    }

    public List<QuestionAnswer> setQuestionAnswer(int index, QuestionAnswer questionAnswer, List<QuestionAnswer> questionAnswers) {


        questionAnswers.set(index, questionAnswer);

        return questionAnswers;
    }
}
