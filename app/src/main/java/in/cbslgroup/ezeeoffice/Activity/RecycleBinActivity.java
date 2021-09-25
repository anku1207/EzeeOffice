package in.cbslgroup.ezeeoffice.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
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

import in.cbslgroup.ezeeoffice.Adapters.RecycleBinListAdapter;
import in.cbslgroup.ezeeoffice.Interface.RecycleBinListListener;
import in.cbslgroup.ezeeoffice.Model.Recyclebin;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class RecycleBinActivity extends AppCompatActivity implements View.OnLongClickListener {

    //permissions
    public static String MULTI_DEL_PERMISSION = "0";
    public static String RESTORE_PERMISSION = "0";
    public static String IMAGE_VIEW_PERMISSION = "0";
    public static String PDF_VIEW_PERMISSION = "0";

    public Boolean in_action_mode = false;
    RecyclerView rvRecycleBin;
    RecycleBinListAdapter recycleBinListAdapter;
    List<Recyclebin> recyclebinList = new ArrayList<>();
    List<Recyclebin> filteredList = new ArrayList<>();
    Toolbar toolbar;
    int counter = 0;
    boolean onlongclick = false;
    //Checkitem list
    ArrayList<String> SelectionList = new ArrayList<>();
    SearchView searchView;
    ProgressBar progressBar, progressBarOnScroll;
    LinearLayout llNoFileFound;
    AlertDialog alertDialog;
    String session_userid;
    TextView tvResultsFound;
    RelativeLayout rlRecyleMain;
    TextView tvFilterResultsFound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_bin);

        toolbar = findViewById(R.id.toolbar_recyclebin);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        session_userid = MainActivity.userid;

        rvRecycleBin = findViewById(R.id.rv_recyclebin);
        llNoFileFound = findViewById(R.id.ll_recyclebin_nofilefound);
        progressBar = findViewById(R.id.progressBar_recyclebin);
        progressBar.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progressbar_rotate));
        //progressBarOnScroll= findViewById(R.id.progressBar_recyclebin_onscroll);
        tvResultsFound = findViewById(R.id.tv_recyclebin_resultsfound);
        tvFilterResultsFound = findViewById(R.id.tv_recyclebin_filter_no_file_found);


        rlRecyleMain = findViewById(R.id.rl_recyclebin_main);
        rvRecycleBin.setLayoutManager(new LinearLayoutManager(this));

        getPrivilages(session_userid);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_recyclebin_multirestore) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            if (counter != 0) {

                //  showMultiShareDialog();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);

                TextView textView = dialogView.findViewById(R.id.tv_recyclebin_restore_message);
                textView.setText("Are you sure you want restore these " + counter + " document?");

                Button btn_no = dialogView.findViewById(R.id.btn_no_restore_popup);

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });

                Button btn_yes = dialogView.findViewById(R.id.btn_yes_restore_popup);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        JSONArray delDocJsonArray = new JSONArray();

                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < RecycleBinListAdapter.checkedlist.size(); i++) {

                            // sb.append(RecycleBinListAdapter.checkedlist.get(i)).append(",");
                            delDocJsonArray.put(Integer.parseInt(RecycleBinListAdapter.checkedlist.get(i)));

                        }

                        String username = MainActivity.username;
                        String userid = MainActivity.userid;
                        String ip = MainActivity.ip;
                        String docidListArray = delDocJsonArray.toString();

                        Log.e("jsonArray", docidListArray);

                        multiRestoreFiles(docidListArray, username, userid, ip);

                        normalMode();


                        alertDialog.dismiss();

                        RecycleBinListAdapter.checkedlist.clear();
                        SelectionList.clear();

                    }
                });


                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                // Toast.makeText(RecycleBinActivity.this, delDocJsonArray.toString(), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(RecycleBinActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }


            return true;
        }

        if (id == R.id.action_recyclebin_multidel) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();
            if (counter != 0) {

                //  showMultiShareDialog();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);


                TextView textView = dialogView.findViewById(R.id.tv_recyclebin_perm);
                textView.setText("Are you sure you want permanently delete these " + counter + " document?");

                Button btn_no = dialogView.findViewById(R.id.btn_no_pDel_popup);

                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();

                    }
                });


                Button btn_yes = dialogView.findViewById(R.id.btn_yes_pDel_popup);


                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        JSONArray delDocJsonArray = new JSONArray();

                        StringBuilder sb = new StringBuilder();

                        for (int i = 0; i < RecycleBinListAdapter.checkedlist.size(); i++) {

                            // sb.append(RecycleBinListAdapter.checkedlist.get(i)).append(",");
                            delDocJsonArray.put(Integer.parseInt(RecycleBinListAdapter.checkedlist.get(i)));

                        }

                        String username = MainActivity.username;
                        String userid = MainActivity.userid;
                        String ip = MainActivity.ip;
                        String docidListArray = delDocJsonArray.toString();

                        Log.e("jsonArraymuldel", docidListArray);

                        // multiRestoreFiles(docidListArray,username,userid,ip);

                        multiDelete(delDocJsonArray.toString(), userid, ip, username);

                        normalMode();


                        alertDialog.dismiss();

                        RecycleBinListAdapter.checkedlist.clear();
                        SelectionList.clear();

                    }
                });


                dialogBuilder.setView(dialogView);

                alertDialog = dialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();

                // Toast.makeText(RecycleBinActivity.this, delDocJsonArray.toString(), Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(RecycleBinActivity.this, "Please select atleast one file", Toast.LENGTH_LONG).show();

            }


            return true;
        }

        if (id == R.id.action_recyclebin_selectall) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            toolbar.getMenu().findItem(R.id.action_recyclebin_selectall).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_recyclebin_deselectall).setVisible(true);

            recycleBinListAdapter.selectAll();

            counter = recycleBinListAdapter.getItemCount();
            updateCounter(counter);

            JSONArray SelectAlljsonArray = new JSONArray();

            for (int i = 0; i < RecycleBinListAdapter.checkedlist.size(); i++) {

                // sb.append(RecycleBinListAdapter.checkedlist.get(i)).append(",");
                SelectAlljsonArray.put(Integer.parseInt(RecycleBinListAdapter.checkedlist.get(i)));

            }

            Log.e("jsonArray", SelectAlljsonArray.toString());


            return true;
        }

        if (id == R.id.action_recyclebin_deselectall) {
            //Toast.makeText(this,"editprofile",Toast.LENGTH_LONG).show();

            toolbar.getMenu().findItem(R.id.action_recyclebin_selectall).setVisible(true);
            toolbar.getMenu().findItem(R.id.action_recyclebin_deselectall).setVisible(false);

            recycleBinListAdapter.deselectAll();

            normalMode();

            JSONArray deSelectAlljsonArray = new JSONArray();

            for (int i = 0; i < RecycleBinListAdapter.checkedlist.size(); i++) {

                // sb.append(RecycleBinListAdapter.checkedlist.get(i)).append(",");
                deSelectAlljsonArray.put(Integer.parseInt(RecycleBinListAdapter.checkedlist.get(i)));

            }

            if (recyclebinList.size() == 0) {

                Log.e("jsonArray", "list is empty");
            } else {

                Log.e("jsonArray", deSelectAlljsonArray.toString());
            }

            Log.e("jsonArray", deSelectAlljsonArray.toString());


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    // Updating the counter number of the selected item
    // How many items are selected in the multislect mode
    public void updateCounter(int counter) {

        if (counter == 0) {

            toolbar.setTitle("0 item selected");

        } else {

            toolbar.setTitle(counter + " item selected");
        }

    }

    public void prepareSelection(View v, int position, ArrayList<String> doclist) {

        //docidList = docidlist;

        if (((CheckBox) v).isChecked()) {

            SelectionList.add("" + position);

            counter++;
            updateCounter(counter);

        } else {

            SelectionList.remove("" + position);
            counter--;
            updateCounter(counter);
        }


    }

    void normalMode() {

        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Recyle Management");

            in_action_mode = false;
            //onlongclick = false;
            recycleBinListAdapter.notifyDataSetChanged();

            //code here for close dialog button
            // alertDialog.dismiss();

        }

    }

    public void getRecycleList(final String userid) {

        recyclebinList.clear();
        progressBar.setVisibility(View.VISIBLE);
        llNoFileFound.setVisibility(View.GONE);
        tvResultsFound.setVisibility(View.GONE);
        rlRecyleMain.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response recycle", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {

                        llNoFileFound.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        tvResultsFound.setText("0 results found");
                        tvResultsFound.setVisibility(View.GONE);
                        rlRecyleMain.setVisibility(View.GONE);

                        if (toolbar.getMenu().findItem(R.id.action_recyclebin_search) != null) {

                            toolbar.getMenu().findItem(R.id.action_recyclebin_search).setVisible(false);

                        }

                    } else {


                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String docname = jsonObject.getString("old_doc_name");
                            String filesize = jsonObject.getString("doc_size");
                            String fileextension = jsonObject.getString("doc_extn");
                            String filepath = jsonObject.getString("doc_path");
                            String docid = jsonObject.getString("doc_id");
                            String date = jsonObject.getString("dateposted");

                            String f = filepath.substring(0, filepath.lastIndexOf("/"));// temp/8/test
                            String storagename = f.substring(f.lastIndexOf("/") + 1);
                            //filepath.substring(filepath.substring(0, filepath.LastIndexOf("/")).LastIndexOf("/") + 1);
                            // String storagename = filepath.substring(0, filepath.indexOf("/"));
                            // String storagename = filepath.substring(filepath.substring(0, filepath.lastIndexOf("/")).lastIndexOf("/") + 1);

                            Double fileSizeDouble = Double.parseDouble(filesize) / 1048576;
                            Log.e("File size ", String.valueOf(fileSizeDouble));

                            String fileSizeInMb = String.valueOf(Math.round(fileSizeDouble * 100D) / 100D);
                            Log.e("File size in mb ", fileSizeInMb);


                            recyclebinList.add(new Recyclebin(docname, fileextension, fileSizeInMb + " " + "mb", storagename, docid, filepath, date));

                        }


                        recycleBinListAdapter = new RecycleBinListAdapter(RecycleBinActivity.this, recyclebinList, new RecycleBinListListener() {
                            @Override
                            public void onRestoreButtonClick(View v, final String docid, final int position, Recyclebin rvObj) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);

                                Button btn_no = dialogView.findViewById(R.id.btn_no_restore_popup);

                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();

                                    }
                                });

                                Button btn_yes = dialogView.findViewById(R.id.btn_yes_restore_popup);

                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                      /*  String docid = holder.tvDocid.getText().toString();
                        String ip = MainActivity.ip;
                        String userid = MainActivity.userid;
                        String username = MainActivity.username;*/


                                        //Toast.makeText(RecycleBinActivity.this, rvObj.getFilename(), Toast.LENGTH_SHORT).show();


                                        restoreFile(docid, MainActivity.userid, MainActivity.ip, MainActivity.username, position, rvObj);

                                        alertDialog.dismiss();

                                    }
                                });


                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }

                            @Override
                            public void onPerDeleteButtonClick(View v, final String docid, final int position, Recyclebin rvObj) {

                                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                                Button btn_yes = dialogView.findViewById(R.id.btn_yes_pDel_popup);

                                btn_yes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                       /* String docid = holder.tvDocid.getText().toString();
                        String ip = MainActivity.ip;
                        String userid = MainActivity.userid;
                        String username = MainActivity.username;*/

                                        delPermanentFile(docid, MainActivity.userid, MainActivity.ip, MainActivity.username, position, rvObj);

//                                        Toast.makeText(RecycleBinActivity.this, rvObj.getFilename(), Toast.LENGTH_SHORT).show();
//
//                                        recyclebinList.remove(recyclebinList.indexOf(rvObj));
//                                        recycleBinListAdapter.notifyDataSetChanged();
//
//                                        for (Recyclebin r:
//                                             recyclebinList) {
//
//                                            Log.e("File name",r.getFilename());
//
//                                        }

                                        alertDialog.dismiss();

                                    }
                                });

                                Button btn_no = dialogView.findViewById(R.id.btn_no_pDel_popup);

                                btn_no.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        alertDialog.dismiss();

                                    }
                                });


                                dialogBuilder.setView(dialogView);

                                alertDialog = dialogBuilder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                alertDialog.show();


                            }
                        });


                        rvRecycleBin.setAdapter(recycleBinListAdapter);
                        rlRecyleMain.setVisibility(View.VISIBLE);
                        llNoFileFound.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found");
                        tvResultsFound.setVisibility(View.VISIBLE);

                        if (toolbar.getMenu().findItem(R.id.action_recyclebin_search) != null) {
                            toolbar.getMenu().findItem(R.id.action_recyclebin_search).setVisible(true);

                        }


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

                Map<String, String> params = new HashMap<String, String>();

                params.put("id", userid);


                return params;
            }
        };

        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recyclebin_menu, menu);


        final MenuItem myActionMenuItem = menu.findItem(R.id.action_recyclebin_search);
        searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {


                s = s.toLowerCase();

                filteredList.clear();

                for (int i = 0; i < recyclebinList.size(); i++) {

                    final String filename = recyclebinList.get(i).getFilename().toLowerCase();
                    final String date = recyclebinList.get(i).getDate().toLowerCase();
                    final String extension = recyclebinList.get(i).getFileExtension().toLowerCase();
                    final String storagename = recyclebinList.get(i).getStorageName().toLowerCase();
                    if (filename.contains(s) || date.contains(s) || extension.contains(s) || storagename.contains(s)) {

                        filteredList.add(recyclebinList.get(i));
                    }

                    rvRecycleBin.setLayoutManager(new LinearLayoutManager(RecycleBinActivity.this));
                    recycleBinListAdapter = new RecycleBinListAdapter(RecycleBinActivity.this, filteredList, new RecycleBinListListener() {
                        @Override
                        public void onRestoreButtonClick(View v, String docid, int position, Recyclebin rvObj) {


                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.alertdialog_restore, null);

                            Button btn_no = dialogView.findViewById(R.id.btn_no_restore_popup);

                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();

                                }
                            });

                            Button btn_yes = dialogView.findViewById(R.id.btn_yes_restore_popup);

                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                      /*  String docid = holder.tvDocid.getText().toString();
                        String ip = MainActivity.ip;
                        String userid = MainActivity.userid;
                        String username = MainActivity.username;*/


                                    Log.e("filtered item", String.valueOf(position));
                                    Log.e("recyclelist item", getMainListItemPosition(position));
                                    restoreFile(docid, MainActivity.userid, MainActivity.ip, MainActivity.username, Integer.parseInt(getMainListItemPosition(position)), rvObj);


                                    filteredList.remove(position);
                                    recycleBinListAdapter.notifyItemRemoved(position);
                                    recycleBinListAdapter.notifyItemRangeChanged(position, filteredList.size());


                                    if (recycleBinListAdapter.getItemCount() == 0) {


                                        rvRecycleBin.setVisibility(View.GONE);
                                        llNoFileFound.setVisibility(View.VISIBLE);
                                        //llNoFileFound.setVisibility(View.VISIBLE);


                                    } else {

                                        rvRecycleBin.setVisibility(View.VISIBLE);
                                        llNoFileFound.setVisibility(View.GONE);

                                    }

                                    alertDialog.dismiss();

                                }
                            });


                            tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found ");


                            dialogBuilder.setView(dialogView);

                            alertDialog = dialogBuilder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertDialog.show();


                        }

                        @Override
                        public void onPerDeleteButtonClick(View v, String docid, int position, Recyclebin rvObj) {


                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RecycleBinActivity.this);

                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View dialogView = inflater.inflate(R.layout.alertdialog_permanent_del, null);

                            Button btn_yes = dialogView.findViewById(R.id.btn_yes_pDel_popup);

                            btn_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                       /* String docid = holder.tvDocid.getText().toString();
                        String ip = MainActivity.ip;
                        String userid = MainActivity.userid;
                        String username = MainActivity.username;*/


                                    delPermanentFile(docid, MainActivity.userid, MainActivity.ip, MainActivity.username, Integer.parseInt(getMainListItemPosition(position)), rvObj);


                                    filteredList.remove(position);
                                    recycleBinListAdapter.notifyItemRemoved(position);
                                    recycleBinListAdapter.notifyItemRangeChanged(position, filteredList.size());


                                    if (recycleBinListAdapter.getItemCount() == 0) {


                                        rvRecycleBin.setVisibility(View.GONE);
                                        tvFilterResultsFound.setVisibility(View.VISIBLE);
                                        //llNoFileFound.setVisibility(View.VISIBLE);


                                    } else {

                                        rvRecycleBin.setVisibility(View.VISIBLE);
                                        tvFilterResultsFound.setVisibility(View.GONE);

                                    }

                                    tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found ");

                                    alertDialog.dismiss();

                                }
                            });

                            Button btn_no = dialogView.findViewById(R.id.btn_no_pDel_popup);

                            btn_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();

                                }
                            });


                            dialogBuilder.setView(dialogView);

                            alertDialog = dialogBuilder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertDialog.show();


                        }
                    });


                    rvRecycleBin.setAdapter(recycleBinListAdapter);
                    recycleBinListAdapter.notifyDataSetChanged();

                    if (recycleBinListAdapter.getItemCount() == 0) {


                        rvRecycleBin.setVisibility(View.GONE);
                        tvFilterResultsFound.setVisibility(View.VISIBLE);
                        //llNoFileFound.setVisibility(View.VISIBLE);


                    } else {

                        rvRecycleBin.setVisibility(View.VISIBLE);
                        tvFilterResultsFound.setVisibility(View.GONE);
                        // llNoFileFound.setVisibility(View.GONE);

                    }

                    if (recycleBinListAdapter.getItemCount() == 0) {

                        //tvCountPanel.setText("Showing 0 to 0 of 0 entries");
                        tvResultsFound.setText("0 results found");

                    } else {


                        // tvCountPanel.setText("Showing 1 to "+auditListAdapter.getItemCount()+" of " +totalLogs+" entries");
                        tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found ");
                    }


                }
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (in_action_mode) {

            counter = 0;
            toolbar.getMenu().clear();
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            toolbar.setTitle("Recycle Management");
            //toolbar.setSubtitle(foldername);
            in_action_mode = false;
            onlongclick = false;
            recycleBinListAdapter.deselectAll();
            recycleBinListAdapter.notifyDataSetChanged();


        } else {

            super.onBackPressed();

        }

    }

    @Override
    public boolean onLongClick(View v) {


        if (!onlongclick) {


            toolbar.setBackgroundColor(getResources().getColor(R.color.black_blur));
            toolbar.getMenu().clear();
            toolbar.setTitle("0 item selected");
            toolbar.inflateMenu(R.menu.recyclebin_multiselect_menu);

            if (MULTI_DEL_PERMISSION.equals("1")) {

                toolbar.getMenu().findItem(R.id.action_recyclebin_multidel).setVisible(true);

            } else {

                toolbar.getMenu().findItem(R.id.action_recyclebin_multidel).setVisible(false);

            }

            if (RESTORE_PERMISSION.equals("1")) {

                toolbar.getMenu().findItem(R.id.action_recyclebin_multirestore).setVisible(true);

            } else {

                toolbar.getMenu().findItem(R.id.action_recyclebin_multirestore).setVisible(false);

            }

            // toolbar.setSubtitle("");
            in_action_mode = true;
            recycleBinListAdapter.notifyDataSetChanged();
            onlongclick = false;

        } else {

            Log.e("onlongclick", "false");

        }


        return true;
    }

    void multiRestoreFiles(final String docidListArray, final String username, final String userid, final String ip) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("multi rest recy respse", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String error = jsonArray.getJSONObject(0).getString("error");
                    String message = jsonArray.getJSONObject(0).getString("message");

                    if (error.equals("true")) {

                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (error.equals("false")) {


                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();

                        getRecycleList(userid);


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
                parameters.put("mulResdocid", docidListArray);
                parameters.put("mulResuserid", userid);
                parameters.put("mulResusername", username);
                parameters.put("mulResip", ip);

                return parameters;
            }


        };

        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);
    }

    void getPrivilages(final String userid) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GET_USER_ROLE_PRIVLAGES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("privilages", response);

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String permanentdel = jsonArray.getJSONObject(0).getString("permanent_del");
                    String restorefile = jsonArray.getJSONObject(0).getString("restore_file");
                    String pdfFile = jsonArray.getJSONObject(0).getString("pdf_file");
                    String imageFile = jsonArray.getJSONObject(0).getString("image_file");

                    if (permanentdel.equals("1")) {

                        MULTI_DEL_PERMISSION = "1";


                    } else {

                        MULTI_DEL_PERMISSION = "0";

                    }

                    if (restorefile.equals("1")) {

                        RESTORE_PERMISSION = "1";


                    } else {

                        RESTORE_PERMISSION = "0";

                    }

                    if (imageFile.equals("1")) {

                        IMAGE_VIEW_PERMISSION = "1";


                    } else {

                        IMAGE_VIEW_PERMISSION = "0";

                    }

                    if (pdfFile.equals("1")) {

                        PDF_VIEW_PERMISSION = "1";


                    } else {

                        PDF_VIEW_PERMISSION = "0";

                    }


                    getRecycleList(session_userid);


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
                params.put("userid", userid);
                params.put("roles", setRoles());
                params.put("action", "getSpecificRoles");
                return params;
            }
        };

        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);


    }

    private String setRoles() {


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("permanent_del").append(",")
                .append("restore_file").append(",")
                .append("pdf_file").append(",")
                .append("image_file");

        return stringBuilder.toString();

    }

    void multiDelete(final String docidsarray, final String userid, final String ip, final String username) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //[{"message":"Document Deleted Successfully !","error":"false"}]
                Log.e("muldel", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    String message = jsonArray.getJSONObject(0).getString("message");
                    String error = jsonArray.getJSONObject(0).getString("error");

                    if (error.equals("true")) {

                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equals("false")) {


                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();
                        getRecycleList(userid);


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
                params.put("mDocid", docidsarray);
                params.put("mUserid", userid);
                params.put("mUsername", username);
                params.put("mIp", ip);

                return params;
            }
        };


        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);


    }

    void restoreFile(final String docid, final String userid, final String ip, final String username, final int position, Recyclebin rvobj) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("restore", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {


                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equals("false")) {

                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();


                        recyclebinList.remove(position);
                        recycleBinListAdapter.notifyItemRemoved(position);
                        recycleBinListAdapter.notifyItemRangeChanged(position, recyclebinList.size());

                        if (recycleBinListAdapter.getItemCount() == 0) {

                            getRecycleList(userid);


                        }


                        tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found");
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
                parameters.put("resdocid", docid);
                parameters.put("resuserid", userid);
                parameters.put("resusername", username);
                parameters.put("resip", ip);


                return parameters;

            }


        };

        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);


    }

    void delPermanentFile(final String docid, final String userid, final String ip, final String username, final int position, Recyclebin rvobj) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.RECYCLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("delete permanent", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("true")) {


                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();


                    } else if (error.equals("false")) {


                        Toast.makeText(RecycleBinActivity.this, message, Toast.LENGTH_SHORT).show();
                        recyclebinList.remove(position);
                        recycleBinListAdapter.notifyItemRemoved(position);
                        recycleBinListAdapter.notifyItemRangeChanged(position, recyclebinList.size());

                        if (recycleBinListAdapter.getItemCount() == 0) {

                            getRecycleList(userid);

                        }

                        tvResultsFound.setText(recycleBinListAdapter.getItemCount() + " results found");

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
                parameters.put("deldocid", docid);
                parameters.put("deluserid", userid);
                parameters.put("delusername", username);
                parameters.put("delip", ip);


                return parameters;

            }

        };

        VolleySingelton.getInstance(RecycleBinActivity.this).addToRequestQueue(stringRequest);


    }

    String getMainListItemPosition(int pos) {

        String filDocid = filteredList.get(pos).getDocid();


        for (int i = 0; i < recyclebinList.size(); i++) {

            if (filDocid.equalsIgnoreCase(recyclebinList.get(i).getDocid())) {


                return String.valueOf(i);

            }

        }

        return null;

    }
}

/*    public void selectAll() {

        recycleBinListAdapter.selectAll();
        if (actionMode == null) {
            actionMode = startActionMode(actionModeCallback);
            actionMode.setTitle("Selected: " + recycleBinListAdapter.getSelected().size());}
    }

    public void deselectAll() {

        recycleBinListAdapter.clearSelected();
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    public void doAction() {

        Toast.makeText(this, String.format("Selected %d items", recycleBinListAdapter.getSelected().size()), Toast.LENGTH_SHORT).show();
    }*/

/*    @Override
    public void onClickAction() {

        int selected = recycleBinListAdapter.getSelected().size();
        if (actionMode == null) {
            actionMode = startActionMode(actionModeCallback);
            actionMode.setTitle("Selected: " + selected);
        } else {
            if (selected == 0) {
                actionMode.finish();
            } else {
                actionMode.setTitle("Selected: " + selected);
            }
        }
    }

    }*/

