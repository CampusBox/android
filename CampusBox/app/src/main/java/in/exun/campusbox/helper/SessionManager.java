package in.exun.campusbox.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ayush on 24/04/17.
 */

public class SessionManager {
    // Shared preferences file name
    private static final String PREF_LOGIN = "AndroidConstants";
    private static final String KEY_LOGIN = "hasLoggedIn";

    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_LOGIN, PRIVATE_MODE);
        editor = pref.edit();
    }

    /* Executed when a user signs in/out
            token = -1  : Logged out
            token = "_some_string_"  : Logged in
     */
    public void setLoginToken(String token) {

        editor.putString(KEY_LOGIN, token);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login status set to: " + token);
    }

    /* Function to query if a user is logged in
            token = -1  : Logged out
            token = "_some_string_"  : Logged in
     */
    public String hasLoggedIn(){
        return pref.getString(KEY_LOGIN, "-1");
    }
}
