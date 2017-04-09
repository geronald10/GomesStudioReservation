package gomes.com.gomesstudioreservation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.utilities.BasicCalendar;
import gomes.com.gomesstudioreservation.utilities.CityStudioJsonUtils;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;

public class BookingActivity extends BaseActivity {

    private final String TAG = BookingActivity.class.getSimpleName();

    public static final String[] MAIN_CITY_PROJECTION = {
            ReservationContract.CityEntry.COLUMN_CITY_ID,
            ReservationContract.CityEntry.COLUMN_CITY_NAMA
    };

    public static final String[] MAIN_STUDIO_PROJECTION = {
            ReservationContract.StudioEntry.COLUMN_STUDIO_ID,
            ReservationContract.StudioEntry.COLUMN_STUDIO_FK_CITY_ID,
            ReservationContract.StudioEntry.COLUMN_STUDIO_NAMA
    };

    private static final int REQUEST_CODE_CALENDAR = 200;

    private Context mContext;

    private HashMap<String, String> cityNames;
    private HashMap<String, String> studioNames;
    private ArrayAdapter<String> mCityAdapter;
    private ArrayAdapter<String> mStudioAdapter;

    private ProgressDialog progressDialog;
    private EditText namaBand;
    private EditText tanggalBooking;
    private String studioKey;
    private String formattedSelectedDate;
    private SearchableSpinner spStudio;
    private SearchableSpinner spCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        mContext = this;

        session.checkLogin();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        getCityStudioData(mContext);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        namaBand = (EditText) findViewById(R.id.edt_band_name);
        tanggalBooking = (EditText) findViewById(R.id.edt_select_date);
        Button btnSearch = (Button) findViewById(R.id.btn_search_button);
        spCity = (SearchableSpinner) findViewById(R.id.sp_select_town);
        spStudio = (SearchableSpinner) findViewById(R.id.sp_select_studio);
        spStudio.setEnabled(false);

        spCity.setTitle("Select Town");
        spCity.setPositiveButton("OK");

        spStudio.setTitle("Select Studio");
        spStudio.setPositiveButton("OK");

