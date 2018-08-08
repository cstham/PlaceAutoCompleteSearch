package code.cameo.placeautocomplete.Animation;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.Interpolator;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import static code.cameo.placeautocomplete.MapsActivity.mMap;

public class SwapMarkerPosition {

    private static SwapMarkerPosition swapMarkerPosition;

    public static SwapMarkerPosition getInstance(){
        if(swapMarkerPosition == null) swapMarkerPosition = new SwapMarkerPosition();
        return swapMarkerPosition;
    }


    public void animate(final Marker marker, final LatLng toPosition,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 400;

        final Interpolator interpolator = new LinearOutSlowInInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;

                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;

                marker.setPosition(new LatLng(lat,lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                    //System.out.println("animation NOT COMPLETE lolc");
                } else {
                    //System.out.println("animation COMPLETED!!! lolc");
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


}
