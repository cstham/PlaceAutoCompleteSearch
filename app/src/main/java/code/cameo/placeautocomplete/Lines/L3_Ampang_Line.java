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

public class L3_Ampang_Line {

    //Unlike Hashmap, "LinkedHashMap" preserve the order of insertion
    //public Map<String,LatLng> stationsHash = new LinkedHashMap<String,LatLng>();
    private List<Marker> stationIcon_markers = new ArrayList<>();
    private List<Marker> stationName_markers = new ArrayList<>();

    public ArrayList<String> stationName = new ArrayList<String>();
    public ArrayList<LatLng> stationPosition = new ArrayList<LatLng>();
    public ArrayList<Integer> timeBetweenStations = new ArrayList<Integer>();
    public ArrayList<String> transit = new ArrayList<>();

    public ArrayList<Integer> polylineColorList = new ArrayList<Integer>();
    int polylineColor[] = {255, 87, 51};

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
        transit.add("Titiwangsa");

        int standardTime = 2;  //2 mins
        //==========================================================================================

        set("Ampang", new LatLng(3.150783, 101.760010), standardTime);
        set("Cahaya", new LatLng(3.140875, 101.756688), standardTime);
        set("Cempaka", new LatLng(3.138874, 101.753087), standardTime);

        set("Pandan Indah", new LatLng(3.135047, 101.746936), standardTime);
        set("Pandan Jaya", new LatLng(3.130747, 101.739242), standardTime);
        set("Maluri", new LatLng(3.12398624137652, 101.72739028930664), standardTime);

        set("Miharja", new LatLng(3.121117, 101.718059), standardTime);
        set("Chan Sow Lin", new LatLng(3.127961, 101.715571), standardTime);
        set("Pudu", new LatLng(3.134967, 101.712296), standardTime);

        set("Hang Tuah", new LatLng(3.140719, 101.706099), standardTime);
        set("Plaza Rakyat", new LatLng(3.145935, 101.700833), standardTime);
        set("Masjid Jamek", new LatLng(3.150108, 101.696587), standardTime);

        set("Bandaraya", new LatLng(3.155726, 101.694593), standardTime);
        set("Sultan Ismail", new LatLng(3.161309, 101.694156), standardTime);
        set("PWTC", new LatLng(3.166861, 101.693662), standardTime);

        set("Titiwangsa", new LatLng(3.173364, 101.695970), standardTime);
        set("Sentul", new LatLng(3.179040, 101.695537), standardTime);
        set("Sentul Timur", new LatLng(3.186061, 101.695274), 0);

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