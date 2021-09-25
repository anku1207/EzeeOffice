package in.cbslgroup.ezeeoffice.Model;

public class UserProfile {

    //profile name and id
    String user_role;
    String role_id;
    //dashboard
    String dashboard_mydms;
    String dashboard_edit_profile;
    String dashboard_query;
    String num_of_folder;
    String num_of_file;
    String memory_used;
    //User manager
    String create_user;
    String modify_userlist;
    String delete_userlist;
    String view_userlist;
    //Authorization
    String storage_auth_plcy;
    String mail_lists;
    String email_config;
    String online_user;
    //Group manager
    String add_group;
    String delete_group;
    String modify_group;
    String view_group_list;
    //User Profile Manager
    String role_add;
    String role_delete;
    String role_modi;
    String role_view;
    //upload/import
    String bulk_upload;
    //MetaData Registry
    String add_metadata;
    String view_metadata;
    String assign_metadata;
    //Storage Manager
    String create_storage;
    String create_child_storage;
    String upload_doc_storage;
    String modify_storage_level;
    String delete_storage_level;
    String move_storage_level;
    String copy_storage_level;
    //Audit Trail
    String view_user_audit;
    String view_storage_audit;
    String workflow_audit;
    //MetaData Search
    String metadata_search;
    String metadata_quick_search;
    //file View Permissions
    String file_edit;
    String file_delete;
    String file_anot;
    String file_anot_delete;
    String file_coment;
    String tif_file;
    String pdf_file;
    String doc_file;
    String excel_file;
    String audio_file;
    String video_file;
    String image_file;
    String pdf_annotation;
    String file_version;
    String delete_version;
    String update_file;
    String export_csv;
    String move_file;
    String copy_file;
    String share_file;
    String bulk_download;
    String checkin_checkout;
    //for pdf viewer
    String pdf_print;
    String pdf_download;
    //Faq / Help
    String view_faq;
    String add_faq;
    String edit_faq;
    String del_faq;
    //Recycle Bin
    String view_recycle_bin;
    String restore_file;
    String permanent_del;
    //Shared & share with me
    String shared_file;
    String share_with_me;
    String feedback_msg;

