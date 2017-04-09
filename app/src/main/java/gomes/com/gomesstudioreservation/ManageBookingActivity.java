package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.models.Schedule;
import gomes.com.gomesstudioreservation.tabs.SlidingTabLayout;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;
import gomes.com.gomesstudioreservation.utilities.SearchJadwalJsonUtils;

public class ManageBookingActivity extends AppCompatActivity {

    private final String TAG = ManageBookingActivity.class.getSimpleName();

    private static final String TANGGAL = "tanggal";
    private static final String STUDIO_ID = "studio_id";
    private Context mContext;

    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private Button btnBook;

    private String selectedDate;
    private String selectedStudio;
    private String userId;
    private String namaStudio;
    private String namaBand;
    private List<Schedule> checkedScheduleList = new ArrayList<>();

    private ProgressDialog progressDialog;
    private ReservationSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking);
        mContext = this;

        session = new ReservationSessionManager(getApplicationContext());
        session.checkLogin();

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // Button Book
        btnBook = (Button) findViewById(R.id.btn_booking_button);
        btnBook.setOnClickListener(operasi);

        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("selected_date");
        selectedStudio = intent.getStringExtra("selected_studio");
        namaStudio = intent.getStringExtra("nama_studio");
        namaBand = intent.getStringExtra("nama_band");

        getJadwalBookingList(selectedDate, selectedStudio);

        // Set up action bar.
        setupToolbar();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_booking_button:
                    Log.d("checkedList size", String.valueOf(checkedScheduleList.size()));
                    String selectedDate = checkedScheduleList.get(0).getTanggal();
                    String selectedRoom = checkedScheduleList.get(0).getRoomId();
                    int totalHarga = 0;
                    String selectedHour = "";
                    String selectedJadwal = "";

                    for (int i = 0; i < checkedScheduleList.size(); i++) {
                        Schedule singleSchedule = checkedScheduleList.get(i);
                        if (singleSchedule.isSelected()) {
                            selectedJadwal += singleSchedule.getJadwalId();
                            selectedHour += i+1 + ". " + singleSchedule.getJadwalStart() + " - " + singleSchedule.getJadwalEnd();
                            if(i != checkedScheduleList.size()-1) {
                                selectedJadwal += ",";
                                selectedHour += "\n";
                            }
                            String selectedHarga = singleSchedule.getHarga();
                            totalHarga = totalHarga + Integer.parseInt(selectedHarga.trim());
                        }
                    }

                    Intent intentToBookingReview = new Intent(ManageBookingActivity.this, BookingReviewActivity.class);
                    intentToBookingReview.putExtra("studioName", namaStudio);
                    intentToBookingReview.putExtra("bandName", namaBand);
                    intentToBookingReview.putExtra("jadwal_id", selectedJadwal);
                    intentToBookingReview.putExtra("selected_date", selectedDate);
                    intentToBookingReview.putExtra("selected_hour", selectedHour);
                    intentToBookingReview.putExtra("selected_room", selectedRoom);
                    intentToBookingReview.putExtra("tagihan", totalHarga);
                    startActivity(intentToBookingReview);
            }
        }
    };

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        TextView textToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textToolbarTitle.setText(R.string.manage_booking_label);
    }

    private void setupTabs() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ScheduleSectionsPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout) findViewById(R.id.tabs);

        //make sure all tabs take the full horizontal screen space and divide it equally amongst themselves
        mTabs.setDistributeEvenly(true);
        mTabs.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });
        mTabs.setViewPager(mPager);
    }

    private void getJadwalBookingList(final String tanggal, final String studioId) {
        // Tag used to cancel the request
        String tag_string_req = "req_jadwal_booking_list";
        StringRequest stringRequest;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog.setMessage("Fetch Schedule Data ...");
        showDialog();

        Log.d("selected date", tanggal);
        Log.d("selected studio", studioId);

        stringRequest = new StringRequest(Request.Method.POST,
                NetworkUtils.MANAGE_BOOKING_CHAMBER_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "List Jadwal: " + response);
                try {
                    List<ContentValues> jadwalValues = SearchJadwalJsonUtils
                            .getJadwalContentValuesFromJson(response);
                    Log.d("jadwal Values", String.valueOf(jadwalValues.size()));
                    Log.d("jadwal Values", String.valueOf(jadwalValues));
//                    loadScheduleSections(jadwalValues);
                    addJadwalBookingList(mContext, jadwalValues);
                    // Set up view pager.
                    setupTabs();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put(TANGGAL, tanggal);
                params.put(STUDIO_ID, studioId);

                return params;
            }
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

    public class ScheduleSectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabText;

        public ScheduleSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            tabText = getDateTitleForTab();
            Log.d("get date title", String.valueOf(getDateTitleForTab()));
        }

        @Override
        public Fragment getItem(int position) {
            FragmentScheduleByDate scheduleSectionFragment = FragmentScheduleByDate.newInstance(position, tabText[position]);
            return scheduleSectionFragment;
        }

        @Override
        public int getCount() {
            return tabText.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabText[position];
        }
    }

    private void addJadwalBookingList(Context context, List<ContentValues> inputJadwalValues) {
        if (inputJadwalValues != null && inputJadwalValues.size() != 0) {
            List<ContentValues> jadwalValues = new ArrayList<ContentValues>();

            /* Delete old weather data because we don't need to keep multiple data */
            context.getContentResolver().delete(
                    ReservationContract.JadwalEntry.CONTENT_URI,
                    null,
                    null);

            // jadwal data
            for (int i = 0; i < inputJadwalValues.size(); i++) {
                ContentValues jadwal = new ContentValues();
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_TANGGAL, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_TANGGAL));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_CODE, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_CODE));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_STATUS, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_STATUS));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_ROOM_ID, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_ROOM_ID));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_ID, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_ID));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_START, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_START));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_END, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_END));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_HARGA, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_HARGA));
                jadwalValues.add(jadwal);
            }

            this.getContentResolver().bulkInsert(
                    ReservationContract.JadwalEntry.CONTENT_URI,
                    jadwalValues.toArray(new ContentValues[inputJadwalValues.size()])
            );
        }
    }

    public String[] getDateTitleForTab() {
        Log.d("getCitySpinnerData", "berhasil get date title");
        Cursor cursor = getBaseContext().getContentResolver().query(
                ReservationContract.JadwalEntry.CONTENT_URI,
                new String[]{"DISTINCT " + ReservationContract.JadwalEntry.COLUMN_TANGGAL},
                null,
                null,
                ReservationContract.JadwalEntry.COLUMN_TANGGAL + " ASC"
        );
        Log.d("cursor tanggal length", String.valueOf(cursor.getCount()));
        List<String> listTanggal = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                listTanggal.add(cursor.getString(cursor.getColumnIndex(ReservationContract.JadwalEntry.COLUMN_TANGGAL)));
                Log.d("tanggal", String.valueOf(listTanggal));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return listTanggal.toArray(new String[listTanggal.size()]);
    }

    public void saveToList(Schedule schedule) {
        checkedScheduleList.add(schedule);
        for (int i = 0; i < checkedScheduleList.size(); i++) {
            Log.d("list masuk", checkedScheduleList.toString());
        }
    }

    public void removeFromList(Schedule schedule) {
        checkedScheduleList.remove(new Schedule(schedule.getTanggal(), schedule.getRoomId(),
                schedule.getJadwalId(), schedule.getJadwalStart(), schedule.getJadwalEnd(),
                schedule.getHarga(), true));
        for (int i = 0; i < checkedScheduleList.size(); i++) {
            Log.d("list masuk", checkedScheduleList.toString());
        }
    }
}
