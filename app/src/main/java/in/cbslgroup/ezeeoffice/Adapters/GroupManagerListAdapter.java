package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.cbslgroup.ezeeoffice.Interface.GroupManagerListInterface;
import in.cbslgroup.ezeeoffice.Model.GroupManager;
import in.cbslgroup.ezeeoffice.R;
import in.cbslgroup.ezeeoffice.Utils.ApiUrl;
import in.cbslgroup.ezeeoffice.Utils.VolleySingelton;


public class GroupManagerListAdapter extends RecyclerView.Adapter<GroupManagerListAdapter.ViewHolder> {


    Context context;
    List<GroupManager> groupManagerList;
    AlertDialog alertDialog;


   GroupManagerListInterface listener;


    public GroupManagerListAdapter(Context context, List<GroupManager> groupManagerList, GroupManagerListInterface listener) {
        this.context = context;
        this.groupManagerList = groupManagerList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_manager_list_layout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvGroupid.setText(groupManagerList.get(position).getGroupId());
        holder.tvGroupname.setText(groupManagerList.get(position).getGroupName());
        holder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {







                //String sUserid = MainActivity.userid;

                //  getGroupMemberList(sUserid, String.valueOf(holder.tvGroupid.getText()));


               /* AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.alertdialog_group_manager_modify, null);

                TextView tvheading = dialogView.findViewById(R.id.tv_alert_group_manager_heading);
                tvheading.setText("Update group : " + holder.tvGroupname.getText());
                EditText etGrpName = dialogView.findViewById(R.id.et_alert_group_manager_group_name);
                etGrpName.setText(holder.tvGroupname.getText());
                TextView tvUseringrp = dialogView.findViewById(R.id.tv_alert_group_manager_users_in_grp);
                tvUseringrp.setText(" Users in group : " + holder.tvGroupname.getText());

                autoCompleteTextView = dialogView.findViewById(R.id.autocomplete_textview_group_manager);
                autoCompleteTextView.allowDuplicates(false);

                //Set the listener that will be notified of changes in the Tokenlist
                autoCompleteTextView.setTokenListener(new TokenCompleteTextView.TokenListener<User>() {
                    @Override
                    public void onTokenAdded(User token) {

                        Log.e("Chip", "Added");

                    }

                    @Override
                    public void onTokenRemoved(User token) {

                        Log.e("Chip", "Added");
                    }
                });

                //Set the action to be taken when a Token is clicked
                autoCompleteTextView.setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Select);



                Button btn_yes = dialogView.findViewById(R.id.btn_group_manager_modify_alert_save);
                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        List<User> tokens = autoCompleteTextView.getObjects();
                        StringBuilder content = new StringBuilder();
                        for (int i = 0; i < tokens.size(); i++) {
                            String slid = tokens.get(i).getSlid();
                            groupMemberSelectedUseridList.add(slid);
                        }


                    }
                });

                Button btn_no = dialogView.findViewById(R.id.btn_group_manager_modify_alert_close);
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
*/

            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                listener.onDeleteButtonClick(v, position,holder.tvGroupid.getText().toString(),holder.tvGroupname.getText().toString());

/*
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Deleting Group");
                alertDialog.setIcon(R.drawable.ic_delete_black_24dp);
                alertDialog.setMessage("Are you sure you want to delete group " + holder.tvGroupname.getText() + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {





                    }
                });
                alertDialog.show();*/


            }
        });


       /* String userid = MainActivity.userid;
        String ip = MainActivity.ip;
        String username = MainActivity.username;
        String groupid = String.valueOf(holder.tvGroupid.getText());

        delGroup(userid,groupid,ip,username);
*/


    }

    @Override
    public int getItemCount() {
        return groupManagerList.size();
    }

    void delGroup(final String userid, final String groupid, final String ip, final String username, final int position) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiUrl.GROUP_MANAGER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("delGrp", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String error = jsonObject.getString("error");
                    String message = jsonObject.getString("message");

                    if (error.equals("false")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        groupManagerList.remove(position);

                        alertDialog.dismiss();

                    } else if (error.equals("true")) {

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

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
                params.put("delUserid", userid);
                params.put("delip", ip);
                params.put("delUserName", username);
                params.put("delGid", groupid);

                return params;
            }
        };

        VolleySingelton.getInstance(context).addToRequestQueue(stringRequest);


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupname, tvGroupid;
        Button btnModify, btnDelete;


        public ViewHolder(View itemView) {
            super(itemView);

            tvGroupname = itemView.findViewById(R.id.tv_group_manager_groupname);
            tvGroupid = itemView.findViewById(R.id.tv_group_manager_groupid);
            btnModify = itemView.findViewById(R.id.btn_group_manager_modify);
            btnDelete = itemView.findViewById(R.id.btn_group_manager_delete);


        }
    }

}
