package np.com.naxa.simpledynamicforms.formloader;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import np.com.naxa.simpledynamicforms.uitils.ToastUtils;
import timber.log.Timber;

/**
 * Created by Nishon Tandukar on 25 Jun 2017 .
 *
 * @email nishon.tan@gmail.com
 */

public class AsyncStorageLoader extends AsyncTask<Void, String, JSONObject> {

    private File jsonFile;

    private JSONObject errorJSON;


    public AsyncStorageLoader(File jsonFile) {
        this.jsonFile = jsonFile;
    }


    @Override
    protected JSONObject doInBackground(Void... params) {

        JSONObject formJson = null;
        try {
            formJson = new JSONObject(FileIOUtils.readFile2String(jsonFile));
        } catch (Exception e) {
            e.printStackTrace();
            formJson = createErrorJSON("Failed to load form");
        }


        return formJson;
    }


    @Override
    protected void onPostExecute(JSONObject formJson) {
        super.onPostExecute(formJson);


        Timber.d(formJson.toString());
    }


    private JSONObject createErrorJSON(@NonNull String msg) {

        try {
            errorJSON.put("error", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return errorJSON;
    }
}
