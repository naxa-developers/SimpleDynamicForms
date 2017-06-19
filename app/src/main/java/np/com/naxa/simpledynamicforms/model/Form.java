package np.com.naxa.simpledynamicforms.model;

import com.orm.SugarApp;
import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by Nishon Tandukar on 19 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

@Table
public class Form extends SugarRecord {


    private String formName;
    private String filledDateTime;
    private String formJson;

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFilledDateTime() {
        return filledDateTime;
    }

    public void setFilledDateTime(String filledDateTime) {
        this.filledDateTime = filledDateTime;
    }

    public String getFormJson() {
        return formJson;
    }

    public void setFormJson(String formJson) {
        this.formJson = formJson;
    }
}
