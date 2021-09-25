package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.cbslgroup.ezeeoffice.Chip.User;
import in.cbslgroup.ezeeoffice.R;

public class AssignMetadataSelectAdapter extends RecyclerView.Adapter<AssignMetadataSelectAdapter.ViewHolder> {




    public OnItemClickListener itemClickListener;
    List<User> metadatalist;
    Context context;

    public AssignMetadataSelectAdapter(List<User> metadatalist, Context context, OnItemClickListener itemClickListener) {
        this.metadatalist = metadatalist;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }


   public interface OnItemClickListener {

        void onButtonClick(int pos,String metaname,User obj);

    }

    @NonNull
    @Override
    public AssignMetadataSelectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.meta_chip_layout, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignMetadataSelectAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        viewHolder.tvMeta.setText(metadatalist.get(i).getUsername());
        viewHolder.llmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListener.onButtonClick(i,metadatalist.get(i).getUsername(),metadatalist.get(i));

            }
        });

        viewHolder.ivIcon.setImageResource(metadatalist.get(i).getDrawableId());


    }

    @Override
    public int getItemCount() {
        return metadatalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMeta;
        ImageView ivIcon;
        LinearLayout llmain;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llmain = itemView.findViewById(R.id.ll_meta_assign_chip);
            tvMeta = itemView.findViewById(R.id.meta_assign_chip_name);
            ivIcon = itemView.findViewById(R.id.meta_assign_chip_icon);

        }
    }

    public List<String> getMetaIdList(){

        List<String> slidlist = new ArrayList<>();
        slidlist.clear();



        for (User u:
             metadatalist) {

            slidlist.add(u.getSlid());
        }

        return slidlist;
    }

}

