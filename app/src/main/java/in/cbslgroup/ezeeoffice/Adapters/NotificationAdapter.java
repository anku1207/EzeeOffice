package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Appointment.ViewAppointmentActivity;
import in.cbslgroup.ezeeoffice.Activity.Todo.TodoActivity;
import in.cbslgroup.ezeeoffice.Model.Notification;
import in.cbslgroup.ezeeoffice.R;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<Notification> notificationList;
    Context context;

    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }


    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {

        Notification noti = notificationList.get(position);
        holder.tvDate.setText(noti.getDate());
        holder.tvName.setText(noti.getName());
        holder.ivIcon.setImageDrawable(noti.getIcon());

        String type = noti.getType();

        if(type.equalsIgnoreCase("todo")){

            holder.tvType.setText("Todo");
            ((GradientDrawable) holder.tvType.getBackground()).setColor(context.getResources().getColor(R.color.green_normal));

        }

        else if(type.equalsIgnoreCase("apt")){

            holder.tvType.setText("Appointment");
            ((GradientDrawable) holder.tvType.getBackground()).setColor(context.getResources().getColor(R.color.light_red));


        }



        holder.cvmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(type.equalsIgnoreCase("todo"))
                {
                    Intent intent = new Intent(context,TodoActivity.class);
                    intent.putExtra("tdid",noti.getId());
                    intent.putExtra("action","Specific");
                    context.startActivity(intent);
                }
                else if (type.equalsIgnoreCase("apt")){

                    Intent intent = new Intent(context,ViewAppointmentActivity.class);
                    intent.putExtra("aid",noti.getId());
                    intent.putExtra("action","Specific");
                    context.startActivity(intent);

                }

            }
        });





    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cvmain;
        ImageView ivIcon;
        TextView tvName, tvDate,tvType;

        public ViewHolder(View itemView) {
            super(itemView);

            cvmain = itemView.findViewById(R.id.cv_notification_item_main);
            ivIcon = itemView.findViewById(R.id.iv_noti_icon);
            tvName = itemView.findViewById(R.id.tv_noti_taskname);
            tvDate = itemView.findViewById(R.id.tv_noti_taskdate);
            tvType = itemView.findViewById(R.id.tv_noti_type);

        }
    }
}
