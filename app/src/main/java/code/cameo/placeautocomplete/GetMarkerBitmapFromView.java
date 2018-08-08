package code.cameo.placeautocomplete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import code.cameo.placeautocomplete.R;

import static code.cameo.placeautocomplete.MapsActivity.mContext;

/**
 * Created by cstha2 on 15-Aug-17.
 */

public class GetMarkerBitmapFromView {

    //to use xml as icon
    public Bitmap getView(int layout, int image_resID, String station_name, int stationIconColor) {
        View customMarkerView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layout,null);
        //============================================================================================
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.options_symbol);
        if (markerImageView!=null) {
            markerImageView.setImageResource(image_resID);
        }

        TextView stationName = (TextView) customMarkerView.findViewById(R.id.station_names);
        if (stationName!=null) {
            stationName.setText(station_name.toUpperCase()); //change station names to uppercase
        }

        ImageView stationIconView = (ImageView) customMarkerView.findViewById(R.id.station_icons);
        if (stationIconView!=null) {
            //stationIconView.setImageResource(image_resID);
            //stationIconView.setColorFilter(Color.parseColor("#AE6118"));
            //stationIconView.setImageAlpha(100);
            stationIconView.getLayoutParams().height = 55;
            stationIconView.getLayoutParams().width = 55;
            //modify circle_ring.xml
            GradientDrawable gd = (GradientDrawable) stationIconView.getBackground().getCurrent();

            gd.setStroke(5, Color.argb(255, 255, 255, 255));
            gd.setColor(stationIconColor);



        }
        //============================================================================================
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }



}
