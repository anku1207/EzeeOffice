package in.cbslgroup.ezeeoffice.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.Password.ForgotPasswordActivity;
import in.cbslgroup.ezeeoffice.Notifications.FirebaseMessagingService;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class LoginActivity extends AppCompatActivity {

    String username, password;
    AlertDialog alertDialog;
    SessionManager session;
    String tokenid = "";
    HurlStack mStack;
    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText tieUsername, tiePassword;
    private Button btnLogin;
    private ProgressDialog progress;
    private TextView tvForgotPwd;

    int counterLoginAttempts = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //intializing Views
        initViews();


        // Session Manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        username = String.valueOf(tieUsername.getText());
        password = String.valueOf(tiePassword.getText());

        tvForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

                finish();


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {

                    username = String.valueOf(tieUsername.getText());
                    password = String.valueOf(tiePassword.getText());


                    if (TextUtils.isEmpty(username)) {

                        tilUsername.setError("Enter Username");

                    } else if (TextUtils.isEmpty(password)) {

                        tilPassword.setError("Enter Password");

                    } else {


                        tilPassword.setErrorEnabled(false);
                        tilUsername.setErrorEnabled(false);
                        tilUsername.setError(null);
                        tilPassword.setError(null);

                        Log.e("username", username);
                        Log.e("password", password);


                        //Login with username and password
                        login(username, password);


                        //new Logintask().execute();


                      /*  FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( LoginActivity.this,  new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                String token = instanceIdResult.getToken();
                                Log.e("FCM_TOKEN",token);



                            }
                        });*/


                    }


                } else {


                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                    LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.alertdialog_error, null);

                    Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                    TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                    tv_error_heading.setText("Error");
                    TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                    tv_error_subheading.setText("No internet connection found kindly check your internet connection and try again.");
                    btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                        }
                    });

                    dialogBuilder.setView(dialogView);

                    alertDialog = dialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    alertDialog.show();


                }

            }
        });


    }

    void initViews() {

        //Textinputlayouts
        tilUsername = findViewById(R.id.til_login_username);
        tilPassword = findViewById(R.id.til_login_password);

        //TextInputEditexts
        tieUsername = findViewById(R.id.tie_login_username);
        tiePassword = findViewById(R.id.tie_login_password);

        btnLogin = findViewById(R.id.btn_login);

        progress = new ProgressDialog(this);

        tvForgotPwd = findViewById(R.id.tv_login_forgot_pass);


    }

    public void login(final String username, final String password) {

        progress.setTitle("Logging In");
        progress.setMessage("Please wait");
        progress.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Login response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");
                    String action = jsonObject.getString("action");


                    if (error.equalsIgnoreCase("false")) {


                        String firstname = jsonObject.getString("FirstName");
                        String lastname = jsonObject.getString("LastName");
                        String email = jsonObject.getString("Email");
                        String userid = jsonObject.getString("userID");
                        String designation = jsonObject.getString("Designation");
                        String phonenumber = jsonObject.getString("Phone");


                        // String fullname = firstname + " " + lastname;

                        Log.e("------", "------");
                        Log.e("firstname", firstname);
                        Log.e("lastname", lastname);
                        Log.e("email", email);
                        Log.e("userid", userid);
                        Log.e("designation", designation);
                        Log.e("phonenumber", phonenumber);


                        //session.createLoginSession(userid, fullname, email, phonenumber, designation);


                        //Redirecting  to DrawerActivity
                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                        intent.putExtra("firstname", firstname);
                        intent.putExtra("lastname", lastname);
                        intent.putExtra("email", email);
                        intent.putExtra("userid", userid);
                        intent.putExtra("designation", designation);
                        intent.putExtra("phonenumber", phonenumber);
                        startActivity(intent);
                        finish();


                        progress.dismiss();


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("active_deactivate")) {


                        progress.dismiss();

                        //Toast.makeText(LoginActivity.this, "Username password dont match", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText("Login Failed");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(msg);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("login")) {

                        progress.dismiss();

                        String login_attempts = jsonObject.getString("login_attempts");

                        //int leftAttempts = 5 - (counterLoginAttempts);

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);

                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText("Login Failed");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText("You have " + login_attempts + " login attempts left otherwise account will be deactivated.");
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.setCancelable(false);
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                       // counterLoginAttempts++;

//                        if (counterLoginAttempts > 4) {
//
//                            //userdeactivate method
//                            deactivateUser(username);
//
//
//
//                        } else {
//
//
//
//
//
//                        }


                    } else if (error.equalsIgnoreCase("true") && action.equalsIgnoreCase("invalid_email")) {

                        progress.dismiss();

                        //Toast.makeText(LoginActivity.this, "Username password dont match", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
                        tv_error_heading.setText("Login Failed");
                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
                        tv_error_subheading.setText(msg);
                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();

                            }
                        });

                        dialogBuilder.setView(dialogView);

                        alertDialog = dialogBuilder.create();
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        alertDialog.show();

                    }

                    else {

                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                String tokenid = FirebaseMessagingService.getToken(LoginActivity.this);
                Log.e("token id in login", tokenid);

                params.put("name", username);
                params.put("pwd", password);
                params.put("ip", getDeviceIpAddress());

                return params;
            }

        };







      /*  RequestQueue queue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
        queue.add(stringRequest);*/

        VolleySingelton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);


    }

