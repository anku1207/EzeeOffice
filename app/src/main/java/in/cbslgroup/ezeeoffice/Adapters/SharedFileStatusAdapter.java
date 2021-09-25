package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Model.SharedFileStatus;
import in.cbslgroup.ezeeoffice.R;


public class SharedFileStatusAdapter extends RecyclerView.Adapter<SharedFileStatusAdapter.ViewHolder> {

    List<SharedFileStatus> sharedFileStatusList;
    Context context;

    public SharedFileStatusAdapter(List<SharedFileStatus> sharedFileStatusList, Context context) {
        this.sharedFileStatusList = sharedFileStatusList;
        this.context = context;
    }

    @NonNull
    @Override
    public SharedFileStatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shared_files_status_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SharedFileStatusAdapter.ViewHolder holder, int position) {

        if(position == sharedFileStatusList.size()-1){

            holder.decorator.setVisibility(View.GONE);
        }

        holder.tvStatus.setText(sharedFileStatusList.get(position).getText());
        holder.ivStatus.setImageResource(sharedFileStatusList.get(position).getStatusImage());

    }

    @Override
    public int getItemCount() {
        return sharedFileStatusList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView tvStatus;
        ImageView ivStatus;
        View decorator;

        public ViewHolder(View itemView) {

            super(itemView);

            tvStatus = itemView.findViewById(R.id.tv_shared_files_status);
            ivStatus = itemView.findViewById(R.id.iv_shared_files_status);
            decorator = itemView.findViewById(R.id.view_shared_files_decorator);


        }
    }
}
