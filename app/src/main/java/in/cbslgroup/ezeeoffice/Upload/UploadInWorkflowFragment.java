package in.cbslgroup.ezeeoffice.Upload;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.UploadActivity;
import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Model.StepList;
import in.cbslgroup.ezeeoffice.Model.TaskList;
import in.cbslgroup.ezeeoffice.Model.WorkFlowList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.NotificationActions;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

import static android.app.Activity.RESULT_OK;
import static net.gotev.uploadservice.Placeholders.ELAPSED_TIME;
import static net.gotev.uploadservice.Placeholders.PROGRESS;
import static net.gotev.uploadservice.Placeholders.UPLOAD_RATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadInWorkflowFragment extends Fragment {


    LinearLayout llSelectWork,llSelectStep,llSelectTask;
    Spinner spSelectWork ,spSelectStep,spSelectTask;

    ArrayAdapter<String> spSelectWorkAdapter;
    ArrayAdapter<String> spSelectStepAdapter;
    ArrayAdapter<String> spSelectTaskAdapter;


    List<String> workFlowNameList = new ArrayList<>();
    List<String> stepNameList = new ArrayList<>();
    List<String> taskNameList = new ArrayList<>();

    List<WorkFlowList> workFlowList = new ArrayList<>();
    List<StepList> stepList = new ArrayList<>();
    List<TaskList> taskList = new ArrayList<>();


    Button btnBack,btnSubmit;

    JSONObject jsonObject;

    String filePath = UploadActivity.filePath;
    MultipartUploadRequest multipartUploadRequest;
    AlertDialog alertDialogProcessing,alertDialogError,alertDialogSuccess;

    ProgressDialog pd;


    public UploadInWorkflowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_upload_in_workflow, container, false);


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

        //LinearLAyouts
        llSelectWork = v.findViewById(R.id.ll_upload_in_workflow_select_workflow);
        llSelectWork.setVisibility(View.VISIBLE);
        llSelectStep = v.findViewById(R.id.ll_upload_in_workflow_select_step);
        llSelectStep.setVisibility(View.GONE);
        llSelectTask = v.findViewById(R.id.ll_upload_in_workflow_select_task);
        llSelectTask.setVisibility(View.GONE);


        //spinners
        spSelectWork = v.findViewById(R.id.sp_upload_in_workflow_select_workflow);
        spSelectStep = v.findViewById(R.id.sp_upload_in_workflow_select_step);
        spSelectTask = v.findViewById(R.id.sp_upload_in_workflow_select_task);

        btnBack = v.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UploadActivity.stepView.done(false);

                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.left_to_right,R.anim.right_to_left);
                ft.replace(R.id.fl_upload_root, new VerifyAndCompleteFragment());
                ft.commit();

                UploadActivity.stepView.go(2, true);

            }
        });
        btnSubmit = v.findViewById(R.id.submit_workflow_doc_upload);
        btnSubmit.setEnabled(true);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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


                String workflowname = spSelectWork.getSelectedItem().toString();
                String stepname = spSelectStep.getSelectedItem().toString();
                String taskname = spSelectTask.getSelectedItem().toString();
                String slid = DmsActivity.dynamicFileSlid;

                String workflowid = "";

                for(WorkFlowList workFlowList : workFlowList) {

                    if (workFlowList.getWorkFlowName().equalsIgnoreCase(workflowname)) {

                        workflowid = workFlowList.getWorkFlowId();

                    }

                }

                String stepid = "";

                for(StepList stepList : stepList){

                    if(stepList.getStepName().equalsIgnoreCase(stepname)){

                        stepid = stepList.getStepId();
                        Log.e("stpID",workflowid);

                    }

                }

                String taskid = "";

                for(TaskList taskList : taskList){

                     Log.e("tskname",taskname);
                    if(taskList.getTaskName().equalsIgnoreCase(taskname)){

                        taskid = taskList.getTaskId();
                        Log.e("tskID",taskid);

                    }

                }

                byte[] data = new byte[0];

                data = slid.getBytes(StandardCharsets.UTF_8);


                String slidBase64 = Base64.encodeToString(data, Base64.DEFAULT);

                //getting workflowid

                Log.e("------","--------");
                Log.e("widBase64", workflowid);
                Log.e("slid",DmsActivity.dynamicFileSlid);
                Log.e("userid",MainActivity.userid);
                Log.e("jsonobject", String.valueOf(jsonObject));
                Log.e("stepid", stepid);
                Log.e("taskid",taskid );
                Log.e("pagecount",VerifyAndCompleteFragment.pagecount);
                Log.e("------","--------");

               // alertProcessing("On", getActivity());

                UploadDoc(slidBase64,MainActivity.userid, String.valueOf(jsonObject),workflowid,stepid,taskid,VerifyAndCompleteFragment.pagecount,"");

               /* Intent intent = new Intent(getActivity(),UploadActivity.class);
                startActivity(intent);*/
                //UploadDoc("0","9","8","8","9","9","5");



            }
        });


        getWorkFlow(MainActivity.userid);


        // Inflate the layout for this fragment
        return v;
    }

    void getWorkFlow(String userid){

         btnSubmit.setEnabled(false);
         workFlowNameList.clear();
         workFlowList.clear();
         llSelectTask.setVisibility(View.GONE);
         llSelectStep.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.WORKFLOW_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("workflow response",response);
                try {
                    JSONArray jsonArray  = new JSONArray(response);

                    for(int i = 0 ; i<jsonArray.length();i++){


                        String workflowid = jsonArray.getJSONObject(i).getString("workflow_id");
                        String workflowname = jsonArray.getJSONObject(i).getString("workflow_name");

                        workFlowList.add(new WorkFlowList(workflowname,workflowid));
                        workFlowNameList.add(workflowname);


                    }


                    spSelectWorkAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, workFlowNameList) {


                        @Override
                        public int getCount() {
                            // don't display last item. It is used as hint.
                            int count = super.getCount();
                            return count > 0 ? count - 1 : count;
                        }


                    };

                    workFlowNameList.add("Select Workflow");


                    spSelectWorkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSelectWork.setAdapter(spSelectWorkAdapter);
                    spSelectWork.setSelection(spSelectWorkAdapter.getCount());
                    spSelectWork.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




                            String workflowname = spSelectWork.getSelectedItem().toString();

                            if(workflowname.equalsIgnoreCase("Select Workflow")){

                                //no code

                            }

                            else{

                                String workflowid = "";

                                for(WorkFlowList workFlowList : workFlowList){

                                    if(workFlowList.getWorkFlowName().equals(workflowname)){

                                        workflowid = workFlowList.getWorkFlowId();

                                    }

                                }

                                getStep(workflowid);

                            }





                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<>();

                //params.put("UseridWork",userid);
                 params.put("userid",userid);

                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);



    }

    private void getStep(String workflowid) {
        btnSubmit.setEnabled(false);
        llSelectStep.setVisibility(View.GONE);
        llSelectTask.setVisibility(View.GONE);

        stepNameList.clear();
        stepList.clear();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("step response",response);

                try {
                    JSONArray jsonArray  = new JSONArray(response);
                    if(jsonArray.length()== 0){

                        llSelectStep.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No Step Found", Toast.LENGTH_SHORT).show();

                    }

                    else{


                        for(int i = 0 ; i<jsonArray.length();i++){


                            String stepid = jsonArray.getJSONObject(i).getString("step_id");
                            String stepName = jsonArray.getJSONObject(i).getString("step_name");
                            String workflowid =  jsonArray.getJSONObject(i).getString("workflow_id");

                            stepList.add(new StepList(stepName,stepid,workflowid));
                            stepNameList.add(stepName);


                        }


                        spSelectStepAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stepNameList) {


                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }


                        };

                        stepNameList.add("Select Step");

                        spSelectStepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSelectStep.setAdapter(spSelectStepAdapter);
                        spSelectStep.setSelection(spSelectStepAdapter.getCount());

                        llSelectStep.setVisibility(View.VISIBLE);

                        spSelectStep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                String stepName = spSelectStep.getSelectedItem().toString();

                                if(stepName.equalsIgnoreCase("Select Step")){

                                    //no code


                                }

                                else{

                                    String workflowid = "";
                                    for(StepList stepList : stepList){

                                        if(stepList.getStepName().equals(stepName)){

                                            workflowid = stepList.getWorkflowId();

                                        }

                                    }

                                    Log.e("workflow id step ",workflowid);

                                    getTask(workflowid);

                                }




                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<>();

                params.put("workflowidStep",workflowid);

                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);


    }

    private void getTask(String workflowid) {

          btnSubmit.setEnabled(true);
         taskNameList.clear();
         taskList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.UPLOAD_DOC_IN_WORKFLOW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("task response",response);
                try {
                    JSONArray jsonArray  = new JSONArray(response);

                    if(jsonArray.length()==0){

                        llSelectTask.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "No Task Found", Toast.LENGTH_SHORT).show();

                    }

                    else{


                        for(int i = 0 ; i<jsonArray.length();i++){


                            String taskId = jsonArray.getJSONObject(i).getString("task_id");
                            String taskName = jsonArray.getJSONObject(i).getString("task_name");
                            //String workflowid =  jsonArray.getJSONObject(i).getString("workflow_id");

                            taskList.add(new TaskList(taskName,taskId));
                            taskNameList.add(taskName);


                        }


                        spSelectTaskAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, taskNameList) {


                            @Override
                            public int getCount() {
                                // don't display last item. It is used as hint.
                                int count = super.getCount();
                                return count > 0 ? count - 1 : count;
                            }


                        };



                        taskNameList.add("Select Task");
                        spSelectTaskAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spSelectTask.setAdapter(spSelectTaskAdapter);
                        spSelectTask.setSelection(spSelectTaskAdapter.getCount());

                        llSelectTask.setVisibility(View.VISIBLE);

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String,String> params = new HashMap<>();

                params.put("workflowidTask",workflowid);

                return params;
            }
        };


        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);



    }

    private void UploadDoc(String slid , String userid , String metaname , String workflowid, String stepid  , String taskid, String pagecount , String taskremark){

        //getting the actual path of the image
        String path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            path = filePath;
        }

        if (path == null) {

            Toast.makeText(getActivity(), "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code


            try {
                final String uploadId = UUID.randomUUID().toString();

                Log.e("uploadid", uploadId);
                final ProgressDialog progress = new ProgressDialog(getActivity());

                UploadNotificationConfig uploadNotificationConfig = new UploadNotificationConfig();

                //Creating a multi part request
                multipartUploadRequest = new MultipartUploadRequest(getActivity(), uploadId, ApiUrl.UPLOAD_DOC_IN_WORKFLOW);
                multipartUploadRequest.addFileToUpload(path, "fileName");



                Log.e("Upload_rate", UPLOAD_RATE);

          /*      uploadNotificationConfig.getProgress().message = "Uploaded " + UPLOADED_FILES + " of " + TOTAL_FILES
                        + " at " + UPLOAD_RATE + " - " + PROGRESS;*/

                uploadNotificationConfig.getProgress().message = PROGRESS;
                uploadNotificationConfig.getProgress().title = filePath.substring(filePath.lastIndexOf("/")+1);

                uploadNotificationConfig.getProgress().actions.add(new UploadNotificationAction(R.drawable.ic_error_red_24dp,"Cancel", NotificationActions.getCancelUploadAction(getActivity(),1,uploadId)));

                //uploadNotificationConfig.getProgress().iconResourceID = R.drawable.ic_upload;
                //uploadNotificationConfig.getProgress().iconColorResourceID = Color.BLUE;

                uploadNotificationConfig.getCompleted().message = "Upload completed successfully in " + ELAPSED_TIME;
                uploadNotificationConfig.getCompleted().title = filePath.substring(filePath.lastIndexOf("/")+1);
                uploadNotificationConfig.getCompleted().iconResourceID = android.R.drawable.stat_sys_upload_done;

                //converting the drawable to bitmap
                // Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_file_upload_blue_dark_24dp);
                //uploadNotificationConfig.getCompleted().largeIcon = bm;
                //uploadNotificationConfig.getCompleted().iconColorResourceID = Color.GREEN;

                uploadNotificationConfig.getError().message = "Error while uploading";
                uploadNotificationConfig.getError().iconResourceID = android.R.drawable.stat_sys_upload_done;
                uploadNotificationConfig.getError().title = filePath.substring(filePath.lastIndexOf("/")+1);

                // uploadNotificationConfig.getError().iconColorResourceID = Color.RED;

                uploadNotificationConfig.getCancelled().message = "Upload has been cancelled";
                uploadNotificationConfig.getCancelled().title = filePath.substring(filePath.lastIndexOf("/")+1);
                uploadNotificationConfig.getCancelled().iconResourceID = android.R.drawable.stat_sys_upload_done;
                // uploadNotificationConfig.getCancelled().iconColorResourceID = Color.YELLOW;


                Log.e("----upload--","--------");
                Log.e("widBase64", workflowid);
                Log.e("slid",slid);
                Log.e("userid",userid);
                Log.e("jsonobject", metaname);
                Log.e("stepid", stepid);
                Log.e("taskid",taskid );

                Log.e("------","--------");


                //Adding file
                multipartUploadRequest

                        .addParameter("metaName", metaname)
                        .addParameter("storageId",slid)
                        .addParameter("pageCount",pagecount)
                        .addParameter("userId",userid)
                        .addParameter("wtsk",taskid)
                        .addParameter("wstp",stepid)
                        .addParameter("wfid",workflowid)
                        .addParameter("taskRemark",taskremark)

                        .setNotificationConfig(

                                uploadNotificationConfig
                                        .setIconColorForAllStatuses(Color.parseColor("#259dc6"))
                                        .setClearOnActionForAllStatuses(true)
                        )

                        .setMaxRetries(5)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {




                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(context, "Upload Error", Toast.LENGTH_LONG).show();
                                Log.e("Server response error", String.valueOf(serverResponse.getBodyAsString()));



                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                //progress.dismiss();

                                String response = serverResponse.getBodyAsString();
                               // alertProcessing("off",getActivity());

                                try {

                                    JSONObject jsonObject = new JSONObject(response);
                                    String msg =  jsonObject.getString("msg");
                                     String error =   jsonObject.getString("error");

                                     if(error.equalsIgnoreCase("false")){

                                     /*    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

                                         Intent intent = new Intent(getActivity(),DmsActivity.class);
                                         // Closing all the Activities
                                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                         // Add new Flag to start new Activity
                                         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                         startActivity(intent);*/


                                         pd.dismiss();
                                         if(getActivity()!=null){

                                             Intent intent = getActivity().getIntent();
                                             intent.putExtra("slid_dms", DmsActivity.dynamicFileSlid);
                                             getActivity().setResult(RESULT_OK, intent);
                                             getActivity().finish();

                                         }

                                         Toast.makeText(context, msg, Toast.LENGTH_LONG).show();



                                         // alertSuccess(msg,getActivity());

                                        // alertProcessing("off",getActivity());

                                     }


                                     else if (error.equalsIgnoreCase("true")){

                                        // alertError(msg,getActivity());
                                         Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                         //alertProcessing("off",getActivity());
                                     }

                                     else{

                                        // alertError("There might me issue try again later",getActivity());
                                         Toast.makeText(context, "There might me issue in uploading try again later", Toast.LENGTH_LONG).show();
                                       //  alertProcessing("off",getActivity());

                                     }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }



                                Log.e("Server res completed", String.valueOf(serverResponse.getBodyAsString()));


                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Toast.makeText(context, "Upload Canceled", Toast.LENGTH_LONG).show();
                                UploadService.stopUpload(uploadId);
                            }
                        });




                if (isNetworkAvailable()){

                    pd.show();
                    multipartUploadRequest.startUpload();

                    Toast.makeText(getActivity(), "Upload Started..", Toast.LENGTH_SHORT).show();



                }
                else{


                    Toast.makeText(getActivity(), "No Internet connection found", Toast.LENGTH_SHORT).show();

                }



            } catch (Exception exc) {
                Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }






    }



        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

}



