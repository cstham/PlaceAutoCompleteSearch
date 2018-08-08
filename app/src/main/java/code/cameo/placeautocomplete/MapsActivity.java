package code.cameo.placeautocomplete;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;

import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import code.cameo.placeautocomplete.Animation.SwapMarkerPosition;
import code.cameo.placeautocomplete.Lines.L3_Ampang_Line;
import code.cameo.placeautocomplete.Lines.L8_KL_Monorail_Line;
import code.cameo.placeautocomplete.Lines.L9_MRT_SgBuloh_Kajang;
import code.cameo.placeautocomplete.SlideUp.SlideUp_Activity;


public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback {

    public static GoogleMap mMap;
    public static Context mContext;
    public static Context currentObj;
    private String mMapWebApiKey;
    private CameraPosition cameraPosition;

    //Marker stationIcon;
    //initialization for onCameraMove()
    float currentZoom, idleZoom;
    //int iconSize = 65;
    private List<Marker> markers = new ArrayList<>();
    private List<Marker> stationName_markers = new ArrayList<>();
    private List<Marker> options_markers = new ArrayList<>();

    //status markers
    //private List<Marker> status_markers = new ArrayList<>();
    Marker[] status_markers = new Marker[3];
    int statusMarker_width = 150;
    int statusMarker_height = 205;  //100,136 when zoom out
    int fromCount=0;
    int toCount=0;
    int viaCount=0;
    LatLng statusMarkerPosition = new LatLng(0, 0);

    Marker fromMarker, toMarker, viaMarker;

    private List<LatLng> positions = new ArrayList<>();

    HashMap<LatLng,String> hashMapMarker = new HashMap<>();
    HashMap<String,Marker> hashMapOptions = new HashMap<>();

    AppBarLayout main_search_bar, detailed_search_bar;

    Adv_SearchBarFragment adv_searchBarFragment;

    L3_Ampang_Line ampang_line = new L3_Ampang_Line();
    L8_KL_Monorail_Line kl_monorail_line = new L8_KL_Monorail_Line();
    L9_MRT_SgBuloh_Kajang mrt_sgBuloh_kajang = new L9_MRT_SgBuloh_Kajang();


    Detailed_Options detailed_options = new Detailed_Options();
    SlideUp_Activity slideUp_activity = new SlideUp_Activity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        currentObj = this;
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        */
        //Window w = getWindow(); // in Activity's onCreate() for instance
        //w.setStatusBarColor(Color.parseColor("#5499c7"));

        setContentView(R.layout.zzz);
        //=========================================================================================
        main_search_bar = (AppBarLayout) findViewById(R.id.main_search_bar);
        detailed_search_bar = (AppBarLayout) findViewById(R.id.detailed_search_bar);

        //to hide detailed_search_bar
        detailed_search_bar.setExpanded(false,false);

        adv_searchBarFragment = (Adv_SearchBarFragment) getSupportFragmentManager().findFragmentById(R.id.detailed_edit_box_fragment);

        //to initialize stations data
        mrt_sgBuloh_kajang.setStations();
        kl_monorail_line.setStations();
        ampang_line.setStations();
        //=========================================================================================
        //TESTING SECTION



        //slideUp_activity.initialize();



        //=========================================================================================
        initVariable();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initVariable() {
        mMapWebApiKey = getString(R.string.google_maps_browser_key);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //=========================================================================================
        //set uber style
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.uber));
        //=========================================================================================
        /*
        //To add station icons
        for (Map.Entry<String, LatLng> entry : mrt_sgBuloh_kajang.stationsHash.entrySet()) {
            String stationName = entry.getKey();
            LatLng position = entry.getValue();
            //System.out.println("Station Name lol: " + stationName + "   ||   Position: " + position + ".");
            addStationIcon(stationName, position);
            myRoute.add(position);
        }
        //------------------------------------------------------------------------------------------
        //add kl monorail stations icon
        for (Map.Entry<String, LatLng> entry : kl_monorail_line.stationsHash.entrySet()) {
            String stationName = entry.getKey();
            LatLng position = entry.getValue();
            addStationIcon(stationName, position);
        }*/
        //to determine time required for this operation
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();

        mrt_sgBuloh_kajang.addAllStations(mMap);
        kl_monorail_line.addAllStations(mMap);
        ampang_line.addAllStations(mMap);

        //------------------------------------------------------------------------------
        endnow = android.os.SystemClock.uptimeMillis();
        System.out.println("Time required: "+ (endnow- startnow) + " ms");


        //=========================================================================================
        //Adding polyline
        mrt_sgBuloh_kajang.setPolyline(mMap, 255);
        kl_monorail_line.setPolyline(mMap, 255);
        ampang_line.setPolyline(mMap,255);
        //=======================================================================================
        /*
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                if(mMap != null) {

                    for (int i = 0; i < routing.number_of_routes; i++) {
                        MapAnimator.getInstance().animateRoute(mMap, "polyline"+i, routing.noOfRoutes.get("myRoute"+i), 1.0f);
                    }
                    pulsatingEffect.addPulsatingEffect(mMap, new LatLng(3.1496433042853, 101.59374117851257));
                } else {
                    Toast.makeText(getApplicationContext(), "Map not ready", Toast.LENGTH_LONG).show();
                }
            }
        });
        */

        //System.out.println("this section will run? lol: ");
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {

            @Override
            public void onCameraIdle() {

                idleZoom = mMap.getCameraPosition().zoom;
            }});

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {
                //System.out.println("camera position: "+mMap.getCameraPosition());
                currentZoom = mMap.getCameraPosition().zoom;

                if (currentZoom!=idleZoom) {

                    //to adjust station station icon size
                    for (Marker marker : markers) {
                        if (currentZoom<=12){
                            marker.setVisible(false);}
                        else{
                            marker.setVisible(true);}
                    }
                    //==============================================================================
                    //to adjust station name size
                    for (Marker nameMarker : stationName_markers) {
                        if (currentZoom<=12){
                            nameMarker.setVisible(false);}
                        else{
                            nameMarker.setVisible(true);}
                    }
                    //==============================================================================
                    //System.out.println("Current zoom lol: " + currentZoom + "   ||   Icon size: " + iconSize + ".");
                }
            }
        });
        //==========================================================================================
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //Toast.makeText(getApplicationContext(), marker.getTag().toString(), Toast.LENGTH_LONG).show();
                //System.out.println("marker size: "+options_markers.size());
                if (adv_searchBarFragment.isAdded()) {
                    if (marker.getTag() != null) {
                        if (marker.getTag() instanceof StationIconTag) {
                            StationIconTag stationIconTag = ((StationIconTag) marker.getTag());
                            if (stationIconTag.getType().equals("station_icon")) {

                                //stationIconTag.isTransferStation();
                                System.out.println("transfer station:   " +stationIconTag.isTransferStation());



                                /*TO REMOVE ANIMATION EFFECTS
                                MapAnimator.getInstance().stopAnimateRoute("polyline1");
                                pulsatingEffect.removePulsatingEffect();
                                */

                                if (detailed_options.options_markers.isEmpty()) {

                                    if (!statusMarkerPosition.equals(stationIconTag.getPosition())) {
                                        detailed_options.addAllOptions(mMap, stationIconTag.getPosition(), stationIconTag.getStationName());
                                    }
                                } else {
                                    //remove markers
                                    detailed_options.removeAllOptions();

                                    //re-add marker
                                    if (!statusMarkerPosition.equals(stationIconTag.getPosition())) {
                                        detailed_options.addAllOptions(mMap, stationIconTag.getPosition(), stationIconTag.getStationName());
                                    }
                                }
                            }
                            //======================================================================

                        }
                        //==============================================================================
                        if (marker.getTag() instanceof OptionsIconTag) {
                            OptionsIconTag optionsIconTag = ((OptionsIconTag) marker.getTag());
                            if (optionsIconTag.getOptionType().equals("from")) {
                                adv_searchBarFragment.stopAnimation();
                                addStatusIcon("from_marker",optionsIconTag.getStationName(), optionsIconTag.getPosition());
                                adv_searchBarFragment.showResults();
                                //------------------------------------------------------------------
                                //TESTING PURPOSES


                                //slideUp_activity.showRouteInfo();


                            }
                            if (optionsIconTag.getOptionType().equals("to")) {
                                adv_searchBarFragment.stopAnimation();
                                addStatusIcon("to_marker",optionsIconTag.getStationName(), optionsIconTag.getPosition());
                                adv_searchBarFragment.showResults();
                            }
                            if (optionsIconTag.getOptionType().equals("via")) {
                                adv_searchBarFragment.stopAnimation();
                                addStatusIcon("via_marker",optionsIconTag.getStationName(), optionsIconTag.getPosition());
                                adv_searchBarFragment.showResults();
                            }
                        }

                        //==============================================================================
                        if (marker.getTag() instanceof StatusIconTag) {
                            StatusIconTag statusIconTag = ((StatusIconTag) marker.getTag());
                            if (statusIconTag.getOptionType().equals("from")) {
                                removeStatus(0);
                                adv_searchBarFragment.setEmptyText(statusIconTag.getOptionType());
                                adv_searchBarFragment.stopAnimation();

                            }
                            if (statusIconTag.getOptionType().equals("to")) {
                                removeStatus(1);
                                adv_searchBarFragment.setEmptyText(statusIconTag.getOptionType());
                                adv_searchBarFragment.stopAnimation();
                            }
                            if (statusIconTag.getOptionType().equals("via")) {
                                removeStatus(2);
                                adv_searchBarFragment.setEmptyText(statusIconTag.getOptionType());
                                adv_searchBarFragment.stopAnimation();
                                adv_searchBarFragment.showResults();
                            }
                        }
                    /*
                    if (marker.getTag().equals("from")) {
                        System.out.println("hashmap marker:lol");

                        //StationIconTag stationIconTag = ((StationIconTag) marker.getTag());
                        Adv_SearchBarFragment adv_searchBarFragment = (Adv_SearchBarFragment) getSupportFragmentManager().findFragmentById(R.id.detailed_edit_box_fragment);
                        if (adv_searchBarFragment.isAdded()){
                            adv_searchBarFragment.setFrom("lkkk");}

                    }

                    if (marker.getTag().equals("options_background")) {
                        System.out.println("hashmap marker:options_background");
                    }
                    */


                    }

                }



                return true;
            }
        });
        //==========================================================================================
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng arg0) {
                //remove all options markers
                detailed_options.removeAllOptions();
            }
        });

        //==========================================================================================

    }

    private void addStatusIcon(String statusType, String stationName, LatLng position){

        adv_searchBarFragment.setStatus(statusType, stationName);
        detailed_search_bar.setExpanded(true, true);

        //replace all options markers
        detailed_options.removeAllOptions();
        //==========================================================================================
        StatusIconTag statusIconTag = new StatusIconTag();

        if (statusType.equals("from_marker")){
            if (fromCount<1) {
                fromMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("from_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                fromCount++;
                statusIconTag.setOptionType("from");
                status_markers[0] = fromMarker;
                fromMarker.setTag(statusIconTag);
            }else{
                fromMarker.remove();
                fromMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("from_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                statusIconTag.setOptionType("from");
                status_markers[0] = fromMarker;
                fromMarker.setTag(statusIconTag);
            }
        }
        //==========================================================================================
        if (statusType.equals("to_marker")){
            if (toCount<1) {
                toMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("to_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                toCount++;
                statusIconTag.setOptionType("to");
                status_markers[1] = toMarker;
                toMarker.setTag(statusIconTag);
            }
            else{
                toMarker.remove();
                toMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("to_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                statusIconTag.setOptionType("to");
                status_markers[1] = toMarker;
                toMarker.setTag(statusIconTag);
            }
        }
        //==========================================================================================
        if (statusType.equals("via_marker")){
            if (viaCount<1) {
                viaMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("via_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                viaCount++;
                statusIconTag.setOptionType("via");
                status_markers[2] = viaMarker;
                viaMarker.setTag(statusIconTag);
            }else{
                viaMarker.remove();
                viaMarker = mMap.addMarker(
                        new MarkerOptions().
                                icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("via_marker", statusMarker_width, statusMarker_height))).
                                position(position));
                statusIconTag.setOptionType("via");
                status_markers[2] = viaMarker;
                viaMarker.setTag(statusIconTag);
            }
        }
        //==========================================================================================
        //marker position recorded to determine whether 2 markers exist on same position
        statusMarkerPosition = position;
        //==========
    }


    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public void removeStatus(int status_inArray){
        //remove status markers
        Marker statusMarker = status_markers[status_inArray];
        if (statusMarker!=null){
            statusMarker.remove();}
        status_markers[status_inArray] = null;
        statusMarkerPosition = new LatLng(0, 0);

        if (status_inArray == 0){
            fromCount = 0;}
        if (status_inArray == 1){
            toCount = 0;}
        if (status_inArray == 2){
            viaCount = 0;}
    }

    public void swapMarker_fromFrag(boolean fromEmpty, String stationName, boolean bothfilled , LatLng firstPos, LatLng lastPos){

        if (bothfilled){

            SwapMarkerPosition.getInstance().animate(fromMarker, lastPos, false);
            SwapMarkerPosition.getInstance().animate(toMarker, firstPos, false);

            adv_searchBarFragment.stopAnimation();
            adv_searchBarFragment.showResults();

        }else{
            if (fromEmpty){
                LatLng new_fromPos = toMarker.getPosition();
                toMarker.remove();
                addStatusIcon("from_marker", stationName, new_fromPos);
            }else {
                LatLng new_toPos = fromMarker.getPosition();
                fromMarker.remove();
                addStatusIcon("to_marker", stationName, new_toPos);}
        }

    }

    private void showStatusBar(boolean show){
        if (show) {
            //to show status bar
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else{
            //to hide status bar
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void onPlaceSelected(LatLng position){

        cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(17)
                .tilt(30)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
    //==============================================================================================

}

//System.out.println("properties lol: "+feature.getProperties());
//System.out.println("linestring style lol: "+feature.getLineStringStyle());
//=========================================================================================
        /*
        //to determine time required for this operation
        long startnow;
        long endnow;

        startnow = android.os.SystemClock.uptimeMillis();
        //------------------------------------------------------------------------------
        endnow = android.os.SystemClock.uptimeMillis();
        System.out.println("Time required: "+ (endnow- startnow) + " ms");
        */
//=========================================================================================