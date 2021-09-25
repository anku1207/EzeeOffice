package in.cbslgroup.ezeeoffice.Fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Adapters.TodolistAdapter;
import in.cbslgroup.ezeeoffice.Model.TodoList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoListFragment extends Fragment {

    ArrayList<TodoList> todoLists = new ArrayList<>();
    RecyclerView rvTodo;
    ProgressBar pb;
    LinearLayout llNoTodoFound;


    private String dateOfTodo = "";

    public TodoListFragment() {

    }


    public static TodoListFragment createInstance(String date) {
        TodoListFragment fragment = new TodoListFragment();
        fragment.dateOfTodo = date;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_todo_list, container, false);

        rvTodo = v.findViewById(R.id.rv_tabbed_todolist);
        rvTodo.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTodo.hasFixedSize();

        llNoTodoFound = v.findViewById(R.id.ll_notodofound_tabbed_todolist);
        pb = v.findViewById(R.id.pb_tabbed_todolist);

        getSpecificDayTodo(dateOfTodo, "getSpecificDateTodo");

        return v;
    }


    void getSpecificDayTodo(String date, String action) {

        rvTodo.setVisibility(View.GONE);
        llNoTodoFound.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String msg = jsonObject.getString("msg");

                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject item = jsonArray.getJSONObject(i);

                            Log.e("item todo", item.toString());

                            TodoList todoList = new TodoList();
                            todoList.setTaskTime(jsonArray.getJSONObject(i).getString("task_time"));//
                            todoList.setTaskName(jsonArray.getJSONObject(i).getString("task_name"));//
                            todoList.setTaskDate(jsonArray.getJSONObject(i).getString("task_date"));//
                            todoList.setTodoId(jsonArray.getJSONObject(i).getString("id"));//
                            todoLists.add(todoList);
                        }

                        rvTodo.setAdapter(new TodolistAdapter(todoLists, getActivity()));

                        llNoTodoFound.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {

                        llNoTodoFound.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.GONE);

                    }

                } catch (Exception e) {


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                HashMap<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("date", date);
                return params;
            }
        };

        VolleySingelton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }


}
