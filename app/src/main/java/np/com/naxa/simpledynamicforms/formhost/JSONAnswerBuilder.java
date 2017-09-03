package np.com.naxa.simpledynamicforms.formhost;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import np.com.naxa.simpledynamicforms.Dump;
import np.com.naxa.simpledynamicforms.savedform.Form;
import np.com.naxa.simpledynamicforms.savedform.QuestionAnswer;
import timber.log.Timber;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class JSONAnswerBuilder {


    private JSONObject header;
    private Form filledForm;


    public void startForm() {
        filledForm = new Form();
    }


    public void addAnswer(QuestionAnswer newAnswer) {


//        #REF https://www.leveluplunch.com/java/examples/find-element-in-list/
        Timber.i(" %s save answer in index %s", newAnswer.getAnswer(), newAnswer.getOrder());


        List<QuestionAnswer> commitedQuestionAnswer = getAnswer(newAnswer);

        if (commitedQuestionAnswer.isEmpty()) {
            //does not have any question for that order
            //so just add the object to list

            filledForm.addQuestionAnswer(newAnswer, filledForm.getQuestionAnswers());
        } else {
            QuestionAnswer oldAnswer = commitedQuestionAnswer.get(0);
            filledForm.setQuestionAnswer(oldAnswer.getOrder(), newAnswer, filledForm.getQuestionAnswers());

        }
    }

    private QuestionAnswer removeNullAnswer(QuestionAnswer newAnswer) {

        boolean isAnswerisNull = TextUtils.isEmpty(newAnswer.getAnswer());

        Timber.i(" is answer null %s", isAnswerisNull);

        if (isAnswerisNull) {
            newAnswer.setAnswer("No Answer Provided");

            Dump.object("JSONAnswerBuilder", newAnswer);
        }


        return newAnswer;
    }

    /**
     * Checks if the a answer already exist for a question in the list
     *
     * @return 0 if none exist, should only return 0 or 1;
     */
    @SuppressWarnings("unchecked")
    private List<QuestionAnswer> getAnswer(final QuestionAnswer newAnswer) {
        Predicate condition = new Predicate() {
            public boolean evaluate(Object sample) {
                return ((QuestionAnswer) sample).getQuestion().equals(newAnswer.getQuestion());
            }
        };

        return (List<QuestionAnswer>) CollectionUtils.select(filledForm.getQuestionAnswers(), condition);

    }

    public String getFinalizedForm(String formName) {
        Gson gson = new Gson();
        filledForm.setFormName(formName);
        return gson.toJson(filledForm);

    }


    public void setFilledForm(Form filledForm) {
        this.filledForm = filledForm;
    }

    public JSONAnswerBuilder() {
        startFormFilling();
        startForm();
    }

    private void startFormFilling() {
        header = new JSONObject();

    }

    public void addAnswerToJSON(String questionId, String answer) {

        try {

            if (header.has(questionId)) {
                header.remove(questionId);
            }

            header.put(questionId, answer);
        } catch (JSONException e) {

            Timber.e("Error - While Adding QuestionAnswer to JSON \n %s", e);
            e.printStackTrace();

        }
    }

    public String finalizeAnswers() {

        return header.toString();

    }
}
