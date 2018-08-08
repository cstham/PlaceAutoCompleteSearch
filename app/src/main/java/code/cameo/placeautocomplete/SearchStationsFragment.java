package code.cameo.placeautocomplete;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import code.cameo.placeautocomplete.Lines.L9_MRT_SgBuloh_Kajang;


@SuppressLint("ValidFragment")
public class SearchStationsFragment extends Fragment {

    private ListView lv;
    private ArrayList<String> stations_mainText = new ArrayList<String>();
    private ArrayList<String> stations_subText = new ArrayList<String>();

    L9_MRT_SgBuloh_Kajang mrt_sgBuloh_kajang = new L9_MRT_SgBuloh_Kajang();

    private String mTitle;

    public static SearchStationsFragment getInstance(String title) {
        SearchStationsFragment sf = new SearchStationsFragment();
        sf.mTitle = title;
        return sf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.search_stations_listview, container, false);

        lv = (ListView) v.findViewById(R.id.contacts_main_list);

        //to prevent duplicates due to arraylist constantly adding everytime this page is displayed
        stations_mainText.clear();
        stations_subText.clear();

        //to initialize stations data
        mrt_sgBuloh_kajang.setStations();

        //To add station data

        for (int i = 0; i <  mrt_sgBuloh_kajang.stationName.size(); i++) {
            String name = mrt_sgBuloh_kajang.stationName.get(i);
            LatLng position = mrt_sgBuloh_kajang.stationPosition.get(i);
            stations_mainText.add(name);
            stations_subText.add(position.toString());
        }
      
        lv.setAdapter(new UsersAdapter(getContext(), stations_subText, stations_mainText));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String  selectedStation    = (String) lv.getItemAtPosition(position);

                int stationIndex = mrt_sgBuloh_kajang.stationName.indexOf(selectedStation);
                LatLng selectedPosition = mrt_sgBuloh_kajang.stationPosition.get(stationIndex);

                Gson gson = new GsonBuilder().create();
                String selectedPosition_String = gson.toJson(selectedPosition);      // serialization

                Intent intent = getActivity().getIntent().putExtra("position", selectedPosition_String);;
                getActivity().setResult(MapsActivity.RESULT_OK, intent);
                getActivity().finish();


            }
        });

        //to use listview as the data for the viewpager to enable coordinatorlayout to hide searchbar
        lv.setNestedScrollingEnabled(true);






        return v;
    }
}


//to remove listview divider
//int[] colors = { Color.parseColor("#D3D3D3"), Color.parseColor("#D3D3D3"), Color.parseColor("#D3D3D3") };
//lv.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors))

//TextView stations_subtext = (TextView) view.findViewById(R.id.textView2);
//((CommonTabActivity)getActivity()).onPlaceSelected(stations_subtext.getText().toString());
//String  itemValue    = (String) lv.getItemAtPosition(position);
//Toast.makeText(getContext(), "You Clicked on: " + position + "||  Subtext: " + stations_subtext.getText(), Toast.LENGTH_SHORT).show();