package code.cameo.placeautocomplete;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cstha2 on 15-Aug-17.
 */

public class Detailed_Options {

    public List<Marker> options_markers = new ArrayList<>();

    public void addAllOptions(GoogleMap googleMap, LatLng position, String stationName)
    {
        addOptionsOnTouch(googleMap, "options_background", R.layout.options_background, 0, position, stationName, 0f, 1.0f, 0f);
        addOptionsOnTouch(googleMap, "from", R.layout.options_stations, R.drawable.depart, position, stationName, -0.2f, 1.45f, 1.0f);
        addOptionsOnTouch(googleMap, "via", R.layout.options_stations, R.drawable.pause, position, stationName, -1.2f, 1.45f, 1.0f);
        addOptionsOnTouch(googleMap, "to", R.layout.options_stations, R.drawable.flag, position, stationName, -2.2f, 1.45f, 1.0f);
        addOptionsOnTouch(googleMap, "station_information", R.layout.options_stations, R.drawable.station_information, position, stationName, -3.2f, 1.45f, 1.0f);
        addOptionsOnTouch(googleMap, "stations_exit", R.layout.options_stations, R.drawable.stations_exit, position, stationName, -4.2f, 1.45f, 1.0f);
    }

    public void removeAllOptions(){
        //remove markers
        for (Marker optionMarker : options_markers) {
            optionMarker.remove();
        }
        options_markers.clear();
    }




    private void addOptionsOnTouch(GoogleMap googleMap, String tag, int layout, int image_resID, LatLng position, String stationName, float anchorX, float anchorY, float zIndex){

        GetMarkerBitmapFromView getMarkerBitmapFromView = new GetMarkerBitmapFromView();

        Marker OptionsOnTouch = googleMap.addMarker(
                new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView.getView(layout, image_resID, null, 0))).
                        position(position).
                        zIndex(zIndex).
                        anchor(anchorX, anchorY));

        OptionsIconTag optionsIconTag = new OptionsIconTag();
        optionsIconTag.setStationName(stationName);
        optionsIconTag.setOptionType(tag);
        optionsIconTag.setPosition(position);
        OptionsOnTouch.setTag(optionsIconTag);
        options_markers.add(OptionsOnTouch);

    }



}
