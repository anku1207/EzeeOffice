package in.cbslgroup.ezeeoffice.Activity.Search;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Activity.MainActivity;
import in.cbslgroup.ezeeoffice.Adapters.FreqQueryAdapter;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;

public class FrequentlyQueriesActivity extends AppCompatActivity {


    RecyclerView rvmain;
    LinearLayoutManager linearLayoutManager;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FreqQueryAdapter freqQueryAdapter;
    List<String> querynameList = new ArrayList<>();
    LinearLayout llNoqueryFound;
    ProgressBar pbMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequently_queries);

        Toolbar toolbar = findViewById(R.id.toolbar_freq_queries);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        rvmain = findViewById(R.id.rv_main_freq_queries);
        pbMain = findViewById(R.id.pb_main__freq_queries);
        llNoqueryFound = findViewById(R.id.ll_freq_queries_no_query_found);

        linearLayoutManager = new LinearLayoutManager(this);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvmain.setLayoutManager(staggeredGridLayoutManager);

        //dummy data
      /*  for (int i = 0; i < 10; i++) {

            freqQueriesList.add(new FreqQueries("Ankit" + i, "Test", "113", "filename", "Like"));

        }*/

        //getting the query list
        getQuertList("getAllFreqQueryList");


    }

    void getQuertList(String action) {


        pbMain.setVisibility(View.VISIBLE);
        rvmain.setVisibility(View.GONE);
        llNoqueryFound.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.FREQ_QUERY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //String msg = jsonObject.getString("msg");

                    String error = jsonObject.getString("error");
                    if (error.equalsIgnoreCase("false")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("querylist");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            String queryname = jsonArray.getJSONObject(i).getString("query_name");
                            querynameList.add(queryname);
                        }


                        freqQueryAdapter = new FreqQueryAdapter(FrequentlyQueriesActivity.this, querynameList);
                        rvmain.setAdapter(freqQueryAdapter);


                        pbMain.setVisibility(View.GONE);
                        llNoqueryFound.setVisibility(View.GONE);
                        rvmain.setVisibility(View.VISIBLE);


                    } else if (error.equalsIgnoreCase("true")) {


                        pbMain.setVisibility(View.GONE);
                        rvmain.setVisibility(View.GONE);
                        llNoqueryFound.setVisibility(View.VISIBLE);


                    } else {

                        pbMain.setVisibility(View.GONE);
                        rvmain.setVisibility(View.GONE);
                        llNoqueryFound.setVisibility(View.VISIBLE);
                        Toast.makeText(FrequentlyQueriesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pbMain.setVisibility(View.GONE);
                rvmain.setVisibility(View.GONE);
                llNoqueryFound.setVisibility(View.VISIBLE);

                Toast.makeText(FrequentlyQueriesActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("userid", MainActivity.userid);

                return params;
            }
        };


        VolleySingelton.getInstance(FrequentlyQueriesActivity.this).addToRequestQueue(stringRequest);

    }
}
