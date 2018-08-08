package code.cameo.placeautocomplete.Lines;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

//**************************************************************************************************
//LINE 9: MRT SUNGAI BULOH - KAJANG LINE
//**************************************************************************************************

public class Lines {

    public static ListMultimap<String, String> names = LinkedListMultimap.create();
    public static ListMultimap<String, LatLng> positions = LinkedListMultimap.create();
    public static ListMultimap<String, String> transits = LinkedListMultimap.create();

    public static ListMultimap<String, Integer> timeBetween = LinkedListMultimap.create();

    public static ListMultimap<String, Integer> lineColor = LinkedListMultimap.create();
    public static ListMultimap<String, String> lineNo = LinkedListMultimap.create();


    L3_Ampang_Line ampang_line = new L3_Ampang_Line();
    L8_KL_Monorail_Line kl_monorail_line = new L8_KL_Monorail_Line();
    L9_MRT_SgBuloh_Kajang mrt_sgBuloh_kajang = new L9_MRT_SgBuloh_Kajang();

    public void setLines() {

        ampang_line.setStations();
        kl_monorail_line.setStations();
        mrt_sgBuloh_kajang.setStations();

        lineNo.put("line3", "3");
        lineNo.put("line8", "8");
        lineNo.put("line9", "9");

        names.putAll("line3", ampang_line.stationName);
        names.putAll("line8", kl_monorail_line.stationName);
        names.putAll("line9", mrt_sgBuloh_kajang.stationName);
        //==========================================================================================
        //positions
        positions.putAll("line3", ampang_line.stationPosition);
        positions.putAll("line8", kl_monorail_line.stationPosition);
        positions.putAll("line9", mrt_sgBuloh_kajang.stationPosition);
        //==========================================================================================
        //time
        timeBetween.putAll("line3",ampang_line.timeBetweenStations);
        timeBetween.putAll("line8",kl_monorail_line.timeBetweenStations);
        timeBetween.putAll("line9",mrt_sgBuloh_kajang.timeBetweenStations);
        //==========================================================================================
        lineColor.putAll("line3", ampang_line.polylineColorList);
        lineColor.putAll("line8",kl_monorail_line.polylineColorList);
        lineColor.putAll("line9", mrt_sgBuloh_kajang.polylineColorList);
        //==========================================================================================
        transits.putAll("line3", ampang_line.transit);
        transits.putAll("line8", kl_monorail_line.transit);
        transits.putAll("line9", mrt_sgBuloh_kajang.transit);

    }





}
