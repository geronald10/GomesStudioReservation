package gomes.com.gomesstudioreservation.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

import gomes.com.gomesstudioreservation.data.ReservationContract;

/**
 * Utility functions to handle Studio Query JSON data.
 */

public class StudioJsonUtils {

    private static final String STUDIO_LIST = "list_studio";
    private static final String STUDIO_ID = "id_studio";
    private static final String STUDIO_NAMA = "nama_studio";
    private static final String STUDIO_ALAMAT = "alamat_studio";
    private static final String STUDIO_HARGA = "harga_studio";
    private static final String STUDIO_TELEPON = "telepon_studio";
//    private static final String STUDIO_OPEN_HOUR = "studio_open";
//    private static final String STUDIO_CLOSE_HOUR = "stuio_close";
    private static final String STUDIO_MESSAGE_CODE = "cod";

    public static ContentValues[] getStudioContentValuesFromJson(Context context, String studioJsonStr)
        throws JSONException {

        JSONObject studioJson = new JSONObject(studioJsonStr);
        if (studioJson.has(STUDIO_MESSAGE_CODE)) {
            int errorCode = studioJson.getInt(STUDIO_MESSAGE_CODE);
            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    return null;
                default:
                    return null;
            }
        }

        JSONArray jsonStudioArray = studioJson.getJSONArray(STUDIO_LIST);

        ContentValues[] studioContentValues = new ContentValues[jsonStudioArray.length()];

        for (int i = 0; i < jsonStudioArray.length(); i++) {

            String namaStudio;
            String alamatStudio;
            int hargaStudio;
            String teleponStudio;
//            String openHour;
//            String closeHour;
            int idStudio;

            JSONObject studio = jsonStudioArray.getJSONObject(i);
            idStudio = studio.getInt(STUDIO_ID);
            namaStudio = studio.getString(STUDIO_NAMA);
            alamatStudio = studio.getString(STUDIO_ALAMAT);
            hargaStudio = studio.getInt(STUDIO_HARGA);
            teleponStudio = studio.getString(STUDIO_TELEPON);
//            openHour = studio.getString(STUDIO_OPEN_HOUR);
//            closeHour = studio.getString(STUDIO_CLOSE_HOUR);

            ContentValues studioValues = new ContentValues();
            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_ID, idStudio);
            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_NAMA, namaStudio);
            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_ALAMAT, alamatStudio);
            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_HARGA, hargaStudio);
            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_TELEPON, teleponStudio);
//            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_OPEN_HOUR, openHour);
//            studioValues.put(ReservationContract.StudioEntry.COLUMN_STUDIO_CLOSE_HOUR, closeHour);

            studioContentValues[i] = studioValues;
        }
    return studioContentValues;
    }
}
