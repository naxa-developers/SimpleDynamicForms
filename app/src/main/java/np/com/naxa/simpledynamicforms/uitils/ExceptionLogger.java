package np.com.naxa.simpledynamicforms.uitils;

import android.util.Log;

/**
 * A helper class that will handle exceptions in the future.
 */

public class ExceptionLogger {


    public static void Log(String tag, String msg) {
        Log.e(tag, msg);
    }
}
