package in.cbslgroup.ezeeoffice.Adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.cbslgroup.ezeeoffice.Fragments.ActivityLogFragment;
import in.cbslgroup.ezeeoffice.Fragments.ReviewLogFragment;
import in.cbslgroup.ezeeoffice.R;


public class BottomsheetPagerAdapter extends FragmentPagerAdapter {


    TabItem[] tabItems;
    Context context;


    public BottomsheetPagerAdapter(FragmentManager fragmentManager, Context context, TabItem... tabItems) {
        super(fragmentManager);
        this.context = context;
        this.tabItems = tabItems;
    }

    public BottomsheetPagerAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        return newInstance(tabItems[position].fragmentClass);
    }

    private Fragment newInstance(Class<? extends Fragment> fragmentClass) {
        try {
            return fragmentClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("fragment must have public no-arg constructor: " + fragmentClass.getName(), e);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(tabItems[position].titleResId);
    }

    @Override
    public int getCount() {
        return tabItems.length;
    }

    public enum TabItem {

        RV_ACTIVITY_LOG(ActivityLogFragment.class, R.string.tab_rv_activity_log),
        RV_REVIEW_LOG(ReviewLogFragment.class, R.string.tab_rv_review_log);

        private final Class<? extends Fragment> fragmentClass;
        private final int titleResId;

        TabItem(Class<? extends Fragment> fragmentClass, int titleResId) {
            this.fragmentClass = fragmentClass;
            this.titleResId = titleResId;
        }
    }

}
