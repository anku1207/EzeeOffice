package in.cbslgroup.ezeeoffice.Interface;

import android.view.View;

import in.cbslgroup.ezeeoffice.Model.Recyclebin;

public interface RecycleBinListListener {

    void onRestoreButtonClick(View v, final String docid, final int position , Recyclebin rvObj);

    void onPerDeleteButtonClick(View v, final String docid, final int position, Recyclebin rvObj);



}
