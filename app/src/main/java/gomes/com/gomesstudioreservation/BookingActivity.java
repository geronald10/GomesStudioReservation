package gomes.com.gomesstudioreservation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.utilities.BasicCalendar;
import gomes.com.gomesstudioreservation.utilities.FakeDataUtils;

public class BookingActivity extends AppCompatActivity {

    private final String TAG = BookingActivity.class.getSimpleName();

    public static final String[] MAIN_CITY_PROJECTION = {
            ReservationContract.CityEntry.COLUMN_CITY_ID,
            ReservationContract.CityEntry.COLUMN_CITY_NAMA
    };

    private static final int REQUEST_CODE_CALENDAR = 100;

    private ArrayList<String> cityNames;
    private ArrayList<String> studioNames;

    private ProgressBar mLoadingIndicator;
    private EditText namaBand;
    private EditText tanggalBooking;
    private String selectedDate;
    private String formattedSelectedDate;
    private Button btnSearch;
    private SearchableSpinner spStudio;
    private SearchableSpinner spCity;
    private ReservationSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        session = new ReservationSessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = getUserDetails();
        String name = user.get(ReservationContract.UserEntry.KEY_USER_NAME);
        String email = user.get(ReservationContract.UserEntry.KEY_USER_EMAIL);
        String noHP = user.get(ReservationContract.UserEntry.KEY_USER_NO_HP);

        FakeDataUtils.insertFakeCityData(this);

        namaBand = (EditText)findViewById(R.id.edt_band_name);
        tanggalBooking = (EditText)findViewById(R.id.edt_select_date);
        btnSearch = (Button)findViewById(R.id.btn_search_button);
        spCity = (SearchableSpinner)findViewById(R.id.sp_select_town);
        spStudio = (SearchableSpinner)findViewById(R.id.sp_select_studio);

        cityNames = new ArrayList<>();
        ArrayAdapter<String> mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCitySpinnerData());
        ArrayAdapter<String> mStudioAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getStudioSpinnerData());
        mCityAdapter.notifyDataSetChanged();
        mStudioAdapter.notifyDataSetChanged();
        spCity.setAdapter(mCityAdapter);
        spStudio.setAdapter(mStudioAdapter);

        tanggalBooking.setOnTouchListener(operation);
        spCity.setTitle("Select Town");
        spCity.setPositiveButton("OK");

        spStudio.setTitle("Select Studio");
        spStudio.setPositiveButton("OK");
    }

    View.OnTouchListener operation = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int id = view.getId();
            switch (id) {
                case R.id.edt_select_date:
                    Intent intent = new Intent(getApplicationContext(), BasicCalendar.class);
                    if (!tanggalBooking.getText().toString().trim().equals("")) {
                        intent.putExtra("dateToShow", tanggalBooking.getText().toString());
                    }
                    startActivityForResult(intent, REQUEST_CODE_CALENDAR);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_CALENDAR) {
            if(resultCode == Activity.RESULT_OK) {
                //parse your returned values from data intent here
                selectedDate = data.getStringExtra("dateToShow");
                formattedSelectedDate = data.getStringExtra("dateToSend");
                tanggalBooking.setText(selectedDate);
            } else {
                tanggalBooking.setHint(R.string.no_selection_label);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("namaBand", namaBand.getText().toString());
        savedInstanceState.putString("tanggalBooking", tanggalBooking.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        namaBand.setText(savedInstanceState.getString("namaBand"));
        tanggalBooking.setText(savedInstanceState.getString("tanggalBooking"));
    }

    // populate city spinner data
    public String[] getCitySpinnerData(){
        Cursor cursor = getBaseContext().getContentResolver().query(
                ReservationContract.CityEntry.CONTENT_URI,
                MAIN_CITY_PROJECTION,
                null,
                null,
                ReservationContract.CityEntry.COLUMN_CITY_NAMA
        );
        ArrayList<String> cityNames = new ArrayList<String>();
        Log.d("cityname sebelum", String.valueOf(cityNames));
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                cityNames.add(cursor.getString(cursor.getColumnIndex(ReservationContract.CityEntry.COLUMN_CITY_NAMA)));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return cityNames.toArray(new String[cityNames.size()]);
    }

    // populate studio spinner data
    public String[] getStudioSpinnerData() {
        return null;
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        Uri uri = ReservationContract.UserEntry.CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put(ReservationContract.UserEntry.KEY_USER_NAME, cursor.getString(1));
            user.put(ReservationContract.UserEntry.KEY_USER_EMAIL, cursor.getString(2));
            user.put(ReservationContract.UserEntry.KEY_USER_NO_HP, cursor.getString(3));
            user.put(ReservationContract.UserEntry.KEY_USER_PASSWORD, cursor.getString(4));
        }
        cursor.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }

    private void logoutUser() {
        session.setLogin(false);
        Uri uri = ReservationContract.UserEntry.CONTENT_URI;
        this.getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Deleted all user info from sqlite");

        // Launching the login activity
        Intent intent = new Intent(BookingActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
