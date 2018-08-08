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


public class LoginPage extends FragmentActivity  {

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

        setContentView(R.layout.login_page);

    }





    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

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