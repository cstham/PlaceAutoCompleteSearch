package code.cameo.placeautocomplete.RoutingDirection;


import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import code.cameo.placeautocomplete.Lines.Lines;

import static code.cameo.placeautocomplete.Adv_SearchBarFragment.via_exist;
import static code.cameo.placeautocomplete.Lines.Lines.names;
import static code.cameo.placeautocomplete.Lines.Lines.positions;
import static code.cameo.placeautocomplete.Lines.Lines.timeBetween;
import static code.cameo.placeautocomplete.Lines.Lines.transits;

public class Route_Analysis {

    Lines lines = new Lines();

    public List<LatLng> routePosition = new ArrayList<>();
    private HashSet<LatLng> routePositionHash = new HashSet<>();
    public List<LatLng> unique_routePosition = new ArrayList<>();

    public List<String> routeNames = new ArrayList<>();
    private HashSet<String> routeNamesHash = new HashSet<>();
    public List<String> unique_routeNames = new ArrayList<>();



    public ListMultimap<String, Integer> timeRequired = LinkedListMultimap.create();
    public List<Integer> timeNos = new ArrayList<Integer>();
    public int index_timeFrom, index_timeTo, totalTime;

    public List<LatLng> transitPos = new ArrayList<LatLng>();
    public List<Integer> stationNos = new ArrayList<Integer>();
    public int totalStations;
    public int transferNos;

    //public static List<String> preferred_transit = new ArrayList<>();

    public static ListMultimap<String, String> preferred_transit = LinkedListMultimap.create();
    public static ListMultimap<String, Integer> getOn_transit = LinkedListMultimap.create();
    public static ListMultimap<String, Integer> transit_on_line = LinkedListMultimap.create();
    //private List<Integer> transit_on_line = new ArrayList<>();

    public static ListMultimap<String, String> lineNoInfo = LinkedListMultimap.create();

    public static String uTurn_transit;
    public static int uTurn_lineNo;
    //public static ListMultimap<String, Integer> sameline_lineNo = LinkedListMultimap.create();
    public static ListMultimap<String, String> sameline_destination = LinkedListMultimap.create();

    public static ListMultimap<String, String> from_stopsNo = LinkedListMultimap.create();
    public static ListMultimap<String, String> transits_stopsNo = LinkedListMultimap.create();
    public static ListMultimap<String, String> destination_stopsNo = LinkedListMultimap.create();
    //public static ListMultimap<String, String> sameline_stopsNo = LinkedListMultimap.create();

