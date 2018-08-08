package code.cameo.placeautocomplete;

import com.google.android.gms.maps.model.LatLng;

public class OptionsIconTag {


    private String optionType;
    private String stationName;
    private LatLng position;
    private boolean visibility;

    public OptionsIconTag() {

    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

}