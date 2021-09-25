package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Interface.MoveStorageListListener;
import in.cbslgroup.ezeeoffice.Model.MoveStorage;
import in.cbslgroup.ezeeoffice.R;


public class MoveStorageListAdapter extends RecyclerView.Adapter<MoveStorageListAdapter.ViewHolder> {


    List<MoveStorage> moveStorageList;
    Context context;
    MoveStorageListListener listener;


    public MoveStorageListAdapter(List<MoveStorage> moveStorageList, Context context, MoveStorageListListener listener) {
        this.moveStorageList = moveStorageList;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public MoveStorageListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.move_storage_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoveStorageListAdapter.ViewHolder holder, final int position) {


        holder.tvfoldername.setText(moveStorageList.get(position).getFoldername());
        holder.tvslid.setText(moveStorageList.get(position).getSlid());
        holder.imageView.setImageResource(moveStorageList.get(position).getDrawableicon());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onCardviewClick(v,position,moveStorageList.get(position).getSlid(),moveStorageList.get(position).getFoldername());

            }
        });


    }

    @Override
    public int getItemCount() {
        return moveStorageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvfoldername, tvslid;
        ImageView imageView;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cv_move_storage_list);
            imageView = itemView.findViewById(R.id.iv_movestorage_list);
            tvfoldername = itemView.findViewById(R.id.tv_move_storage_foldername);
            tvslid = itemView.findViewById(R.id.tv_move_storage_slid);



        }
    }
}
