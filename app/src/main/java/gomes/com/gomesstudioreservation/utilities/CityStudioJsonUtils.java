package gomes.com.gomesstudioreservation.utilities;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gomes.com.gomesstudioreservation.data.ReservationContract;

/**
 * Utility functions to handle Studio Query JSON data.
 */
public class CityStudioJsonUtils {

    private static final String CITY_LIST = "city";
    private static final String CITY_ID = "city_id";
    private static final String CITY_NAMA = "city_name";

    private static final String STUDIO_LIST = "studio";
    private static final String STUDIO_ID = "studio_id";
    private static final String STUDIO_NAMA = "studio_nama";
    private static final String STUDIO_ALAMAT = "studio_alamat";
    private static final String STUDIO_TELEPON = "studio_telepon";
    private static final String STUDIO_OPEN_HOUR = "studio_open_hour";
    private static final String STUDIO_CLOSE_HOUR = "studio_close_hour";

    public static ContentValues[] getCityContentValuesFromJson(String cityStudioJsonResponse)
            throws JSONException {
        JSONObject cityStudioJsonObject = new JSONObject(cityStudioJsonResponse);

        Log.d("test city volley", "masuk sini");
        Log.d("test city volley", String.valueOf(cityStudioJsonObject.length()));
        if (cityStudioJsonObject.length() > 0) {

            JSONArray jsonCityArray = cityStudioJsonObject.getJSONArray(CITY_LIST);
            ContentValues[] cityContentValues = new ContentValues[jsonCityArray.length()];

            for (int i = 0; i < jsonCityArray.length(); i++) {
                String idCity;
                String namaCity;

                JSONObject city = jsonCityArray.getJSONObject(i);
                idCity = city.getString(CITY_ID);
                namaCity = city.getString(CITY_NAMA);

                ContentValues cityValues = new ContentValues();
                cityValues.put(ReservationContract.CityEntry.COLUMN_CITY_ID, idCity);
                cityValues.put(ReservationContract.CityEntry.COLUMN_CITY_NAMA, namaCity);

                cityContentValues[i] = cityValues;
            }
            return cityContentValues;
        } else {
            return null;
        }
    }

    public static ContentValues[] getStudioContentValuesFromJson(String cityStudioJsonResponse)
            throws JSONException {
        JSONObject cityStudioJsonObject = new JSONObject(cityStudioJsonResponse);

        Log.d("test studio volley", "masuk sini");
        Log.d("test studio volley", String.valueOf(cityStudioJsonObject.length()));
        if (cityStudioJsonObject.length() > 0) {
            JSONArray jsonStudioArray = cityStudioJsonObject.getJSONArray(STUDIO_LIST);
            ContentValues[] studioContentValues = new ContentValues[jsonStudioArray.length()];

            for (int i = 0; i < jsonStudioArray.length(); i++) {
                String idStudio;
                String namaStudio;
                String alamatStudio;
                String teleponStudio;
                String openHour;
                String closeHour;
                String fkIdCity;

                JSONObject studio = jsonStudioArray.getJSONObject(i);
                idStudio = studio.getString(STUDIO_ID);
                fkIdCity = studio.getString(CITY_ID);
                namaStudio = studio.getString(STUDIO_NAMA);
                alamatStudio = studio.getString(STUDIO_ALAMAT);
                teleponStudio = studio.getString(STUDIO_TELEPON);
                openHour = studio.getString(STUDIO_OPEN_HOUR);
                closeHour = studio.getString(STUDIO_CLOSE_HOUR);

                ContentValues studioValues = new ContentValues();
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_ID, idStudio);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_NAMA, namaStudio);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_ALAMAT, alamatStudio);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_TELEPON, teleponStudio);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_OPEN, openHour);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_CLOSE, closeHour);
                studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_FK_CITY_ID, fkIdCity);

                studioContentValues[i] = studioValues;
            }
            return studioContentValues;
        } else {
            return null;
        }
    }
}