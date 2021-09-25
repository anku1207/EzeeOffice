package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.AprrovedRejectTaskActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.TaskInProcessActivity;
import in.cbslgroup.ezeeoffice.Model.DocumentListWorkflow;
import in.cbslgroup.ezeeoffice.Model.InTray;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class InTrayAdapter extends RecyclerView.Adapter<InTrayAdapter.ViewHolder> {

    List<InTray> inTrayList ;
    Context context;


    BottomSheetDialog bottomSheetDialog;
    RecyclerView rvBottomSheetDocumentView;
    List<DocumentListWorkflow> documentListWorkFlow = new ArrayList<>();
    WorkflowDocListAdapter workFlowDoclistAdapter;

    LinearLayout llNofileFound;
    ProgressBar progressBar;
    TextView tvBottomSheetHeading;

    public InTrayAdapter(List<InTray> inTrayList, Context context) {
        this.inTrayList = inTrayList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.in_tray_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        InTray item =  inTrayList.get(position);
        holder.tvTaskname.setText(item.getTaskname());
        holder.tvDeadline.setText(item.getDeadLine());
        holder.tvAssignedDate.setText(item.getAssignDate());
        holder.tvPriority.setText(item.getPriority());
        holder.tvStatus.setText(item.getStatus());
        holder.tvStartedBy.setText(item.getStartedBy());
        holder.tvDocid.setText(item.getDocid());
        holder.tvTaskid.setText(item.getTaskid());
        holder.tvWarning.setText(item.getWarning());

        String warning = holder.tvWarning.getText().toString();
        String deadline = holder.tvDeadline.getText().toString();
        String docid = holder.tvDocid.getText().toString();

        Log.e("doc id in tray 1",docid);


        if(deadline.equalsIgnoreCase("0 Secounds")){

            holder.tvDeadline.setTextColor(Color.RED);

        }

        else{

            holder.tvDeadline.setTextColor(Color.BLACK);
        }


        if(warning.equalsIgnoreCase("null")){

            holder.llWarning.setVisibility(View.GONE);

        }

        else{

            holder.llWarning.setVisibility(View.VISIBLE);
        }



        holder.btnViewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             showBottomSheetDialog(holder.tvDocid.getText().toString());


            }
        });

        holder.btnTaskAprRej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AprrovedRejectTaskActivity.class);
                intent.putExtra("taskid",holder.tvTaskid.getText().toString());
                intent.putExtra("taskname",holder.tvTaskname.getText().toString());
                intent.putExtra("taskStatus",holder.tvStatus.getText().toString());
                context.startActivity(intent);


            }
        });



        String taskstatus = holder.tvStatus.getText().toString().trim();

        String priority = holder.tvPriority.getText().toString().trim();


        if(priority.equalsIgnoreCase("No Task Priority")){

            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.red));


        }

        if(priority.equalsIgnoreCase("Normal")){



            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.colorPrimary));

        }

        else if (priority.equalsIgnoreCase("Urgent")){



            ((GradientDrawable) holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.red));


        }
        else if (priority.equalsIgnoreCase("Medium")){



            ((GradientDrawable)holder.tvPriority.getBackground()).setColor(context.getResources().getColor(R.color.light_yellow));


        }



        if(taskstatus.equalsIgnoreCase("Approved")){



            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }

        else if (taskstatus.equalsIgnoreCase("Rejected")){



            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.red));


        }
        else if (taskstatus.equalsIgnoreCase("Aborted")){



            ((GradientDrawable)holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.red));


        }
        else if (taskstatus.equalsIgnoreCase("Processed")){


            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));


        }
        else if (taskstatus.equalsIgnoreCase("Complete")){

            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));



        }

        else if (taskstatus.equalsIgnoreCase("Done")){


            ((GradientDrawable)holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));


        }
        else if (taskstatus.equalsIgnoreCase("Pending")){


            ((GradientDrawable) holder.tvStatus.getBackground()).setColor(context.getResources().getColor(R.color.light_yellow));


        }

        holder.ivAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context, "Working", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, TaskInProcessActivity.class);
                intent.putExtra("taskid",holder.tvTaskid.getText().toString());
                intent.putExtra("taskname",holder.tvTaskname.getText().toString());
                intent.putExtra("taskStatus",holder.tvStatus.getText().toString());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return inTrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWarning,tvTaskname,tvDeadline,tvPriority,tvStatus,tvAssignedDate,tvStartedBy,tvDocid,tvTaskid;
        ImageView ivAction;
        Button btnViewDoc,btnTaskAprRej;
        LinearLayout llWarning;

        public ViewHolder(View itemView) {
            super(itemView);

            llWarning = itemView.findViewById(R.id.ll_in_tray_item_warning);
            tvWarning = itemView.findViewById(R.id.tv_in_tray_item_warning);

            tvTaskname = itemView.findViewById(R.id.tv_in_tray_item_taskname);
            tvDeadline = itemView.findViewById(R.id.tv_in_tray_item_deadline);
            tvAssignedDate = itemView.findViewById(R.id.tv_in_tray_item_assigned_date);
            tvPriority = itemView.findViewById(R.id.tv_in_tray_item_priority);
            tvStatus = itemView.findViewById(R.id.tv_in_tray_item_status);
            tvStartedBy = itemView.findViewById(R.id.tv_in_tray_item_started_by);
            tvDocid = itemView.findViewById(R.id.tv_in_tray_item_docid);
            tvTaskid = itemView.findViewById(R.id.tv_in_tray_item_taskid);

            ivAction = itemView.findViewById(R.id.iv_in_tray_item_action);

            btnViewDoc = itemView.findViewById(R.id.btn_in_tray_viewdocument);
            btnTaskAprRej = itemView.findViewById(R.id.btn_in_tray_task_action);



        }
    }

    public void showBottomSheetDialog(String docid) {


        // View view = context.getLayoutInflater().inflate(R.layout.bottom_sheet_workflowdoclist, null);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view= inflater.inflate(R.layout.bottom_sheet_workflowdoclist, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        rvBottomSheetDocumentView= bottomSheetDialog.findViewById(R.id.rv_bottom_sheet_workflow_doc_list);
        rvBottomSheetDocumentView.setLayoutManager(new LinearLayoutManager(context));
        rvBottomSheetDocumentView.setHasFixedSize(true);
        rvBottomSheetDocumentView.setItemViewCacheSize(documentListWorkFlow.size());

        llNofileFound =  bottomSheetDialog.findViewById(R.id.ll_workflow_bottom_sheet_doclist_nofilefound);
        progressBar = bottomSheetDialog.findViewById(R.id.progressBar_workflowlist_doclist);
        tvBottomSheetHeading = bottomSheetDialog.findViewById(R.id.tv_bottom_sheet_workflow_heading);
        tvBottomSheetHeading.setText("Document "+"/"+" Description");

        getDoc(docid);


    }

    void getDoc(final String docid){

        llNofileFound.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        rvBottomSheetDocumentView.setVisibility(View.GONE);

        Log.e("docid in tray",docid);

        documentListWorkFlow.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TASK_TRACK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response tracklist",response);

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length() == 0){

                        progressBar.setVisibility(View.GONE);
                        rvBottomSheetDocumentView.setVisibility(View.GONE);
                        llNofileFound.setVisibility(View.VISIBLE);
                        bottomSheetDialog.show();
                        //Toast.makeText(context, "No Document Found", Toast.LENGTH_SHORT).show();

                    }

                    else{

                        for(int i = 0;  i<jsonArray.length();i++){

                            String docname = jsonArray.getJSONObject(i).getString("old_doc_name");
                            String docid= jsonArray.getJSONObject(i).getString("doc_id");
                            String path = jsonArray.getJSONObject(i).getString("doc_path");
                            String extension = jsonArray.getJSONObject(i).getString("doc_extn");

                            documentListWorkFlow.add(new DocumentListWorkflow(docname,docid,path,extension));

                        }

                        workFlowDoclistAdapter = new WorkflowDocListAdapter(documentListWorkFlow,context);
                        rvBottomSheetDocumentView.setAdapter(workFlowDoclistAdapter);
                        llNofileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        rvBottomSheetDocumentView.setVisibility(View.VISIBLE);
                        bottomSheetDialog.show();

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
                params.put("docid",docid);
                params.put("userId", MainActivity.userid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);

    }




}