        spCity.setOnItemSelectedListener(selection);
        spStudio.setOnItemSelectedListener(selection);
        tanggalBooking.setOnTouchListener(operation);
        btnSearch.setOnClickListener(todo);
    }

    View.OnClickListener todo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_search_button:
                    if (namaBand.getText() != null && spStudio.getSelectedItem() != null && spCity != null && tanggalBooking.getText() != null) {
                        Intent intent = new Intent(mContext, ManageBookingActivity.class);
                        intent.putExtra("selected_date", formattedSelectedDate);
                        intent.putExtra("selected_studio", studioKey);
                        intent.putExtra("nama_studio", (String) spStudio.getSelectedItem());
                        intent.putExtra("nama_band", namaBand.getText().toString());
                        startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "All fields must be filled", Toast.LENGTH_SHORT).show();
                        break;
                    }
            }
        }
    };

    AdapterView.OnItemSelectedListener selection = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            switch (adapterView.getId()) {
                case R.id.sp_select_town:
                    String cityKey;
                    String cityValue = (String) spCity.getSelectedItem();
                    for (Map.Entry entry : cityNames.entrySet()) {
                        if (cityValue.equals(entry.getValue())) {
                            cityKey = (String) entry.getKey();
                            mStudioAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, getStudioSpinnerData(cityKey));
                            mStudioAdapter.notifyDataSetChanged();
                            spStudio.setAdapter(mStudioAdapter);
                            spStudio.setEnabled(true);
                            if (mStudioAdapter.getCount() == 0) {
                                spStudio.setEnabled(false);
                                Toast.makeText(mContext, "Studio not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    break;
                case R.id.sp_select_studio:
                    String[] key;
                    String studioValue = (String) spStudio.getSelectedItem();
                    for (Map.Entry entry : studioNames.entrySet()) {
                        if (studioValue.equals(entry.getValue())) {
                            key = entry.getKey().toString().split("\\.");
                            studioKey = key[1];
                            Log.d("studio key selected", studioKey);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

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
        if (requestCode == REQUEST_CODE_CALENDAR) {
            if (resultCode == Activity.RESULT_OK) {
                //parse your returned values from data intent here
                String selectedDate = data.getStringExtra("dateToShow");
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.dialog_message)
                .setTitle("Exit Application?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BookingActivity.super.onBackPressed();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // populate city spinner data
    public String[] getCitySpinnerData() {
        Log.d("getCitySpinnerData", "berhasil call");
        Cursor cursor = getBaseContext().getContentResolver().query(
                ReservationContract.CityEntry.CONTENT_URI,
                MAIN_CITY_PROJECTION,
                null,
                null,
                ReservationContract.CityEntry.COLUMN_CITY_NAMA
        );
        Log.d("city cursor length", String.valueOf(cursor.getCount()));
        cityNames = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                cityNames.put(cursor.getString(cursor.getColumnIndex(ReservationContract.CityEntry.COLUMN_CITY_ID)),
                        cursor.getString(cursor.getColumnIndex(ReservationContract.CityEntry.COLUMN_CITY_NAMA)));
                Log.d("cityNames", String.valueOf(cityNames));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cityNames.values().toArray(new String[cityNames.size()]);
    }

    // populate studio spinner data
    public String[] getStudioSpinnerData(String cityId) {
        Log.d("getStudioSpinnerData", "berhasil call");
        Cursor cursor = getBaseContext().getContentResolver().query(
                ReservationContract.StudioEntry.CONTENT_URI,
                MAIN_STUDIO_PROJECTION,
                ReservationContract.StudioEntry.COLUMN_STUDIO_FK_CITY_ID + " LIKE ?",
                new String[]{cityId},
                ReservationContract.StudioEntry.COLUMN_STUDIO_NAMA
        );
        Log.d("studio cursor length", String.valueOf(cursor.getCount()));
        studioNames = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                studioNames.put(cursor.getString(cursor.getColumnIndex(ReservationContract.StudioEntry.COLUMN_STUDIO_FK_CITY_ID)) + "." +
                                cursor.getString(cursor.getColumnIndex(ReservationContract.StudioEntry.COLUMN_STUDIO_ID)),
                        cursor.getString(cursor.getColumnIndex(ReservationContract.StudioEntry.COLUMN_STUDIO_NAMA)));
                Log.d("studioNames", String.valueOf(studioNames));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return studioNames.values().toArray(new String[studioNames.size()]);
    }

    public void getCityStudioData(final Context context) {
        // Tag used to cancel the request

        String tag_string_req = "req_city_studio_data";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Sync reservation data..");
        showDialog();

        stringRequest = new StringRequest(Request.Method.GET,
                NetworkUtils.KOTA_STUDIO_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "City Studio Response: " + response);
                try {
                    ContentValues[] cityValues = CityStudioJsonUtils
                            .getCityContentValuesFromJson(response);
                    ContentValues[] studioValues = CityStudioJsonUtils
                            .getStudioContentValuesFromJson(response);
                    insertCityData(context, cityValues);
                    insertStudioData(context, studioValues);
                    mCityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getCitySpinnerData());
                    mCityAdapter.notifyDataSetChanged();
                    spCity.setAdapter(mCityAdapter);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Fetch Data Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
        };

        // Adding request to request queue
        stringRequest.setTag(tag_string_req);
        requestQueue.add(stringRequest);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public static void insertCityData(Context context, ContentValues[] inputCityValues) {
        if (inputCityValues != null && inputCityValues.length != 0) {
            List<ContentValues> cityValues = new ArrayList<ContentValues>();

            /* Delete old weather data because we don't need to keep multiple data */
            context.getContentResolver().delete(
                    ReservationContract.CityEntry.CONTENT_URI,
                    null,
                    null);

            Log.d("length city", String.valueOf(inputCityValues.length));
            // Studio data
            Collections.addAll(cityValues, inputCityValues);

            /* Insert our new studio data into Studio ContentProvider */
            context.getContentResolver().bulkInsert(
                    ReservationContract.CityEntry.CONTENT_URI,
                    cityValues.toArray(new ContentValues[inputCityValues.length])
            );
            Log.d("length city inserted", String.valueOf(cityValues.size()));
        }
    }

    public static void insertStudioData(Context context, ContentValues[] inputStudioValues) {
        if (inputStudioValues != null && inputStudioValues.length != 0) {
            List<ContentValues> studioValues = new ArrayList<ContentValues>();

            /* Delete old studio data */
            context.getContentResolver().delete(
                    ReservationContract.StudioEntry.CONTENT_URI,
                    null,
                    null);

            Log.d("length studio", String.valueOf(inputStudioValues.length));
            // Studio data
            Collections.addAll(studioValues, inputStudioValues);

            /* Insert our new studio data into Studio ContentProvider */
            context.getContentResolver().bulkInsert(
                    ReservationContract.StudioEntry.CONTENT_URI,
                    studioValues.toArray(new ContentValues[inputStudioValues.length])
            );
            Log.d("length studio inserted", String.valueOf(studioValues.size()));
        }
    }
}
