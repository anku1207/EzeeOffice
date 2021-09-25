package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Interface.CustomItemClickListener;
import in.cbslgroup.ezeeoffice.Model.Foldername;
import in.cbslgroup.ezeeoffice.R;


public class FileViewHorizontalAdapter extends RecyclerView.Adapter<FileViewHorizontalAdapter.ViewHolder> {


    private List<Foldername> foldernameList;
    private Context context;
    private CustomItemClickListener listener;
    private int lastPosition = -1;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    //String fName;

    public FileViewHorizontalAdapter(List<Foldername> foldernameList, Context context, CustomItemClickListener listener) {
        this.foldernameList = foldernameList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FileViewHorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_horizontal_cardview, parent, false);
        return new FileViewHorizontalAdapter.ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final FileViewHorizontalAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String slid = foldernameList.get(position).getFoldername().substring(foldernameList.get(position).getFoldername().indexOf("&&") + 2);
        String fullFolderName = foldernameList.get(position).getFoldername();

        Log.e("Fullname in horiadapter",fullFolderName);
        String foldername = fullFolderName.substring(0,fullFolderName.indexOf("&&"));
        holder.tvFolderArrow.setText(fullFolderName);
        holder.tvFolderArrowslid.setText(slid);
        holder.tvFolderArrowFoldername.setText(foldername);

        // Here you apply the animation when the view is bound
        setAnimation(holder.itemView, position);

        Log.e("list size ", String.valueOf(foldernameList.size()));



       /* if(position== foldernameList.size()-1){

            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

        }
        else{

            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

        }*/

       /* if(position == lastPosition){

            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.grey));
           // notifyDataSetChanged();

        }
        else{
            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));
            // notifyDataSetChanged();

        }*/


        holder.llHorizontal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                v.startAnimation(buttonClick);

                String slid = foldernameList.get(position).getFoldername().substring(foldernameList.get(position).getFoldername().indexOf("&&") + 2);
                Log.e("slid in hori adap",slid);
                String fullFolderName = foldernameList.get(position).getFoldername();
                Log.e("fname in horiadapter", fullFolderName);
                listener.onItemClick(v, position, slid,fullFolderName);
                setAnimation(holder.itemView, position);


            }
        });

    }

    @Override
    public int getItemCount() {

        return foldernameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFolderArrow,tvFolderArrowslid,tvFolderArrowFoldername;
        ImageView ivArrow;
        LinearLayout llHorizontal;
        CardView cvHori;

        public ViewHolder(View itemView) {

            super(itemView);

            tvFolderArrow = itemView.findViewById(R.id.tv_rv_hierarcy_list_horizontal);
            tvFolderArrowslid = itemView.findViewById(R.id.tv_rv_hierarcy_list_horizontal_slid);
            tvFolderArrowFoldername= itemView.findViewById(R.id.tv_rv_hierarcy_list_horizontal_foldername);
            ivArrow = itemView.findViewById(R.id.iv_rv_horizonatal_arrow);
            llHorizontal = itemView.findViewById(R.id.ll_horizonatl_list);
            cvHori = itemView.findViewById(R.id.cv_hori);


        }


    }

    public void addItem(int position,Foldername foldername,ViewHolder holder){

        foldernameList.add(foldername);

        if(position == foldernameList.size()-1){

            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.grey));

        }
        else{

            holder.cvHori.setCardBackgroundColor(context.getResources().getColor(R.color.transparent));

        }

        notifyItemInserted(position);

    }

    public void deleteItem(int position){

        foldernameList.remove(position);
        notifyItemRemoved(position);


    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
