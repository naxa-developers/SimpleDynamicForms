package np.com.naxa.simpledynamicforms.formhost;


import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

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

    @SuppressWarnings("unchecked")
    public void addAnswer(final QuestionAnswer newAnswer) {

//        #REF https://www.leveluplunch.com/java/examples/find-element-in-list/

        Predicate condition = new Predicate() {
            public boolean evaluate(Object sample) {
                return ((QuestionAnswer) sample).getQuestion().equals(newAnswer.getQuestion());
            }
        };

        List<QuestionAnswer> commitedQuestionAnswer = (List<QuestionAnswer>) CollectionUtils.select(filledForm.getQuestionAnswers(), condition);




        if (commitedQuestionAnswer.isEmpty()) {



            //does not have any question for that order
            //so just add the object to list

            filledForm.addQuestionAnswer(newAnswer, filledForm.getQuestionAnswers());
        } else {

            QuestionAnswer oldAnswer = commitedQuestionAnswer.get(0);
            filledForm.setQuestionAnswer(oldAnswer.getOrder(),newAnswer, filledForm.getQuestionAnswers());

        }


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
