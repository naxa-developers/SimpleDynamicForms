package np.com.naxa.simpledynamicforms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Nishon Tandukar on 21 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class Test implements Serializable {
   private String test;
    private String test2;

    private String A1;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTest2() {
        return test2;
    }

    public void setTest2(String test2) {
        this.test2 = test2;
    }
}
