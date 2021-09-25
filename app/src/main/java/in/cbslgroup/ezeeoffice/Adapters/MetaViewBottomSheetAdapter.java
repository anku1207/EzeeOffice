package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Model.MetaBottomSheet;
import in.cbslgroup.ezeeoffice.R;


public class MetaViewBottomSheetAdapter extends RecyclerView.Adapter<MetaViewBottomSheetAdapter.ViewHolder> {

    List<MetaBottomSheet> metaBottomSheetList;
    Context context;


    public MetaViewBottomSheetAdapter(List<MetaBottomSheet> metaBottomSheetList, Context context) {
        this.metaBottomSheetList = metaBottomSheetList;
        this.context = context;
    }

    @Override
    public MetaViewBottomSheetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottomsheet_metaview_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MetaViewBottomSheetAdapter.ViewHolder holder, int position) {

        holder.tvMetaname.setText(metaBottomSheetList.get(position).getMetaname());


    }

    @Override
    public int getItemCount() {
        return metaBottomSheetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMetaname;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMetaname = itemView.findViewById(R.id.bottom_sheet_tv_metadataname);
        }
    }
}
