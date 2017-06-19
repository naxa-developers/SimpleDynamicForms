package np.com.naxa.simpledynamicforms.form.listeners;

import np.com.naxa.simpledynamicforms.model.Form;

/**
 * Created by nishon.tan on 4/21/2017.
 */

public interface onFormFinishedListener {
    void uploadForm();

    void saveForm(Form form);
}
