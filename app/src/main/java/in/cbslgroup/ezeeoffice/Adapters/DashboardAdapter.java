package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeeoffice.Activity.AuditTrail.AuditTrailStorageActivity;
import in.cbslgroup.ezeeoffice.Activity.AuditTrail.AuditTrailUserActivity;
import in.cbslgroup.ezeeoffice.Activity.Dms.DmsActivity;
import in.cbslgroup.ezeeoffice.Activity.Search.MetaDataSearchActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.AuditTrailWorkFlowActivity;
import in.cbslgroup.ezeeoffice.Activity.WorkFlow.InTrayActivity;
import in.cbslgroup.ezeeoffice.Model.DashBoard;
import in.cbslgroup.ezeeoffice.R;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    List<DashBoard> dashBoardList;
    Context context;
    private int lastPosition = -1;

    public DashboardAdapter(List<DashBoard> dashBoardList,Context context) {
        this.dashBoardList = dashBoardList;
        this.context = context;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_card_layout, viewGroup, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull DashboardAdapter.ViewHolder viewHolder, int i) {

        viewHolder.civIcon.setImageDrawable(dashBoardList.get(i).getImage());
        viewHolder.tvHeading.setText(dashBoardList.get(i).getHeading());
        viewHolder.tvSubHeading.setText(dashBoardList.get(i).getSubheading());

        String labelName = viewHolder.tvHeading.getText().toString();


        // Here you apply the animation when the view is bound
        setAnimation(viewHolder.itemView, i);


        viewHolder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(labelName.equalsIgnoreCase("My Dms"))
                {
                    Intent intent = new Intent(context,DmsActivity.class);
                    context.startActivity(intent);

                }

                else if(labelName.equalsIgnoreCase("In Tray"))
                {


                    Intent intent = new Intent(context, InTrayActivity.class);
                    context.startActivity(intent);

                } else if(labelName.equalsIgnoreCase("User Report"))
                {


                    Intent intent = new Intent(context, AuditTrailUserActivity.class);
                    context.startActivity(intent);

                } else if(labelName.equalsIgnoreCase("Storage Report"))
                {


                    Intent intent = new Intent(context, AuditTrailStorageActivity.class);
                    context.startActivity(intent);

                } else if(labelName.equalsIgnoreCase("Workflow Report"))
                {


                    Intent intent = new Intent(context, AuditTrailWorkFlowActivity.class);
                    context.startActivity(intent);

                } else if(labelName.equalsIgnoreCase("Metadata Search"))
                {


                    Intent intent = new Intent(context, MetaDataSearchActivity.class);
                    context.startActivity(intent);

                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return dashBoardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeading,tvSubHeading;
        CircleImageView civIcon;
        CardView cvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tvHeading =  itemView.findViewById(R.id.tv_dashboard_heading);
            tvSubHeading =  itemView.findViewById(R.id.tv_dashboard_subheading);
            civIcon =  itemView.findViewById(R.id.civ_dashboard_icon);
            cvItem = itemView.findViewById(R.id.cv_dashboard_item);


        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
