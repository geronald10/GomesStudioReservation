package gomes.com.gomesstudioreservation;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import gomes.com.gomesstudioreservation.tabs.SlidingTabLayout;
import gomes.com.gomesstudioreservation.utilities.NetworkUtils;
import gomes.com.gomesstudioreservation.utilities.SearchJadwalJsonUtils;

//public class ManageBookingActivity extends FragmentActivity {
//
//    private final String TAG = ManageBookingActivity.class.getSimpleName();
//
//    private static final String TANGGAL = "tanggal";
//    private static final String STUDIO_ID = "studio_id";
//    private Context mContext;
//
//    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
//
//    private ProgressDialog progressDialog;
//
//    ScheduleSectionsPagerAdapter mScheduleSectionsPagerAdapter;
////    RoomSectionsPagerAdapter mRoomSectionsPagerAdapter;
//    ViewPager mViewPager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_booking);
//
//        mContext = this;
//        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
//
//        // Set up action bar.
//        final ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode();
////        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Progress Dialog
//        progressDialog = new ProgressDialog(this);
//        progressDialog.setCancelable(false);
//
//        Intent intent = getIntent();
//        String selectedDate = intent.getStringExtra("selected_date");
//        String selectedStudio = intent.getStringExtra("selected_studio");
//        String userId = intent.getStringExtra("user_id");
//        String namaStudio = intent.getStringExtra("nama_studio");
//
//        getJadwalBookingList(selectedDate, selectedStudio);
//
//        mViewPager = (ViewPager)findViewById(R.id.pager);
//        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });
//
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            actionBar.addTab(
//                    actionBar.newTab()
//                    .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                    .setTabListener((ActionBar.TabListener) this));
//        }
//    }

public class ManageBookingActivity extends AppCompatActivity {

    private final String TAG = ManageBookingActivity.class.getSimpleName();

    private static final String TANGGAL = "tanggal";
    private static final String STUDIO_ID = "studio_id";
    private Context mContext;

    private Toolbar toolbar;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;

    private ProgressDialog progressDialog;

    ScheduleSectionsPagerAdapter mScheduleSectionsPagerAdapter;
//        RoomSectionsPagerAdapter mRoomSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_booking_1);
        mContext = this;

        // Set up action bar.
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new ScheduleSectionsPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
        mTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorAccent);
            }
        });

        // Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selected_date");
        String selectedStudio = intent.getStringExtra("selected_studio");
        String userId = intent.getStringExtra("user_id");
        String namaStudio = intent.getStringExtra("nama_studio");

//        getJadwalBookingList(selectedDate, selectedStudio);

//        mViewPager = (ViewPager)findViewById(R.id.pager);
//        mViewPager.setAdapter(mAppSectionsPagerAdapter);
//        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                actionBar.setSelectedNavigationItem(position);
//            }
//        });
//
//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
//                            .setTabListener((ActionBar.TabListener) this));
//        }
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
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class ScheduleSectionsPagerAdapter extends FragmentPagerAdapter {

        public ScheduleSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ScheduleSectionFragment scheduleSectionFragment = ScheduleSectionFragment.getInstance(position);
            return scheduleSectionFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Room " + (position + 1);
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class ScheduleSectionFragment extends Fragment {

        public static ScheduleSectionFragment getInstance(int position) {
            ScheduleSectionFragment scheduleSectionFragment = new ScheduleSectionFragment();
            Bundle args = new Bundle();
            args.putInt("position", position);
            scheduleSectionFragment.setArguments(args);
            return scheduleSectionFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View layout = inflater.inflate(R.layout.fragment_jadwal_by_date, container, false);
            TextView textView = (TextView)layout.findViewById(R.id.text1);
            Bundle bundle = getArguments();
            if(bundle != null) {
                textView.setText("The page selected is " + bundle.getInt("position"));
            }
            return layout;
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
            for(int i = 0; i < inputJadwalValues.size(); i++) {
                ContentValues jadwal = new ContentValues();
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_TANGGAL, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_TANGGAL));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_CODE, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_CODE));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_STATUS, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_STATUS));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_ROOM_ID, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_ROOM_ID));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_ID, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_ID));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_START, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_START));
                jadwal.put(ReservationContract.JadwalEntry.COLUMN_JADWAL_END, inputJadwalValues.get(i).getAsString(ReservationContract.JadwalEntry.COLUMN_JADWAL_END));
                jadwalValues.add(jadwal);
            }
            
            this.getContentResolver().bulkInsert(
                    ReservationContract.JadwalEntry.CONTENT_URI,
                    jadwalValues.toArray(new ContentValues[inputJadwalValues.size()])
            );
        }
    }
}
