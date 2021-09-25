package in.cbslgroup.ezeeoffice.Utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import in.cbslgroup.ezeeoffice.Activity.LoginActivity;


public class SessionManager {


    // (make variable public to access from outside)
    public static final String KEY_NAME = "user_name";
    public static final String KEY_EMAIL = "user_email";
    public static final String KEY_CONTACT = "user_contact";
    public static final String KEY_USERID = "user_id";
    public static final String KEY_DESIGNATION = "user_designation";




    // Sharedpref file name
    private static final String PREF_NAME = "UserDetails";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    SharedPreferences sharedPreferences;
    Context context;
    SharedPreferences.Editor editor;


    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = sharedPreferences.edit();
        this.context = context;

    }


    public void createLoginSession(String userid, String name, String email, String contact, String designation) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        //Storing User Details
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_CONTACT, contact);
        editor.putString(KEY_USERID, userid);
        editor.putString(KEY_DESIGNATION, designation);




        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     */

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, "null"));
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, "null"));
        user.put(KEY_CONTACT, sharedPreferences.getString(KEY_CONTACT, "null"));
        user.put(KEY_USERID, sharedPreferences.getString(KEY_USERID, "null"));
        user.put(KEY_DESIGNATION, sharedPreferences.getString(KEY_DESIGNATION, "null"));



        // return user
        return user;
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {

            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(context, LoginActivity.class);

            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            context.startActivity(i);

        }


    }


    /**
     * Clear session details
     */

    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        // Staring Login Activity
        context.startActivity(i);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }


}
