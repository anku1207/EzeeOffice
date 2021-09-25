package in.cbslgroup.ezeeoffice.Activity.WorkFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.CommentListAdapter;
import in.cbslgroup.ezeeoffice.Adapters.TaskProcessDocAdapter;
import in.cbslgroup.ezeeoffice.Model.CommentTaskApproveReject;
import in.cbslgroup.ezeeoffice.Model.TaskProcessDocs;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class TaskInProcessActivity extends AppCompatActivity {

    BottomSheetDialog bsControlPanel;
    AlertDialog alertDialog;

    ImageView ivDesc;

    String description;

    RecyclerView rvDocs;

    TextView tvHeading, tvDoc, tvPriority, tvTaskStatus, tvDeadline, tvWorkFlowName, tvTaskName, tvSubmittedBy, tvAssignedTo, tvSupervisor, tvAlternateUser;

    List<TaskProcessDocs> taskProcessDocsList = new ArrayList<>();

    TaskProcessDocAdapter taskProcessDocAdapter;

    LinearLayout llmain;
    ProgressBar pb;

    List<CommentTaskApproveReject> commentList = new ArrayList<>();

    String taskStatus,taskid,taskname ;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_in_process);



        toolbar = findViewById(R.id.toolbar_task_in_process);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
               /* Intent intent = new Intent(TaskInProcessActivity.this, InTrayActivity.class);
                startActivity(intent);*/
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        Intent intent = getIntent();
        taskid = intent.getStringExtra("taskid");
        taskname = intent.getStringExtra("taskname");
        taskStatus = intent.getStringExtra("taskStatus");


        Log.e("taskid in process",taskid);
        Log.e("taskname in process",taskname);


        toolbar.setSubtitle("Task in process " + taskname);

        rvDocs = findViewById(R.id.rv_task_in_process_docname);
        rvDocs.setLayoutManager(new LinearLayoutManager(this));
        rvDocs.setItemViewCacheSize(taskProcessDocsList.size());
        rvDocs.setHasFixedSize(true);

        llmain = findViewById(R.id.ll_task_in_process);
        pb = findViewById(R.id.progressBar_task_in_process);

        getTaskInProcess(MainActivity.userid, taskid);

        ivDesc = findViewById(R.id.iv_task_in_process_desc);
        ivDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDescription(description);

            }
        });

        tvHeading = findViewById(R.id.tv_task_in_process_heading);

        tvHeading.setText("Task in process " + taskname);


        tvDoc = findViewById(R.id.tv_task_in_process_documents);
        tvDoc.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvDoc.setPaintFlags(tvDoc.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        tvPriority = findViewById(R.id.tv_task_in_process_priority);
        tvSubmittedBy = findViewById(R.id.tv_task_in_process_submitted_by);
        tvTaskStatus = findViewById(R.id.tv_task_in_process_task_status);
        tvDeadline = findViewById(R.id.tv_task_in_process_deadline);
        tvWorkFlowName = findViewById(R.id.tv_task_in_process_workflow_name);
        tvTaskName = findViewById(R.id.tv_task_in_process_task_name);
        tvAssignedTo = findViewById(R.id.tv_task_in_process_assigned_to);
        tvSupervisor = findViewById(R.id.tv_task_in_process_supervisor);

        tvAlternateUser = findViewById(R.id.tv_task_in_process_alternate_user);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.task_in_process_menu, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_control_panel) {

            showControlPanel();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    void getTaskInProcess(final String userid, final String taskid) {

        llmain.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);
        taskProcessDocsList.clear();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("taskinprocess", response);
          /*      {
                    "priority": "Normal",
                        "task_status": "Pending",
                        "deadline": "0 Seconds",
                        "workflow_name": null,
                        "task_name": "for annotation test",
                        "submitted_by": "Ajay Kumar Sharma",
                        "assigned_to": "Devendra Verma",
                        "supervisor": "Devendra Verma",
                        "alternate_user": "sant swaroop",
                        "description": "<p>for annotation sign testing</p>",
                        "doc": [
                    {
                        "old_doc_name": "EzeeFile  Document Management System",
                            "doc_id": "7734",
                            "doc_path": "testworkflow/1520484049_EzeeFile  Document Management System.pdf",
                            "doc_extn": "pdf"
                    }
    ]
                }*/

                try {


                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.length() == 0) {

                        Toast.makeText(TaskInProcessActivity.this, "No Info found", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                       /* Intent intent = new Intent(TaskInProcessActivity.this, InTrayActivity.class);
                        startActivity(intent);*/

                       onBackPressed();


                    }

                    else if (jsonObject.getString("error").equalsIgnoreCase("true")){


                        Toast.makeText(TaskInProcessActivity.this, "No Info found", Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);

                       /* Intent intent = new Intent(TaskInProcessActivity.this, InTrayActivity.class);
                        startActivity(intent);*/

                        onBackPressed();
                    }

                    else if(jsonObject.getString("error").equalsIgnoreCase("false")) {

                        for (int i = 0; i < jsonObject.length(); i++) {

                            String priority = jsonObject.getString("priority");

                            if(priority.length()==0){

                                tvPriority.setText("No Task Priority");
                            }
                            else{

                                tvPriority.setText(priority);

                            }


                            String taskStatus = jsonObject.getString("task_status");

                            tvTaskStatus.setText(taskStatus);

                            String taskName = jsonObject.getString("task_name");

                            tvTaskName.setText(taskName);

                            String deadline = jsonObject.getString("deadline");

                            if (deadline.equalsIgnoreCase("0 seconds")) {

                                tvDeadline.setTextColor(Color.RED);
                            }

                            tvDeadline.setText(deadline);


                            String workflowName = jsonObject.getString("workflow_name");

                            if (workflowName.equalsIgnoreCase("null")) {

                                tvWorkFlowName.setText("-");
                            } else {

                                tvWorkFlowName.setText(workflowName);
                            }


                            String submittedBy = jsonObject.getString("submitted_by");


                            tvSubmittedBy.setText(submittedBy);

                            String assignedTo = jsonObject.getString("assigned_to");

                            tvAssignedTo.setText(assignedTo);


                            String supervisor = jsonObject.getString("supervisor");

                            tvSupervisor.setText(supervisor);

                            String alternateUser = jsonObject.getString("alternate_user");

                            tvAlternateUser.setText(alternateUser);


                            description = jsonObject.getString("description");

                            Log.e("description",description);


                            if (TextUtils.isEmpty(description) || description.equalsIgnoreCase("")|| description.equalsIgnoreCase("null")) {

                                description = "No Description";

                            }


                        }

                        JSONArray jsonArray = jsonObject.getJSONArray("doc");

                        Log.e("lenghth", String.valueOf(jsonArray.length()));


                        for (int j = 0; j < jsonArray.length(); j++) {

                            String docname = jsonArray.getJSONObject(j).getString("old_doc_name");
                             String docid= jsonArray.getJSONObject(j).getString("doc_id");
                            String path = jsonArray.getJSONObject(j).getString("doc_path");
                            String extension = jsonArray.getJSONObject(j).getString("doc_extn");

                            //docname+"."+extension, path, extension,docid
                            taskProcessDocsList.add(new TaskProcessDocs(docname, path, extension,docid));


                        }

                        rvDocs.setAdapter(new TaskProcessDocAdapter(taskProcessDocsList, TaskInProcessActivity.this));

                        pb.setVisibility(View.GONE);
                        llmain.setVisibility(View.VISIBLE);



                    }


                    if(taskStatus.equalsIgnoreCase("Done")||taskStatus.equalsIgnoreCase("Aborted")
                            ||taskStatus.equalsIgnoreCase("Processed")||taskStatus.equalsIgnoreCase("Complete")){


                        toolbar.getMenu().findItem(R.id.action_control_panel).setVisible(false);

                    }

                    else{

                        toolbar.getMenu().findItem(R.id.action_control_panel).setVisible(true);

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
                params.put("taskid", taskid);

                return params;


            }
        };

        VolleySingelton.getInstance(TaskInProcessActivity.this).addToRequestQueue(stringRequest);

    }

    void showControlPanel() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottom_sheet_control_panel, null);

        bsControlPanel = new BottomSheetDialog(this);
        bsControlPanel.setContentView(view);

        Button btnTaskProperties = bsControlPanel.findViewById(R.id.btn_in_tray_control_panel_document_properties);
        btnTaskProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        Button btnCommentTask = bsControlPanel.findViewById(R.id.btn_in_tray_control_panel_comment_task);
        btnCommentTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentTask();

            }
        });

        Button btnApporveReject = bsControlPanel.findViewById(R.id.btn_in_tray_control_panel_approve_reject_task);
        btnApporveReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(TaskInProcessActivity.this, AprrovedRejectTaskActivity.class);
                intent.putExtra("taskid",taskid);
                intent.putExtra("taskname",taskname);
                startActivity(intent);

            }
        });


        bsControlPanel.show();

    }

    void showCommentTask() {

        bsControlPanel.dismiss();

        Intent intent = getIntent();
        final String taskid = intent.getStringExtra("taskid");

        Log.e("task id", taskid);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TaskInProcessActivity.this);
        LayoutInflater inflater = TaskInProcessActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_comment_task, null);


     /*   BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TaskInProcessActivity.this);
        bottomSheetDialog.setContentView(dialogView);*/

        RecyclerView rvCmnt = dialogView.findViewById(R.id.rv_comment_task_comment_list);
        rvCmnt.setLayoutManager(new LinearLayoutManager(this));
        rvCmnt.setHasFixedSize(true);


        getComments(MainActivity.userid,taskid,rvCmnt);


        Button btnCancel = dialogView.findViewById(R.id.btn_comment_task_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();


            }
        });

        Button btnSubmit = dialogView.findViewById(R.id.btn_comment_task_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText etComment = dialogView.findViewById(R.id.et_comment_task_enter_comment);
                String comment = etComment.getText().toString().trim();
                Log.e("comment", comment);

                addComment(MainActivity.userid, taskid, comment);

            }
        });

        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        alertDialog.show();

       // bottomSheetDialog.show();


    }

    void showDescription(String message) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = TaskInProcessActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_task_desc, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog alertDialog = dialogBuilder.create();

        WebView webview = dialogView.findViewById(R.id.webview_task_desc);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadDataWithBaseURL("", message, "text/html", "UTF-8", "");

        Button cancel = dialogView.findViewById(R.id.btn_task_desc_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

            }
        });

        TextView tvdesc = dialogView.findViewById(R.id.tv_alert_dialog_task_desc);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tvdesc.setText(Html.fromHtml(message, Html.FROM_HTML_MODE_LEGACY));
        } else {
            tvdesc.setText(Html.fromHtml(message));
        }


        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    void addComment(final String userid, final String taskid, final String Comment) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("comment", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(TaskInProcessActivity.this, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    } else if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(TaskInProcessActivity.this, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();


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
                params.put("useridComment", userid);
                params.put("taskidComment", taskid);
                params.put("comment", Comment);

                return params;
            }
        };

        VolleySingelton.getInstance(TaskInProcessActivity.this).addToRequestQueue(stringRequest);

    }

    void getComments(final String userid, final String taskid, RecyclerView rv) {


        Log.e("taskid", taskid);

        commentList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.PROCESS_TASK_APPROVED_REJECT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("comments api", response);

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0){

                        rv.setVisibility(View.GONE);

                    }

                    else{

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String username = jsonArray.getJSONObject(i).getString("username");
                            Log.e("username", username);
                            String commenttime = jsonArray.getJSONObject(i).getString("comment_time");
                            Log.e("date", commenttime);
                            String comment = jsonArray.getJSONObject(i).getString("comment");
                            Log.e("comment", comment);


                            String taskname = jsonArray.getJSONObject(i).getString("taskname");
                            Log.e("taskname", taskname);
                            String profilepic = jsonArray.getJSONObject(i).getString("profile_picture");
                            Log.e("profilepic", profilepic);

                            commentList.add(new CommentTaskApproveReject(username, taskname, comment, commenttime, profilepic));

                        }


                        rv.setAdapter(new CommentListAdapter(commentList, TaskInProcessActivity.this));
                        rv.setVisibility(View.VISIBLE);



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
                params.put("useridComment", userid);
                params.put("taskidComment", taskid);

                return params;
            }

        };

        VolleySingelton.getInstance(TaskInProcessActivity.this).addToRequestQueue(stringRequest);


    }


}
