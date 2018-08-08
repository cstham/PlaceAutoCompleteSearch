package code.cameo.placeautocomplete;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import code.cameo.placeautocomplete.Animation.MapAnimator;
import code.cameo.placeautocomplete.Animation.PulsatingEffect;


import code.cameo.placeautocomplete.Lines.Lines;

import static code.cameo.placeautocomplete.Adv_SearchBarFragment.via_exist;
import static code.cameo.placeautocomplete.Lines.Lines.names;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.destination_stopsNo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.from_stopsNo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.lineNoInfo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.getOn_transit;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.preferred_transit;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.sameline_destination;
//import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.sameline_lineNo;
//import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.sameline_stopsNo;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.transit_on_line;
import static code.cameo.placeautocomplete.RoutingDirection.Route_Analysis.transits_stopsNo;

import code.cameo.placeautocomplete.RoutingDirection.Route_Analysis;

public class Routing {
    Route_Analysis route_analysis = new Route_Analysis();

    Lines lines = new Lines();

    public static ListMultimap<String, LatLng> noOfRoutes = LinkedListMultimap.create();

    public static ListMultimap<String, String> routeStationNames = LinkedListMultimap.create();

    public List<LatLng> transitPos = new ArrayList<LatLng>();
    public HashSet<LatLng> transitHash = new HashSet<LatLng>();
    public List<LatLng> unique_transitPos = new ArrayList<LatLng>();


    public ListMultimap<String, Integer> timeRequired = LinkedListMultimap.create();
    public int index_timeFrom, index_timeTo;
    public static String totalTime, totalStations , totalTransfers;

    public List<Integer> noOfTransfers = new ArrayList<Integer>();

    public ListMultimap<String, Integer> noOfStations = LinkedListMultimap.create();

    boolean onSameLine;

    public void evaluate(String fromStation, String destination, int routeNo){

        onSameLine = false;

        lines.setLines();
        //noOfRoutes.clear();
        //==========================================================================================
        System.out.println("fromSearch lol: " + fromStation + "   ||   toSearch lol: " + destination);
        //fromStation = "Bukit Bintang";
        //destination = "PWTC";
        //==========================================================================================
        ///*
        for (int x = 0; x < 10; x++) {
            if ((names.get("line" +x).contains(fromStation))&&
                    (names.get("line" +x).contains(destination))){

                onSameLine = true;

                route_analysis.sameLine_execute(x, fromStation, destination, routeNo);
            }
        }
        //==================================================================================
        if (!onSameLine) {
            //from line 0 to line 1 (MRT line to Monorail line)
            if ((names.get("line8").contains(fromStation))&& (names.get("line9").contains(destination))){
                route_analysis.execute(8, 9, fromStation, destination, routeNo);
            }
            else if ((names.get("line9").contains(fromStation))&& (names.get("line8").contains(destination))){
                route_analysis.execute(9, 8, fromStation, destination, routeNo);
            }
            //======================================================================================
            //from line 0 to line 2 (MRT line to Ampang line)
            else if ((names.get("line3").contains(fromStation))&& (names.get("line9").contains(destination))){
                route_analysis.execute(3, 9, fromStation, destination, routeNo);
            }
            else if ((names.get("line9").contains(fromStation))&& (names.get("line3").contains(destination))){
                route_analysis.execute(9, 3, fromStation, destination, routeNo);
            }
            //======================================================================================
            //from line 1 to line 2 (Monorail line to Ampang Line)
            else if ((names.get("line3").contains(fromStation))&& (names.get("line8").contains(destination))){
                route_analysis.execute(3, 8, fromStation, destination, routeNo);
            }
            else if ((names.get("line8").contains(fromStation))&& (names.get("line3").contains(destination))){
                route_analysis.execute(8, 3, fromStation, destination, routeNo);
            }
            //--------------------------------------------------------------------------------------

            //System.out.println("(routing) routes lola: " + noOfTransfers);


        }
        noOfRoutes.putAll("myRoute" +routeNo, route_analysis.unique_routePosition);
        routeStationNames.putAll("route" +routeNo, route_analysis.unique_routeNames);
        transitPos = route_analysis.transitPos;
        noOfStations.put("myRoute" +routeNo, route_analysis.totalStations);
        timeRequired.put("myRoute" +routeNo, route_analysis.totalTime);
        noOfTransfers.add(route_analysis.transferNos);

    } //"draw" end

