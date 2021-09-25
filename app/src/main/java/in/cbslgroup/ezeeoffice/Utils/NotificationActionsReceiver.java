package in.cbslgroup.ezeeoffice.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import net.gotev.uploadservice.UploadService;

public class NotificationActionsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("working_gotev_recev","ok");

        if (intent == null || !NotificationActions.INTENT_ACTION.equals(intent.getAction())) {
            Log.e("NotifiActReceiver","null");
            return;
        }

       else if (NotificationActions.ACTION_CANCEL_UPLOAD.equals(intent.getStringExtra(NotificationActions.PARAM_ACTION))) {
            onUserRequestedUploadCancellation(context, intent.getStringExtra(NotificationActions.PARAM_UPLOAD_ID));

            Log.e("NotifiActReceiver",NotificationActions.ACTION_CANCEL_UPLOAD+"\n"+intent.getStringExtra(NotificationActions.PARAM_ACTION));
        }

        else{

            Log.e("NotifiActReceiver","null");
        }

    }

    private void onUserRequestedUploadCancellation(Context context, String uploadId) {
        Log.e("CANCEL_UPLOAD", "User requested cancellation of upload with ID: " + uploadId);
        UploadService.stopUpload(uploadId);
    }

}
