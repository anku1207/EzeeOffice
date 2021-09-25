package in.cbslgroup.ezeeoffice.Utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AlertDialog;

import in.cbslgroup.ezeeoffice.R;

public class DevOps {

    AlertDialog alertDialog = null;

    public interface DevOpsButtonListeners {

        void onSubmitButtonClickListener(String etPass);

    }

    DevOpsButtonListeners devOpsButtonListeners;

    public void setDevOpsButtonListeners(DevOpsButtonListeners devOpsButtonListeners) {

        this.devOpsButtonListeners = devOpsButtonListeners;
    }

    public void DevOpsAlert(String etHint, Context context) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alertdialog_dev_options, null);
        EditText et_passcode = dialogView.findViewById(R.id.alert_dev_op_et_pass_code);
        Button btn_sumbit = dialogView.findViewById(R.id.alert_dev_op_btn_submit);

        btn_sumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                devOpsButtonListeners.onSubmitButtonClickListener(et_passcode.getText().toString());

            }
        });


        et_passcode.setHint(etHint);

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.show();
    }


}
