package code.cameo.placeautocomplete;

import com.google.android.gms.maps.model.LatLng;

public class StationIconTag {


    private String type;
    private String stationName;
    private LatLng position;
    private boolean transferStation;

    public StationIconTag() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public boolean isTransferStation() {
        return transferStation;
    }

    public void setTransferStation(boolean transferStation) {
        this.transferStation = transferStation;
    }
}