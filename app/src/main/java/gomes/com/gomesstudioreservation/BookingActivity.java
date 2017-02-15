package gomes.com.gomesstudioreservation;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;
import gomes.com.gomesstudioreservation.utilities.BasicCalendar;
import gomes.com.gomesstudioreservation.utilities.FakeDataUtils;

public class BookingActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private final String TAG = BookingActivity.class.getSimpleName();

    public static final String[] MAIN_CITY_PROJECTION = {
            ReservationContract.CityEntry.COLUMN_CITY_ID,
            ReservationContract.CityEntry.COLUMN_CITY_NAMA
    };

    public static final String[] MAIN_USER_PROJECTION = {
            ReservationContract.UserEntry.KEY_USER_NAME,
            ReservationContract.UserEntry.KEY_USER_EMAIL
    };

    private static final int USER_SESSION_LOADER = 100;
    private static final int REQUEST_CODE_CALENDAR = 200;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private ArrayList<String> cityNames;
    private ArrayList<String> studioNames;
    private HashMap<String, String> user;

    private View navHeader;
    private TextView textName;
    private TextView textEmail;
    private ProgressBar mLoadingIndicator;
    private EditText namaBand;
    private EditText tanggalBooking;
    private String selectedDate;
    private String formattedSelectedDate;
    private Button btnSearch;
    private SearchableSpinner spStudio;
    private SearchableSpinner spCity;
    private ReservationSessionManager session;

    private String name;
    private String email;
    private String noHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView textToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textToolbarTitle.setText(R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        textName = (TextView) navHeader.findViewById(R.id.tv_username);
        textEmail = (TextView) navHeader.findViewById(R.id.tv_email);

        session = new ReservationSessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        Intent intent = getIntent();
//        String nama = intent.getStringExtra(ReservationContract.UserEntry.KEY_USER_NAME);
//        String email = intent.getStringExtra(ReservationContract.UserEntry.KEY_USER_EMAIL);
//        String noHp = intent.getStringExtra(ReservationContract.UserEntry.KEY_USER_NO_HP);
//        addUser(nama, email, noHp);

        FakeDataUtils.insertFakeCityData(this);

        namaBand = (EditText) findViewById(R.id.edt_band_name);
        tanggalBooking = (EditText) findViewById(R.id.edt_select_date);
        btnSearch = (Button) findViewById(R.id.btn_search_button);
        spCity = (SearchableSpinner) findViewById(R.id.sp_select_town);
        spStudio = (SearchableSpinner) findViewById(R.id.sp_select_studio);

        cityNames = new ArrayList<>();
        ArrayAdapter<String> mCityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getCitySpinnerData());
//        ArrayAdapter<String> mStudioAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getStudioSpinnerData());
        mCityAdapter.notifyDataSetChanged();
//        mStudioAdapter.notifyDataSetChanged();
        spCity.setAdapter(mCityAdapter);
//        spStudio.setAdapter(mStudioAdapter);

        tanggalBooking.setOnTouchListener(operation);
        spCity.setTitle("Select Town");
        spCity.setPositiveButton("OK");

        spStudio.setTitle("Select Studio");
        spStudio.setPositiveButton("OK");

        user = new HashMap<String, String>();
        getSupportLoaderManager().initLoader(USER_SESSION_LOADER, null, this);
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
        if (requestCode == REQUEST_CODE_CALENDAR) {
            if (resultCode == Activity.RESULT_OK) {
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
    public String[] getCitySpinnerData() {
        Cursor cursor = getBaseContext().getContentResolver().query(
                ReservationContract.CityEntry.CONTENT_URI,
                MAIN_CITY_PROJECTION,
                null,
                null,
                ReservationContract.CityEntry.COLUMN_CITY_NAMA
        );
        ArrayList<String> cityNames = new ArrayList<String>();
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

//    public HashMap<String, String> getUserDetails() {
//        Uri singleUri = ContentUris.withAppendedId(ReservationContract.UserEntry.CONTENT_URI, 1);
//        Cursor cursor = getBaseContext().getContentResolver().query(
//                singleUri,
//                MAIN_USER_PROJECTION,
//                null,
//                null,
//                null
//        );
//        HashMap<String, String> user = new HashMap<String, String>();
//        if (cursor != null && cursor.moveToFirst()) {
//            while (cursor.moveToNext()) {
//                Log.d("test masuk ", String.valueOf(cursor.getCount()));
//                user.put(ReservationContract.UserEntry.KEY_USER_NAME, cursor.getString(cursor.getColumnIndex(ReservationContract.UserEntry.KEY_USER_NAME)));
//                user.put(ReservationContract.UserEntry.KEY_USER_EMAIL, cursor.getString(cursor.getColumnIndex(ReservationContract.UserEntry.KEY_USER_EMAIL)));
//            }
////            user.put(ReservationContract.UserEntry.KEY_USER_NAME, cursor.getString(1));
////            user.put(ReservationContract.UserEntry.KEY_USER_EMAIL, cursor.getString(2));
//        }
//        if(cursor != null) {
//            cursor.close();
//        }
//        Log.d("fetch_user", user.get(ReservationContract.UserEntry.KEY_USER_NAME));
//        Log.d("Cursor Total User", String.valueOf(cursor.getCount()));
//        Log.d(TAG, "Fetching user from Sqlite " + user.toString());
//        return user;
//    }

    private void loadNavHeader() {
        name = user.get(ReservationContract.UserEntry.KEY_USER_NAME);
        email = user.get(ReservationContract.UserEntry.KEY_USER_EMAIL);

        textName.setText(name);
        textEmail.setText(email);
        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);
//
//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);
//
//        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void logoutUser() {
        getSupportLoaderManager().destroyLoader(USER_SESSION_LOADER);
        session.setLogin(false);

        Uri uri = ReservationContract.UserEntry.CONTENT_URI;
        getApplicationContext().getContentResolver().delete(uri, null, null);
        Log.d(TAG, "Deleted all user info from sqlite");

        // Launching the login activity
        Intent intent = new Intent(BookingActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_home:
                navItemIndex = 0;
                startActivity(new Intent(getApplicationContext(), BookingActivity.class));
                break;
            case R.id.nav_profile:
                navItemIndex = 1;
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                break;
            case R.id.nav_booking_history:
                navItemIndex = 2;
                startActivity(new Intent(getApplicationContext(), BookingHistoryActivity.class));
                break;
            case R.id.nav_about_us:
                // launch new intent instead of loading fragment
                startActivity(new Intent(getApplicationContext(), AboutUsActivity.class));
                break;
            case R.id.nav_privacy_policy:
                // launch new intent instead of loading fragment
                startActivity(new Intent(getApplicationContext(), PrivacyPolicyActivity.class));
                break;
            default:
                navItemIndex = 0;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case USER_SESSION_LOADER:
                Uri userQueryUri = ReservationContract.UserEntry.CONTENT_URI;
                return new CursorLoader(
                        this,
                        userQueryUri,
                        MAIN_USER_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("User terlogin: ", String.valueOf(data.getCount()));
        data.moveToFirst();
        user.put(ReservationContract.UserEntry.KEY_USER_NAME, data.getString(data.getColumnIndex(ReservationContract.UserEntry.KEY_USER_NAME)));
        user.put(ReservationContract.UserEntry.KEY_USER_EMAIL, data.getString(data.getColumnIndex(ReservationContract.UserEntry.KEY_USER_EMAIL)));
        loadNavHeader();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
