package in.cbslgroup.ezeeoffice.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.cbslgroup.ezeeoffice.Activity.LoginActivity;
import in.cbslgroup.ezeeoffice.Activity.Password.ForgotPasswordActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassFormFragment extends Fragment {

    TextInputEditText tieOtp, tieNewPwd, tieConfirmPwd;
    TextInputLayout tilOtp, tilNewPwd, tilConfirmPwd;

    Button btnreset, btnSubmit;
    TextView tvOTP;

    String otpDecoded = null;

    ProgressDialog progressDialog;

    String userid;

   // SmsVerifyCatcher smsVerifyCatcher;


    public ResetPassFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_pass_form, container, false);



        tieOtp = v.findViewById(R.id.tie_reset_pwd_form_enter_otp);
        tieConfirmPwd = v.findViewById(R.id.tie_reset_pwd_form_enter_confirm_pwd);
        tieNewPwd = v.findViewById(R.id.tie_reset_pwd_form_enter_new_pwd);

        tilOtp = v.findViewById(R.id.til_reset_pwd_form_enter_otp);
        tilConfirmPwd = v.findViewById(R.id.til_reset_pwd_form_enter_confirm_pwd);
        tilNewPwd = v.findViewById(R.id.til_reset_pwd_form_enter_new_pwd);

        tvOTP = v.findViewById(R.id.tv_reset_pwd_form_otp_countdown);

        btnreset = v.findViewById(R.id.btn_reset_pwd_form_reset);
        btnSubmit = v.findViewById(R.id.btn_reset_pwd_form_submit);


        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tieOtp.getText().clear();
                tieConfirmPwd.getText().clear();
                tieNewPwd.getText().clear();

            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otperrormsg = tilOtp.getError().toString();

                if (!otperrormsg.equalsIgnoreCase("Valid OTP")) {

                    Toast.makeText(getActivity(), "Entered OTP isn't a valid OTP", Toast.LENGTH_SHORT).show();

                } else {


                    String ip = ForgotPasswordActivity.ip;
                    String pwd = tieNewPwd.getText().toString();
                    changePassword(userid, ip, pwd);

                }


            }
        });


        //getting params from previous fragment
        if (getArguments() != null) {

            String otpBase64 = getArguments().getString("otp");
            String timeout = getArguments().getString("timeout");
            userid = getArguments().getString("userid");


            byte[] bytes = Base64.decode(otpBase64, Base64.DEFAULT);
            otpDecoded = new String(bytes, StandardCharsets.UTF_8);


            Log.e("otp", String.valueOf(otpDecoded));
            Log.e("timeout", String.valueOf(timeout));

            String timeExp = String.valueOf(timeout);
            int timeinmili = (int) TimeUnit.MINUTES.toMillis(Long.parseLong(timeExp));

            new CountDownTimer(timeinmili, 1000) {

                public void onTick(long millisUntilFinished) {

                    tvOTP.setText("OTP expires in " + Util.millisecondsToTime(millisUntilFinished));
                    //here you can have your logic to set text to edittext
                }

                public void onFinish() {

                    tvOTP.setText("Sorry!! OTP has been Expired");
                    btnSubmit.setEnabled(false);
                    //Toast.makeText(getActivity(), "Sorry!! OTP has been Expired", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    // Closing all the Activities
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // Add new Flag to start new Activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);


                }

            }.start();


        }


        tieOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String otp = s.toString();
                String savedOtp = otpDecoded;

                tilOtp.setErrorEnabled(false);
                tilOtp.setError(null);

                if (otp.equalsIgnoreCase(savedOtp)) {

                    tilOtp.setError("Valid OTP");
                    tilOtp.setErrorTextAppearance(R.style.no_error_appearance);


                } else {

                    tilOtp.setError("Invalid OTP");
                    tilOtp.setErrorTextAppearance(R.style.error_appearance);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tieNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Pattern specialCharPattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
                Pattern UpperCasePattern = Pattern.compile("[A-Z ]");
                Pattern lowerCasePattern = Pattern.compile("[a-z ]");
                Pattern digitCasePattern = Pattern.compile("[0-9 ]");

                String password = s.toString();

                tilNewPwd.setError(null);
                tilNewPwd.setErrorEnabled(false);
                tieConfirmPwd.setEnabled(false);

                if (Util.isNullOrEmpty(password)) {

                    tilNewPwd.setError("Please Enter New Password");

                } else if (password.length() < 8) {

                    tilNewPwd.setError("Password lenght must have atleast 8 character");

                } else if (!specialCharPattern.matcher(password).find()) {

                    tilNewPwd.setError("Password must have atleast one special character");
                } else if (!UpperCasePattern.matcher(password).find()) {

                    tilNewPwd.setError("Password must have atleast one uppercase character");


                } else if (!lowerCasePattern.matcher(password).find()) {

                    tilNewPwd.setError("Password must have atleast one lowercase character");
                } else if (!digitCasePattern.matcher(password).find()) {

                    tilNewPwd.setError("Password must have atleast one digit character");
                } else {

                    tilNewPwd.setError(null);
                    tilNewPwd.setErrorEnabled(false);
                    tieConfirmPwd.setEnabled(true);


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tieConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String newpassword = tieNewPwd.getText().toString();
                String confirmpassword = s.toString();

                tilConfirmPwd.setError(null);
                tilConfirmPwd.setErrorEnabled(false);
                btnSubmit.setEnabled(false);

                if (!confirmpassword.equalsIgnoreCase(newpassword)) {

                    tilConfirmPwd.setError("New Password and Confirm Password don't match");

                } else {

                    tilConfirmPwd.setError(null);
                    tilConfirmPwd.setErrorEnabled(false);
                    btnSubmit.setEnabled(true);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        smsVerifyCatcher = new SmsVerifyCatcher(getActivity(), new OnSmsCatchListener<String>() {
//            @Override
//            public void onSmsCatch(String message) {
//                String code = parseCode(message);//Parse verification code
//                tieOtp.setText(code);//set code in edit text
//                //then you can send verification code to server
//
//
//            }
//        });



        return v;
    }

    private void changePassword(String userid, String ip, String pwd) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Password Reseting");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RESET_PWD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("changePwd Res",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_reset_pass_container, new PasswordResetSuccessFragment(),"resetPassSuccess")
                                .setCustomAnimations(R.anim.left_to_right,R.anim.right_to_left)
                                //.addToBackStack("ResetPassSuccess")
                                .commit();

                        progressDialog.dismiss();


                    } else if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();


                    } else {


                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
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

                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("action", "changePassword");
                params.put("pwd", pwd);
                params.put("ip", ip);

                Util.printParams(params,"chnge pwd params");

                return params;


            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onStart() {
        super.onStart();
       // smsVerifyCatcher.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
       // smsVerifyCatcher.onStop();
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
