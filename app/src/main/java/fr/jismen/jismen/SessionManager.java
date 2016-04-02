package fr.jismen.jismen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Finelam on 27/03/2016.
 */
public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_ID         = "JismenPref";
    private static final int PRIVATE_MODE       = 0;

    public static final String KEY_ID           = "id";
    public static final String KEY_USERNAME     = "username";
    public static final String KEY_NAME         = "name";
    public static final String KEY_FIRSTNAME    = "firstname";
    public static final String KEY_EMAIL        = "email";
    public static final String KEY_ADDRESS      = "address";
    public static final String KEY_PC           = "pc";
    public static final String KEY_CITY         = "city";
    public static final String KEY_BIRTHDAY     = "birthday";
    public static final String KEY_NEWSLETTER   = "newsletter";

    public SessionManager(Context c){
        context = c;
        pref    = context.getSharedPreferences(PREF_ID, PRIVATE_MODE);
        editor  = pref.edit();
    }

    public void createUser(JSONObject jsonUser) throws JSONException{
        editor.putInt(KEY_ID, jsonUser.getInt("id"));
        editor.putString(KEY_USERNAME, jsonUser.getString("username"));
        editor.putString(KEY_NAME, jsonUser.getString("name"));
        editor.putString(KEY_FIRSTNAME, jsonUser.getString("firstname"));
        editor.putString(KEY_EMAIL, jsonUser.getString("email"));
        editor.putString(KEY_ADDRESS, jsonUser.getString("address"));
        editor.putString(KEY_PC, jsonUser.getString("pc"));
        editor.putString(KEY_CITY, jsonUser.getString("city"));
        editor.putString(KEY_BIRTHDAY, jsonUser.getString("birthday"));
        editor.putBoolean(KEY_NEWSLETTER, Boolean.parseBoolean(jsonUser.getString("newsletter")));
        editor.commit();
    }

    public HashMap<String, Object> getUser(){
        HashMap<String, Object> user = new HashMap<>();
        user.put(KEY_ID, pref.getInt(KEY_ID, 0));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, ""));
        user.put(KEY_NAME, pref.getString(KEY_NAME, ""));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_ADDRESS, pref.getString(KEY_ADDRESS, ""));
        user.put(KEY_PC, pref.getString(KEY_PC, ""));
        user.put(KEY_CITY, pref.getString(KEY_CITY, ""));
        user.put(KEY_BIRTHDAY, pref.getString(KEY_BIRTHDAY, ""));
        user.put(KEY_NEWSLETTER, pref.getBoolean(KEY_NEWSLETTER, false));
        return user;
    }

    public void disconnect(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
