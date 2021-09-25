package in.cbslgroup.ezeeoffice.Activity.SharedFiles;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import in.cbslgroup.ezeeoffice.Adapters.SharedFilesAdapter;
import in.cbslgroup.ezeeoffice.Interface.SharedFilesListListener;
import in.cbslgroup.ezeeoffice.Model.SharedFiles;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class SharedFilesActivity extends AppCompatActivity {


    RecyclerView rvSharedFiles;
    ProgressBar progressBar, progressBarOnscroll;
    LinearLayout llNofilefound;
    SharedFilesAdapter sharedFilesAdapter;
    SearchView searchView;
    List<SharedFiles> sharedFilesList = new ArrayList<>();
    AlertDialog alertDialog;
    String userid;
    TextView tvResultsFound;

    String totalFilesinList;

    // pagination
    String startIndex = "0", lastIndex = "20";
    private boolean isLoading = false;
    private boolean isLastPage = false;

    LinearLayoutManager linearLayoutManager;

    RelativeLayout rlRVMain;
    List<SharedFiles> filteredList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_files);

        userid = MainActivity.userid;

        Toolbar toolbar = findViewById(R.id.toolbar_Shared_Files_activity);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        progressBar = findViewById(R.id.progressBarSharedFiles);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));

        progressBarOnscroll = findViewById(R.id.progressBar_sharedfile_onsroll);
        progressBarOnscroll.setBackgroundColor(getResources().getColor(R.color.transparent));
        progressBarOnscroll.setAlpha(0.2f);


        linearLayoutManager = new LinearLayoutManager(this);

        llNofilefound = findViewById(R.id.ll_sharedFilesactvity_nofilefound);
        tvResultsFound = findViewById(R.id.tv_shared_files_resultsfound);
        rlRVMain = findViewById(R.id.rl_rv_shared_files_main);

        rvSharedFiles = findViewById(R.id.rv_shared_files_list);
        rvSharedFiles.setLayoutManager(linearLayoutManager);
        rvSharedFiles.setHasFixedSize(true);
        rvSharedFiles.setItemViewCacheSize(sharedFilesList.size());
        rvSharedFiles.setItemAnimator(new DefaultItemAnimator());
       /* rvSharedFiles.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {

                       // loadMoreItems();


                   Log.e("startindex",startIndex);
                   Log.e("lastindex",lastIndex);

                  // getSharedFilesList(userid,"21","40");
                    Log.e("itemcount", String.valueOf(sharedFilesAdapter.getItemCount()));
                    Log.e("totalfileinlist",totalFilesinList);

                    if(sharedFilesAdapter.getItemCount()==Integer.parseInt(totalFilesinList)){

                       // Toast.makeText(SharedFilesActivity.this, "No more files", Toast.LENGTH_SHORT).show();
                        progressBarOnscroll.setVisibility(View.GONE);



                    }

                   else{

                        startIndex = String.valueOf(Integer.parseInt(lastIndex)+1);
                        lastIndex = String.valueOf(Integer.parseInt(lastIndex)+20);

                        getMoreFiles(userid,startIndex,lastIndex);


                    }


}

            }
        });*/

        getSharedFilesList(userid);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shared_files_menu, menu);


        final MenuItem myActionMenuItem = menu.findItem(R.id.action_shared_file_search);
        searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                //UserFeedback.show( "SearchOnQueryTextSubmit: " + query);


            /*    if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }*/
                //myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);

                s = s.toLowerCase();
                filteredList = new ArrayList<>();

                for (int i = 0; i < sharedFilesList.size(); i++) {

                    final String text = sharedFilesList.get(i).getFilename().toLowerCase();
                    final String sharedTo = sharedFilesList.get(i).getSharedTo().toLowerCase();
                    final String storageName = sharedFilesList.get(i).getStorageName().toLowerCase();
                    final String sharedDate = sharedFilesList.get(i).getSharedDate().toLowerCase();
                    final String noofpages = sharedFilesList.get(i).getNoOfPages().toLowerCase();

                    if (text.contains(s) || sharedDate.contains(s) || sharedTo.contains(s) || storageName.contains(s) || noofpages.contains(s)) {

                        //  tvResultsFound.setText(sharedFilesList.size() + " results found");
                        filteredList.add(sharedFilesList.get(i));
                    }

                    rvSharedFiles.setLayoutManager(new LinearLayoutManager(SharedFilesActivity.this));
                    sharedFilesAdapter = new SharedFilesAdapter(filteredList, SharedFilesActivity.this, new SharedFilesListListener() {
                        @Override
                        public void onUndoButtonClick(View v, String docid, int position, String toId) {

                            //searchView.setIconified(false);

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SharedFilesActivity.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.alertdialog_error_2, null);
                            TextView tvHeading = dialogView.findViewById(R.id.tv_error_heading_popup);
                            TextView tvMessage = dialogView.findViewById(R.id.tv_error_message_popup);

                            tvHeading.setText("Undo Shared File");
                            tvMessage.setText("Are you want to undo the shared file ?");
                            Button btn_yes = dialogView.findViewById(R.id.btn_yes_error_popup);
                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    String undousername = MainActivity.username;
                                    String undoIp = MainActivity.ip;

                                    if (!searchView.isIconified()) {
                                        searchView.setIconified(true);
                                        searchView.onActionViewCollapsed();
                                    }


                                    UndoSharedFiles(MainActivity.userid, undousername, undoIp, docid, toId, Integer.parseInt(getMainListItemPosition(position)));

                                    filteredList.remove(position);
                                    sharedFilesAdapter.notifyItemRemoved(position);
                                    sharedFilesAdapter.notifyItemRangeChanged(position, filteredList.size());


                                    alertDialog.dismiss();


                                }
                            });

                            Button btn_no = dialogView.findViewById(R.id.btn_no_error_popup);
                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();
                                }
                            });

                            dialogBuilder.setView(dialogView);

                            alertDialog = dialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertDialog.show();

                            //UndoSharedFiles(MainActivity.userid, MainActivity.username, MainActivity.ip, docid, toId, position);

                        }
                    });
                    rvSharedFiles.setAdapter(sharedFilesAdapter);
                    sharedFilesAdapter.notifyDataSetChanged();

                    if (filteredList.size() == 0) {


                        rvSharedFiles.setVisibility(View.GONE);
                        llNofilefound.setVisibility(View.VISIBLE);


                    } else {

                        rvSharedFiles.setVisibility(View.VISIBLE);
                        llNofilefound.setVisibility(View.GONE);

                    }

                    if (sharedFilesAdapter.getItemCount() == 0) {

                        //tvCountPanel.setText("Showing 0 to 0 of 0 entries");
                        tvResultsFound.setText("0 results found");

                    } else {


                        // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries");
                        tvResultsFound.setText(sharedFilesAdapter.getItemCount() + " results found ");
                    }

                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    void getSharedFilesList(final String userid) {

        sharedFilesList.clear();
        llNofilefound.setVisibility(View.GONE);
        rlRVMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tvResultsFound.setVisibility(View.GONE);

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("shared list", response);


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.e("jsonObject length", String.valueOf(jsonObject.length()));


                    if (jsonObject.getString("error").equalsIgnoreCase("true")) {

                        // Toast.makeText(SharedFilesActivity.this, "No file found", Toast.LENGTH_LONG).show();
                        rvSharedFiles.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        llNofilefound.setVisibility(View.VISIBLE);
                        tvResultsFound.setText("0 results found");
                        tvResultsFound.setVisibility(View.GONE);


                    } else if (jsonObject.getString("error").equalsIgnoreCase("false")) {


                        totalFilesinList = jsonObject.getString("totalfiles");
                        Log.e("totalfilesInlIst", String.valueOf(totalFilesinList));

                        JSONArray jsonArray = jsonObject.getJSONArray("files");

                        Log.e("jsonArraysharedfiles", String.valueOf(jsonArray.length()));


                        for (int i = 0; i < jsonArray.length(); i++) {


                            String storagename = jsonArray.getJSONObject(i).getString("storagename");
                            //Log.e("storagename sharedlist",storagename);
                            // String FromUsername = jsonObject.getString("FromUsername");
                            String ToUsername = jsonArray.getJSONObject(i).getString("Tousername");
                            String docname = jsonArray.getJSONObject(i).getString("docname");
                            String filetype = jsonArray.getJSONObject(i).getString("filetype");
                            String noofpages = jsonArray.getJSONObject(i).getString("noofpages");
                            String filepath = jsonArray.getJSONObject(i).getString("filepath");
                            String dateShare = jsonArray.getJSONObject(i).getString("dateshared");
                            String docid = jsonArray.getJSONObject(i).getString("docid");
                            String toUserid = jsonArray.getJSONObject(i).getString("Touserid");

                            Log.e("sharedlist", storagename + " " + ToUsername + docname + filetype + noofpages + filepath + dateShare + toUserid);

                            sharedFilesList.add(new SharedFiles(docname, ToUsername, dateShare, noofpages, storagename, filepath, filetype, docid, toUserid));

                        }

                        sharedFilesAdapter = new SharedFilesAdapter(sharedFilesList, SharedFilesActivity.this, new SharedFilesListListener() {
                            @Override
                            public void onUndoButtonClick(View v, final String docid, final int position, final String toids) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SharedFilesActivity.this);
                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_error_2, null);
                                TextView tvHeading = dialogView.findViewById(R.id.tv_error_heading_popup);
                                TextView tvMessage = dialogView.findViewById(R.id.tv_error_message_popup);

                                tvHeading.setText("Undo Shared File");
                                tvMessage.setText("Are you want to undo the shared file ?");
                                Button btn_yes = dialogView.findViewById(R.id.btn_yes_error_popup);
                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        String undousername = MainActivity.username;
                                        String undoIp = MainActivity.ip;


                                        UndoSharedFiles(MainActivity.userid, undousername, undoIp, docid, toids, position);

                                        alertDialog.dismiss();


                                    }
                                });

                                Button btn_no = dialogView.findViewById(R.id.btn_no_error_popup);
                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();
                                    }
                                });

                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }
                        });
                        rvSharedFiles.setAdapter(sharedFilesAdapter);

                        tvResultsFound.setVisibility(View.VISIBLE);
                        tvResultsFound.setText(jsonArray.length() + " results found");
                        rlRVMain.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        llNofilefound.setVisibility(View.GONE);

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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userID", userid);
                return parameters;

            }

        };

        VolleySingelton.getInstance(SharedFilesActivity.this).addToRequestQueue(stringRequest);
    }

  /*  void getMoreFiles(final String userid, final String startIndex , final String finalIndex){

        progressBarOnscroll.setVisibility(View.GONE);
        rlRVMain.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("files");

                    Log.e("jsonArraysharedfiles", String.valueOf(jsonArray.length()));


                    for (int i = 0; i < jsonArray.length(); i++) {


                        String storagename = jsonArray.getJSONObject(i).getString("storagename");
                        //Log.e("storagename sharedlist",storagename);
                        // String FromUsername = jsonObject.getString("FromUsername");
                        String ToUsername = jsonArray.getJSONObject(i).getString("Tousername");
                        String docname = jsonArray.getJSONObject(i).getString("docname");
                        String filetype = jsonArray.getJSONObject(i).getString("filetype");
                        String noofpages = jsonArray.getJSONObject(i).getString("noofpages");
                        String filepath = jsonArray.getJSONObject(i).getString("filepath");
                        String dateShare = jsonArray.getJSONObject(i).getString("dateshared");
                        String docid = jsonArray.getJSONObject(i).getString("docid");
                        String toUserid = jsonArray.getJSONObject(i).getString("Touserid");

                        Log.e("sharedlist", storagename + " " + ToUsername + docname + filetype + noofpages + filepath + dateShare + toUserid);

                        sharedFilesList.add(new SharedFiles(docname, ToUsername, dateShare, noofpages, storagename, filepath, filetype, docid, toUserid));

                    }

                    sharedFilesAdapter = new SharedFilesAdapter(sharedFilesList, SharedFilesActivity.this, new SharedFilesListListener() {
                        @Override
                        public void onUndoButtonClick(View v, final String docid, final int position) {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SharedFilesActivity.this);
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.alertdialog_error_2, null);
                            TextView tvHeading = dialogView.findViewById(R.id.tv_error_heading_popup);
                            TextView tvMessage = dialogView.findViewById(R.id.tv_error_message_popup);

                            tvHeading.setText("Undo Shared File");
                            tvMessage.setText("Are you want to undo the shared file ?");
                            Button btn_yes = dialogView.findViewById(R.id.btn_yes_error_popup);
                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    String undouserid = MainActivity.userid;
                                    String undousername = MainActivity.username;
                                    String undoIp = MainActivity.ip;
                                    //String undodocid = holder.tvDocid.getText().toString();
                                    //String undoTouserid = MainActivity.userid;


                                     *//*   Log.e("Undoshare log", "docid" + " " + undodocid + "\n"
                                                + "username" + " " + undousername + "\n" +
                                                "ip" + " " + undoIp + "\n" + "userid" + " " + undouserid + "\n");
*//*
                                    UndoSharedFiles(MainActivity.userid, undousername, undoIp, docid, position);

                                    alertDialog.dismiss();


                                }
                            });

                            Button btn_no = dialogView.findViewById(R.id.btn_no_error_popup);
                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();
                                }
                            });

                            dialogBuilder.setView(dialogView);

                            alertDialog = dialogBuilder.create();
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertDialog.show();


                        }
                    });
                    rvSharedFiles.setAdapter(sharedFilesAdapter);
                    sharedFilesAdapter.notifyDataSetChanged();
                    rlRVMain.setVisibility(View.VISIBLE);
                    progressBarOnscroll.setVisibility(View.GONE);





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userID", userid);
                parameters.put("startIndex", startIndex);
                parameters.put("lastIndex",finalIndex);


                return parameters;

            }




        };

        VolleySingelton.getInstance(SharedFilesActivity.this).addToRequestQueue(stringRequest);

    }*/

    void UndoSharedFiles(final String userid, final String username, final String ip, final String docid, final String todocid, int position) {

//        llNofilefound.setVisibility(View.GONE);
//        rvSharedFiles.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.SHARE_FILES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("undo", response);


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0 || response.equals("") || response.isEmpty()) {

                        llNofilefound.setVisibility(View.VISIBLE);


                    } else {

                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String error = jsonObject.getString("error");
                        String message = jsonObject.getString("message");

                        Log.e("error and message", error + " " + message);

                        switch (error) {
                            case "null":

                                Toast.makeText(SharedFilesActivity.this, message, Toast.LENGTH_LONG).show();

                                break;
                            case "true":

                                Toast.makeText(SharedFilesActivity.this, message, Toast.LENGTH_LONG).show();

                                break;

                            case "false":

                                Toast.makeText(SharedFilesActivity.this, message, Toast.LENGTH_LONG).show();

                                sharedFilesList.remove(position);
                                sharedFilesAdapter.notifyItemRemoved(position);
                                sharedFilesAdapter.notifyItemRangeChanged(position, sharedFilesList.size());

                                if (sharedFilesAdapter.getItemCount() == 0) {

                                   getSharedFilesList(userid);
                                }

                                tvResultsFound.setText(sharedFilesAdapter.getItemCount() + " results found");



                             /*  // sharedFilesList.remove(position);
                                notifyDataSetChanged();*/

                                //getSharedFilesList(userid);


                                break;
                        }

                    }


//                    llNofilefound.setVisibility(View.GONE);
//                    rvSharedFiles.setVisibility(View.VISIBLE);
//                    progressBar.setVisibility(View.GONE);


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

                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("UndoUserid", userid);
                parameters.put("UndoIp", ip);
                parameters.put("UndoUsername", username);
                parameters.put("UndoDocid", docid);
                parameters.put("UndoTodocid", todocid);


                return parameters;

            }


        };

        VolleySingelton.getInstance(SharedFilesActivity.this).addToRequestQueue(stringRequest);

    }


    String getMainListItemPosition(int pos) {

        String filDocid = filteredList.get(pos).getDocid();


        for (int i = 0; i < sharedFilesList.size(); i++) {

            if (filDocid.equalsIgnoreCase(sharedFilesList.get(i).getDocid())) {


                return String.valueOf(i);

            }

        }

        return null;

    }


}
