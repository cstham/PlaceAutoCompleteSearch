package code.cameo.placeautocomplete.Lines;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import code.cameo.placeautocomplete.GetMarkerBitmapFromView;
import code.cameo.placeautocomplete.R;
import code.cameo.placeautocomplete.StationIconTag;

import static code.cameo.placeautocomplete.Colors.manipulateColor;

//**************************************************************************************************
//LINE 8: KL MONORAIL LINE
//**************************************************************************************************

public class L8_KL_Monorail_Line {

    //Unlike Hashmap, "LinkedHashMap" preserve the order of insertion
    //public Map<String,LatLng> stationsHash = new LinkedHashMap<String,LatLng>();
    private List<Marker> stationIcon_markers = new ArrayList<>();
    private List<Marker> stationName_markers = new ArrayList<>();

    public ArrayList<String> stationName = new ArrayList<String>();
    public ArrayList<LatLng> stationPosition = new ArrayList<LatLng>();
    public ArrayList<Integer> timeBetweenStations = new ArrayList<Integer>();
    public ArrayList<String> transit = new ArrayList<>();

    public ArrayList<Integer> polylineColorList = new ArrayList<Integer>();
    int polylineColor[] = {165, 109, 189};

    public void setStations() {
        //==========================================================================================
        stationName.clear();
        stationPosition.clear();
        timeBetweenStations.clear();
        polylineColorList.clear();
        transit.clear();

        //for use in Lines.java
        for (int color : polylineColor) {
            polylineColorList.add(color);
        }

        //for use in Lines.java
        transit.add("Bukit Bintang");
        transit.add("Titiwangsa");

        int standardTime = 2;  //2 mins
        //==========================================================================================

        set("KL Sentral", new LatLng(3.134410, 101.686064), 0);
        set("Tun Sambanthan", new LatLng(3.131484, 101.690830), standardTime);
        set("Maharajalela", new LatLng(3.138812, 101.699203), standardTime);
        //
        set("Hang Tuah", new LatLng(3.140719, 101.706099), standardTime);
        set("Imbi", new LatLng(3.142940, 101.709447), standardTime);
        set("Bukit Bintang", new LatLng(3.14615098121251, 101.71067476272583), standardTime);
        //
        set("Raja Chulan", new LatLng(3.150835, 101.710420), standardTime);
        set("Bukit Nanas", new LatLng(3.156490, 101.704841), standardTime);
        set("Medan Tuanku", new LatLng(3.159914, 101.698911), standardTime);
        //
        set("Chow Kit", new LatLng(3.167537, 101.698415), standardTime);
        set("Titiwangsa", new LatLng(3.173364, 101.695970), standardTime);

    }

    private void set(String station_name, LatLng position, int time){
        stationName.add(station_name);
        stationPosition.add(position);
        timeBetweenStations.add(time);
    }

    public void setPolyline(GoogleMap googleMap, int alpha){
        PolylineOptions polyoptions = new PolylineOptions().width(20).color(Color.argb(alpha, polylineColor[0], polylineColor[1], polylineColor[2])).geodesic(true); //255 = fully opaque , 125

        for (LatLng position : stationPosition) {
            polyoptions.add(position);
        }

        googleMap.addPolyline(polyoptions);
    }

    public void addAllStations(GoogleMap googleMap){

        for (int i = 0; i < stationName.size(); i++) {
            String name = stationName.get(i);
            LatLng position = stationPosition.get(i);
            addStationIcon(googleMap, name, position, manipulateColor(Color.argb(255, polylineColor[0], polylineColor[1], polylineColor[2]) ,0.8f) );
        }
    }

    private void addStationIcon (GoogleMap googleMap,String stationName, LatLng position, int stationIconColor) {

        GetMarkerBitmapFromView getMarkerBitmapFromView = new GetMarkerBitmapFromView();

        Marker marker = googleMap.addMarker(
                new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView.getView(R.layout.station_icons, 0, null, stationIconColor))).
                        position(position).
                        anchor(0.5f, 0.5f));

        StationIconTag stationIconTag = new StationIconTag();
        stationIconTag.setStationName(stationName);
        stationIconTag.setType("station_icon");
        stationIconTag.setPosition(position);
        marker.setTag(stationIconTag);

        stationIcon_markers.add(marker);

        Marker stationNames = googleMap.addMarker(
                new MarkerOptions().
                        icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView.getView(R.layout.station_names, 0, stationName, 0))).
                        position(position).
                        anchor(0.5f, -0.5f));

        stationName_markers.add(stationNames);
    }
}

//46, 204, 113 - green colour