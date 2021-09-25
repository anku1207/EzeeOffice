package in.cbslgroup.ezeeoffice.Utils;


import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;


public class CustomSpinner extends AppCompatSpinner
{
   private boolean flag = true;

    public CustomSpinner(Context context, AttributeSet attrs,
                         int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
    }

    public CustomSpinner(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSpinner(Context context, int mode) {
        super(context, mode);
    }

    public CustomSpinner(Context context) {
        super(context);
    }


    @Override
    public int getSelectedItemPosition() {
        // this toggle is required because this method will get called in other
        // places too, the most important being called for the
        // OnItemSelectedListener
        if (!flag) {
            return 0; // get us to the first element
        }
        return super.getSelectedItemPosition();
    }


    @Override public void
    setSelection(int position, boolean animate)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            //getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override public void
    setSelection(int position)
    {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now


           // getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
        }
    }

    @Override
    public boolean performClick() {
        // this method shows the list of elements from which to select one.
        // we have to make the getSelectedItemPosition to return 0 so you can
        // fool the Spinner and let it think that the selected item is the first
        // element
        flag = false;
        boolean result = super.performClick();
        flag = true;
        return result;
    }


}
