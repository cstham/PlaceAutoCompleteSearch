package code.cameo.placeautocomplete.SlideUp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.model.LatLng;

import code.cameo.placeautocomplete.MapsActivity;
import code.cameo.placeautocomplete.R;
import code.cameo.placeautocomplete.Routing;
import code.cameo.placeautocomplete.SlideUp.enums.Style;

import static code.cameo.placeautocomplete.Lines.Fares.fare;
import static code.cameo.placeautocomplete.Lines.Lines.lineColor;

import static code.cameo.placeautocomplete.Lines.Lines.transits;
import static code.cameo.placeautocomplete.MapsActivity.currentObj;
import static code.cameo.placeautocomplete.MapsActivity.mContext;

import static code.cameo.placeautocomplete.Routing.routeStationNames;
import static code.cameo.placeautocomplete.Routing.totalStations;
import static code.cameo.placeautocomplete.Routing.totalTime;
import static code.cameo.placeautocomplete.Routing.totalTransfers;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.from_stopsNo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.getOn_transit;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.lineNoInfo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.preferred_transit;

import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.sameline_destination;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.getOn_transit;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.transits_stopsNo;

public class SlideUp_Activity {

    public static MaterialStyledDialog dialogHeader_5;

    //public TextView duration, stationNos ,transferNos, fareAmount;

    //public TextView fromlineNo_dialog, fromStation_dialog , direction_dialog, from_stopsNo_dialog,
    //tolineNo_dialog, toStation_dialog, toDoor_dialog, to_stopsNo_dialog;

    //public TextView transit_dynamic, get_on_dynamic;

    private int initial_entryPoint;
    private int toView_routeNo;
    private int getOff_Color;
    private int getOn_Color_dynamic;
    private int getOn_Color;
    private boolean isVia;
    private int getOn_lineNo;
    private String nextStation;
    private String noOfStops;
    //String getOn_direction;

    public void initialize(boolean via_exist, String fromText, String viaText, String toText){

        //inflate view
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_view,null);

        //initialize Route Summary View
        TextView duration = (TextView) customView.findViewById(R.id.duration);
        TextView stationNos = (TextView) customView.findViewById(R.id.station_number);
        TextView transferNos = (TextView) customView.findViewById(R.id.transfer_number);
        TextView fareAmount = (TextView) customView.findViewById(R.id.fare);

        TextView fromlineNo_dialog = (TextView) customView.findViewById(R.id.circle_icon);
        TextView fromStation_dialog = (TextView) customView.findViewById(R.id.from_station);
        TextView direction_dialog = (TextView) customView.findViewById(R.id.direction);
        TextView from_stopsNo_dialog = (TextView) customView.findViewById(R.id.number_of_stops);
        View view1 = customView.findViewById(R.id.view1);

        TextView tolineNo_dialog = (TextView) customView.findViewById(R.id.destination_icon);
        TextView toStation_dialog = (TextView) customView.findViewById(R.id.destination);
        TextView toDoor_dialog = (TextView) customView.findViewById(R.id.destination_door_direction);
        //TextView to_stopsNo_dialog = (TextView) customView.findViewById(R.id.destination_number_of_stops);
        View view3 = customView.findViewById(R.id.view3);
        //==========================================================================================
        //display route information summary

        duration.setText(totalTime);
        stationNos.setText(totalStations);
        transferNos.setText(totalTransfers);
        fareAmount.setText(fare);

        //==========================================================================================
        //ADDING FROM_VIEW

        fromlineNo_dialog.setText(lineNoInfo.get("route0").get(0));
        fromStation_dialog.setText(fromText);


        from_stopsNo_dialog.setText(String.valueOf(from_stopsNo.get("route0").get(0)));

        //get direction
        direction_dialog.setText(routeStationNames.get("route0").get(1)); //index 1 is second station

        GradientDrawable fromStation_drawable = (GradientDrawable) fromlineNo_dialog.getBackground().getCurrent();

        getOff_Color = Color.argb(255,
                lineColor.get("line" +lineNoInfo.get("route0").get(0)).get(0),
                lineColor.get("line" +lineNoInfo.get("route0").get(0)).get(1),
                lineColor.get("line" +lineNoInfo.get("route0").get(0)).get(2));


        fromStation_drawable.setColor(getOff_Color);

        view1.setBackgroundColor(getOff_Color);
        //direction_dialog.setText();
        //from_stopsNo_dialog.setText();

        //------------------------------------------------------------------------------------------
        //adding view to an already inflated view

        initial_entryPoint = 2;
        if (via_exist) {
            toView_routeNo = 1;
            //==========================================================================================
            // VIA DOES NOT EXIST
        }else{
            toView_routeNo = 0;
System.out.println("transit_stopsNo lolx: " + transits_stopsNo.get("route0"));
            for (int i = 0; i < preferred_transit.get("route0").size(); i++){
                if (i>0){
                    getOff_Color = getOn_Color_dynamic;
                }

                //System.out.println("on line Number lolx: " + getOn_transit);

                getOn_Color_dynamic = Color.argb(255,
                        lineColor.get("line" + getOn_transit.get("route0").get(i)).get(0),
                        lineColor.get("line" + getOn_transit.get("route0").get(i)).get(1),
                        lineColor.get("line" + getOn_transit.get("route0").get(i)).get(2));

                int indexOf_nextStation = routeStationNames.get("route0").indexOf(preferred_transit.get("route0").get(i))+1;
                String nextStation = routeStationNames.get("route0").get(indexOf_nextStation);

                addTransitViews(customView, inflater,
                        //Sring get_on_transit
                        preferred_transit.get("route0").get(i),
                        //int getOff_Color, int getOn_lineNo
                        getOff_Color, getOn_transit.get("route0").get(i),
                        //getOn_Color_dynamic
                        getOn_Color_dynamic, nextStation,
                        noOfStops,
                        false);

            }
        }
        //------------------------------------------------------------------------------------------
        //ADDING TO_VIEW

