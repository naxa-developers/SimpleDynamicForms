package np.com.naxa.simpledynamicforms;

/**
 * nishon.tan@gmail.com
 */

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Dump {

    private String TAG;


    public Dump(String TAG, Bundle bundle) {
        this.TAG = TAG;
        printBundle(bundle);
    }


    public String getTag() {
        return TAG;
    }


    /**
     * prints all the attributes of any object
     *
     * @param someObject
     */
    public static void object(String TAG, @NonNull Object someObject) {


        for (Field field : someObject.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            Object value = null;

            try {
                value = field.get(someObject);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (value != null) {
                Log.i(TAG, field.getName() + " = " + value);
            } else if (value == null) {
                Log.i(TAG, field.getName() + " = " + "null");
            }
        }

        printLine(TAG);
    }


    private static void printLine(String TAG) {
        Log.i(TAG, "-------------------------------------------------------");
    }


    /**
     * Prints all the attributes of a hashmap
     */

    private static void printHashMap(String TAG, @NonNull Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            Log.e(TAG, key + " = " + value);
        }

        printLine(TAG);
    }


    /**
     * prints all the attributes of an objects in a arraylist
     *
     * @param someList
     */

    private static void printArrayList(String TAG, @NonNull ArrayList<Object> someList) {
        for (Object someObject : someList) {
            for (Field field : someObject.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = null;

                try {
                    value = field.get(someObject);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                if (value != null) {
                    Log.e(TAG, field.getName() + " = " + value);
                }

            }
        }

        printLine(TAG);
    }

    /**
     * Prints all the values and keys of a contentvalue
     *
     * @param vals
     */
    public static void contentValue(String TAG, ContentValues vals) {


        Set<Map.Entry<String, Object>> s = vals.valueSet();
        Iterator itr = s.iterator();

        Log.d(TAG, "ContentValue Length :: " + vals.size());

        while (itr.hasNext()) {
            Map.Entry me = (Map.Entry) itr.next();
            String key = me.getKey().toString();
            Object value = me.getValue();

            Log.i(TAG, "Key:" + key + ", value:" + (String) (value == null ? null : value.toString()));
        }

        printLine(TAG);
    }


    /**
     * @param filename
     */
    private static void printXml(String TAG, File filename) {

        FileReader fileReader;

        try {
            fileReader = new FileReader(filename);
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            StringBuilder sb = new StringBuilder();

            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line.trim());
                    Log.e(TAG, sb.toString());
                }
            } catch (IOException e) {

                Log.e(TAG, e.toString());
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();

        }

        printLine(TAG);
    }

    private String printBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }


    private static String generateTAG() {
        String callerClassName = new Exception().getStackTrace()[1].getClassName();
        String calleeClassName = new Exception().getStackTrace()[0].getClassName();
        return callerClassName;
    }

}
