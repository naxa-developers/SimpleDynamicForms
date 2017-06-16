package np.com.naxa.simpledynamicforms.uitils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class SnackBarUtils {

    private View view;

    public SnackBarUtils(View view) {
        this.view = view;
    }

    public void showSnackLong(String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

}
