package np.com.naxa.simpledynamicforms.form.utils;

/**
 * Created by Nishon Tandukar on 19 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class StringFormatter {

    public static String replaceStringWithUnderScore(String s) {

        return s.replaceAll(" ", "_").toLowerCase();
    }
}
