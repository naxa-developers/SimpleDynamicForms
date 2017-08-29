package np.com.naxa.simpledynamicforms.savedform;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 8/28/17
 * by nishon.tan@gmail.com
 */

public class QuestionAnswer extends DropOptions {


    private int order;

    @SerializedName("question")
    private String question;
    @SerializedName("answer")
    private String answer;

    @SerializedName("question_type")
    private String questionType;

    @SerializedName("hint")
    private String hint;

    @SerializedName("answer_text_type")
    private int inputType;

    @SerializedName("is_required")
    private boolean isRequired = false;

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getHint() {
        return hint;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public void setDropOptions(ArrayList<String> dropOptions) {
        super.setDropOptions(dropOptions);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }


}