    public UserProfile(String user_role, String role_id, String dashboard_mydms, String dashboard_edit_profile, String dashboard_query, String num_of_folder, String num_of_file, String memory_used, String create_user, String modify_userlist, String delete_userlist, String view_userlist, String storage_auth_plcy, String mail_lists, String email_config, String online_user, String add_group, String delete_group, String modify_group, String view_group_list, String role_add, String role_delete, String role_modi, String role_view, String bulk_upload, String add_metadata, String view_metadata, String assign_metadata, String create_storage, String create_child_storage, String upload_doc_storage, String modify_storage_level, String delete_storage_level, String move_storage_level, String copy_storage_level, String view_user_audit, String view_storage_audit, String workflow_audit, String metadata_search, String metadata_quick_search, String file_edit, String file_delete, String file_anot, String file_anot_delete, String file_coment, String tif_file, String pdf_file, String doc_file, String excel_file, String audio_file, String video_file, String image_file, String pdf_annotation, String file_version, String delete_version, String update_file, String export_csv, String move_file, String copy_file, String share_file, String bulk_download, String checkin_checkout, String pdf_print, String pdf_download, String view_faq, String add_faq, String edit_faq, String del_faq, String view_recycle_bin, String restore_file, String permanent_del, String shared_file, String share_with_me, String feedback_msg) {
        this.user_role = user_role;
        this.role_id = role_id;
        this.dashboard_mydms = dashboard_mydms;
        this.dashboard_edit_profile = dashboard_edit_profile;
        this.dashboard_query = dashboard_query;
        this.num_of_folder = num_of_folder;
        this.num_of_file = num_of_file;
        this.memory_used = memory_used;
        this.create_user = create_user;
        this.modify_userlist = modify_userlist;
        this.delete_userlist = delete_userlist;
        this.view_userlist = view_userlist;
        this.storage_auth_plcy = storage_auth_plcy;
        this.mail_lists = mail_lists;
        this.email_config = email_config;
        this.online_user = online_user;
        this.add_group = add_group;
        this.delete_group = delete_group;
        this.modify_group = modify_group;
        this.view_group_list = view_group_list;
        this.role_add = role_add;
        this.role_delete = role_delete;
        this.role_modi = role_modi;
        this.role_view = role_view;
        this.bulk_upload = bulk_upload;
        this.add_metadata = add_metadata;
        this.view_metadata = view_metadata;
        this.assign_metadata = assign_metadata;
        this.create_storage = create_storage;
        this.create_child_storage = create_child_storage;
        this.upload_doc_storage = upload_doc_storage;
        this.modify_storage_level = modify_storage_level;
        this.delete_storage_level = delete_storage_level;
        this.move_storage_level = move_storage_level;
        this.copy_storage_level = copy_storage_level;
        this.view_user_audit = view_user_audit;
        this.view_storage_audit = view_storage_audit;
        this.workflow_audit = workflow_audit;
        this.metadata_search = metadata_search;
        this.metadata_quick_search = metadata_quick_search;
        this.file_edit = file_edit;
        this.file_delete = file_delete;
        this.file_anot = file_anot;
        this.file_anot_delete = file_anot_delete;
        this.file_coment = file_coment;
        this.tif_file = tif_file;
        this.pdf_file = pdf_file;
        this.doc_file = doc_file;
        this.excel_file = excel_file;
        this.audio_file = audio_file;
        this.video_file = video_file;
        this.image_file = image_file;
        this.pdf_annotation = pdf_annotation;
        this.file_version = file_version;
        this.delete_version = delete_version;
        this.update_file = update_file;
        this.export_csv = export_csv;
        this.move_file = move_file;
        this.copy_file = copy_file;
        this.share_file = share_file;
        this.bulk_download = bulk_download;
        this.checkin_checkout = checkin_checkout;
        this.pdf_print = pdf_print;
        this.pdf_download = pdf_download;
        this.view_faq = view_faq;
        this.add_faq = add_faq;
        this.edit_faq = edit_faq;
        this.del_faq = del_faq;
        this.view_recycle_bin = view_recycle_bin;
        this.restore_file = restore_file;
        this.permanent_del = permanent_del;
        this.shared_file = shared_file;
        this.share_with_me = share_with_me;
        this.feedback_msg = feedback_msg;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getDashboard_mydms() {
        return dashboard_mydms;
    }

    public void setDashboard_mydms(String dashboard_mydms) {
        this.dashboard_mydms = dashboard_mydms;
    }

    public String getDashboard_edit_profile() {
        return dashboard_edit_profile;
    }

    public void setDashboard_edit_profile(String dashboard_edit_profile) {
        this.dashboard_edit_profile = dashboard_edit_profile;
    }

    public String getDashboard_query() {
        return dashboard_query;
    }

    public void setDashboard_query(String dashboard_query) {
        this.dashboard_query = dashboard_query;
    }

    public String getNum_of_folder() {
        return num_of_folder;
    }

    public void setNum_of_folder(String num_of_folder) {
        this.num_of_folder = num_of_folder;
    }

    public String getNum_of_file() {
        return num_of_file;
    }

    public void setNum_of_file(String num_of_file) {
        this.num_of_file = num_of_file;
    }

    public String getMemory_used() {
        return memory_used;
    }

    public void setMemory_used(String memory_used) {
        this.memory_used = memory_used;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getModify_userlist() {
        return modify_userlist;
    }

    public void setModify_userlist(String modify_userlist) {
        this.modify_userlist = modify_userlist;
    }

    public String getDelete_userlist() {
        return delete_userlist;
    }

    public void setDelete_userlist(String delete_userlist) {
        this.delete_userlist = delete_userlist;
    }

    public String getView_userlist() {
        return view_userlist;
    }

    public void setView_userlist(String view_userlist) {
        this.view_userlist = view_userlist;
    }

    public String getStorage_auth_plcy() {
        return storage_auth_plcy;
    }

    public void setStorage_auth_plcy(String storage_auth_plcy) {
        this.storage_auth_plcy = storage_auth_plcy;
    }

    public String getMail_lists() {
        return mail_lists;
    }

    public void setMail_lists(String mail_lists) {
        this.mail_lists = mail_lists;
    }

    public String getEmail_config() {
        return email_config;
    }

    public void setEmail_config(String email_config) {
        this.email_config = email_config;
    }

    public String getOnline_user() {
        return online_user;
    }

    public void setOnline_user(String online_user) {
        this.online_user = online_user;
    }

    public String getAdd_group() {
        return add_group;
    }

    public void setAdd_group(String add_group) {
        this.add_group = add_group;
    }

    public String getDelete_group() {
        return delete_group;
    }

    public void setDelete_group(String delete_group) {
        this.delete_group = delete_group;
    }

    public String getModify_group() {
        return modify_group;
    }

    public void setModify_group(String modify_group) {
        this.modify_group = modify_group;
    }

    public String getView_group_list() {
        return view_group_list;
    }

    public void setView_group_list(String view_group_list) {
        this.view_group_list = view_group_list;
    }

    public String getRole_add() {
        return role_add;
    }

    public void setRole_add(String role_add) {
        this.role_add = role_add;
    }

    public String getRole_delete() {
        return role_delete;
    }

    public void setRole_delete(String role_delete) {
        this.role_delete = role_delete;
    }

    public String getRole_modi() {
        return role_modi;
    }

    public void setRole_modi(String role_modi) {
        this.role_modi = role_modi;
    }

    public String getRole_view() {
        return role_view;
    }

    public void setRole_view(String role_view) {
        this.role_view = role_view;
    }

    public String getBulk_upload() {
        return bulk_upload;
    }

    public void setBulk_upload(String bulk_upload) {
        this.bulk_upload = bulk_upload;
    }

    public String getAdd_metadata() {
        return add_metadata;
    }

    public void setAdd_metadata(String add_metadata) {
        this.add_metadata = add_metadata;
    }

    public String getView_metadata() {
        return view_metadata;
    }

    public void setView_metadata(String view_metadata) {
        this.view_metadata = view_metadata;
    }

    public String getAssign_metadata() {
        return assign_metadata;
    }

    public void setAssign_metadata(String assign_metadata) {
        this.assign_metadata = assign_metadata;
    }

    public String getCreate_storage() {
        return create_storage;
    }

    public void setCreate_storage(String create_storage) {
        this.create_storage = create_storage;
    }

    public String getCreate_child_storage() {
        return create_child_storage;
    }

    public void setCreate_child_storage(String create_child_storage) {
        this.create_child_storage = create_child_storage;
    }

    public String getUpload_doc_storage() {
        return upload_doc_storage;
    }

    public void setUpload_doc_storage(String upload_doc_storage) {
        this.upload_doc_storage = upload_doc_storage;
    }

    public String getModify_storage_level() {
        return modify_storage_level;
    }

    public void setModify_storage_level(String modify_storage_level) {
        this.modify_storage_level = modify_storage_level;
    }

    public String getDelete_storage_level() {
        return delete_storage_level;
    }

    public void setDelete_storage_level(String delete_storage_level) {
        this.delete_storage_level = delete_storage_level;
    }

    public String getMove_storage_level() {
        return move_storage_level;
    }

    public void setMove_storage_level(String move_storage_level) {
        this.move_storage_level = move_storage_level;
    }

    public String getCopy_storage_level() {
        return copy_storage_level;
    }

    public void setCopy_storage_level(String copy_storage_level) {
        this.copy_storage_level = copy_storage_level;
    }

    public String getView_user_audit() {
        return view_user_audit;
    }

    public void setView_user_audit(String view_user_audit) {
        this.view_user_audit = view_user_audit;
    }

    public String getView_storage_audit() {
        return view_storage_audit;
    }

    public void setView_storage_audit(String view_storage_audit) {
        this.view_storage_audit = view_storage_audit;
    }

    public String getWorkflow_audit() {
        return workflow_audit;
    }

    public void setWorkflow_audit(String workflow_audit) {
        this.workflow_audit = workflow_audit;
    }

    public String getMetadata_search() {
        return metadata_search;
    }

    public void setMetadata_search(String metadata_search) {
        this.metadata_search = metadata_search;
    }

    public String getMetadata_quick_search() {
        return metadata_quick_search;
    }

    public void setMetadata_quick_search(String metadata_quick_search) {
        this.metadata_quick_search = metadata_quick_search;
    }

    public String getFile_edit() {
        return file_edit;
    }

    public void setFile_edit(String file_edit) {
        this.file_edit = file_edit;
    }

    public String getFile_delete() {
        return file_delete;
    }

    public void setFile_delete(String file_delete) {
        this.file_delete = file_delete;
    }

    public String getFile_anot() {
        return file_anot;
    }

    public void setFile_anot(String file_anot) {
        this.file_anot = file_anot;
    }

    public String getFile_anot_delete() {
        return file_anot_delete;
    }

    public void setFile_anot_delete(String file_anot_delete) {
        this.file_anot_delete = file_anot_delete;
    }

    public String getFile_coment() {
        return file_coment;
    }

    public void setFile_coment(String file_coment) {
        this.file_coment = file_coment;
    }

    public String getTif_file() {
        return tif_file;
    }

    public void setTif_file(String tif_file) {
        this.tif_file = tif_file;
    }

    public String getPdf_file() {
        return pdf_file;
    }

    public void setPdf_file(String pdf_file) {
        this.pdf_file = pdf_file;
    }

    public String getDoc_file() {
        return doc_file;
    }

    public void setDoc_file(String doc_file) {
        this.doc_file = doc_file;
    }

    public String getExcel_file() {
        return excel_file;
    }

    public void setExcel_file(String excel_file) {
        this.excel_file = excel_file;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public String getVideo_file() {
        return video_file;
    }

    public void setVideo_file(String video_file) {
        this.video_file = video_file;
    }

    public String getImage_file() {
        return image_file;
    }

    public void setImage_file(String image_file) {
        this.image_file = image_file;
    }

    public String getPdf_annotation() {
        return pdf_annotation;
    }

    public void setPdf_annotation(String pdf_annotation) {
        this.pdf_annotation = pdf_annotation;
    }

    public String getFile_version() {
        return file_version;
    }

    public void setFile_version(String file_version) {
        this.file_version = file_version;
    }

    public String getDelete_version() {
        return delete_version;
    }

    public void setDelete_version(String delete_version) {
        this.delete_version = delete_version;
    }

    public String getUpdate_file() {
        return update_file;
    }

    public void setUpdate_file(String update_file) {
        this.update_file = update_file;
    }

    public String getExport_csv() {
        return export_csv;
    }

    public void setExport_csv(String export_csv) {
        this.export_csv = export_csv;
    }

    public String getMove_file() {
        return move_file;
    }

    public void setMove_file(String move_file) {
        this.move_file = move_file;
    }

    public String getCopy_file() {
        return copy_file;
    }

    public void setCopy_file(String copy_file) {
        this.copy_file = copy_file;
    }

    public String getShare_file() {
        return share_file;
    }

    public void setShare_file(String share_file) {
        this.share_file = share_file;
    }

    public String getBulk_download() {
        return bulk_download;
    }

    public void setBulk_download(String bulk_download) {
        this.bulk_download = bulk_download;
    }

    public String getCheckin_checkout() {
        return checkin_checkout;
    }

    public void setCheckin_checkout(String checkin_checkout) {
        this.checkin_checkout = checkin_checkout;
    }

    public String getPdf_print() {
        return pdf_print;
    }

    public void setPdf_print(String pdf_print) {
        this.pdf_print = pdf_print;
    }

    public String getPdf_download() {
        return pdf_download;
    }

    public void setPdf_download(String pdf_download) {
        this.pdf_download = pdf_download;
    }

    public String getView_faq() {
        return view_faq;
    }

    public void setView_faq(String view_faq) {
        this.view_faq = view_faq;
    }

    public String getAdd_faq() {
        return add_faq;
    }

    public void setAdd_faq(String add_faq) {
        this.add_faq = add_faq;
    }

    public String getEdit_faq() {
        return edit_faq;
    }

    public void setEdit_faq(String edit_faq) {
        this.edit_faq = edit_faq;
    }

    public String getDel_faq() {
        return del_faq;
    }

    public void setDel_faq(String del_faq) {
        this.del_faq = del_faq;
    }

    public String getView_recycle_bin() {
        return view_recycle_bin;
    }

    public void setView_recycle_bin(String view_recycle_bin) {
        this.view_recycle_bin = view_recycle_bin;
    }

    public String getRestore_file() {
        return restore_file;
    }

    public void setRestore_file(String restore_file) {
        this.restore_file = restore_file;
    }

    public String getPermanent_del() {
        return permanent_del;
    }

    public void setPermanent_del(String permanent_del) {
        this.permanent_del = permanent_del;
    }

    public String getShared_file() {
        return shared_file;
    }

    public void setShared_file(String shared_file) {
        this.shared_file = shared_file;
    }

    public String getShare_with_me() {
        return share_with_me;
    }

    public void setShare_with_me(String share_with_me) {
        this.share_with_me = share_with_me;
    }

    public String getFeedback_msg() {
        return feedback_msg;
    }

    public void setFeedback_msg(String feedback_msg) {
        this.feedback_msg = feedback_msg;
    }
}
