package np.com.naxa.simpledynamicforms.form;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class JSONAnswerBuilder {

    private JSONObject header;

    public JSONAnswerBuilder() {
        startFormFilling();
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

            Timber.e("Error - While Adding Answer to JSON \n %s", e);

            e.printStackTrace();
        }
    }

    public String finalizeAnswers() {
        try {
            header.put("token", "534545sDfkjHuy589io8gj983jtdfkjj&ihs@->89<-ioj389OiJijor9834%67");
        } catch (JSONException e) {
            Timber.e("Error - While Adding Answer to JSON \n %s", e);
            e.printStackTrace();
        }

        return header.toString();

    }
}
