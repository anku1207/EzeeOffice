package in.cbslgroup.ezeeoffice.Activity.Todo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.ManageTodoAdapter;
import in.cbslgroup.ezeeoffice.Model.TodoList;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.CustomSpinner;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class TodoActivity extends AppCompatActivity {

    public static String PERMISSION_VIEW_TODO, PERMISSION_ADD_TODO, PERMISSION_ARCHIEVE_TODO, PERMISSION_EDIT_TODO;
    RecyclerView rvTodo;
    ArrayList<TodoList> todoLists = new ArrayList<>();
    CustomSpinner spSelectDate;
    ArrayList<String> datelist = new ArrayList<>();
    Toolbar toolbar;
    ManageTodoAdapter manageTodoAdapter;
    ProgressBar pb;
    LinearLayout llNoTodoFound;
    ArrayAdapter<String> spadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);


        toolbar = findViewById(R.id.toolbar_manage_todo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        rvTodo = findViewById(R.id.rv_manage_todo);
        pb = findViewById(R.id.pb_manage_todo);
        llNoTodoFound = findViewById(R.id.ll_no_todo_found_manage_todo);
        //rvTodo.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        //rvTodo.setLayoutManager(new LinearLayoutManager(this));
        manageTodoAdapter = new ManageTodoAdapter(TodoActivity.this, todoLists);
        //rvTodo.setHasFixedSize(true);


        datelist.add("Today");//0
        datelist.add("Tomorrow");//1
        datelist.add("This week");//2
        datelist.add("All");//3
        datelist.add("Select Date");


        spSelectDate = findViewById(R.id.sp_manage_todo);
        //spinner adapter
        spadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, datelist) {

            @Override
            public int getCount() {
                // don't display last item. It is used as hint.
                int count = super.getCount();
                return count > 0 ? count - 1 : count;
            }


        };

        spSelectDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                TextView selectedText = (TextView) parent.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(Color.WHITE);
                }
                //spSelectDate.setSelection(position);
                String itemselected = spSelectDate.getSelectedItem().toString();

                if (!itemselected.equalsIgnoreCase("Select Date")) {
                    toolbar.setSubtitle(itemselected + " Todo");
                }

                switch (itemselected) {

                    case "Today":
                        getUserPermission(MainActivity.userid, "getTodaysTodo");
                        break;
                    case "Tomorrow":
                        getUserPermission(MainActivity.userid, "getTomorrowTodo");
                        break;
                    case "This week":
                        getUserPermission(MainActivity.userid, "getWeeksTodo");
                        break;
                    case "All":
                        getUserPermission(MainActivity.userid, "getAllTodo");
                        break;

                  /*  case "Select Date":
                        Toast.makeText(TodoActivity.this, "Select Date for search", Toast.LENGTH_SHORT).show();
                        break;*/
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        spadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSelectDate.setAdapter(spadapter);
        spSelectDate.setSelection(spadapter.getCount());


        Intent intent = getIntent();
        String action = intent.getStringExtra("action");

        switch (action) {

            case "Today":
                //getUserPermission(MainActivity.userid, "getTodaysTodo");
                toolbar.setSubtitle(action + " Todo");
                spSelectDate.setSelection(0);


                break;
            case "Tomorrow":
                //getUserPermission(MainActivity.userid, "getTommorowTodo");
                toolbar.setSubtitle(action + " Todo");
                spSelectDate.setSelection(1);
                break;
            case "This week":
               // getUserPermission(MainActivity.userid, "getWeeksTodo");
                toolbar.setSubtitle(action + " Todo");
                spSelectDate.setSelection(2);
                break;
            case "All":
               // getUserPermission(MainActivity.userid, "getAllTodo");
                toolbar.setSubtitle(action + " Todo");
                spSelectDate.setSelection(3);
                break;

            case "Specific":
                getUserPermission(MainActivity.userid, "getSpecificDayTodo");
                toolbar.setSubtitle(action + " day Todo");

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_todo, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_manage_todo_add_todo) {

            Intent intent = new Intent(TodoActivity.this, AddToDoActivity.class);
            intent.putExtra("todoid", "null2");
            startActivityForResult(intent, 123);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123) {

            //Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();

            String subtitle = toolbar.getSubtitle().toString();

            String action = subtitle.substring(0, subtitle.indexOf("Todo")).trim();
            Log.e("action in onacres", action);

            switch (action) {

                case "Today":
                    getUserPermission(MainActivity.userid, "getTodaysTodo");
                    toolbar.setSubtitle(action + " Todo");
                    break;
                case "Tomorrow":
                    getUserPermission(MainActivity.userid, "getTomorrowTodo");
                    toolbar.setSubtitle(action + " Todo");
                    break;
                case "This week":
                    getUserPermission(MainActivity.userid, "getWeeksTodo");
                    toolbar.setSubtitle(action + " Todo");
                    break;
                case "All":
                    getUserPermission(MainActivity.userid, "getAllTodo");
                    toolbar.setSubtitle(action + " Todo");
                    break;

            }

        }
    }

    void getTodoList(String userid, String action) {



        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Response Todo " + action, response);

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
                            todoList.setEmpId(jsonArray.getJSONObject(i).getString("emp_id"));//
                            todoList.setTaskNotiFreq(getNotiFreqName(jsonArray.getJSONObject(i).getString("task_notification_frequency")));//
                            todoList.setTodoId(jsonArray.getJSONObject(i).getString("id"));//
                            todoList.setTaskDesc(jsonArray.getJSONObject(i).getString("task_description"));//
                            todoList.setTaskNotiTime(jsonArray.getJSONObject(i).getString("task_notify_time"));//
                            todoList.setIsArchived(jsonArray.getJSONObject(i).getString("is_archived"));//

                            todoLists.add(todoList);


                        }


                        llNoTodoFound.setVisibility(View.GONE);
                        pb.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {


                        // Toast.makeText(TodoActivity.this, msg, Toast.LENGTH_SHORT).show();

                        pb.setVisibility(View.GONE);
                        rvTodo.setVisibility(View.GONE);
                        llNoTodoFound.setVisibility(View.VISIBLE);


                    }

                    rvTodo.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
                    rvTodo.setAdapter(manageTodoAdapter);
                    manageTodoAdapter.setOnButtonClickListener(new ManageTodoAdapter.OnButtonClickListener() {
                        @Override
                        public void OnArchiveButtonListener(String tdid, int pos) {

                            archiveTodo("archeiveTodo", tdid, pos);
                        }


                        @Override
                        public void OnEditButtonListener(String tdid, String taskname, String taskdate, String taskdesc, String tasktime, String tasknotifreq, String tasknotitime) {

                            Intent intent = new Intent(TodoActivity.this, AddToDoActivity.class);
                            //Toast.makeText(TodoActivity.this, tdid, Toast.LENGTH_SHORT).show();
                            intent.putExtra("todoid", tdid);
                            intent.putExtra("taskname", taskname);
                            intent.putExtra("taskdate", taskdate);
                            intent.putExtra("tasktime", tasktime);
                            intent.putExtra("taskdesc", taskdesc);
                            intent.putExtra("tasknotifreq", tasknotifreq);
                            intent.putExtra("tasknotitime", tasknotitime);

                            startActivityForResult(intent, 123);
                        }
                    });
                    manageTodoAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(TodoActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();


                if (action.equalsIgnoreCase("getTodaysTodo")) {

                    params.put("action", action);
                    params.put("userid", userid);

                } else if (action.equalsIgnoreCase("getTomorrowTodo")) {

                    params.put("action", action);
                    params.put("userid", userid);

                } else if (action.equalsIgnoreCase("getWeeksTodo")) {

                    params.put("action", action);
                    params.put("userid", userid);

                } else if (action.equalsIgnoreCase("getAllTodo")) {

                    params.put("action", action);
                    params.put("userid", userid);

                } else if (action.equalsIgnoreCase("getSpecificDayTodo")) {

                    String tdid = getIntent().getStringExtra("tdid");
                    params.put("action", action);
                    params.put("tdid", tdid);

                }


                return params;
            }
        };


        VolleySingelton.getInstance(TodoActivity.this).addToRequestQueue(stringRequest);


    }

    String getNotiFreqName(String noti) {

        if (noti.equalsIgnoreCase("10")) {

            return "Select Task Notification Frequency";
        } else if (noti.equalsIgnoreCase("0")) {
            return "Same day";

        } else {
            return noti + " Day Before";
        }

    }

    void archiveTodo(String action, String tdid, int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.TODO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.getString("msg");
                    String error = jsonObject.getString("error");

                    if (error.equalsIgnoreCase("false")) {

                        Toast.makeText(TodoActivity.this, msg, Toast.LENGTH_SHORT).show();
                        manageTodoAdapter.removeItem(position);

                        manageTodoAdapter.removeItem(position);
                        todoLists.remove(position);

                        if (todoLists.size() == 0) {

                            showNoFileFound();
                        }


                    } else if (error.equalsIgnoreCase("true")) {


                        Toast.makeText(TodoActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("tdid", tdid);
                params.put("action", action);
                return params;
            }
        };

        VolleySingelton.getInstance(TodoActivity.this).addToRequestQueue(stringRequest);

    }

    private void showNoFileFound() {

        rvTodo.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
        llNoTodoFound.setVisibility(View.VISIBLE);
    }

    void getUserPermission(final String userid,final String action) {

        todoLists.clear();
        rvTodo.setVisibility(View.GONE);
        llNoTodoFound.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response profile", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);


                    //Todo permissions
                    String todo_view = jsonObject.getString("todo_view");
                    String todo_add = jsonObject.getString("todo_add");
                    String todo_edit = jsonObject.getString("todo_edit");
                    String todo_archieve = jsonObject.getString("todo_archive");


                    if (todo_view.equalsIgnoreCase("1")) {

                        PERMISSION_VIEW_TODO = "1";
                    } else {

                        PERMISSION_VIEW_TODO = "0";

                    }

                    if (todo_add.equalsIgnoreCase("1")) {

                        PERMISSION_ADD_TODO = "1";
                        toolbar.getMenu().findItem(R.id.action_manage_todo_add_todo).setVisible(true);


                    } else {

                        PERMISSION_ADD_TODO = "0";
                        toolbar.getMenu().findItem(R.id.action_manage_todo_add_todo).setVisible(false);


                    }

                    if (todo_edit.equalsIgnoreCase("1")) {

                        PERMISSION_EDIT_TODO = "1";
                    } else {

                        PERMISSION_EDIT_TODO = "0";

                    }

                    if (todo_archieve.equalsIgnoreCase("1")) {

                        PERMISSION_ARCHIEVE_TODO = "1";
                    } else {

                        PERMISSION_ARCHIEVE_TODO = "0";

                    }



                    getTodoList(userid,action);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("i am here: " + new Exception().getStackTrace()[0]);
                // getUserPermission(userid);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");
                return params;
            }
        };


        VolleySingelton.getInstance(TodoActivity.this).addToRequestQueue(stringRequest);


    }


    String setRoles(){


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("todo_view").append(",")
                .append("todo_add").append(",")
                .append("todo_edit").append(",")
                .append("todo_archive");

        return stringBuilder.toString();

    }

}
