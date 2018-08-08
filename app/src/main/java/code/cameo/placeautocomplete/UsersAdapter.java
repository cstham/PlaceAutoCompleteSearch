package code.cameo.placeautocomplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UsersAdapter extends BaseAdapter {

    ArrayList<String> listItem;
    ArrayList<String> listAddress;

    Context mContext;

    //constructor
    public UsersAdapter(Context mContext, ArrayList<String> listItem, ArrayList<String> listAddress) {
        this.mContext = mContext;
        this.listItem = listItem;
        this.listAddress = listAddress;
    }

   private static class ViewHolder{
        TextView addresstext;
        TextView bodytext;
    }


    public int getCount() {
        return listItem.size();
    }

    public Object getItem(int arg0) {
        return listAddress.get(arg0);
    }

    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View arg1, ViewGroup viewGroup) {

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View row = inflater.inflate(R.layout.listitem_row, viewGroup, false);

        if (arg1 == null) {
            arg1 = inflater.inflate(R.layout.listitem_row, null);
            holder = new ViewHolder();

            holder.addresstext = (TextView) arg1.findViewById(R.id.textView1);
            holder.bodytext = (TextView) arg1.findViewById(R.id.textView2);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }

        holder.bodytext.setText(listItem.get(position));
        holder.addresstext.setText(listAddress.get(position));
        //To change font
        //Typeface comic_sans = Typeface.createFromAsset(mContext.getAssets(), "fonts/comic_sans.ttf");
        //bodytext.setTypeface(comic_sans);
        //addresstext.setTypeface(comic_sans);


        return arg1;
    }
}