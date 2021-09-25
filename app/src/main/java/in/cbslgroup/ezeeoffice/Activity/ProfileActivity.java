package in.cbslgroup.ezeeoffice.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class ProfileActivity extends AppCompatActivity {

    SessionManager session;
    TextView tvUsernameHeading, tvDesignationHeading, tvUsername, tvDesignation, tvContact, tvEmail, tvUserRole;

    TextInputEditText tieFirstname, tieLastname, tieContact, tieEmail, tieDesignation, tiePassword, tieRetypePassword, tieOldPassword;
    TextInputLayout tilFirstname, tilLastname, tilContact, tilEmail, tilDesignation, tilPassword, tilRetypePassword, tilOldPassword;


    ImageView ivLogo;


    View dialogView;
    AlertDialog alertDialog;
    ProgressBar progressbar;
    LinearLayout ll_profile_main;
    private String username, designation, contact, email, firstname, lastname, userid, agrid;

    private String newPassword, confirmPassword, oldPassword;

    CardView cvProfilemain;
    AppBarLayout appbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        CollapsingToolbarLayout c = findViewById(R.id.toolbar_layout);
        appbar = findViewById(R.id.app_bar);
        toolbar.setTitle("");


        //intializing the views

        initViews();


        // Session Manager
        session = new SessionManager(getApplicationContext());

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name
        username = user.get(SessionManager.KEY_NAME);
        // email
        email = user.get(SessionManager.KEY_EMAIL);
        // contact
        contact = user.get(SessionManager.KEY_CONTACT);
        // designation
        designation = user.get(SessionManager.KEY_DESIGNATION);

        //userid

        userid = user.get(SessionManager.KEY_USERID);


        firstname = username.substring(0, username.indexOf(" "));
        lastname = username.substring(username.indexOf(" "));
        tvUsername.setText(username);
        tvContact.setText(contact);
        tvEmail.setText(email);
        tvDesignation.setText(designation);
     /*   tvUsernameHeading.setText(username);
        tvDesignationHeading.setText(designation);*/
//scroll|exitUntilCollapsed


        c.setExpandedTitleTypeface(Typeface.create(c.getExpandedTitleTypeface(), Typeface.BOLD));
        c.setTitle(username);


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isVisible = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
               if (scrollRange + verticalOffset == 0) {

                    c.setTitleEnabled(false);
                    toolbar.setTitle("Profile");
                    isVisible = true;
                } else if (isVisible) {
                    c.setTitleEnabled(true);
                    toolbar.setTitle("");
                    isVisible = false;

                }
            }
        });


        getUserRole(userid);

    }


    void initViews() {

        /*tvDesignationHeading = findViewById(R.id.tv_profile_heading_designation);
        tvUsernameHeading = findViewById(R.id.tv_profile_heading_username);*/
        tvEmail = findViewById(R.id.tv_profile_email);
        tvContact = findViewById(R.id.tv_profile_contact);
        tvDesignation = findViewById(R.id.tv_profile_designation);
        tvUsername = findViewById(R.id.tv_profile_username);
        ivLogo = findViewById(R.id.iv_profile_pic);
        tvUserRole = findViewById(R.id.tv_profile_userrole);
        progressbar = findViewById(R.id.progressBar_profile);
        progressbar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        ll_profile_main = findViewById(R.id.ll_profile_main);
        cvProfilemain = findViewById(R.id.cv_profile_info_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            editProfileForm();


            return true;
        }

        if (id == R.id.action_change_password) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            changePasswordForm();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void editProfileForm() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alertdialog_edit_profile, null);

        tieFirstname = dialogView.findViewById(R.id.tie_edit_profile_firstname);
        tieLastname = dialogView.findViewById(R.id.tie_edit_profile_lastname);
        tieContact = dialogView.findViewById(R.id.tie_edit_profile_contact);
        tieDesignation = dialogView.findViewById(R.id.tie_edit_profile_designation);
        tieEmail = dialogView.findViewById(R.id.tie_edit_profile_email);


        tilFirstname = dialogView.findViewById(R.id.til_edit_profile_firstname);
        tilLastname = dialogView.findViewById(R.id.til_edit_profile_lastname);
        tilContact = dialogView.findViewById(R.id.til_edit_profile_contact);
        tilEmail = dialogView.findViewById(R.id.til_edit_profile_email);
        tilDesignation = dialogView.findViewById(R.id.til_edit_profile_designation);


        String username = tvUsername.getText().toString();
        String firstname = username.substring(0, username.lastIndexOf(" "));
        String lastname = username.substring(username.lastIndexOf(" ") + 1);

        tieEmail.setText(tvEmail.getText().toString());
        tieDesignation.setText(tvDesignation.getText().toString());
        tieContact.setText(tvContact.getText().toString());
        tieFirstname.setText(firstname);
        tieLastname.setText(lastname);


        Button btn_cancel = dialogView.findViewById(R.id.btn_edit_profile_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        Button btn_save = dialogView.findViewById(R.id.btn_edit_profile_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tilFirstname.setError(null);
                tilLastname.setError(null);
                tilContact.setError(null);
                tilDesignation.setError(null);
                tilEmail.setError(null);
                tilEmail.setErrorEnabled(false);
                tilLastname.setErrorEnabled(false);
                tilContact.setErrorEnabled(false);
                tilDesignation.setErrorEnabled(false);
                tilEmail.setErrorEnabled(false);


                if (TextUtils.isEmpty(tieFirstname.getText().toString().trim()) || tieFirstname.getText().toString().trim().equals("")) {


                    tilFirstname.setError("Enter First Name");


                } else if (TextUtils.isEmpty(tieLastname.getText().toString().trim()) || tieLastname.getText().toString().trim().equals("")) {

                    tilLastname.setError("Enter Last Name");


                } else if (TextUtils.isEmpty(tieContact.getText().toString().trim()) || tieContact.getText().toString().trim().equals("")) {

                    tilContact.setError("Enter Contact");


                } else if (tieContact.getText().toString().trim().length() != 10) {

                    tilContact.setError("Enter a valid contact number");

                }

                else if (TextUtils.isEmpty(tieEmail.getText().toString().trim()) || tieEmail.getText().toString().trim().equals("")) {

                    tilEmail.setError("Enter Email");


                }

                else if (TextUtils.isEmpty(tieDesignation.getText().toString().trim()) || tieDesignation.getText().toString().trim().equals("")) {

                    tilDesignation.setError("Enter Designation");


                } else {


                    tilFirstname.setError(null);
                    tilLastname.setError(null);
                    tilContact.setError(null);
                    tilDesignation.setError(null);
                    tilEmail.setError(null);
                    tilEmail.setErrorEnabled(false);
                    tilLastname.setErrorEnabled(false);
                    tilContact.setErrorEnabled(false);
                    tilDesignation.setErrorEnabled(false);
                    tilEmail.setErrorEnabled(false);

                    String fname = tieFirstname.getText().toString().trim();
                    String lname = tieLastname.getText().toString().trim();
                    String phn = tieContact.getText().toString().trim();
                    String desg = tieDesignation.getText().toString().trim();
                    String email = tieEmail.getText().toString().trim();
                    String ip = getDeviceIpAddress();
                    String fullname = fname + " " + lname;


                    String log = checkUpdateProfile(fullname, email, phn, desg);

                    updateProfile(userid, fname, lname, email, phn, desg, ip, log);


                    alertDialog.dismiss();


                  /*  AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                    LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_success, null);
                    TextView tv_error_heading = dialogView.findViewById(R.id.tv_alert_success_heading);
                    tv_error_heading.setText("Success");
                    TextView tv_error_subheading = dialogView.findViewById(R.id.tv_alert_success_subheading);
                    tv_error_subheading.setText("Working");
                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_alert_success);
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialogSuccess.dismiss();
                            alertDialog.dismiss();




                        }
                    });

                    dialogBuilder.setView(dialogView);
                    alertDialogSuccess = dialogBuilder.create();
                    alertDialogSuccess.setCancelable(false);
                    alertDialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialogSuccess.show();*/


                }


            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();


    }


    void changePasswordForm() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater = ProfileActivity.this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.alertdialog_change_password, null);

        tieOldPassword = dialogView.findViewById(R.id.tie_edit_profile_old_password);
        tilOldPassword = dialogView.findViewById(R.id.til_edit_profile_old_password);

        tiePassword = dialogView.findViewById(R.id.tie_edit_profile_new_password);
        tieRetypePassword = dialogView.findViewById(R.id.tie_edit_profile_retype_password);

        tilPassword = dialogView.findViewById(R.id.til_edit_profile_new_password);
        tilRetypePassword = dialogView.findViewById(R.id.til_edit_profile_retype_password);

        Button btn_save = dialogView.findViewById(R.id.btn_change_password_save);

        Button btn_cancel = dialogView.findViewById(R.id.btn_change_password_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        tiePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Pattern UpperCasePattern = Pattern.compile("[A-Z ]");
                Pattern lowerCasePattern = Pattern.compile("[a-z ]");
                Pattern digitCasePattern = Pattern.compile("[0-9 ]");

                newPassword = s.toString();

                tilPassword.setError(null);
                tilPassword.setErrorEnabled(false);

                if (newPassword.equalsIgnoreCase("") || newPassword.isEmpty()) {

                    tilPassword.setError("Please Enter New Password");
                    btn_save.setEnabled(false);
                    tieRetypePassword.setEnabled(false);

                } else {

                    tilPassword.setError(null);
                    tilPassword.setErrorEnabled(false);

                    if (newPassword.length() < 8) {


                        tilPassword.setError("Password lenght must have atleast 8 character");
                        btn_save.setEnabled(false);
                        tieRetypePassword.setEnabled(false);


                    } else {

                        if (!specialCharPattern.matcher(newPassword).find()) {

                            tilPassword.setError("Password must have atleast one special character");
                            btn_save.setEnabled(false);
                            tieRetypePassword.setEnabled(false);

                        } else {

                            tilPassword.setErrorEnabled(false);
                            tilPassword.setError(null);

                            if (!UpperCasePattern.matcher(newPassword).find()) {


                                tilPassword.setError("Password must have atleast one uppercase character");
                                btn_save.setEnabled(false);
                                tieRetypePassword.setEnabled(false);

                            } else {

                                tilPassword.setErrorEnabled(false);
                                tilPassword.setError(null);

                                if (!lowerCasePattern.matcher(newPassword).find()) {

                                    tilPassword.setError("Password must have atleast one lowercase character");
                                    btn_save.setEnabled(false);
                                    tieRetypePassword.setEnabled(false);


                                } else {

                                    tilPassword.setErrorEnabled(false);
                                    tilPassword.setError(null);

                                    if (!digitCasePattern.matcher(newPassword).find()) {

                                        tilPassword.setError("Password must have atleast one digit character");
                                        btn_save.setEnabled(false);
                                        tieRetypePassword.setEnabled(false);


                                    } else {

                                        tilPassword.setErrorEnabled(false);
                                        tilPassword.setError(null);

                                        btn_save.setEnabled(true);
                                        tieRetypePassword.setEnabled(true);
                                        tieRetypePassword.setHintTextColor(getResources().getColor(R.color.colorPrimary));
                                        //changePasswordRequest(userid, confirmPassword, name, getDeviceIpAddress());


                                    }


                                }


                            }


                        }


                    }

                }

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tieRetypePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                confirmPassword = s.toString();


                tilRetypePassword.setError(null);
                tilRetypePassword.setErrorEnabled(false);


                if (confirmPassword.length() == 0) {

                    tilRetypePassword.setError(" Please enter confirm password");
                } else if (!confirmPassword.equals(newPassword)) {

                    tilRetypePassword.setError("New password and confirm password does not match");


                } else {

                    tilRetypePassword.setError(null);
                    tilRetypePassword.setErrorEnabled(false);


                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newPassword = tiePassword.getText().toString().trim();
                confirmPassword = tieRetypePassword.getText().toString().trim();
                oldPassword = tieOldPassword.getText().toString().trim();

                tilPassword.setError(null);
                tilRetypePassword.setError(null);
                tilOldPassword.setError(null);
                tilPassword.setErrorEnabled(false);
                tilRetypePassword.setErrorEnabled(false);
                tilOldPassword.setErrorEnabled(false);

                if (oldPassword.isEmpty() || oldPassword.equals("")) {

                    tilOldPassword.setError("Please Enter Old Password");

                } else if (newPassword.isEmpty() || newPassword.equals("")) {


                    tilPassword.setError("Please Enter New Password");

                } else if (confirmPassword.isEmpty() || confirmPassword.equals("")) {

                    tilRetypePassword.setError("Please Enter confirm Password");

                } else if (!confirmPassword.equals(newPassword)) {

                    tilRetypePassword.setError("New password and confirm password does not match");

                } else if (!newPassword.equals(confirmPassword)) {

                    tilRetypePassword.setError("New password and confirm password does not match");

                } else {

                    tilPassword.setError(null);
                    tilRetypePassword.setError(null);
                    tilOldPassword.setError(null);
                    tilPassword.setErrorEnabled(false);
                    tilRetypePassword.setErrorEnabled(false);
                    tilOldPassword.setErrorEnabled(false);


                    String name = tvUsername.getText().toString().trim();

                    Log.e("confirmpass", confirmPassword);

                    changePasswordRequest(userid, confirmPassword, oldPassword, name, getDeviceIpAddress());

                }


            }
        });

        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    private JSONObject makeJsonObject(ArrayList<String> metaData, ArrayList<String> condition, ArrayList<String> text,
                                      int formcount) throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();

        for (int i = 0; i < formcount; i++) {

            obj = new JSONObject();

            try {
                obj.put("metadata", metaData.get(i));
                obj.put("cond", condition.get(i));
                obj.put("searchText", text.get(i));

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("multiMetaSearch", jsonArray1);
        return finalobject;
    }


    void getUserRole(final String userid) {

        cvProfilemain.setVisibility(View.GONE);
        progressbar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String userrole = jsonObject.getString("user_role");
                    tvUserRole.setText(userrole);

                    getProfilePic(userid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("roles", "user_role");
                params.put("action", "getSpecificRoles");

                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }

    void getProfilePic(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String profilepic = jsonObject.getString("profilepic");

                    if (profilepic.isEmpty()) {

                        ivLogo.setImageResource(R.drawable.ic_person_white_192dp);
                        ivLogo.setScaleType(ImageView.ScaleType.CENTER);

                    } else {
                        byte[] decodedString = Base64.decode(profilepic, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        ivLogo.setImageBitmap(decodedByte);
                        ivLogo.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    appbar.setExpanded(true);
                    progressbar.setVisibility(View.GONE);
                    cvProfilemain.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }

    void updateProfile(final String userid, final String firstname, final String lastname, final String email, final String phone, final String designation, final String ip, final String log) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile edit", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equals("false")) {


                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();

                        setUserDetails(userid);


                    } else {

                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("fname", firstname);
                params.put("lname", lastname);
                params.put("ip", ip);
                params.put("email", email);
                params.put("phn", phone);
                params.put("desg", designation);
                params.put("log", log);


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }

    void changePasswordRequest(final String userid, final String password, final String oldPassword, final String name, final String ip) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.EDIT_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response changepwd ", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");
                    if (error.equals("false")) {


                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


                    } else {

                        alertDialog.dismiss();
                        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("pwd", password);
                params.put("name", name);
                params.put("ip", ip);
                params.put("user_agr_id", agrid);
                params.put("oldpwd", oldPassword);


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }


    void setUserDetails(final String userid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("userdetails profileAct", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String firstname = jsonObject.getString("firstname");
                    String lastname = jsonObject.getString("lastname");
                    String email = jsonObject.getString("email");
                    String designation = jsonObject.getString("designation");
                    String contact = jsonObject.getString("contact");

                    tvUsername.setText(firstname + " " + lastname);
                    tvContact.setText(contact);
                    tvDesignation.setText(designation);
                   /* tvDesignationHeading.setText(designation);
                    tvUsernameHeading.setText(firstname + " " + lastname);*/
                    tvEmail.setText(email);


                    SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    session.createLoginSession(userid, firstname + " " + lastname, email, contact, designation);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);


                return params;
            }
        };

        VolleySingelton.getInstance(ProfileActivity.this).addToRequestQueue(stringRequest);


    }

    @NonNull
    private String getDeviceIpAddress() {
        String actualConnectedToNetwork = null;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager != null) {
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) {
                actualConnectedToNetwork = getWifiIp();
            }
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = getNetworkInterfaceIpAddress();
        }
        if (TextUtils.isEmpty(actualConnectedToNetwork)) {
            actualConnectedToNetwork = "127.0.0.1";
        }
        return actualConnectedToNetwork;
    }

    @Nullable
    private String getWifiIp() {
        final WifiManager mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null && mWifiManager.isWifiEnabled()) {
            int ip = mWifiManager.getConnectionInfo().getIpAddress();
            return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                    + ((ip >> 24) & 0xFF);
        }
        return null;
    }


    @Nullable
    public String getNetworkInterfaceIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        String host = inetAddress.getHostAddress();


                        /* Log.e("Hostname",host);*/
                        if (!TextUtils.isEmpty(host)) {
                            return host;
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Log.e("IP Address", "getLocalIpAddress", ex);
        }
        return null;
    }

    String checkUpdateProfile(String name, String eml, String phone, String desig) {


        StringBuilder log = new StringBuilder();


     /*   if(firstname.equals(fname.trim())){

            Log.e("firstname","has not been updated");


        }
        else{

            Log.e("firstname","has been updated");

        }


        if(lastname.equals(lname.trim())){

            Log.e("lastname","has not been updated");


        }*/
      /*  else{

            Log.e("lastname","has been updated");

         }*/
        Log.e("previous details", tvUsername.getText().toString() + " " + tvContact.getText().toString() + " " + tvContact.getText().toString() + " " + " " + tvDesignation.getText().toString());
        Log.e("after details", name + " " + eml + " " + phone + " " + " " + desig);
        if (tvUsername.getText().toString().equals(name)) {

            Log.e("Username", "has not been updated");


        } else {

            Log.e("Username", "has been updated");
            log.append("Username has been updated ");

        }

        if (tvEmail.getText().toString().equals(eml.trim())) {

            Log.e("email", "has not been updated");


        } else {

            Log.e("email", "has been updated");
            log.append("Email has been updated ");
        }

        if (tvContact.getText().toString().equals(phone.trim())) {

            Log.e("contact", "has not been updated");


        } else {

            Log.e("contact", "has been updated");
            log.append("Contact has been updated ");

        }

        if (tvDesignation.getText().toString().equals(desig.trim())) {

            Log.e("designation", "has not been updated");


        } else {

            Log.e("designation", "has been updated");
            log.append("Designation has been updated ");

        }


        Log.e("log profile updated", log.toString());

        if (log.toString().equals("")) {

            return "Nothing is updated ";
        } else {

            return log.toString();
        }

    }


    void checkvalidation(String newPassword, String confirmPassword) {


    }

}
