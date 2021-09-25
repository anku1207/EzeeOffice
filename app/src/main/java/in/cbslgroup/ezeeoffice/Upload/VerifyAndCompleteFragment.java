package in.cbslgroup.ezeeoffice.Upload;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.lowagie.text.pdf.PdfReader;

import net.gotev.uploadservice.BuildConfig;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.UploadActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.ConnectivityReceiver;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.SessionManager;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static android.app.Activity.RESULT_OK;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyAndCompleteFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    //contants
    public static String INTENT_ACTION = BuildConfig.APPLICATION_ID + ".notification.action";

    protected static final String PARAM_ACTION = "action";
    protected static final String PARAM_UPLOAD_ID = "uploadId";
    protected static final String ACTION_CANCEL_UPLOAD = "cancelUpload";

    public static String pagecount;
    LinearLayout llVerifyComplete;
    String filePath = UploadActivity.filePath;
    AlertDialog alertDialog;

    Button btnBack, btnNxt, btnupload, btnUploadinWorkflow;


    CheckBox checkBox;

    JSONObject jsonObject;
    TextView tvFileSize, tvFileFormat, tvFilename, tvPageCount;
    ArrayList<String> metaenteredlist = new ArrayList<>();
    ArrayList<String> metalabellist = new ArrayList<>();
    String uploadingPercent, uploadingTotalSize;
    Long uploadedBytes;
    Context mContext;

    // declare a variable activity in your fragment
    MultipartUploadRequest multipartUploadRequest;
    SessionManager sessionManager;
    String session_product_name;
    ProgressDialog pd;
    AlertDialog alertDialogProcessing;
    private List<View> myLayouts = new ArrayList<>();

    public VerifyAndCompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_verify_and_complete, container, false);
        llVerifyComplete = v.findViewById(R.id.ll_frag_verify_complete_dynamicview);

        sessionManager = new SessionManager(getActivity());

        Log.e("App id", INTENT_ACTION);

       /* SharedPreferences prefs = getActivity().getSharedPreferences("jsonobject", MODE_PRIVATE);

        String meta_label= prefs.getString("meta_label",null);
        String meta_entered = prefs.getString("meta_entered",null);*/

        tvPageCount = v.findViewById(R.id.tv_frag_verify_complete_pagecount);
        tvFileFormat = v.findViewById(R.id.tv_frag_verify_complete_fileformat);
        tvFilename = v.findViewById(R.id.tv_frag_verify_complete_filename);
        tvFileSize = v.findViewById(R.id.tv_frag_verify_complete_filesize);
        btnNxt = v.findViewById(R.id.next);
        btnBack = v.findViewById(R.id.back);
        checkBox = v.findViewById(R.id.upload_frag_verify_checkbox);
        btnupload = v.findViewById(R.id.btn_upload_file_storage);
        btnUploadinWorkflow = v.findViewById(R.id.btn_upload_file_workflow);

        btnNxt.setVisibility(View.INVISIBLE);

        pd = new ProgressDialog(getActivity());
        pd.setTitle("File Uploading");
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.setButton("Run in background", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = getActivity().getIntent();
                intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();

            }
        });


        checkPermission(MainActivity.userid);


        PdfReader reader;
        try {
            if (UploadActivity.filetypedynamic.equals("pdf") || UploadActivity.filetypedynamic.equals("PDF")) {
                reader = new PdfReader(filePath);
                int noOfPages = reader.getNumberOfPages();
                String pages = String.valueOf(noOfPages);
                tvPageCount.setText(pages);

                pagecount = pages;

            } else {

                tvPageCount.setText("1");
                pagecount = "1";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        Long filesize = UploadActivity.filesize;
        String fileSize = String.valueOf(filesize);


        tvFilename.setText(UploadActivity.fileName);
        tvFileSize.setText(fileSize);
        tvFileFormat.setText(UploadActivity.filetypedynamic);

        UploadActivity.stepView.done(false);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new UploadFileFragment());
                ft.commit();

                UploadActivity.stepView.go(1, true);

            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    btnupload.setEnabled(true);
                    btnUploadinWorkflow.setEnabled(true);

                } else {

                    btnupload.setEnabled(false);
                    btnUploadinWorkflow.setEnabled(false);

                }

            }
        });

        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int count = DescribesFragment.metalabellist.size();


                jsonObject = null;

                try {

                    jsonObject = makeJsonObject(DescribesFragment.metalabellist, DescribesFragment.metaEnteredlist, count);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("json", String.valueOf(jsonObject));
                Log.e("filename", tvFilename.getText().toString());


                checkSameFileExist(tvFilename.getText().toString(),DmsActivity.dynamicFileSlid);

                //uploadMultipart();


            }
        });

        btnUploadinWorkflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right, R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new UploadInWorkflowFragment());
                ft.commit();

                UploadActivity.stepView.go(3, true);


            }
        });

        Log.e("metalabel on verify", Arrays.toString(new ArrayList[]{DescribesFragment.metalabellist}));
        Log.e("metaname in verify", Arrays.toString(new ArrayList[]{DescribesFragment.metaEnteredlist}));

        for (int i = 0; i < DescribesFragment.metalabellist.size(); i++) {

            createDynamicView(DescribesFragment.metalabellist.get(i), DescribesFragment.metaEnteredlist.get(i));

        }


        // Inflate the layout for this fragment
        return v;
    }

    private void checkSameFileExist(String filename,String slid) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPLOAD_DOC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String msg = jsonObject.getString("message");
                    String error = jsonObject.getString("error");

                    if(error.equalsIgnoreCase("false"))
                    {

                       uploadMultipart();

                    }

                    else if (error.equalsIgnoreCase("true")){

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                    }

                    else{

                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("file_name",filename);
                params.put("folder_slid",slid);
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


    private void createDynamicView(String metaname, String metafilled) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.upload_verfiy_complete_dynamic_layout, null);

        myLayouts.add(rowView);

        TextView tvMetaName = rowView.findViewById(R.id.tv_frag_verify_complete_metadata_name);
        tvMetaName.setText(metaname);

        TextView tvMetaFilled = rowView.findViewById(R.id.tv_frag_verify_complete_metadata_entered);
        tvMetaFilled.setText(metafilled);

        llVerifyComplete.addView(rowView, llVerifyComplete.getChildCount() - 1);


    }


    public void uploadMultipart() {

        //getting the actual path of the image
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(getActivity(), "Please select any file and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code


            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);
                final ProgressDialog progress = new ProgressDialog(getActivity());

                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                String ipadress = MainActivity.ip;
                String userId = MainActivity.userid;
                String pageCount = tvPageCount.getText().toString();
                String sliD = DmsActivity.dynamicFileSlid;
                String userName = MainActivity.username;
                String metadata = String.valueOf(jsonObject);

                Log.e("uploading ", ipadress + userId + pageCount + sliD + userName);


                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(getActivity(), uploadId, ApiUrl.UPLOAD_DOC_URL);
                multipartUploadRequest.addFileToUpload(path, "file");


                Log.e("Upload_rate", UPLOAD_RATE);

          /*      uploadNotificationConfig.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                        + " at " + UPLOAD_RATE + " - " + PROGRESS;*/

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp, "Cancel", NotificationActions.getCancelUploadAction(getActivity(), 1, uploadId)));

                //uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_upload;
                //uploadNotificationConfig.getProgress().iconColorResourceID = Color.BLUE;

                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;

                //converting the drawable to bitmap
                // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload_blue_dark_24dp);
                //uploadNotificationConfig.getCompleted().largeIcon = bm;
                //uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/") + 1);

                // uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/") + 1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
                // uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;


                Log.e("-----------fileupload", "-------");

                Log.e("metadata", metadata);
                Log.e("slid", sliD);
                Log.e("pagecount", pageCount);
                Log.e("username", userName);
                Log.e("userid", userId);
                Log.e("ip", ipadress);

                //Adding file
                multipartUploadRequest

                        .addParameter("metadata", metadata)
                        .addParameter("slid", sliD)
                        .addParameter("pagecount", pageCount)
                        .addParameter("username", userName)
                        .addParameter("userid", userId)
                        .addParameter("ip", ipadress)

                        .setNotificationConfig(

                                uploadNotificationConfig
                                        .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                        .setClearOnActionForAllStatuses(true)
                        )

                        .setMaxRetries(5)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {


                                uploadingPercent = String.valueOf(uploadInfo.getProgressPercent());
                                uploadingTotalSize = String.valueOf(uploadInfo.getTotalBytes() / 1024);


                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                String response = serverResponse.getBodyAsString();
                                Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg = jsonObject.getString("message");
                                    String error = jsonObject.getString("error");


                                    if (error.equalsIgnoreCase("false")) {


                                       /*   Intent intent = new Intent(getActivity(), DmsActivity.class);
                                        // Closing all the Activities
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        // Add new Flag to start new Activity
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);*/
                                        pd.dismiss();
                                        if (getActivity() != null) {

                                            Intent intent = getActivity().getIntent();
                                            intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                                            getActivity().setResult(RESULT_OK, intent);
                                            getActivity().finish();


                                        }
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();


                                    } else if (error.equalsIgnoreCase("true")) {
                                        pd.dismiss();

                                        Log.e("COntext in upload", String.valueOf(context));
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                        UploadService.stopUpload(uploadInfo.getUploadId());
                                        //UploadService.stopAllUploads();


                                    } else {
                                        pd.dismiss();
                                        Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                                        UploadService.stopUpload(uploadInfo.getUploadId());


                                    }


                                } catch (JSONException e) {
                                    pd.dismiss();
                                    e.printStackTrace();
                                }


                              /*  Toast.makeText(getActivity(), "File upload Succesfully", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getActivity(),DmsActivity.class);
                                startActivity(intent);
*/


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                pd.dismiss();
                                Toast.makeText(context, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadInfo.getUploadId());
                            }
                        });


                if (isNetworkAvailable()) {

                    multipartUploadRequest.startUpload();
                    pd.show();

                    Toast.makeText(getActivity(), "Upload Started..", Toast.LENGTH_SHORT).show();


                } else {


                    Toast.makeText(getActivity(), "No Internet connection found", Toast.LENGTH_SHORT).show();

                }


            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }


    private JSONObject makeJsonObject(ArrayList<String> label, ArrayList<String> value,
                                      int count)
            throws JSONException {
        JSONObject obj;
        JSONArray jsonArray1 = new JSONArray();


        for (int i = 0; i < count; i++) {

            obj = new JSONObject();

            try {
                obj.put("metaLabel", label.get(i));
                obj.put("metaEntered", value.get(i));


            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray1.put(obj);
        }
        JSONObject finalobject = new JSONObject();
        finalobject.put("meta", jsonArray1);
        return finalobject;
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {


        if (!isConnected) {

            Toast.makeText(getActivity(), "No Internet Connection found", Toast.LENGTH_SHORT).show();

            UploadService.stopAllUploads();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void checkPermission(final String userid) {

        //checking the specific roles
        String roles = setRoles();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    String intiate_file = jsonObject.getString("initiate_file");
                    String workflow_initiate_file = jsonObject.getString("workflow_initiate_file");


                    if (intiate_file.equals("1") || workflow_initiate_file.equals("1")) {

                        Log.e("upload work btn", "Visible");

                        btnUploadinWorkflow.setVisibility(View.VISIBLE);


                    } else {

                        Log.e("upload work btn", "Gone");

                        btnUploadinWorkflow.setVisibility(View.GONE);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                // getUserPermission(userid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", userid);
                params.put("roles", roles);
                params.put("action", "getSpecificRoles");

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                JSONObject js = new JSONObject(params);
                Log.e("getuserper params", js.toString());

                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);
    }


    String setRoles() {

        //checking the specific roles
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("initiate_file").append(",")
                .append("workflow_initiate_file");

         return stringBuilder.toString();
    }


}
