package in.cbslgroup.ezeeoffice.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import in.cbslgroup.ezeeoffice.Activity.LoginActivity;
import in.cbslgroup.ezeeoffice.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordResetSuccessFragment extends Fragment {

    Button btnSignin;

    public PasswordResetSuccessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_password_reset_success, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        btnSignin = v.findViewById(R.id.btn_reset_pwd_success_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                // Closing all the Activities
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });


        return v;
    }

}
