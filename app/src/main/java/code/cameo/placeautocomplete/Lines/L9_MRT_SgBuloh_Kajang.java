package code.cameo.placeautocomplete.Lines;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import code.cameo.placeautocomplete.MapsActivity;
import code.cameo.placeautocomplete.R;
import code.cameo.placeautocomplete.StationIconTag;
import code.cameo.placeautocomplete.GetMarkerBitmapFromView;
import static code.cameo.placeautocomplete.Colors.manipulateColor;
//**************************************************************************************************
//LINE 9: MRT SUNGAI BULOH - KAJANG LINE
//**************************************************************************************************

public class L9_MRT_SgBuloh_Kajang {

    //Unlike Hashmap, "LinkedHashMap" preserve the order of insertion
    //public Map<String,LatLng> stationsHash = new LinkedHashMap<String,LatLng>();
    private List<Marker> stationIcon_markers = new ArrayList<>();
    private List<Marker> stationName_markers = new ArrayList<>();

    public ArrayList<String> stationName = new ArrayList<String>();
    public ArrayList<LatLng> stationPosition = new ArrayList<LatLng>();
    public ArrayList<Integer> timeBetweenStations = new ArrayList<Integer>();
    public ArrayList<String> transit = new ArrayList<>();

    public ArrayList<Integer> polylineColorList = new ArrayList<Integer>();
    int polylineColor[] = {93, 173, 226};
    //int stationIconColor = Color.argb(255, 35, 155, 86);

    public void setStations() {
        //==========================================================================================
        stationName.clear();
        stationPosition.clear();
        timeBetweenStations.clear();

        polylineColorList.clear();

        //for use in Lines.java
        for (int color : polylineColor) {
            polylineColorList.add(color);
        }

        //for use in Lines.java
        transit.add("Bukit Bintang");

        int standardTime = 2;  //2 mins
        //==========================================================================================
        set("Sungai Buloh", new LatLng(3.20630085095559, 101.58181071281432), standardTime);
        set("Kg. Selamat", new LatLng(3.19708845394776, 101.57851696014404), standardTime);
        set("Kwasa Damansara", new LatLng(3.17659593027712, 101.572744846344), standardTime);

        set("Kwasa Sentral", new LatLng(3.16982570225043, 101.5649127960205), standardTime);
        set("Kota Damansara", new LatLng(3.15029677447997, 101.57866716384888), standardTime);
        set("Surian", new LatLng(3.1496433042853, 101.59374117851257), standardTime);

        set("Mutiara Damansara", new LatLng(3.15517100642785, 101.60870790481567), standardTime);
        set("Bandar Utama", new LatLng(3.146753, 101.618524), standardTime);
        set("Taman Tun Dr. Ismail", new LatLng(3.136059601597, 101.630659103393), standardTime);

        set("Phileo Damansara", new LatLng(3.12917126848696, 101.6429328918457), standardTime);
        set("Pusat Bandar Damansara", new LatLng(3.14325855753886, 101.66239500045776), standardTime);
        set("Semantan", new LatLng(3.15096631318839, 101.66535079479218), standardTime);

        set("Muzium Negara", new LatLng(3.13725675284274, 101.68701767921448), standardTime);
        set("Pasar Seni", new LatLng(3.14197303333184, 101.69543981552123), standardTime);
        set("Merdeka", new LatLng(3.14193018249766, 101.70199513435364), standardTime);

        set("Bukit Bintang", new LatLng(3.14615098121251, 101.71067476272583), standardTime);
        set("Tun Razak Exchange", new LatLng(3.14268007184167, 101.71955823898315), standardTime);
        set("Cochrane", new LatLng(3.13273863794872, 101.72312021255493), standardTime);

        set("Maluri", new LatLng(3.12398624137652, 101.72739028930664), standardTime);
        set("Taman Permata", new LatLng(3.11273764416349, 101.72927856445312), standardTime);
        set("Taman Midah", new LatLng(3.10433861324112, 101.73234701156616), standardTime);

        set("Taman Mutiara", new LatLng(3.09135426572974, 101.74032926559448), standardTime);
        set("Taman Connaught", new LatLng(3.07929110739058, 101.74526453018188), standardTime);
        set("Taman Suntex", new LatLng(3.07162032459638, 101.76355719566345), standardTime);

        set("Sri Raya", new LatLng(3.06210676324887, 101.77300930023193), standardTime);
        set("Bandar Tun Hussein Onn", new LatLng(3.04833980476725, 101.77514433860779), standardTime);
        set("Batu Sebelas Cheras", new LatLng(3.04116163198051, 101.77324533462524), standardTime);

        set("Bukit Dukung", new LatLng(3.02632301898505, 101.7711853981018), standardTime);
        set("Sungai Jernih", new LatLng(3.00073798800188, 101.78410291671753), standardTime);
        set("Stadium Kajang", new LatLng(2.99399878240905, 101.78629159927368), standardTime);

        set("Kajang", new LatLng(2.98173096475404, 101.79030418395996), 0);


        //System.out.println("Station Name lolz: " + stationName.size() + "   ||   Position: " + stationPosition.size() + ".");
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

    private void addStationIcon (GoogleMap googleMap, String stationName, LatLng position, int stationIconColor) {

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
        //==========================================================================================
        //specifying transfer station within line
        if ((stationName.equals("Bukit Bintang")) || (stationName.equals("Pasar Seni"))){
            stationIconTag.setTransferStation(true);
        }
        else {
            stationIconTag.setTransferStation(false);
        }
        //==========================================================================================
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
//System.out.println("Station Name lol: " + stationName + "   ||   Position: " + position + ".");