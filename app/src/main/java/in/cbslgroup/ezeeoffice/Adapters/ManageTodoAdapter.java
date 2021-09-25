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

import in.cbslgroup.ezeeoffice.Activity.Todo.TodoActivity;
import in.cbslgroup.ezeeoffice.Model.TodoList;
import in.cbslgroup.ezeeoffice.R;

public class ManageTodoAdapter extends RecyclerView.Adapter<ManageTodoAdapter.ViewHolder> {

    Context context;
    List<TodoList> todoList;
    BottomSheetDialog bottomSheetDialog;
    OnButtonClickListener onButtonClickListener;

    public ManageTodoAdapter(Context context, List<TodoList> todoList) {
        this.context = context;
        this.todoList = todoList;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {

        this.onButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public ManageTodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_todo_apt_list_2, parent, false);
        return new ManageTodoAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageTodoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        TodoList todo = todoList.get(position);

        if(TodoActivity.PERMISSION_EDIT_TODO.equalsIgnoreCase("1")){

            holder.btnEditTodo.setVisibility(View.VISIBLE);

        }

        else{

            holder.btnEditTodo.setVisibility(View.GONE);

        }

        if(TodoActivity.PERMISSION_ARCHIEVE_TODO.equalsIgnoreCase("1")){

            holder.btnArchiveTodo.setVisibility(View.VISIBLE);

        }

        else{

            holder.btnArchiveTodo.setVisibility(View.GONE);

        }



        holder.tvTaskName.setText(todo.getTaskName());
        holder.tvTaskDesc.setText(todo.getTaskDesc());
        holder.tvTaskNotiTime.setText(todo.getTaskNotiTime());
        holder.tvTaskTime.setText(todo.getTaskDate() + " " + todo.getTaskTime());
        holder.btnEditTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tdid = todo.getTodoId();
                String taskname = todo.getTaskName();
                String taskdesc = todo.getTaskDesc();
                String tasknotitime = todo.getTaskNotiTime();
                String tasktime = todo.getTaskTime();
                String taskdate = todo.getTaskDate();
                String tasknotifreq = todo.getTaskNotiFreq();

                onButtonClickListener.OnEditButtonListener(tdid, taskname, taskdate, taskdesc, tasktime, tasknotifreq, tasknotitime);

            }
        });

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taskname = todo.getTaskName();
                String taskdesc = todo.getTaskDesc();
                String tasknotitime = todo.getTaskNotiTime();
                String tasktime = todo.getTaskTime();
                String taskdate = todo.getTaskDate();
                String tasknotifreq = todo.getTaskNotiFreq();

                showBottomSheetDialog(taskname, taskdate, taskdesc, tasktime, tasknotifreq, tasknotitime);

            }
        });

        holder.btnArchiveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showArchievePopup(position, todo.getTaskName());


            }
        });

    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void showBottomSheetDialog(String... task) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bottomsheet_manage_todo, null);

        bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(view);

        TextView tvTaskName, tvTaskDate, tvTaskDesc, tvTaskTime, tvTaskNotificationFreq, tvTaskNotiTime;
        tvTaskName = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_taskname);
        tvTaskDate = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_taskdate);
        tvTaskDesc = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_task_desc);
        tvTaskTime = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_tasktime);
        tvTaskNotificationFreq = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_tasknotifreq);
        tvTaskNotiTime = bottomSheetDialog.findViewById(R.id.bs_tv_manage_todo_list_notitime);

        tvTaskName.setText(task[0]);
        tvTaskDate.setText(task[1]);
        tvTaskDesc.setText(task[2]);
        tvTaskTime.setText(task[3]);
        tvTaskNotificationFreq.setText(task[4]);
        tvTaskNotiTime.setText(task[5]);


        if(task[2].length()==0)
        {
            tvTaskDesc.setText("No task description found");
            tvTaskDesc.setTextColor(Color.RED);

        }
        else{

            tvTaskDesc.setText(task[2]);
        }

        bottomSheetDialog.show();


    }

    void showArchievePopup(int position, String todoname) {

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
        tvMsg.setText("Are you sure you want archive " + todoname + " ?");

        Button btnConfirmAtten = dialog.findViewById(R.id.btn_archieve_alert_confirm);
        btnConfirmAtten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonClickListener.OnArchiveButtonListener(todoList.get(position).getTodoId(), position);

                dialog.dismiss();

            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();


    }

    public void removeItem(int pos) {

        //todoList.remove(pos);
        notifyItemRemoved(pos);

    }


    public interface OnButtonClickListener {

        //taskname, taskdate, taskdesc, tasktime, tasknotifreq, tasknotitime
        void OnArchiveButtonListener(String tdid, int pos);

        void OnEditButtonListener(String tdid, String taskname, String taskdate, String taskdesc, String tasktime, String tasknotifreq, String tasknotitime);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTaskName, tvTaskDesc, tvTaskTime,tvTaskNotiTime;
        Button btnEditTodo, btnArchiveTodo;
        LinearLayout llMain;

        public ViewHolder(View itemView) {
            super(itemView);


            llMain = itemView.findViewById(R.id.ll_manage_apt_todo_list_main);
            tvTaskName = itemView.findViewById(R.id.tv_manage_todo_apt_list_taskname);
            tvTaskDesc = itemView.findViewById(R.id.tv_manage_todo_apt_list_task_desc);
            tvTaskTime = itemView.findViewById(R.id.tv_manage_todo_apt_list_tasktime);
            tvTaskNotiTime = itemView.findViewById(R.id.tv_manage_todo_apt_list_notitime);

            btnEditTodo = itemView.findViewById(R.id.btn_manage_todo_apt_list_edit);
            btnArchiveTodo = itemView.findViewById(R.id.btn_manage_todo_apt_list_archive);


        }
    }

}
