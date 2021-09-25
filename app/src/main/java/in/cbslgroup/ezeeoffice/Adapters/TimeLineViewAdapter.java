package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.cbslgroup.ezeeoffice.Model.TimelineView;
import in.cbslgroup.ezeeoffice.R;


public class TimeLineViewAdapter extends RecyclerView.Adapter<TimeLineViewAdapter.ViewHolder> {

    Context context;
    List<TimelineView> timelineViewList ;

    public TimeLineViewAdapter(Context context, List<TimelineView> timelineViewList) {
        this.context = context;
        this.timelineViewList = timelineViewList;
    }

    @NonNull
    @Override
    public TimeLineViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timelineview_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewAdapter.ViewHolder holder, int position) {

        TimelineView row = timelineViewList.get(position);

        holder.rowTaskCount.setText(row.getTaskcount());
        holder.rowTaskStatus.setText(row.getTaskStatus());
        holder.rowTitle.setText(row.getTitle());



        //changing the background of the task count





        holder.rowDate.setText(row.getDate());

        if(row.getDescription().equals("")|| TextUtils.isEmpty(row.getDescription())||(row.getDescription().equals("null"))){

            holder.rowDescription.setVisibility(View.GONE);
        }
       else{
            holder.rowDescription.setText(row.getDescription());

        }
        holder.rowTaskHeading.setText(row.getTaskHeading());
        holder.rowApproval.setText(row.getTaskApprovedBy());

        if(!row.getAvatar().equals("")){


            byte[] decodedString = Base64.decode(row.getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            holder.rowAvatar.setImageBitmap(decodedByte);

        }


     /*   GradientDrawable bgShape = (GradientDrawable)holder.cvtaskApproval.getBackground();
        bgShape.setColor(Color.BLACK);

        holder.cvtaskApproval.setCardBackgroundColor(bgShape);*/

       String taskstatus = holder.rowTaskStatus.getText().toString();

       if(taskstatus.equalsIgnoreCase("Pending")){

          holder.cvComment.setVisibility(View.GONE);


       }

        if(taskstatus.equalsIgnoreCase("Approved")){


            holder.cvtaskApproval.setCardBackgroundColor(context.getResources().getColor(R.color.green_normal));
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }

        else if (taskstatus.equalsIgnoreCase("Rejected")){


            holder.cvtaskApproval.setCardBackgroundColor(Color.RED);
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(Color.RED);

        }
        else if (taskstatus.equalsIgnoreCase("Aborted")){


            holder.cvtaskApproval.setCardBackgroundColor(Color.RED);
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(Color.RED);

        }
        else if (taskstatus.equalsIgnoreCase("Processed")){

            holder.cvtaskApproval.setCardBackgroundColor(context.getResources().getColor(R.color.green_normal));
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }
        else if (taskstatus.equalsIgnoreCase("Complete")){
            holder.cvtaskApproval.setCardBackgroundColor(context.getResources().getColor(R.color.green_normal));
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }

        else if (taskstatus.equalsIgnoreCase("Done")){

            holder.cvtaskApproval.setCardBackgroundColor(context.getResources().getColor(R.color.green_normal));
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }
        else if (taskstatus.equalsIgnoreCase("Pending")){

            holder.cvtaskApproval.setCardBackgroundColor(context.getResources().getColor(R.color.light_yellow));
            ((GradientDrawable) holder.rowTaskCount.getBackground()).setColor(context.getResources().getColor(R.color.light_yellow));

        }

        float scale = context.getResources().getDisplayMetrics().density;
        int pixels;
        if (position == 0 && position == this.timelineViewList.size() - 1) {
            holder.rowUpperLine.setVisibility(View.INVISIBLE);
            holder.rowLowerLine.setVisibility(View.INVISIBLE);
        } else if (position == 0) {
            pixels = (int) ((float) row.getBellowLineSize() * scale + 0.5F);
            holder.rowUpperLine.setVisibility(View.INVISIBLE);
            holder.rowLowerLine.setBackgroundColor(row.getBellowLineColor());
            holder.rowLowerLine.getLayoutParams().width = pixels;
        } else if (position == this.timelineViewList.size() - 1) {
            pixels = (int) ((float) (this.timelineViewList.get(position - 1)).getBellowLineSize() * scale + 0.5F);
            holder.rowLowerLine.setVisibility(View.INVISIBLE);
            holder.rowUpperLine.setBackgroundColor((this.timelineViewList.get(position - 1)).getBellowLineColor());
            holder.rowUpperLine.getLayoutParams().width = pixels;
        } else {
            pixels = (int) ((float) row.getBellowLineSize() * scale + 0.5F);
            int pixels2 = (int) ((float) (this.timelineViewList.get(position - 1)).getBellowLineSize() * scale + 0.5F);
            holder.rowLowerLine.setBackgroundColor(row.getBellowLineColor());
            holder.rowUpperLine.setBackgroundColor((this.timelineViewList.get(position - 1)).getBellowLineColor());
            holder.rowLowerLine.getLayoutParams().width = pixels;
            holder.rowUpperLine.getLayoutParams().width = pixels2;
        }




    }

    @Override
    public int getItemCount() {
        return timelineViewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View rowUpperLine;
        View rowLowerLine;

        TextView rowDate;
        TextView rowTitle;
        TextView rowDescription;
        TextView rowTaskCount;
        TextView rowApproval;
        TextView rowTaskHeading;
        TextView rowTaskStatus;

        CircleImageView rowAvatar;
        CardView cvtaskApproval;
        CardView cvComment;



        public ViewHolder(View itemView) {
            super(itemView);
            rowUpperLine = itemView.findViewById(R.id.timeline_View_UpperLine);
            rowLowerLine = itemView.findViewById(R.id.timeline_View_LowerLine);

            rowDate = itemView.findViewById(R.id.timeline_View_Date);
            rowTitle = itemView.findViewById(R.id.timeline_View_Title);

            rowDescription = itemView.findViewById(R.id.timeline_View_Desc);

            rowAvatar = itemView.findViewById(R.id.iv_circular_timeline_view_avatar);

            rowTaskCount = itemView.findViewById(R.id.timeline_View_task_count);

            rowApproval = itemView.findViewById(R.id.tv_timeline_view_task_approval_status);

            cvtaskApproval = itemView.findViewById(R.id.cv_timeline_view_approval);

            rowTaskHeading = itemView.findViewById(R.id.tv_timeline_view_task_name);

            rowTaskStatus = itemView.findViewById(R.id.tv_timeline_view_task_status);

            cvComment = itemView.findViewById(R.id.cv_timeline_comment);
        }
    }
}
