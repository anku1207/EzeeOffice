package in.cbslgroup.ezeeoffice.Chip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.tokenautocomplete.TokenCompleteTextView;

import in.cbslgroup.ezeeoffice.R;


public class UsernameCompletionView extends TokenCompleteTextView<User> {

    private boolean showAlways;


    public UsernameCompletionView(Context context) {

        super(context);
        allowDuplicates(false);
        setTokenClickStyle(TokenCompleteTextView.TokenClickStyle.Delete);
    }

    public UsernameCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsernameCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setShowAlways(boolean showAlways) {

        this.showAlways = showAlways;
    }





    @Override
    public boolean enoughToFilter() {

        return showAlways || super.enoughToFilter();
    }



    @Override
    public void setOnTouchListener(View.OnTouchListener l) {
        super.setOnTouchListener(l);

        showDropDownIfFocused();
        //setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

     // showDropDown();

    }


    @Override
    public void onFocusChanged(boolean hasFocus, int direction, Rect previous) {
        super.onFocusChanged(hasFocus, direction, previous);

        showDropDownIfFocused();


      //  showDropDown();

    }


    @Override
    protected View getViewForObject(User user) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View tokenView = l.inflate(R.layout.item_autocomplete_username, (ViewGroup) getParent(), false);
        TokenTextView textView = tokenView.findViewById(R.id.token_text);
        TokenTextView textView2 = tokenView.findViewById(R.id.token_text_slid);
        ImageView icon = tokenView.findViewById(R.id.icon);
        textView.setText(user.getUsername());
        textView2.setText(user.getSlid());
        icon.setImageResource(user.getDrawableId());

        return tokenView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

       showDropDownIfFocused();
      // showDropDown();

    }

    private void showDropDownIfFocused() {

        if (enoughToFilter() && isFocused() && getWindowVisibility() == View.VISIBLE || !isPopupShowing()) {

            //showDropDown();
            //setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            //requestFocus();

        }
    }

    //Delete the unwanted/invalid text which doesnt belong in list
    public void deleteText() {
        Editable text = getText();
        text.replace(text.length() - currentCompletionText().length(), text.length(), "");
    }

    @Override
    protected User defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
   /*     int index = completionText.indexOf('@');

        if (index == -1) {
            return new User(completionText,completionText, R.drawable.ic_person_darkblue_24dp);
        } else {
            return new User(completionText,completionText, R.drawable.ic_person_darkblue_24dp);
        }*/
   return null;
    }
}
