package code.cameo.placeautocomplete.Lines;


import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static code.cameo.placeautocomplete.Adv_SearchBarFragment.via_exist;
import static code.cameo.placeautocomplete.MapsActivity.mContext;
import static code.cameo.placeautocomplete.Routing.noOfRoutes;


public class Fares {


    private List<String> stationFares = new ArrayList<>();
    public static String fare;

    public void setStations(){
        stationFares.clear();

        String ListofStations = "Gombak,Taman Melawati,Wangsa Maju,Sri Rampai,Setiawangsa,Jelatek,Dato' Keramat,Damai," +
                "Ampang Park,KLCC,Kampung Baru,Dang Wangi,Masjid Kamek KJ,Pasar Seni,Kl Sentral KJ,Bangsar,Abdullah Hukum,Kerinchi," +
                "Universiti,Taman Jaya,Asia Jaya,Taman Paramount,Taman Bahagia,Kelana Jaya,Lembah Subang,Ara Damansara,Glenmarie," +
                "Subang Jaya,SS15,SS18,USJ7,Taipan,Wawasan,USJ21,Alam Megah,Subang Alam,Putta Heights KJ,Ampang,Cahaya,Cempaka," +
                "Pandan Indah,Pandan Jaya,Maluri,Miharja,Chan Sow Lin,Pudu,Hang Tuah,Plaza Rakyat,Masjid Jamek,Bandaraya," +
                "Sultan Ismail,PWTC,Titwangsa,Sentul,Sentul Timur,Cheras,Salak Selatan,Bandar Run Razak,Bandar Tasek Selatan," +
                "Sungai Besi,Bukit Jalil,Sri Petaling,Awan Besar,Muhibah,Alam Sutera,Kinrara BK5,KIV1,IOI Puchong," +
                "Pusat Bandar Puchong,Taman Perindustrian Puchong,Bandar Puteri,Puchong Perdana,Puchong Prima,KIV2," +
                "Putra Heights AG,KL Sentral,Tun Sambanthan,Maharajalela,Hang Tuah,Imbi,Bukit Bintang,Raja Chulan," +
                "Bukit Nanas,Medan Tuanku,Chow Kit,Titiwangsa,Sunway - Setia Jaya,Mentari,Sunway Lagoon,Sunmed," +
                "Sunu- Monash,South Quay - USJ1,USJ7 BRT,Sungai Buloh,Kg. Selamat,Kwasa Damansara,Kwasa Sentral,Kota Damansara," +
                "Surian,Mutiara Damansara,Bandar Utama,Taman Tun Dr. Ismail,Phileo Damansara,Pusat Bandar Damansara,Semantan," +
                "Muzium Negara,Pasar Seni,Merdeka,Bukit Bintang,Tun Razak Exchange,Cochrane,Maluri,Taman Permata,Taman Midah," +
                "Taman Mutiara,Taman Connaught,Taman Suntex,Sri Raya,Bandar Tun Hussein Onn,Batu Sebelas Cheras,Bukit Dukung," +
                "Sungai Jernih,Stadium Kajang,Kajang";

        stationFares = Arrays.asList(ListofStations.split(","));


    }

    public void evaluate(String fileName, String fromText, String toText){
        //-----------------------------------------------------------------------------------------
        //read from CSV
        InputStreamReader is = null;
        try {
            is = new InputStreamReader(mContext.getAssets()
                    .open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(is);

        String line = "";

        List<String[]> lines = new ArrayList<String[]>();

        try {
            while ((line = reader.readLine()) != null) {
                lines.add(line.split(";"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // convert our list to a String array.
        String[][] array = new String[lines.size()][0];
        lines.toArray(array);
        //-----------------------------------------------------------------------------------------
        String fromText_row = array[stationFares.indexOf(fromText)][0];
        List<String> rowList = Arrays.asList(fromText_row.split(","));

        int toText_column = stationFares.indexOf(toText);
        fare = rowList.get(toText_column);

        //System.out.println("fare lolz: RM " + fare);
        //System.out.println("fromText_row lolz: " + fromText_row);
        //System.out.println("toText column lolz: " + toText_column);

    }

    public void getTotalFares(String fromText, String viaText, String toText){

        setStations();



        if (via_exist) {

            //check whether u-turn exist
            int oneIndex_b4_last = noOfRoutes.get("myRoute0").size()-2;
            LatLng position_b4_last = noOfRoutes.get("myRoute0").get(oneIndex_b4_last);
            LatLng second_position_start = noOfRoutes.get("myRoute1").get(1);

            //if u-turn exist, add route0 and route1 fare (because exit station to switch station is needed)
            if (position_b4_last == second_position_start){
                evaluate("fares_csv.csv", fromText, viaText);
                Double route0_fare = Double.parseDouble(fare);
                evaluate("fares_csv.csv", viaText, toText);
                Double route1_fare = Double.parseDouble(fare);
                fare = String.format("%.2f", route0_fare + route1_fare);
            }
            else{
                evaluate("fares_csv.csv", fromText, toText);
            }
            //System.out.println("double lolz : " + fare);
        }else{
            evaluate("fares_csv.csv", fromText, toText);
        }



    }


}
