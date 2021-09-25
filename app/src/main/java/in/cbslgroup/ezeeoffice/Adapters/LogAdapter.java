package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Model.Logs;
import in.cbslgroup.ezeeoffice.R;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    List<Logs> logsList;
    Context context;

    public LogAdapter(List<Logs> logsList, Context context) {
        this.logsList = logsList;
        this.context = context;

    }


    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LogAdapter.ViewHolder holder, int position) {

        Logs logs = logsList.get(position);
        holder.tvAction.setText(logs.getAction());
        holder.tvActionBy.setText(logs.getActionBy());
        holder.tvActionTime.setText(logs.getActionTime());

    }

    @Override
    public int getItemCount() {

        return logsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvAction, tvActionBy, tvActionTime;

        public ViewHolder(View itemView) {
            super(itemView);

            tvAction = itemView.findViewById(R.id.tv_logs_action);
            tvActionBy = itemView.findViewById(R.id.tv_logs_action_name);
            tvActionTime = itemView.findViewById(R.id.tv_logs_action_time);
        }
    }
}
