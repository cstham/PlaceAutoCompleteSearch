package code.cameo.placeautocomplete;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import net.cachapa.expandablelayout.ExpandableLayout;

import code.cameo.placeautocomplete.Animation.MapAnimator;
import code.cameo.placeautocomplete.Animation.PulsatingEffect;
import code.cameo.placeautocomplete.Lines.Fares;
import code.cameo.placeautocomplete.SlideUp.SlideUp_Activity;



import static code.cameo.placeautocomplete.MapsActivity.mMap;

import static code.cameo.placeautocomplete.SlideUp.SlideUp_Activity.dialogHeader_5;

public class Adv_SearchBarFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, TextWatcher {

    Routing routing = new Routing();

    String fromText, viaText, toText;

    private ImageView switch_station, fromClear, removeBypass, viaClear, addBypass, toClear;
    public EditText fromSearch, viaSearch, toSearch;
    private ExpandableLayout expandableLayout0;
    boolean fromEmpty, bothfilled;

    public static boolean via_exist;

    //TextView duration, stationNos ,transferNos, fareAmount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detailed_edit_box, container, false);

        //From View
        switch_station = (ImageView) view.findViewById(R.id.switch_station);
        fromClear = (ImageView) view.findViewById(R.id.from_clear);
        fromSearch = (EditText) view.findViewById(R.id.from_search);

        //Via View
        expandableLayout0 = (ExpandableLayout) view.findViewById(R.id.expandable_layout_0);
        removeBypass = (ImageView) view.findViewById(R.id.remove_bypass);
        viaClear = (ImageView) view.findViewById(R.id.via_clear);
        viaSearch = (EditText) view.findViewById(R.id.via_search);

        //To View
        addBypass = (ImageView) view.findViewById(R.id.add_bypass_station);
        toClear = (ImageView) view.findViewById(R.id.to_clear);
        toSearch = (EditText) view.findViewById(R.id.to_search);

        //Route Summary View
        //duration = (TextView) view.findViewById(R.id.timeReq);
        //stationNos = (TextView) view.findViewById(R.id.stations);
        //transferNos = (TextView) view.findViewById(R.id.transfer);
        //fareAmount = (TextView) view.findViewById(R.id.fare);

        //From Listeners
        switch_station.setOnClickListener(this);
        fromClear.setOnClickListener(this);
        fromSearch.setOnTouchListener(this);
        fromSearch.addTextChangedListener(this);

        //Via Listeners
        removeBypass.setOnClickListener(this);
        viaClear.setOnClickListener(this);
        viaSearch.setOnTouchListener(this);
        viaSearch.addTextChangedListener(this);

        //To Listeners
        addBypass.setOnClickListener(this);
        toClear.setOnClickListener(this);
        toSearch.setOnTouchListener(this);
        toSearch.addTextChangedListener(this);

        //listeners for expandable layout
        view.findViewById(R.id.add_bypass_station).setOnClickListener(this);
        view.findViewById(R.id.remove_bypass).setOnClickListener(this);


