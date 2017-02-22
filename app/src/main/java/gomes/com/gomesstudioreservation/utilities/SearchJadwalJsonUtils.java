package gomes.com.gomesstudioreservation.utilities;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchJadwalJsonUtils {

    private static final String JADWAL_LIST = "list_jadwal";
    private static final String TANGGAL = "tanggal";
    private static final String ROOM_LIST = "room";
    private static final String ROOM_ID = "room_id";
    private static final String JADWAL = "jadwal";
    private static final String JADWAL_ID = "jadwal_id";
    private static final String JADWAL_START = "jadwal_start";
    private static final String JADWAL_END = "jadwal_end";
    private static final String HARGA = "harga";
    private static final String CODE = "code";
    private static final String STATUS = "status";

    public static List<ContentValues> getJadwalContentValuesFromJson(String jadwalJsonResponse)
            throws JSONException {

        String tanggal, roomId, jadwalId, jadwalStart, jadwalEnd, code, status, harga;
        List<ContentValues> jadwalContentValues = new ArrayList<ContentValues>();

        Log.d("test jadwal volley", "masuk sini");
        JSONObject jadwalJsonObject = new JSONObject(jadwalJsonResponse);
        Log.d("test jadwal volley", String.valueOf(jadwalJsonObject.length()));

        if (jadwalJsonObject.length() > 0) {

            JSONArray listJadwalJsonArray = jadwalJsonObject.getJSONArray(JADWAL_LIST);
            Log.d("length listJadwal", String.valueOf(listJadwalJsonArray.length()));
            for (int i = 0; i < listJadwalJsonArray.length(); i++) {
                JSONObject tanggalRuangJsonObject = listJadwalJsonArray.getJSONObject(i);
                tanggal = tanggalRuangJsonObject.getString(TANGGAL);
                code = tanggalRuangJsonObject.getString(CODE);
                status = tanggalRuangJsonObject.getString(STATUS);
                JSONArray listRuangJsonArray = tanggalRuangJsonObject.getJSONArray(ROOM_LIST);

                Log.d("length list Ruang", String.valueOf(listRuangJsonArray.length()));
                for (int j = 0; j < listRuangJsonArray.length(); j++) {
                    JSONObject roomJadwalJsonObject = listRuangJsonArray.getJSONObject(j);
                    roomId = roomJadwalJsonObject.getString(ROOM_ID);
                    harga = roomJadwalJsonObject.getString(HARGA);
                    JSONArray jadwalJsonArray = roomJadwalJsonObject.getJSONArray(JADWAL);

                    Log.d("length jadwal", String.valueOf(jadwalJsonArray.length()));
                    for (int k = 0; k < jadwalJsonArray.length(); k++) {
                        JSONObject detailJadwalJsonObject = jadwalJsonArray.getJSONObject(k);
                        jadwalId = detailJadwalJsonObject.getString(JADWAL_ID);
                        jadwalStart = detailJadwalJsonObject.getString(JADWAL_START);
                        jadwalEnd = detailJadwalJsonObject.getString(JADWAL_END);

                        ContentValues jadwalValues = new ContentValues();
                        jadwalValues.put(TANGGAL, tanggal);
                        jadwalValues.put(CODE, code);
                        jadwalValues.put(STATUS, status);
                        jadwalValues.put(ROOM_ID, roomId);
                        jadwalValues.put(JADWAL_ID, jadwalId);
                        jadwalValues.put(JADWAL_START, jadwalStart);
                        jadwalValues.put(JADWAL_END, jadwalEnd);
                        jadwalValues.put(HARGA, harga);

                        jadwalContentValues.add(jadwalValues);
                    }
                }
            }
            return jadwalContentValues;
        } else {
            return null;
        }
    }
}