    public void execute(int start_lineNo, int end_lineNo, String fromStation, String destination, int routeNo){
        clean();
        lines.setLines();
        //transferNos = Math.abs(start_lineNo - end_lineNo); //Math.abs always return positive number
        //======================================================================================
        //from MRT line to Monorail Line
        if ((start_lineNo == 8) && (end_lineNo ==9)) {
            String prefTransArray[] = {"Bukit Bintang"};
            int getOn_Array[] = {end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, true);
        }
        else if ((start_lineNo == 9) && (end_lineNo ==8)) {
            String prefTransArray[] = {"Bukit Bintang"};
            int getOn_Array[] = {end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, true);
        }
        else if ((start_lineNo == 3) && (end_lineNo ==9)) {
            String prefTransArray[] = {"Titiwangsa", "Bukit Bintang"};
            int getOn_Array[] = {8, end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, false);
        }
        else if ((start_lineNo == 9) && (end_lineNo ==3)) {
            String prefTransArray[] = {"Bukit Bintang", "Titiwangsa"};
            int getOn_Array[] = {8, end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, false);
        }
        else if ((start_lineNo == 3) && (end_lineNo ==8)) {
            String prefTransArray[] = {"Titiwangsa"};
            int getOn_Array[] = {end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, true);
        }
        else if ((start_lineNo == 8) && (end_lineNo ==3)) {
            String prefTransArray[] = {"Titiwangsa"};
            int getOn_Array[] = {end_lineNo};
            add_preferred_transit(routeNo, prefTransArray, getOn_Array, true);
        }

        transferNos = preferred_transit.get("route" +routeNo).size();
        //System.out.println("total xxz lola: " + transferNos);
        //---------------------------------------------------------------------------
        //plot from fromStation to transit
        storePos(start_lineNo, fromStation, preferred_transit.get("route" +routeNo).get(0), routeNo, false, "FROM");


        //plot from transit to transit
        if (preferred_transit.get("route" +routeNo).size() > 1){
            for (int x = 0; x < transit_on_line.get("route" +routeNo).size(); x++) {
                storePos(transit_on_line.get("route" +routeNo).get(x), preferred_transit.get("route" +routeNo).get(x),
                        preferred_transit.get("route" +routeNo).get(x+1), routeNo, false, "TRANSIT");
            }
        }

        //plot from transit to destination
        int index_lastTransit = preferred_transit.get("route" +routeNo).size()-1;
        storePos(end_lineNo, preferred_transit.get("route" +routeNo).get(index_lastTransit), destination, routeNo, true, "DESTINATION");
        //==========================================================================================
        //total no. of stations
        int sum = 0;
        for (int count: stationNos ){
            sum += count;
        }

        totalStations = sum;

        //to get total time
        sumTotalTime(routeNo);
        //==========================================================================================
        //get lineNo info for dialog

        //lineNoInfo = "routeNo1 ---> startNo, endLineNo"
        lineNoInfo.put("route" +routeNo, String.valueOf(start_lineNo));
        lineNoInfo.put("route" +routeNo, String.valueOf(end_lineNo));

        remove_routeNames_duplicates();
    }
    //==============================================================================================
    public void sameLine_execute(int currentLineNo, String fromStation, String destination, int routeNo){
        clean();
        lines.setLines();
        transferNos = 0;
        //------------------------------------------------------------------------------------------
        storePos(currentLineNo, fromStation, destination, routeNo, true, "SAME_LINE");

        //total no. of stations
        int sum = 0;
        for (int count: stationNos ){
            sum += count;
        }

        totalStations = sum;

        //to get total time
        sumTotalTime(routeNo);
        //------------------------------------------------------------------------------------------
        //get lineNo info for dialog

        //lineNoInfo = "routeNo1 ---> startNo, endLineNo"
        lineNoInfo.put("route" +routeNo, String.valueOf(currentLineNo));
        lineNoInfo.put("route" +routeNo, String.valueOf(currentLineNo));

        //exclusive lines in "sameLine_execute"
        getOn_transit.put("route" +routeNo, currentLineNo);

        if (routeNo == 0){
            sameline_destination.put("route" +routeNo, destination);
        }
        else{
            sameline_destination.put("route" +routeNo, fromStation);
        }

        remove_routeNames_duplicates();
    }
    //==============================================================================================
    private void add_preferred_transit(int routeNo, String prefTransArray[],
                                       int getOn_Array[],
                                       boolean isSingleLineDif){

        for (String pref_trans: prefTransArray){
            preferred_transit.put("route" +routeNo, pref_trans);
        }

        for (int get_on_trans: getOn_Array){
            getOn_transit.put("route" +routeNo, get_on_trans);
        }

        if (!isSingleLineDif){
            //array length - 1 to prevent getting "end_lineNo"
            for (int i = 0; i < getOn_Array.length-1; i++) {
                transit_on_line.put("route" +routeNo, getOn_transit.get("route" +routeNo).get(i));
            }

        }


    }
    //==============================================================================================
    //loopNo is only applicable for "transit to transit" section
    private void storePos(int lineNo, String fromStation, String destination, int routeNo, boolean isLast, String type){
        int indexOf_From = names.get("line" +lineNo).indexOf(fromStation);
        int indexOf_Destination = names.get("line" +lineNo).indexOf(destination);
        //------------------------------------------------------------------------------------------
        //to get no. of stops
        int noOfStops = Math.abs(indexOf_Destination - indexOf_From);   //always get positive answer

        if (type.equals("FROM") || type.equals("SAME_LINE")){
            from_stopsNo.put("route" +routeNo, String.valueOf(noOfStops)); //only one answer
        }
        else if (type.equals("TRANSIT")){
            transits_stopsNo.put("route" +routeNo, String.valueOf(noOfStops));      //more than one answer for "noOfStops" due to transits
        }
        else if (type.equals("DESTINATION")){
            destination_stopsNo.put("route" +routeNo, String.valueOf(noOfStops));    //only one answer
        }
        //------------------------------------------------------------------------------------------
        //to get u-turn transit
        if ((routeNo==0)&&(isLast)){
            uTurn_transit = names.get("line" +lineNo).get(indexOf_Destination);
            uTurn_lineNo = lineNo;
        }
        //System.out.println("lola yes" +uTurn_check.get("route"));
        //------------------------------------------------------------------------------------------
        //to determine whether the last position is needed for pulse
        String name_last_destination = names.get("line" + lineNo).get(indexOf_Destination);
        boolean isTransit;
        //------------------------------------------------------------------------------------------
        if (transits.get("line" +lineNo).contains(name_last_destination)){
            isTransit = true;
        }else{
            isTransit = false;
        }

        //to get transit coordinates
        if ((!isLast) || (routeNo==0 && via_exist && isTransit)) {
            LatLng transit_position = positions.get("line" + lineNo).get(indexOf_Destination);
            transitPos.add(transit_position);
        }
        //------------------------------------------------------------------------------------------
        if (indexOf_From < indexOf_Destination) {
            for (int y = indexOf_From; y <= indexOf_Destination; y++) {
                LatLng position = positions.get("line" +lineNo).get(y);
                routePosition.add(position);

                String station = names.get("line" +lineNo).get(y);
                routeNames.add(station);
            }

            int station_num = indexOf_Destination - indexOf_From;
            stationNos.add(station_num);
        } else {
            for (int y = indexOf_From; y >= indexOf_Destination; y--) {
                LatLng position = positions.get("line" +lineNo).get(y);
                routePosition.add(position);

                String station = names.get("line" +lineNo).get(y);
                routeNames.add(station);
            }

            int station_num = indexOf_From - indexOf_Destination;
            stationNos.add(station_num);
        }

        calculateTime(indexOf_From, indexOf_Destination, routeNo, "line" +lineNo);
    }
    //==============================================================================================
    private void remove_routeNames_duplicates(){
        for (int i = 0; i < routeNames.size(); i++) {
            // keep unique only
            if (routeNamesHash.add(routeNames.get(i))) {
                unique_routeNames.add(routeNames.get(i));
            }
        }

        for (int i = 0; i < routePosition.size(); i++) {
            // keep unique only
            if (routePositionHash.add(routePosition.get(i))) {
                unique_routePosition.add(routePosition.get(i));
            }
        }
    }
    //==============================================================================================
    private void calculateTime(int indexOf_From, int indexOf_To ,int routeNo, String lineNo){
        //ensure that index_timeFrom is the smaller number
        if (indexOf_From > indexOf_To) {
            index_timeFrom = indexOf_To;
            index_timeTo = indexOf_To + (indexOf_From - indexOf_To);
        }else{
            index_timeFrom = indexOf_From;
            index_timeTo = indexOf_To;
        }

        //index_timeFrom must be smaller for the following codes to work
        int distance = index_timeTo - index_timeFrom;

        if (distance==1) {
            int time = timeBetween.get(lineNo).get(index_timeTo);
            timeRequired.put("myRoute" + routeNo, time);
        }
        else{
            index_timeFrom = index_timeFrom + 1;
            //index_timeTo = index_timeTo;

            for (int z = index_timeFrom; z <= index_timeTo; z++) {
                int time = timeBetween.get(lineNo).get(z);
                timeRequired.put("myRoute" + routeNo, time);
            }
        }

    }

    private void sumTotalTime(int routeNo){
        int sum = 0;
        for (int count: timeRequired.get("myRoute" +routeNo) ){
            sum += count;
        }

        totalTime = sum;
    }
    //==============================================================================================
    public void clean(){
        //transit_on_line.clear();
        //preferred_transit.clear();
        //lineNoInfo.clear();
        routePosition.clear();
        routePositionHash.clear();
        unique_routePosition.clear();

        routeNames.clear();
        routeNamesHash.clear();
        unique_routeNames.clear();

        transitPos.clear();
        timeRequired.clear();

        stationNos.clear();
        totalStations = 0;

        timeNos.clear();
        index_timeFrom = 0;
        index_timeTo = 0;
        totalTime = 0;
    }


}
