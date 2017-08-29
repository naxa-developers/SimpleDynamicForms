package np.com.naxa.simpledynamicforms.savedform;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created on 8/28/17
 * by nishon.tan@gmail.com
 */

public abstract class DropOptions {

    @SerializedName("drop_options")
    private ArrayList<String> DropOptions;

    public ArrayList<String> getDropOptions() {
        return DropOptions;
    }

    public void setDropOptions(ArrayList<String> dropOptions) {
        DropOptions = dropOptions;
    }
}
