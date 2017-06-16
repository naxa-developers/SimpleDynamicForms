package np.com.naxa.simpledynamicforms;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Nishon Tandukar on 16 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class SimpleDynamicForm extends Application {


    private static SimpleDynamicForm simpleDynamicForm;

    @Override
    public void onCreate() {
        super.onCreate();
        simpleDynamicForm = this;
        initDebugThings();
    }

    private void initDebugThings() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }


    public static synchronized SimpleDynamicForm getInstance() {
        return simpleDynamicForm;
    }

}
