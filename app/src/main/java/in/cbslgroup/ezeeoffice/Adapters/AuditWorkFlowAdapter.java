package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeeoffice.Model.AuditTrailWorkflow;
import in.cbslgroup.ezeeoffice.R;


public class AuditWorkFlowAdapter extends RecyclerView.Adapter<AuditWorkFlowAdapter.ViewHolder> {

    List<AuditTrailWorkflow> auditTrailWorkflowList = new ArrayList<>();
    Context context;



    public AuditWorkFlowAdapter(List<AuditTrailWorkflow> auditTrailWorkflowList, Context context) {
        this.auditTrailWorkflowList = auditTrailWorkflowList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.audittrail_workflow_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AuditTrailWorkflow list  = auditTrailWorkflowList.get(position);

        holder.tvSno.setText(list.getSno());
        holder.tvActionName.setText(list.getActionPerformed());
        holder.tvUsername.setText(list.getUserName());
        holder.tvip.setText(list.getIp());
        holder.tvStartDate.setText(list.getActionDateTime());
        holder.tvDocid.setText(list.getSno());

    }

    @Override
    public int getItemCount() {

        return auditTrailWorkflowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSno,tvUsername,tvDocid,tvip,tvActionName,tvStartDate;
       public ViewHolder(View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_audit_workflow_list_username);
            tvSno = itemView.findViewById(R.id.tv_audit_workflow_list_sno);
            tvDocid = itemView.findViewById(R.id.tv_audit_workflow_list_docid);
            tvip = itemView.findViewById(R.id.tv_audit_workflow_list_sytemip);
            tvActionName = itemView.findViewById(R.id.tv_audit_workflow_list_actionperformed);
            tvStartDate = itemView.findViewById(R.id.tv_audit_workflow_list_actionstartdate);


        }
    }
}
