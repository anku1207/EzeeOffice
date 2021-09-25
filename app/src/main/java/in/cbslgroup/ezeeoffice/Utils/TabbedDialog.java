package in.cbslgroup.ezeeoffice.Utils;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import in.cbslgroup.ezeeoffice.Adapters.AlertPagerAdapter;
import in.cbslgroup.ezeeoffice.Fragments.AppointmentListFragment;
import in.cbslgroup.ezeeoffice.Fragments.TodoListFragment;
import in.cbslgroup.ezeeoffice.R;

public class TabbedDialog extends DialogFragment {


    public OnButtonClickListener onButtonClickListener;
    TabLayout tabLayout;
    ViewPager viewPager;
    String specificDate;

    public static TabbedDialog createInstance(String date) {

        TabbedDialog tabbedDialog = new TabbedDialog();
        tabbedDialog.specificDate = date;

        return tabbedDialog;
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {

        this.onButtonClickListener = onButtonClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.alertdialog_todo_apt_tabbed, container, false);

        tabLayout = rootview.findViewById(R.id.alertdialog_todo_apt_tablayout);
        viewPager = rootview.findViewById(R.id.alertdialog_todo_apt_viewpager);

        Button btncancel = rootview.findViewById(R.id.btn_cancel_tabbed);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onButtonClickListener.onClickCancelButton();

            }
        });

        AlertPagerAdapter adapter = new AlertPagerAdapter(getChildFragmentManager());
        adapter.addFragment("Todo", TodoListFragment.createInstance(specificDate));
        adapter.addFragment("Appointments", AppointmentListFragment.createInstance(specificDate));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    /*    int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
*/
        return rootview;
    }



 /*   @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setView(getView().getRootView());
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        return alertDialog;


    }
*/


    public interface OnButtonClickListener {

        void onClickCancelButton();

    }

}

