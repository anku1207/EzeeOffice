package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import in.cbslgroup.ezeeoffice.Interface.UserProfileListInterface;
import in.cbslgroup.ezeeoffice.Model.UserProfile;
import in.cbslgroup.ezeeoffice.R;

public class UserProfileListAdapter extends RecyclerView.Adapter<UserProfileListAdapter.ViewHolder> {
    List<UserProfile> userProfileList = new ArrayList<>();
    Context context;
    UserProfileListInterface listener;


    public UserProfileListAdapter(List<UserProfile> userProfileList, Context context, UserProfileListInterface listener) {
        this.userProfileList = userProfileList;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_list_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {

        return userProfileList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvFeedbackMsg, tvSharedFileWithMe, tvSharedFile, tvPermanentDel, tvViewRecyclebin, tvRestoreFile, tvDelFaq, tvEditFaq, tvAddFaq, tvViewFaq, tvPdfDownload, tvPdfPrint, tvCheckinCheckout, tvBulkDownload, tvShareFile, tvCopyFile, tvMoveFile, tvexportcsv, tvUpdateFile, tvDeleteVersion, tvFileVersion, tvPdfAnnotation, tvImageFile, tvVideoFile, tvAudioFile, tvExcelFile, tvDocFile, tvPdfFile, tvTifFile, tvFileComment, tvFileAnotDelete, tvFileDelete, tvFileEdit, tvMetadataQuickSearch, tvMetadataSearch, tvWorkFlowAudit, tvViewUserAudit, tvCopyStorageLevel, tvMoveStorageLevel, tvDeleteStorageLevel, tvModifyStorageLevel, tvUploadDocStorage, tvCreateChildStorage, tvCreateStorage, tvAsignMetaData, tvViewMetaData, tvAddMetaData, tvBulkUpload, tvRoleView, tvRoleModify, tvRoleDelete, tvRoleAdd, tvViewGroupList, tvModifyGroup, tvDeleteGroup, tvAddGroup, tvOnlineUser, tvEmailConfig, tvMailList, tvStorageAuthPolicy, tvViewUserList, tvDeleteUserList, tvModifyUserList, tvCreateUser, tvdashboardmemory, tvdashboardnumoffiles, tvProfilename, tvProfileid, tvdashboardMydms, tvdashboardEditProfile, tvdashboardquery, tvdashboardnumoffolders;

        public ViewHolder(View itemView) {
            super(itemView);

            tvProfilename = itemView.findViewById(R.id.tv_user_profile_profilename);
            tvProfileid = itemView.findViewById(R.id.tv_user_profile_profileid);
            tvdashboardMydms = itemView.findViewById(R.id.tv_user_profile_dashboard_mydms);
            tvdashboardEditProfile = itemView.findViewById(R.id.tv_user_profile_dashboard_edit_profile);
            tvdashboardquery = itemView.findViewById(R.id.tv_user_profile_dashboard_query);
            tvdashboardnumoffolders = itemView.findViewById(R.id.tv_user_profile_dashboard_num_of_folder);
            tvdashboardnumoffiles = itemView.findViewById(R.id.tv_user_profile_dashboard_num_of_file);
            tvdashboardmemory = itemView.findViewById(R.id.tv_user_profile_memory_used);
            tvCreateUser = itemView.findViewById(R.id.tv_user_profile_create_user);
            tvModifyUserList = itemView.findViewById(R.id.tv_user_profile_modify_userlist);
            tvDeleteUserList = itemView.findViewById(R.id.tv_user_profile_delete_userlist);
            tvViewUserList = itemView.findViewById(R.id.tv_user_profile_view_group_list);
            tvStorageAuthPolicy = itemView.findViewById(R.id.tv_user_profile_storage_auth_plcy);
            tvMailList = itemView.findViewById(R.id.tv_user_profile_mail_lists);
            tvEmailConfig = itemView.findViewById(R.id.tv_user_profile_email_config);
            tvOnlineUser = itemView.findViewById(R.id.tv_user_profile_online_user);
            tvAddGroup = itemView.findViewById(R.id.tv_user_profile_add_group);
            tvDeleteGroup = itemView.findViewById(R.id.tv_user_profile_delete_group);
            tvModifyGroup = itemView.findViewById(R.id.tv_user_profile_modify_group);
            tvViewGroupList = itemView.findViewById(R.id.tv_user_profile_view_group_list);
            tvRoleAdd = itemView.findViewById(R.id.tv_user_profile_role_add);
            tvRoleDelete = itemView.findViewById(R.id.tv_user_profile_role_delete);
            tvRoleModify = itemView.findViewById(R.id.tv_user_profile_role_modi);
            tvRoleView = itemView.findViewById(R.id.tv_user_profile_role_view);
            tvBulkUpload = itemView.findViewById(R.id.tv_user_profile_bulk_upload);
            tvAddMetaData = itemView.findViewById(R.id.tv_user_profile_add_metadata);
            tvViewMetaData = itemView.findViewById(R.id.tv_user_profile_view_metadata);
            tvAsignMetaData = itemView.findViewById(R.id.tv_user_profile_assign_metadata);
            tvCreateStorage = itemView.findViewById(R.id.tv_user_profile_create_storage);
            tvCreateChildStorage = itemView.findViewById(R.id.tv_user_profile_create_child_storage);
            tvUploadDocStorage = itemView.findViewById(R.id.tv_user_profile_upload_doc_storage);
            tvModifyStorageLevel = itemView.findViewById(R.id.tv_user_profile_modify_storage_level);
            tvDeleteStorageLevel = itemView.findViewById(R.id.tv_user_profile_delete_storage_level);
            tvMoveStorageLevel = itemView.findViewById(R.id.tv_user_profile_move_storage_level);
            tvCopyStorageLevel = itemView.findViewById(R.id.tv_user_profile_copy_storage_level);
            tvViewUserAudit = itemView.findViewById(R.id.tv_user_profile_view_user_audit);
            tvWorkFlowAudit = itemView.findViewById(R.id.tv_user_profile_workflow_audit);
            tvMetadataSearch = itemView.findViewById(R.id.tv_user_profile_metadata_search);
            tvMetadataQuickSearch = itemView.findViewById(R.id.tv_user_profile_metadata_quick_search);
            tvFileEdit = itemView.findViewById(R.id.tv_user_profile_file_edit);
            tvFileDelete = itemView.findViewById(R.id.tv_user_profile_file_delete);
            tvFileAnotDelete = itemView.findViewById(R.id.tv_user_profile_file_anot_delete);
            tvFileComment = itemView.findViewById(R.id.tv_user_profile_file_coment);
            tvTifFile = itemView.findViewById(R.id.tv_user_profile_tif_file);
            tvPdfFile = itemView.findViewById(R.id.tv_user_profile_pdf_file);
            tvDocFile = itemView.findViewById(R.id.tv_user_profile_doc_file);
            tvExcelFile = itemView.findViewById(R.id.tv_user_profile_excel_file);
            tvAudioFile = itemView.findViewById(R.id.tv_user_profile_audio_file);
            tvVideoFile = itemView.findViewById(R.id.tv_user_profile_video_file);
            tvImageFile = itemView.findViewById(R.id.tv_user_profile_image_file);
            tvPdfAnnotation = itemView.findViewById(R.id.tv_user_profile_pdf_annotation);
            tvFileVersion = itemView.findViewById(R.id.tv_user_profile_file_version);
            tvDeleteVersion = itemView.findViewById(R.id.tv_user_profile_delete_version);
            tvUpdateFile = itemView.findViewById(R.id.tv_user_profile_update_file);
            tvexportcsv = itemView.findViewById(R.id.tv_user_profile_export_csv);
            tvMoveFile = itemView.findViewById(R.id.tv_user_profile_move_file);
            tvCopyFile = itemView.findViewById(R.id.tv_user_profile_copy_file);
            tvShareFile = itemView.findViewById(R.id.tv_user_profile_share_file);
            tvBulkDownload = itemView.findViewById(R.id.tv_user_profile_bulk_download);
            tvCheckinCheckout = itemView.findViewById(R.id.tv_user_profile_checkin_checkout);
            tvPdfPrint = itemView.findViewById(R.id.tv_user_profile_pdf_print);
            tvPdfDownload = itemView.findViewById(R.id.tv_user_profile_pdf_download);
            tvViewFaq = itemView.findViewById(R.id.tv_user_profile_view_faq);
            tvAddFaq = itemView.findViewById(R.id.tv_user_profile_add_faq);
            tvEditFaq = itemView.findViewById(R.id.tv_user_profile_edit_faq);
            tvDelFaq = itemView.findViewById(R.id.tv_user_profile_del_faq);
            tvRestoreFile = itemView.findViewById(R.id.tv_user_profile_restore_file);
            tvViewRecyclebin = itemView.findViewById(R.id.tv_user_profile_view_recycle_bin);
            tvPermanentDel = itemView.findViewById(R.id.tv_user_profile_permanent_del);
            tvSharedFile = itemView.findViewById(R.id.tv_user_profile_shared_file);
            tvSharedFileWithMe = itemView.findViewById(R.id.tv_user_profile_share_with_me);
            tvFeedbackMsg = itemView.findViewById(R.id.tv_user_profile_feedback_msg);


        }
    }
}