        //tolineNo_dialog.setText(lineNoInfo.get("routeNo0").get(1));
        toStation_dialog.setText(toText);
        //toDoor_dialog.setText();
        //to_stopsNo_dialog.setText();

        GradientDrawable toStation_drawable = (GradientDrawable) tolineNo_dialog.getBackground().getCurrent();

        getOn_Color = Color.argb(255,
                lineColor.get("line" +lineNoInfo.get("route" +toView_routeNo).get(1)).get(0),
                lineColor.get("line" +lineNoInfo.get("route" +toView_routeNo).get(1)).get(1),
                lineColor.get("line" +lineNoInfo.get("route" +toView_routeNo).get(1)).get(2));

        toStation_drawable.setColor(getOn_Color);
        view3.setBackgroundColor(getOn_Color);

        //------------------------------------------------------------------------------------------
        //initialize dialog
        dialogHeader_5 = new MaterialStyledDialog.Builder(currentObj)
                //.setIcon(new IconicsDrawable(context).icon(MaterialDesignIconic.Icon.gmi_comment_alt).color(Color.WHITE))
                .withDialogAnimation(true)
                //.setDescription("What can we improve? Your feedback is always welcome.")
                .setHeaderColor(R.color.black)
                .setStyle(Style.HEADER_WITH_TITLE)
                .setTitle("ROUTE INFORMATION")
                .setScrollable(true)
                .setCustomView(customView, 20, 20, 20, 0)
                .setPositiveText("Feedback")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/javiersantos/MaterialStyledDialogs/issues")));
                    }
                }).build();

        //------------------------------------------------------------------------------------------
        addSpinner(customView);

    }

    private void addTransitViews(View customView, LayoutInflater inflater,
                                 String get_on_transit,
                                 int getOff_Color, int getOn_lineNo,
                                 int getOn_Color_dynamic, String getOnDirection,
                                 String noOfStops,
                                 boolean isVia){

        LinearLayout main = (LinearLayout)customView.findViewById(R.id.custom_view_layout);
        View transitView = inflater.inflate(R.layout.transit_dynamic, null);

        TextView transit_dynamic = (TextView) transitView.findViewById(R.id.transit);
        TextView get_on_dynamic = (TextView) transitView.findViewById(R.id.get_on);
        TextView get_on_icon = (TextView) transitView.findViewById(R.id.get_on_icon);
        TextView exit_icon = (TextView) transitView.findViewById(R.id.exit_icon);
        TextView get_on_direction = (TextView) transitView.findViewById(R.id.get_on_direction);
        TextView get_on_noOfStops = (TextView) transitView.findViewById(R.id.get_on_number_of_stops);
        View view2 = transitView.findViewById(R.id.view2);
        View view4 = transitView.findViewById(R.id.view4);

        transit_dynamic.setText(get_on_transit);
        get_on_dynamic.setText(get_on_transit);
        get_on_icon.setText(String.valueOf(getOn_lineNo));
        get_on_direction.setText(getOnDirection);


        get_on_noOfStops.setText(noOfStops);

        if (isVia){
            //exit_icon.setTextSize(9);
            exit_icon.setText("VIA");
        }else{
            //exit_icon.setTextSize(5);
            exit_icon.setText("▼");
        }

        GradientDrawable getOff_drawable = (GradientDrawable) exit_icon.getBackground().getCurrent();
        getOff_drawable.setColor(getOff_Color);
        view2.setBackgroundColor(getOff_Color);

        GradientDrawable getOn_drawable = (GradientDrawable) get_on_icon.getBackground().getCurrent();
        getOn_drawable.setColor(getOn_Color_dynamic);
        view4.setBackgroundColor(getOn_Color_dynamic);



        main.addView(transitView, initial_entryPoint);
        initial_entryPoint++;
    }

    private void addSpinner(View customView){
        //new spinner code (To enable custom font for spinner)
        String[] fareTypes = mContext.getResources().getStringArray(R.array.fare_type);
        Spinner spinner = (Spinner) customView.findViewById(R.id.fare_type);

        //RelativeLayout rl =(RelativeLayout)findViewById(R.id.custom_view_layout);

        //Change to view.getContext() if "this" displays an error
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(customView.getContext(),
                R.layout.spinner_faretype_display, fareTypes);

        adapter.setDropDownViewResource(R.layout.spinner_faretype_dropdown);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {

                //String gear_speed = parent.getItemAtPosition(position).toString();
                //------------------------------------------------------------------------------------------------
                //SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                //SharedPreferences.Editor editor = saved_values.edit();
                //.putString("gear_speed", String.valueOf(gear_speed));
                //editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        //------------------------------------------------------------------------------------------
    }

    public void showRouteInfo(){
        dialogHeader_5.show();
    }




}
