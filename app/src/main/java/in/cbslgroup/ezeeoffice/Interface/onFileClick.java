package in.cbslgroup.ezeeoffice.Interface;

import android.view.View;

public interface onFileClick {

    void onFileClick(View v, int position);

    void onCheckInBtnClick(View v, int position, String docid, String docname);

    void onCheckOutBtnClick(View v, int position, String docid, String docname);

     void onDelVersionBtnClick(View v, int position, final String docid);

    void onDeleteBtnClick(View v, int position, final String docid, final String fullname, final String filename);
}
