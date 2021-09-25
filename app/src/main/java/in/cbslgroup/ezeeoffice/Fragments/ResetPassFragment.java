package in.cbslgroup.ezeeoffice.Fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.Util;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResetPassFragment extends Fragment {

    Button btnResetPwd;
    ProgressBar pbResetPwd;
    EditText etMail;
    LinearLayout llBtn;

    public ResetPassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reset_pass, container, false);

        btnResetPwd = v.findViewById(R.id.btn_frag_reset_pass);
        pbResetPwd = v.findViewById(R.id.pb_frag_reset_pass);
        llBtn = v.findViewById(R.id.ll_btn_frag_reset_pwd);
        etMail = v.findViewById(R.id.et_reset_pass_enter_email);
        etMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = s.toString();

                etMail.setError(null);
                btnResetPwd.setEnabled(false);


                if(Util.isNullOrEmpty(text)){

                    etMail.setError("Plese enter email");


                }

                else{

                    btnResetPwd.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             sendVerificationCode(etMail.getText().toString());


            }
        });

        return v;
    }


     void sendVerificationCode(String email){

         btnResetPwd.setVisibility(View.GONE);
         pbResetPwd.setVisibility(View.VISIBLE);
         llBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

         StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RESET_PWD, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 Log.e("sent veri code",response);

                 try {
                     JSONObject jsonObject = new JSONObject(response);
                     String msg = jsonObject.getString("msg");
                     String error = jsonObject.getString("error");

                     if(error.equalsIgnoreCase("true")){

                         Toast.makeText(getActivity(),msg, Toast.LENGTH_LONG).show();

                         btnResetPwd.setVisibility(View.VISIBLE);
                         pbResetPwd.setVisibility(View.GONE);
                         llBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                     }

                     else if(error.equalsIgnoreCase("false"))
                     {

                         String otp = jsonObject.getString("otp");
                         String timeout = jsonObject.getString("timeout");
                         String userid = jsonObject.getString("userid");


                         ResetPassFormFragment resetPassFormFragment = new ResetPassFormFragment();
                         Bundle args = new Bundle();
                         args.putString("otp",otp);
                         args.putString("timeout",timeout);
                         args.putString("userid",userid);
                         resetPassFormFragment.setArguments(args);

                         getFragmentManager()
                                 .beginTransaction()
                                 .replace(R.id.fragment_reset_pass_container, resetPassFormFragment,"resetPassForm")
                                 .setCustomAnimations(R.anim.left_to_right,R.anim.right_to_left)
                                 //.addToBackStack("ResetPass")
                                 .commit();
                     }

                     else{

                         Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                         btnResetPwd.setVisibility(View.VISIBLE);
                         pbResetPwd.setVisibility(View.GONE);
                         llBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                     }

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }


             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

                 Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                 btnResetPwd.setVisibility(View.VISIBLE);
                 pbResetPwd.setVisibility(View.GONE);
                 llBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


             }
         }){

             @Override
             protected Map<String, String> getParams() {

                 Map<String,String> params = new HashMap<>();
                 params.put("action","verificationCodeSent");
                 params.put("email",email);
                 params.put("projectName","EzeeOffice");
                 params.put("module","forgotpwd");

                 return params;
             }
         };

         VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);


     }




}
