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

import in.cbslgroup.ezeeoffice.Activity.Todo.TodoActivity;
import in.cbslgroup.ezeeoffice.Model.TodoList;
import in.cbslgroup.ezeeoffice.R;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.ViewHolder> {


    List<TodoList> todoList;
    Context context;


    public TodolistAdapter(List<TodoList> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodolistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull TodolistAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TodoActivity.class);
                intent.putExtra("tdid", todoList.get(position).getTodoId());
                intent.putExtra("action", "Specific");
                context.startActivity(intent);


            }
        });

        //excluding  the last view line in the rv
        if (position == todoList.size() - 1) {
            holder.todoListDecorator.setVisibility(View.GONE);
        } else {
            holder.todoListDecorator.setVisibility(View.VISIBLE);
        }


        holder.tvTodoName.setText(todoList.get(position).getTaskName());
        holder.tvDate.setText(todoList.get(position).getTaskTime() + " " + todoList.get(position).getTaskDate());


    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View todoListDecorator;
        LinearLayout llMain;
        TextView tvTodoName;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            todoListDecorator = itemView.findViewById(R.id.todo_list_decorator);
            llMain = itemView.findViewById(R.id.ll_todo_list_layout_main);
            tvDate = itemView.findViewById(R.id.tv_todo_list_layout_date);
            tvTodoName = itemView.findViewById(R.id.tv_todo_list_layout_taskname);


        }
    }
}
