package gomes.com.gomesstudioreservation.utilities;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import gomes.com.gomesstudioreservation.data.ReservationContract;

public class FakeDataUtils {

    private static int [] cityID = {1,2,3,4};
    private static String [] cityName = {"Surabaya", "Sidoarjo", "Malang", "Jakarta"};

    private static ContentValues createTestCityContentValues(int id) {
        ContentValues testCityValues = new ContentValues();
        testCityValues.put(ReservationContract.CityEntry.COLUMN_CITY_ID, cityID[id]);
        testCityValues.put(ReservationContract.CityEntry.COLUMN_CITY_NAMA, cityName[id]);
        return testCityValues;
    }

    public static void insertFakeCityData(Context context)  {
        List<ContentValues> fakeValues = new ArrayList<ContentValues>();
        // Fake City data
        for(int i=0; i<4; i++) {
            fakeValues.add(FakeDataUtils.createTestCityContentValues(i));
        }
        // Bulk Insert our new city data into reservation DB
        context.getContentResolver().bulkInsert(
                ReservationContract.CityEntry.CONTENT_URI,
                fakeValues.toArray(new ContentValues[4]));
//        Log.d("jumlah data masuk", String.valueOf(fakeValues.size()));
    }
}
