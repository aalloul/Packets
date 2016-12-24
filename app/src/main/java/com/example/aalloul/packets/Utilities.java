package com.example.aalloul.packets;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by aalloul on 02/07/16.
 */
final class Utilities {
    static HashMap<String, String> countries = new HashMap<>();
    final static String LOG_TAG = "Utilities";

    public Bitmap getImage(byte[] theimage){
        byte[] imgByte = theimage;
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    // Maps country name to its code
    static String CountryToCountryCode(String country){
        Log.i(LOG_TAG, "CountryToCountryCode - Start");
        countries.put("Afghanistan","AF");
        countries.put("Åland Islands","AX");
        countries.put("Albania","AL");
        countries.put("Algeria","DZ");
        countries.put("American Samoa","AS");
        countries.put("Andorra","AD");
        countries.put("Angola","AO");
        countries.put("Anguilla","AI");
        countries.put("Antarctica","AQ");
        countries.put("Antigua and Barbuda","AG");
        countries.put("Argentina","AR");
        countries.put("Armenia","AM");
        countries.put("Aruba","AW");
        countries.put("Australia","AU");
        countries.put("Austria","AT");
        countries.put("Azerbaijan","AZ");
        countries.put("Bahamas","BS");
        countries.put("Bahrain","BH");
        countries.put("Bangladesh","BD");
        countries.put("Barbados","BB");
        countries.put("Belarus","BY");
        countries.put("Belgium","BE");
        countries.put("Belize","BZ");
        countries.put("Benin","BJ");
        countries.put("Bermuda","BM");
        countries.put("Bhutan","BT");
        countries.put("Bolivia (Plurinational State of)","BO");
        countries.put("Bonaire, Sint Eustatius and Saba","BQ");
        countries.put("Bosnia and Herzegovina","BA");
        countries.put("Botswana","BW");
        countries.put("Bouvet Island","BV");
        countries.put("Brazil","BR");
        countries.put("British Indian Ocean Territory","IO");
        countries.put("Brunei Darussalam","BN");
        countries.put("Bulgaria","BG");
        countries.put("Burkina Faso","BF");
        countries.put("Burundi","BI");
        countries.put("Cabo Verde","CV");
        countries.put("Cambodia","KH");
        countries.put("Cameroon","CM");
        countries.put("Canada","CA");
        countries.put("Cayman Islands","KY");
        countries.put("Central African Republic","CF");
        countries.put("Chad","TD");
        countries.put("Chile","CL");
        countries.put("China","CN");
        countries.put("Christmas Island","CX");
        countries.put("Cocos (Keeling) Islands","CC");
        countries.put("Colombia","CO");
        countries.put("Comoros","KM");
        countries.put("Congo","CG");
        countries.put("Congo (Democratic Republic of the)","CD");
        countries.put("Cook Islands","CK");
        countries.put("Costa Rica","CR");
        countries.put("Côte d'Ivoire","CI");
        countries.put("Croatia","HR");
        countries.put("Cuba","CU");
        countries.put("Curaçao","CW");
        countries.put("Cyprus","CY");
        countries.put("Czechia","CZ");
        countries.put("Denmark","DK");
        countries.put("Djibouti","DJ");
        countries.put("Dominica","DM");
        countries.put("Dominican Republic","DO");
        countries.put("Ecuador","EC");
        countries.put("Egypt","EG");
        countries.put("El Salvador","SV");
        countries.put("Equatorial Guinea","GQ");
        countries.put("Eritrea","ER");
        countries.put("Estonia","EE");
        countries.put("Ethiopia","ET");
        countries.put("Falkland Islands (Malvinas)","FK");
        countries.put("Faroe Islands","FO");
        countries.put("Fiji","FJ");
        countries.put("Finland","FI");
        countries.put("France","FR");
        countries.put("French Guiana","GF");
        countries.put("French Polynesia","PF");
        countries.put("French Southern Territories","TF");
        countries.put("Gabon","GA");
        countries.put("Gambia","GM");
        countries.put("Georgia","GE");
        countries.put("Germany","DE");
        countries.put("Ghana","GH");
        countries.put("Gibraltar","GI");
        countries.put("Greece","GR");
        countries.put("Greenland","GL");
        countries.put("Grenada","GD");
        countries.put("Guadeloupe","GP");
        countries.put("Guam","GU");
        countries.put("Guatemala","GT");
        countries.put("Guernsey","GG");
        countries.put("Guinea","GN");
        countries.put("Guinea-Bissau","GW");
        countries.put("Guyana","GY");
        countries.put("Haiti","HT");
        countries.put("Heard Island and McDonald Islands","HM");
        countries.put("Holy See","VA");
        countries.put("Honduras","HN");
        countries.put("Hong Kong","HK");
        countries.put("Hungary","HU");
        countries.put("Iceland","IS");
        countries.put("India","IN");
        countries.put("Indonesia","ID");
        countries.put("Iran (Islamic Republic of)","IR");
        countries.put("Iraq","IQ");
        countries.put("Ireland","IE");
        countries.put("Isle of Man","IM");
        countries.put("Israel","IL");
        countries.put("Italy","IT");
        countries.put("Jamaica","JM");
        countries.put("Japan","JP");
        countries.put("Jersey","JE");
        countries.put("Jordan","JO");
        countries.put("Kazakhstan","KZ");
        countries.put("Kenya","KE");
        countries.put("Kiribati","KI");
        countries.put("Korea (Democratic People's Republic of)","KP");
        countries.put("Korea (Republic of)","KR");
        countries.put("Kuwait","KW");
        countries.put("Kyrgyzstan","KG");
        countries.put("Lao People's Democratic Republic","LA");
        countries.put("Latvia","LV");
        countries.put("Lebanon","LB");
        countries.put("Lesotho","LS");
        countries.put("Liberia","LR");
        countries.put("Libya","LY");
        countries.put("Liechtenstein","LI");
        countries.put("Lithuania","LT");
        countries.put("Luxembourg","LU");
        countries.put("Macao","MO");
        countries.put("Macedonia (the former Yugoslav Republic of)","MK");
        countries.put("Madagascar","MG");
        countries.put("Malawi","MW");
        countries.put("Malaysia","MY");
        countries.put("Maldives","MV");
        countries.put("Mali","ML");
        countries.put("Malta","MT");
        countries.put("Marshall Islands","MH");
        countries.put("Martinique","MQ");
        countries.put("Mauritania","MR");
        countries.put("Mauritius","MU");
        countries.put("Mayotte","YT");
        countries.put("Mexico","MX");
        countries.put("Micronesia (Federated States of)","FM");
        countries.put("Moldova (Republic of)","MD");
        countries.put("Monaco","MC");
        countries.put("Mongolia","MN");
        countries.put("Montenegro","ME");
        countries.put("Montserrat","MS");
        countries.put("Morocco","MA");
        countries.put("Mozambique","MZ");
        countries.put("Myanmar","MM");
        countries.put("Namibia","NA");
        countries.put("Nauru","NR");
        countries.put("Nepal","NP");
        countries.put("Netherlands","NL");
        countries.put("New Caledonia","NC");
        countries.put("New Zealand","NZ");
        countries.put("Nicaragua","NI");
        countries.put("Niger","NE");
        countries.put("Nigeria","NG");
        countries.put("Niue","NU");
        countries.put("Norfolk Island","NF");
        countries.put("Northern Mariana Islands","MP");
        countries.put("Norway","NO");
        countries.put("Oman","OM");
        countries.put("Pakistan","PK");
        countries.put("Palau","PW");
        countries.put("Palestine, State of","PS");
        countries.put("Panama","PA");
        countries.put("Papua New Guinea","PG");
        countries.put("Paraguay","PY");
        countries.put("Peru","PE");
        countries.put("Philippines","PH");
        countries.put("Pitcairn","PN");
        countries.put("Poland","PL");
        countries.put("Portugal","PT");
        countries.put("Puerto Rico","PR");
        countries.put("Qatar","QA");
        countries.put("Réunion","RE");
        countries.put("Romania","RO");
        countries.put("Russian Federation","RU");
        countries.put("Rwanda","RW");
        countries.put("Saint Barthélemy","BL");
        countries.put("Saint Helena, Ascension and Tristan da Cunha","SH");
        countries.put("Saint Kitts and Nevis","KN");
        countries.put("Saint Lucia","LC");
        countries.put("Saint Martin (French part)","MF");
        countries.put("Saint Pierre and Miquelon","PM");
        countries.put("Saint Vincent and the Grenadines","VC");
        countries.put("Samoa","WS");
        countries.put("San Marino","SM");
        countries.put("Sao Tome and Principe","ST");
        countries.put("Saudi Arabia","SA");
        countries.put("Senegal","SN");
        countries.put("Serbia","RS");
        countries.put("Seychelles","SC");
        countries.put("Sierra Leone","SL");
        countries.put("Singapore","SG");
        countries.put("Sint Maarten (Dutch part)","SX");
        countries.put("Slovakia","SK");
        countries.put("Slovenia","SI");
        countries.put("Solomon Islands","SB");
        countries.put("Somalia","SO");
        countries.put("South Africa","ZA");
        countries.put("South Georgia and the South Sandwich Islands","GS");
        countries.put("South Sudan","SS");
        countries.put("Spain","ES");
        countries.put("Sri Lanka","LK");
        countries.put("Sudan","SD");
        countries.put("Suriname","SR");
        countries.put("Svalbard and Jan Mayen","SJ");
        countries.put("Swaziland","SZ");
        countries.put("Sweden","SE");
        countries.put("Switzerland","CH");
        countries.put("Syrian Arab Republic","SY");
        countries.put("Taiwan","TW");
        countries.put("Tajikistan","TJ");
        countries.put("Tanzania","TZ");
        countries.put("Thailand","TH");
        countries.put("Timor-Leste","TL");
        countries.put("Togo","TG");
        countries.put("Tokelau","TK");
        countries.put("Tonga","TO");
        countries.put("Trinidad and Tobago","TT");
        countries.put("Tunisia","TN");
        countries.put("Turkey","TR");
        countries.put("Turkmenistan","TM");
        countries.put("Turks and Caicos Islands","TC");
        countries.put("Tuvalu","TV");
        countries.put("Uganda","UG");
        countries.put("Ukraine","UA");
        countries.put("United Arab Emirates","AE");
        countries.put("United Kingdom","GB");
        countries.put("United States of America","US");
        countries.put("United States","UM");
        countries.put("Uruguay","UY");
        countries.put("Uzbekistan","UZ");
        countries.put("Vanuatu","VU");
        countries.put("Venezuela (Bolivarian Republic of)","VE");
        countries.put("Viet Nam","VN");
        countries.put("Virgin Islands (British)","VG");
        countries.put("Virgin Islands (U.S.)","VI");
        countries.put("Wallis and Futuna","WF");
        countries.put("Western Sahara","EH");
        countries.put("Yemen","YE");
        countries.put("Zambia","ZM");
        countries.put("Zimbabwe","ZW");
        Log.d(LOG_TAG, "CountryToCountryCode - country = "+country);
        if (countries.containsKey(country)) {
            Log.d(LOG_TAG,"CountryToCountryCode - Found in key");
            return countries.get(country);
        } else {
            Log.d(LOG_TAG,"CountryToCountryCode - Not found in key");
            return country;
        }

    }

    // Epoch to date
    static String Epoch2DateStringSeconds(long epochSeconds, String formatString) {
        Date updatedate = new Date(epochSeconds * 1000);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }
    static String Epoch2DateStringSeconds(String epochSeconds, String formatString) {
        Long epochSeconds2 = Long.parseLong(epochSeconds);
        Date updatedate = new Date(epochSeconds2 * 1000);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }
    static String Epoch2DateStringMillis(long epochSeconds, String formatString) {
        Date updatedate = new Date(epochSeconds);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }
    static String Epoch2DateStringMillis(String epochSeconds, String formatString) {
        Long epochSeconds2 = Long.parseLong(epochSeconds);
        Date updatedate = new Date(epochSeconds2);
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        return format.format(updatedate);
    }

    static String getTomorrow(String format) {
        return Epoch2DateStringMillis(CurrentTimeMS() + 24*3600*1000, format);
    }

    static String DateToDate(String date, String inputFormat,String outputFormat) {
        SimpleDateFormat input = new SimpleDateFormat(inputFormat);
        SimpleDateFormat output = new SimpleDateFormat(outputFormat);
        String resu = "";
        try {
            resu = output.format(input.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resu;
    }

    // Gets the current time in ms
    static long CurrentTimeMS() {
        return System.currentTimeMillis();
    }

    // Hide the keyboard
    static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // The snackbar
    static void makeThesnack(View theview, String the_message, String action_message) {
        final Snackbar snackbar = Snackbar.make(theview, the_message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action_message, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    // BitMap to String
    static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    // String to BitMap
    static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

}
