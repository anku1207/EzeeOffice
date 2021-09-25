package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Appointment.ViewAppointmentActivity;
import in.cbslgroup.ezeeoffice.Model.AppointmentList;
import in.cbslgroup.ezeeoffice.R;

public class AppointmentlistAdapter extends RecyclerView.Adapter<AppointmentlistAdapter.ViewHolder> {

    List<AppointmentList> appointmentList;
    Context context;


    public AppointmentlistAdapter(List<AppointmentList> appointmentList, Context context) {

        this.appointmentList = appointmentList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ViewAppointmentActivity.class);
                intent.putExtra("aid",appointmentList.get(position).getId());
                intent.putExtra("action", "Specific");
                context.startActivity(intent);


            }
        });

        //excluding  the last view line in the rv
        if(position == appointmentList.size()-1){
            holder.aptListDecorator.setVisibility(View.GONE);
        }
        else{
            holder.aptListDecorator.setVisibility(View.VISIBLE);
        }


        holder.tvAptName.setText(appointmentList.get(position).getTitle());
        holder.tvDate.setText(appointmentList.get(position).getAppDate()+" "+appointmentList.get(position).getAppTime());


    }

    @Override
    public int getItemCount() {

        return appointmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        View aptListDecorator;
        LinearLayout llMain;
        TextView tvAptName;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            aptListDecorator = itemView.findViewById(R.id.appointment_list_decorator);
            llMain = itemView.findViewById(R.id.ll_appointment_list_layout_main);
            tvDate = itemView.findViewById(R.id.tv_appointment_list_layout_date);
            tvAptName = itemView.findViewById(R.id.tv_appointment_list_layout_taskname);


        }
    }
}