        return view;
    }

    public void setStatus(String statusType, String stationName){

        if (statusType.equals("from_marker")){
            fromSearch.setText(stationName);}
        if (statusType.equals("to_marker")){
            toSearch.setText(stationName);}
        if (statusType.equals("via_marker")){
            viaSearch.setText(stationName);
            if (!expandableLayout0.isExpanded()){
                addBypass.setImageResource(android.R.color.transparent);
                expandableLayout0.expand();
            }
        }
        //==========================================================================================


    }

    public void showResults(){
        if ((!toSearch.getText().toString().equals("")) && (!fromSearch.getText().toString().equals(""))){

            fromText = fromSearch.getText().toString();
            viaText = viaSearch.getText().toString();
            toText = toSearch.getText().toString();

            if (!viaText.equals("")){

                via_exist = true;

                for (int i = 0; i < 2; i++) {
                    //first loop
                    if (i==0){
                        routing.evaluate(fromText, viaText, i);
                    }
                    //second loop
                    else if (i==1){
                        routing.evaluate(viaText, toText, i);
                    }
                    routing.drawRoute(mMap, i);
                }
            }else{
                //only fromSearch and toSearch
                via_exist = false;
                routing.evaluate(fromText, toText, 0);      //routeNo = 0
                routing.drawRoute(mMap, 0);                 //higher z-index = higher priority, total_RouteNo = 0
            }

            routing.animateRoute(0);         //routeNo = 0
            routing.pulsateTransit(mMap);
            routing.getTotalStations();
            routing.getTotalTime();
            routing.getTransferFreq();
            //-----------
            Fares fares = new Fares();
            fares.getTotalFares(fromText, viaText, toText);

            //-----------
            //System.out.println("fare? lolz: RM " + fares.getTotalFares(via_exist, "fares_csv.csv", fromText, viaText, toText));

            SlideUp_Activity slideUp_activity = new SlideUp_Activity();
            slideUp_activity.initialize(via_exist, fromText, viaText, toText);


            //display route information summary
            /*
            duration.setText(routing.totalTime);
            stationNos.setText(routing.totalStations);
            transferNos.setText(routing.totalTransfers);
            fareAmount.setText(fares.fare);
            */

            dialogHeader_5.show();

            //System.out.println("Total stations lolz: " + routing.totalStations + "   ||   Total minutes: " + routing.totalTime);
            //tab2_details5.setText(String.format( "%.2f", list19/-0.01));

            //System.out.println("showResults lols: "+routing.noOfRoutes);

        }
    }


    public void setEmptyText(String statusType){
        if (statusType.equals("from")){
            fromSearch.setText("");}
        if (statusType.equals("to")){
            toSearch.setText("");}
        if (statusType.equals("via")){
            viaSearch.setText("");
            if (expandableLayout0.isExpanded()){
                addBypass.setImageResource(R.drawable.add_station);
                viaSearch.setText("");
                expandableLayout0.collapse();
            }
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP){
            //startActivity( new Intent(getActivity(), CommonTabActivity.class));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        //==========================================================================================
        //clear behaviour
        if(id == R.id.from_clear){
            fromSearch.setText("");
            ((MapsActivity)getActivity()).removeStatus(0);
            stopAnimation();
        }
        if(id == R.id.to_clear){
            toSearch.setText("");
            ((MapsActivity)getActivity()).removeStatus(1);
            stopAnimation();
        }
        if(id == R.id.via_clear){
            viaSearch.setText("");
            expandableLayout0.collapse();
            ((MapsActivity)getActivity()).removeStatus(2);
            stopAnimation();
            showResults();

        }

        //==========================================================================================
        //special behaviours
        if (id == R.id.switch_station) {
            //if both of the Editboxes are filled
            if ((!toSearch.getText().toString().equals("")) && (!fromSearch.getText().toString().equals(""))){
                String tempFromName = fromSearch.getText().toString();
                String tempToName = toSearch.getText().toString();

                fromSearch.setText(tempToName);
                toSearch.setText(tempFromName);

                bothfilled = true;

                LatLng firstPos, lastPos;
                int index_lastPos;
                //System.out.println("myRoute lols: "+routing.noOfRoutes);


                firstPos = routing.noOfRoutes.get("myRoute0").get(0);

                if (!viaSearch.getText().toString().equals("")){
                    index_lastPos = routing.noOfRoutes.get("myRoute1").size()-1;
                    lastPos =routing.noOfRoutes.get("myRoute1").get(index_lastPos);
                }else{
                    index_lastPos = routing.noOfRoutes.get("myRoute0").size()-1;
                    lastPos =routing.noOfRoutes.get("myRoute0").get(index_lastPos);
                }

                ((MapsActivity)getActivity()).swapMarker_fromFrag(fromEmpty, null, bothfilled,
                        firstPos, lastPos);

            }else{
                if (!toSearch.getText().toString().equals("")){
                    fromEmpty = true;
                    bothfilled = false;
                    ((MapsActivity)getActivity()).swapMarker_fromFrag(fromEmpty, toSearch.getText().toString(), bothfilled,
                            null, null);

                    toSearch.setText("");}
                else {
                    if (!fromSearch.getText().toString().equals("")) {
                        fromEmpty = false;
                        bothfilled = false;
                        ((MapsActivity) getActivity()).swapMarker_fromFrag(fromEmpty, fromSearch.getText().toString(), bothfilled,
                                null, null);
                        fromSearch.setText("");
                    }
                }
            }
        }

        if(id == R.id.add_bypass_station){
            if (!expandableLayout0.isExpanded()){
                addBypass.setImageResource(android.R.color.transparent);
                expandableLayout0.expand();
            }
        }

        if(id == R.id.remove_bypass){
            if (expandableLayout0.isExpanded()){
                addBypass.setImageResource(R.drawable.add_station);
                viaSearch.setText("");
                expandableLayout0.collapse();
                ((MapsActivity)getActivity()).removeStatus(2);
            }
            stopAnimation();
            showResults();
        }
        //==========================================================================================







    }

    public void stopAnimation(){
        //------------------------------------------------------------------------------------------
        //stop route animations
        if (via_exist){
            //System.out.println("TRUE lol");
            for (int i = 0; i < 2; i++) {
                MapAnimator.getInstance().stopAnimateRoute("polyline" +i);
                MapAnimator.getInstance().removeDrawRoute("polyline" +i);
            }

        }else{
            //System.out.println("FALSE lol: ");
            MapAnimator.getInstance().stopAnimateRoute("polyline0");
            MapAnimator.getInstance().removeDrawRoute("polyline0");

        }
        MapAnimator.getInstance().polyline_list.clear();
        MapAnimator.getInstance().animation_list.clear();
        MapAnimator.getInstance().animationCount = 0;
        MapAnimator.getInstance().zIndex = 1f;
        //------------------------------------------------------------------------------------------
        //stop pulsating animations
        for (int i = 0; i < routing.unique_transitPos.size(); i++) {
            PulsatingEffect.getInstance().removePulsatingEffect("transit" +i);
        }
        PulsatingEffect.getInstance().circleHashMap.clear();
        PulsatingEffect.getInstance().pulseHashMap.clear();
        //------------------------------------------------------------------------------------------
        //variables cleanup
        routing.clear_All_Routes_Variables();
        //clear route summary details
        //duration.setText("-");
        //stationNos.setText("-");
        //transferNos.setText("-");
        //fareAmount.setText("-");
    }


    //==============================================================================================
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence query, int start, int before, int count) {
        String searchText = query.toString();
        //System.out.println("searchText lol: "+searchText);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