//    private void deactivateUser(String username) {
//
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.LOGIN_URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String error = jsonObject.getString("error");
//                    String msg = jsonObject.getString("msg");
//
//                    if (error.equalsIgnoreCase("false")) {
//
//                        progress.dismiss();
//
//                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//                        //counterLoginAttempts = 1;
//
//
//
//                    } else if (error.equalsIgnoreCase("true")) {
//
//                        progress.dismiss();
//
//                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
//                        LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
//                        View dialogView = inflater.inflate(R.layout.alertdialog_error, null);
//                        TextView tv_error_heading = dialogView.findViewById(R.id.tv_Error_heading);
//                        tv_error_heading.setText("Login Failed");
//                        TextView tv_error_subheading = dialogView.findViewById(R.id.tv_Error_subHeading);
//                        tv_error_subheading.setText(msg);
//                        Button btn_cancel_ok = dialogView.findViewById(R.id.btn_login_dialog_error_ok);
//                        btn_cancel_ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                alertDialog.dismiss();
//
//                            }
//                        });
//
//                        dialogBuilder.setView(dialogView);
//
//                        alertDialog = dialogBuilder.create();
//                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//                        alertDialog.show();
//
//                        //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
//
//
//
//                    } else {
//
//                        Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//
//                Map<String, String> params = new HashMap<>();
//                params.put("user_email", username);
//
//                Util.printParams(params,"user_deac_params");
//
//                return params;
//
//
//            }
//        };
//
//        VolleySingelton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
//
//    }

    @Override
    public void onBackPressed() {

        //bug on back goes to mainactivity

        // super.onBackPressed();

        finishAffinity();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    void updateToken(String userid, String tokenid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPDATE_FIREBASE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("updatefbtoken", response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("tokenid", tokenid);


                return params;
            }
        };

        VolleySingelton.getInstance(this).addToRequestQueue(stringRequest);


    }





   /* private String httpConnectionPost(String sUrl, String data) {


        String result = "";

        try {


             //listen i am giving the api link and


            CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");
            if (getApplicationContext() != null) {
                AssetManager assManager = getAssets();
                InputStream is = null;
                try {
                    is = assManager.open("certi.crt");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                InputStream caInput = new BufferedInputStream(is);
                Certificate ca;
                try {
                    ca = cf.generateCertificate(caInput);
                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
                } finally {
                    caInput.close();
                }

               // not getting the exact issue
                //m getting ssl error if i cal in postman wait ill show u
                //open paint in ur system this is ubuntu no paint ek sec ruko
                //app mail krdo if possible
                //app i cant mail, but i doubt there is issue with certificate, and in postman also it asks fr certificte
                //can u send me the postman snapshot mail itsennnnnnnnnnnnnnnnnnnnnnnnnnnnnnt check it
                //pwd =1 hatana zara link se
                //only id and name paas krna hay kya?
                //nahi nahi url mai pwd =1 ja rha hai it is a post req right ?
                //i removed , m getting same error okay

                // wait copy the certificate and try
                //kiya copy

                String keyStoreType = KeyStore.getDefaultType();
                KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(null, null);
                keyStore.setCertificateEntry("ca", ca);

                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(keyStore);

                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, tmf.getTrustManagers(), null);

                 //ssl socket factory apply nhi kia may be jabhi error hai but meth

                URL url = new URL(sUrl);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory()); //gya error debubug kru ?
                conn.setReadTimeout(30000);
                conn.setConnectTimeout(30000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("ip", "123");
                conn.setRequestProperty("name", "Ankit");
                conn.setRequestProperty("pwd", "123");
                // conn.setRequestProperty("Authorization", "Bearer " + AuthorizationValue);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os));
                writer.write(data);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                } else if (responseCode == HttpsURLConnection.HTTP_UNAUTHORIZED || responseCode == HttpsURLConnection.HTTP_GATEWAY_TIMEOUT) {
                    //AuthHelper.signOut(appContext);
                    return String.valueOf(responseCode);
                }
              }






        } catch (Exception e) {

          //keep it in async task, its called on main thread thats y exception

            //ok wait do u have code of async i dont use that wait ill do it
            //do u hav key? which key ?in postman it asks fr kkey actually
           // m send me certifi  waittly sen kar dia ok
            //if i turn off ssl certificate verifiication in   postman, its working

            Log.e("HttpError", e.getMessage());

        }
        return result;
        //in postman also it wud ask fr certificate actually
        //cn u show me how its working in postman? wait

        //r u sure this is the correct certificate?? is it worrrrking in postman with this certificate>>
        //in postman it runs directly no certicat needed
        //wait ill check
        // ok
  //link is working even without certificate , i dont kno how have this certi been generated
          }
    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }
    private class Logintask extends AsyncTask<Void,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... voids) {


            String res=httpConnectionPost(ApiUrl.LOGIN_URL,"");

            return res;
        }
    }*/


}



   /* private SSLSocketFactory getSocketFactory() {

        CertificateFactory cf = null;
        try {

            cf = CertificateFactory.getInstance("X.509");
            InputStream caInput = getResources().openRawResource(R.raw.certificate);
            Certificate ca;
            try {

                ca = cf.generateCertificate(caInput);
                Log.e("CERT", "ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }


            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);


            HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {

                    Log.e("CipherUsed", session.getCipherSuite());
                    return hostname.compareTo("13.126.104.102")==0; //The Hostname of your server.

                }
            };


            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
            SSLContext context = null;
            context = SSLContext.getInstance("TLS");

            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            SSLSocketFactory sf = context.getSocketFactory();


            return sf;

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return  null;
    }
*/


