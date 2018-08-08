package code.cameo.placeautocomplete;

import com.google.android.gms.maps.model.LatLng;

public class StatusIconTag {


    private String optionType;
    private String stationName;
    private LatLng position;

    public StatusIconTag() {

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

}