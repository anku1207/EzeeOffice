package in.cbslgroup.ezeeoffice.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import in.cbslgroup.ezeeoffice.Activity.Appointment.ViewAppointmentActivity;
import in.cbslgroup.ezeeoffice.Model.AppointmentList;
import in.cbslgroup.ezeeoffice.R;

public class ManageAppointmentAdapter extends RecyclerView.Adapter<ManageAppointmentAdapter.ViewHolder> {

    Context context;
    List<AppointmentList> appointmentList;
    BottomSheetDialog bottomSheetDialog;
    OnButtonClickListener onButtonClickListener;


    public ManageAppointmentAdapter(Context context, List<AppointmentList> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {

        this.onButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public ManageAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_todo_apt_list_2, parent, false);
        return new ManageAppointmentAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAppointmentAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        AppointmentList apt = appointmentList.get(position);

        if (ViewAppointmentActivity.PERMISSION_ARCHIEVE_APPOINTMENT.equalsIgnoreCase("1")) {

            holder.btnArchApt.setVisibility(View.VISIBLE);

        } else {

            holder.btnArchApt.setVisibility(View.GONE);

        }

        if (ViewAppointmentActivity.PERMISSION_EDIT_APPOINTMENT.equalsIgnoreCase("1")) {

            holder.btnEditApt.setVisibility(View.VISIBLE);

        } else {

            holder.btnArchApt.setVisibility(View.GONE);

        }


        if (apt.getAppNotes().isEmpty() || apt.getAppNotes().length() == 0) {
            holder.tvNotes.setText("No notes found");
            holder.tvNotes.setTextColor(Color.RED);

        } else {

            holder.tvNotes.setText(apt.getAppNotes());
            holder.tvNotes.setTextColor(Color.DKGRAY);
        }

        holder.tvNotiTime.setText(apt.getNotifyTime());
        holder.tvTime.setText(apt.getAppDate() + " " + apt.getAppTime());
        holder.tvTitle.setText(apt.getTitle());
        holder.btnArchApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showArchievePopup(position, apt.getTitle());

            }
        });


        holder.btnEditApt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonClickListener.OnEditButtonListener(apt.getId(), apt.getTitle(), apt.getAppDate(), apt.getAppNotes(), apt.getAppTime(), apt.getNotifyFrequency(), apt.getNotifyTime(), apt.getAgenda(), apt.getContact(), apt.getContactEmail());

            }
        });

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskname = apt.getTitle();
                String taskdesc = apt.getAppNotes();
                String tasknotitime = apt.getNotifyTime();
                String tasktime = apt.getAppTime();
                String taskdate = apt.getAppDate();
                String tasknotifreq = apt.getNotifyFrequency();
                String agenda = apt.getAgenda();
                String contactname = apt.getContact();
                String contactEmail = apt.getContactEmail();
                showBottomSheetDialog(taskname, taskdate, taskdesc, tasktime, tasknotifreq, tasknotitime, agenda, contactname, contactEmail);

            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public void showBottomSheetDialog(String... task) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_manage_apt, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        TextView tvTaskName, tvNotes, tvTaskDate, tvTaskDesc, tvTaskTime, tvTaskNotificationFreq, tvTaskNotiTime, tvAgenda, tvContactName, tvContactEmail;
        tvTaskName = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_title);
        tvTaskDate = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_date);
        tvTaskDesc = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_notes);
        tvTaskTime = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_time);
        tvTaskNotificationFreq = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_notifreq);
        tvTaskNotiTime = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_notitime);
        tvAgenda = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_agenda);
        tvContactName = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_contact_name);
        tvContactEmail = bottomSheetDialog.findViewById(R.id.bs_tv_manage_appointment_list_contact_email);


        tvTaskName.setText(task[0]);
        tvTaskDate.setText(task[1]);
        tvTaskDesc.setText(task[2]);
        tvTaskTime.setText(task[3]);
        tvTaskNotificationFreq.setText(task[4]);
        tvTaskNotiTime.setText(task[5]);
        tvAgenda.setText(task[6]);
        tvContactName.setText(task[7]);
        tvContactEmail.setText(task[8]);

        if (task[2].length() == 0) {

            tvTaskDesc.setText("No Notes Found");
            tvTaskDesc.setTextColor(Color.RED);

        } else {

            tvTaskDesc.setText(task[2]);
        }


        bottomSheetDialog.show();


    }

    void showArchievePopup(int position, String aptname) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialog_archieve);

        ImageView ivClose = dialog.findViewById(R.id.iv_archive_alert_close_popup);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });

        TextView tvMsg = dialog.findViewById(R.id.tv_archive_alert_message);
        tvMsg.setText("Are you sure you want archive " + aptname + " ?");

        Button btnConfirmAtten = dialog.findViewById(R.id.btn_archieve_alert_confirm);
        btnConfirmAtten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonClickListener.OnArchiveButtonListener(appointmentList.get(position).getId(), position);

                dialog.dismiss();

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


    }

    public void removeItem(int pos) {

        // appointmentList.remove(pos);
        notifyItemRemoved(pos);

    }


    public interface OnButtonClickListener {

        //taskname, taskdate, taskdesc, tasktime, tasknotifreq, tasknotitime
        void OnArchiveButtonListener(String aptid, int pos);

        void OnEditButtonListener(String aptid, String taskname, String taskdate, String taskdesc, String tasktime, String tasknotifreq, String tasknotitime, String agenda, String contactname, String contactemail);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvNotes, tvTime, tvNotiTime;
        Button btnArchApt, btnEditApt;
        LinearLayout llMain;

        public ViewHolder(View itemView) {
            super(itemView);

            llMain = itemView.findViewById(R.id.ll_manage_apt_todo_list_main);
            tvTitle = itemView.findViewById(R.id.tv_manage_todo_apt_list_taskname);
            tvNotes = itemView.findViewById(R.id.tv_manage_todo_apt_list_task_desc);
            tvTime = itemView.findViewById(R.id.tv_manage_todo_apt_list_tasktime);
            tvNotiTime = itemView.findViewById(R.id.tv_manage_todo_apt_list_notitime);

            btnEditApt = itemView.findViewById(R.id.btn_manage_todo_apt_list_edit);
            btnArchApt = itemView.findViewById(R.id.btn_manage_todo_apt_list_archive);


        }
    }

}
