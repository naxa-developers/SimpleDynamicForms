package np.com.naxa.simpledynamicforms.demo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class JSONFormatter {
    public static String formatString(String text) {

        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n" + indentString + letter + "\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n" + indentString + letter);
                    break;
                case ',':
                    json.append(letter + "\n" + indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }

    public static String formatQuestionAnswer(String formJson) {
        StringBuilder json = new StringBuilder();

        try {


            JSONObject savedFormJson = new JSONObject(formJson);
            JSONArray jsonArray = savedFormJson.getJSONArray("questionAnswers");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                json.append(jsonObject.get("question"));
                json.append(" : ");
                json.append(jsonObject.get("answer"));
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }
}
