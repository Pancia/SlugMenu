package com.dayzerostudio.utils.storage.shrdprfs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyShrdPrfs {

    public static SharedPreferences myShrdPrfs;

    public static void init(Context context) {
        myShrdPrfs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveObject(String key, Object value) {
        if (value == null)
            return;
        SharedPreferences.Editor editor = myShrdPrfs.edit();
        try {
            String methodName = value.getClass().getSimpleName();
            methodName = "put" + Character.toUpperCase(methodName.charAt(0)) + methodName.substring(1);
            Method[] ms = editor.getClass().getDeclaredMethods();
            for (Method m : ms) {
                if (m.getName().equals(methodName)) {
                    m.invoke(editor, key, value);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        editor.commit();
    }

    public static void reset() {
        myShrdPrfs.edit().clear().commit();
    }

}