    //==============================================================================================

    public void drawRoute(GoogleMap mMap, int routeNo){
        MapAnimator.getInstance().drawRoute(mMap, "polyline" +routeNo, noOfRoutes.get("myRoute" + routeNo));
        //System.out.println("transitPos size lol: " + transitPos.size());
        for (int i = 0; i < transitPos.size(); i++) {
            // keep unique only
            if (transitHash.add(transitPos.get(i))) {
                unique_transitPos.add(transitPos.get(i));
            }
        }
    }

    public void pulsateTransit(GoogleMap mMap){
        //System.out.println("unique trans lol: " + unique_transitPos);
        for (int i = 0; i < unique_transitPos.size(); i++) {
            PulsatingEffect.getInstance().addPulsatingEffect(mMap, "transit" +i, unique_transitPos.get(i));
        }
    }


    public void animateRoute(int routeNo) {

        MapAnimator.getInstance().animateRoute(routeNo);

    }

    public void getTotalStations(){
        int sum = 0;
        if (via_exist){

            for (int i = 0; i < 2; i++) {
                for (int count: noOfStations.get("myRoute" +i) ){
                    sum += count;
                }
            }
        }else{
            for (int count: noOfStations.get("myRoute0") ){
                sum += count;
            }
        }
        totalStations = Integer.toString(sum);
    }

    public void getTotalTime(){
        int sum = 0;
        if (via_exist){
            for (int i = 0; i < 2; i++) {
                for (int count: timeRequired.get("myRoute" +i) ){
                    sum += count;
                }
            }
        }else{
            for (int count: timeRequired.get("myRoute0") ){
                sum += count;
            }

        }

        //totalTime = sum;
        //==========================================================================================
        //to convert from "mins" to "hrs mins" format
        //sum = 61;

        if (sum > 60) {
            if (sum % 60 != 0) {
                //has remainder (mins exist)
                int mins = sum % 60;

                if (Math.floor(sum / 60) == 1) {
                    totalTime = (int) Math.floor(sum / 60) + " hr " + mins + " mins";
                } else {
                    totalTime = (int) Math.floor(sum / 60) + " hrs " + mins + " mins";
                }

            } else {
                //no remainder
                if (Math.floor(sum / 60) == 1) {
                    totalTime = (int) Math.floor(sum / 60) + " hr";
                } else {
                    totalTime = (int) Math.floor(sum / 60) + " hrs";
                }
            }
        }
        else
        {
            totalTime = sum + " mins";
        }
    }

    public void getTransferFreq(){
        int sum = 0;
        for (int count: noOfTransfers){
            sum += count;
        }
        totalTransfers = Integer.toString(sum);
    }



    public void clear_All_Routes_Variables(){
        //clear current class variables
        noOfRoutes.clear();
        transitHash.clear();
        transitPos.clear();
        unique_transitPos.clear();

        timeRequired.clear();
        index_timeFrom = 0;
        index_timeTo = 0;
        totalTime = null;

        noOfStations.clear();
        totalStations = null;

        noOfTransfers.clear();
        totalTransfers = null;

        lineNoInfo.clear();
        transit_on_line.clear();
        preferred_transit.clear();
        getOn_transit.clear();

        sameline_destination.clear();
        //sameline_lineNo.clear();
        //uTurn_check.clear();

        routeStationNames.clear();

        from_stopsNo.clear();
        transits_stopsNo.clear();
        destination_stopsNo.clear();
        //sameline_stopsNo.clear();
    }





}

//System.out.println("REMOVED unique trans lol: " + unique_transitPos);