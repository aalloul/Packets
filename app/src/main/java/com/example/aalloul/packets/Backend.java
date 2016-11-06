package com.example.aalloul.packets;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aalloul on 31/07/16.
 */
public class Backend {
    // TODO implement the logic to call the backend

    // URL to make calls to the backend
    private final String url;
    // key for authentication
    private final String key;
    // Logging
    private final String LOG_TAG = "Backend";

    public Backend() {
        Log.i(LOG_TAG, "Constructor Start");
        url = "www.example.com";
        key = this.getAuthKey();

        Log.i(LOG_TAG, "Constructor End");
    }

    private String getAuthKey(){
        // Call the Database and retrieve the auth key
        String tmp = "123";
        return key;
    }

    // This method builds the query to be sent to the API
    private String queryBuilder(Map<String, String> queryParameters) {
        String mystr = "";
        return mystr;
    }

    // Method to request the update from the backend
    protected String requestUpdate(HashMap<String, String> queryParameters) {

        // TODO when requesting update, the back-end must check for neighbouring cities + wider date ranges if needed
        // TODO needs to check for connectivity and throw an exception if no internet

        Long t_end = System.currentTimeMillis() + 24*3600*1000;
        Long t_start = System.currentTimeMillis();

        String mystr = "[{"+
                "\"name\": \"Alloul\","+
                "\"firstname\": \"Adam\","+
                "\"city\": \"Nieuw-Vennep\","+
                "\"street\": \"Kerkstraat\","+
                "\"country\": \"Netherlands\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Amsterdam\","+
                "\"source_country\": \"Netherlands\","+
                "\"source_street\": \"\","+
                "\"destination_city\": \"Paris\","+
                "\"destination_country\": \"France\","+
                "\"destination_street\": \"\","+
                "\"transport_comment\": \"Paris\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Car\""+
        "},";
        mystr += "{"+
                "\"name\": \"Ottersbach\","+
                "\"firstname\": \"John\","+
                "\"city\": \"Utrecht\","+
                "\"street\": \"Gastronomicstraat\","+
                "\"country\": \"Netherlands\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Utrecht\","+
                "\"source_country\": \"Netherlands\","+
                "\"source_street\": \"Gastronomicstraat\","+
                "\"destination_city\": \"Dusseldorf\","+
                "\"destination_country\": \"Germany\","+
                "\"destination_street\": \"my street\","+
                "\"transport_comment\": \"With my wonderful Alfa Romeo\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Car\""+
                "},";

        mystr += "{"+
                "\"name\": \"Sanchez\","+
                "\"firstname\": \"Xitzel\","+
                "\"city\": \"Utrecht\","+
                "\"street\": \"Gastronomicstraat\","+
                "\"country\": \"Netherlands\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Utrecht\","+
                "\"source_country\": \"Netherlands\","+
                "\"source_street\": \"Gastronomicstraat\","+
                "\"destination_city\": \"Mexico\","+
                "\"destination_country\": \"Mexico\","+
                "\"destination_street\": \"avenida de los nachos\","+
                "\"transport_comment\": \"Flying to Mexico, no drugs!\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Plane\""+
                "},";

        mystr += "{"+
                "\"name\": \"Parchirie\","+
                "\"firstname\": \"Miruna\","+
                "\"city\": \"Nieuw Vennep\","+
                "\"street\": \"Lovestraat\","+
                "\"country\": \"Netherlands\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Nieuw Vennep\","+
                "\"source_country\": \"Netherlands\","+
                "\"source_street\": \"Lovestraat\","+
                "\"destination_city\": \"Sighisoara\","+
                "\"destination_country\": \"Romania\","+
                "\"destination_street\": \"Str. Ana Ipatescu\","+
                "\"transport_comment\": \"Flying to Sighisoara, it's not a village!\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Plane\""+
                "},";

        mystr += "{"+
                "\"name\": \"Alloul\","+
                "\"firstname\": \"Gluino\","+
                "\"city\": \"Nieuw Vennep\","+
                "\"street\": \"Foodstraat\","+
                "\"country\": \"Netherlands\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Nieuw Vennep\","+
                "\"source_country\": \"Netherlands\","+
                "\"source_street\": \"somewhere\","+
                "\"destination_city\": \"Strasbourg\","+
                "\"destination_country\": \"France\","+
                "\"destination_street\": \"Avenue de la Foret Noire\","+
                "\"transport_comment\": \"As long as you give me enough Pate, I can take it!\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Train\""+
                "},";


        mystr += "{"+
                "\"name\": \"Alloul\","+
                "\"firstname\": \"Naima\","+
                "\"city\": \"Villepinte\","+
                "\"street\": \"Rue Des Iris\","+
                "\"country\": \"France\","+
                "\"phone_number\": \"0628363477\","+
                "\"user_rating\": 5,"+
                "\"source_city\": \"Villepinte\","+
                "\"source_country\": \"France\","+
                "\"source_street\": \"Rue des Iris\","+
                "\"destination_city\": \"Algiers\","+
                "\"destination_country\": \"Algeria\","+
                "\"destination_street\": \"rue des martyrs\","+
                "\"transport_comment\": \"I request 10 euros per package, please I need money!\","+
                "\"packet_size\": \"small\","+
                "\"number_packages\": 1,"+
                "\"packet_take_by_date\": 1471268191728,"+
                "\"packet_deliver_by_date\": 1471354591728,"+
                "\"package_transport_method\": \"Plane\""+
                "}";

        // End of mystr
        mystr += "]";

        return mystr;
    }

    protected String requestUpdate(long start_time, long end_time, String sourceCity, String sourceCountry,
                                   String destCity, String destCountry) {
        String resu = "";
        return resu;
    }

}
