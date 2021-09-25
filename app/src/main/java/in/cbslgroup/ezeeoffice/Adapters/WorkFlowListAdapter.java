package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.WorkFlow.IntiateWorkFlowActivity;
import in.cbslgroup.ezeeoffice.Model.WorkFlowList;
import in.cbslgroup.ezeeoffice.R;


public class WorkFlowListAdapter extends RecyclerView.Adapter<WorkFlowListAdapter.ViewHolder> {

    List<WorkFlowList> workFlowList = new ArrayList<>();
    Context context;


    public WorkFlowListAdapter(List<WorkFlowList> workFlowList, Context context) {
        this.workFlowList = workFlowList;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkFlowListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workflow_list_items, parent, false);
        return new ViewHolder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull final WorkFlowListAdapter.ViewHolder holder, int position) {

        holder.tvWorkFLowId.setText(workFlowList.get(position).getWorkFlowId());
        holder.tvWorkFlowName.setText(workFlowList.get(position).getWorkFlowName());
        holder.llWorkFlowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(context, holder.tvWorkFlowName.getText(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, IntiateWorkFlowActivity.class);
                intent.putExtra("workflowName",holder.tvWorkFlowName.getText().toString().trim());
                intent.putExtra("workflowID",holder.tvWorkFLowId.getText().toString().trim());
                context.startActivity(intent);


            }
        });






    }

    @Override
    public int getItemCount() {
        return workFlowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvWorkFlowName, tvWorkFLowId;
        LinearLayout llWorkFlowItem;

        public ViewHolder(View itemView) {
            super(itemView);

            llWorkFlowItem = itemView.findViewById(R.id.ll_workflow_item_main);
            tvWorkFlowName = itemView.findViewById(R.id.tv_workflow_name);
            tvWorkFLowId = itemView.findViewById(R.id.tv_workflow_id);


        }
    }
}
