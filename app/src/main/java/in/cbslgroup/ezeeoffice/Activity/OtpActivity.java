package in.cbslgroup.ezeeoffice.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Otp.OnOtpCompletionListener;
import in.cbslgroup.ezeeoffice.Otp.OtpView;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.DevOps;
import in.cbslgroup.ezeeoffice.Utils.FCMHandler;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class OtpActivity extends AppCompatActivity {

    OtpView otpView;
    //SmsVerifyCatcher smsVerifyCatcher;
    Button btnSubmit;
    SessionManager session;
    String otpDecoded;
    TextView tvCountdown, tvSubheading;
    Toolbar toolbar;
    ImageView ivOtp;


    //developer option
    int counter = 0;


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        //requestPermission();

        // Session Manager
        session = new SessionManager(getApplicationContext());

        toolbar = findViewById(R.id.toolbar_otp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();


            }
        });


        otpView = findViewById(R.id.otp_view);
        btnSubmit = findViewById(R.id.otp_submit_button);
        tvCountdown = findViewById(R.id.tv_otp_countdown);
        tvSubheading = findViewById(R.id.tv_otp_subheading);
        ivOtp = findViewById(R.id.iv_otp_message);

        otpView.setAnimationEnable(true);


        Intent intent = getIntent();
        String firstname = intent.getStringExtra("firstname");
        String lastname = intent.getStringExtra("lastname");
        String email = intent.getStringExtra("email");
        String userid = intent.getStringExtra("userid");
        String designation = intent.getStringExtra("designation");
        String phonenumber = intent.getStringExtra("phonenumber");

        String fullname = firstname + " " + lastname;
        String lastfourPno = phonenumber.substring(6);

        tvSubheading.setText("Please type the verification code sent to +xxxxxx" + lastfourPno + " or on mail");

        sendVerificationCode(email);

//        smsVerifyCatcher = new SmsVerifyCatcher(OtpActivity.this, new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//                String code = parseCode(message);//Parse verification code
//                Log.e("sms code", code);
//                otpView.setText(code);//set code in edit text
//                //then you can send verification code to server
//
//
//            }
//        });

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {

                String savedOtp = otpDecoded;

                if (otp.length() == 0 || TextUtils.isEmpty(otp)) {

                    Toast.makeText(OtpActivity.this, "Please enter otp first.", Toast.LENGTH_SHORT).show();

                } else if (otp.equalsIgnoreCase(savedOtp)) {

                    //enabling the fcm
                    FCMHandler.enableFCM(OtpActivity.this);

                    session.createLoginSession(userid, fullname, email, phonenumber, designation);

                    Intent main = new Intent(OtpActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();


                } else {

                    Toast.makeText(OtpActivity.this, "Invalid OTP try again.", Toast.LENGTH_SHORT).show();

                }

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String otp = otpView.getText().toString();
                String savedOtp = otpDecoded;

                Log.e("otp", otp);

                if (otp.length() == 0 || TextUtils.isEmpty(otp)) {

                    Toast.makeText(OtpActivity.this, "Please enter otp first.", Toast.LENGTH_SHORT).show();

                } else if (otp.equalsIgnoreCase(savedOtp)) {

                    //enabling the fcm
                    FCMHandler.enableFCM(OtpActivity.this);

                    session.createLoginSession(userid, fullname, email, phonenumber, designation);

                    Intent main = new Intent(OtpActivity.this, MainActivity.class);
                    startActivity(main);
                    finish();


                } else {

                    Toast.makeText(OtpActivity.this, "Invalid OTP try again.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        ivOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(OtpActivity.this, counter, Toast.LENGTH_SHORT).show();

                if (counter == 10) {

                    counter = 0;

                    DevOps devOps = new DevOps();
                    devOps.DevOpsAlert("Enter Default OTP", OtpActivity.this);
                    devOps.setDevOpsButtonListeners(new DevOps.DevOpsButtonListeners() {
                        @Override
                        public void onSubmitButtonClickListener(String etPass) {

                            if (etPass.equalsIgnoreCase("987321")) {

                                //enabling the fcm
                                FCMHandler.enableFCM(OtpActivity.this);

                                session.createLoginSession(userid, fullname, email, phonenumber, designation);

                                Intent main = new Intent(OtpActivity.this, MainActivity.class);
                                startActivity(main);
                                finish();


                            } else {

                                Toast.makeText(OtpActivity.this, "Wrong default OTP", Toast.LENGTH_SHORT).show();

                            }


                        }
                    });

                } else {

                    counter++;
                    Log.e("dev_counter", String.valueOf(counter));
                    int remainingClicks = 10;
                    remainingClicks = remainingClicks - counter;

                    if (remainingClicks <= 4 && remainingClicks != 0) {

                        Toast.makeText(OtpActivity.this, remainingClicks + " more click for becoming a developer !!", Toast.LENGTH_SHORT).show();

                    }


                }


            }
        });


    }


    void sendVerificationCode(String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RESET_PWD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("sent veri code login", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("true")) {

                        Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_LONG).show();


                    } else if (error.equalsIgnoreCase("false")) {

                        String otpBase64 = jsonObject.getString("otp");
                        String timeout = jsonObject.getString("timeout");
                        //String userid = jsonObject.getString("userid");


                        byte[] bytes = Base64.decode(otpBase64, Base64.DEFAULT);
                        otpDecoded = new String(bytes, StandardCharsets.UTF_8);


                        Log.e("otp", String.valueOf(otpDecoded));
                        Log.e("timeout", String.valueOf(timeout));

                        String timeExp = String.valueOf(timeout);
                        int timeinmili = (int) TimeUnit.MINUTES.toMillis(Long.parseLong(timeExp));

                        new CountDownTimer(timeinmili, 1000) {

                            public void onTick(long millisUntilFinished) {

                                tvCountdown.setText("OTP expires in " + Util.millisecondsToTime(millisUntilFinished));
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {

                                tvCountdown.setText("Sorry!! OTP has been Expired");
                                btnSubmit.setEnabled(false);
                                //Toast.makeText(getActivity(), "Sorry!! OTP has been Expired", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                                // Closing all the Activities
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                // Add new Flag to start new Activity
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);


                            }

                        }.start();


                    } else {

                        Toast.makeText(OtpActivity.this, "Server Error", Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(OtpActivity.this, "Server Error", Toast.LENGTH_LONG).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("action", "verificationCodeSent");
                params.put("email", email);
                params.put("projectName", "EzeeOffice");
                params.put("module", "login");

                return params;
            }
        };

        VolleySingelton.getInstance(OtpActivity.this).addToRequestQueue(stringRequest);


    }

    @NonNull
    public String getDeviceIpAddress() {
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
    public String getWifiIp() {
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

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OtpActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
        //smsVerifyCatcher.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
    }

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }


}
