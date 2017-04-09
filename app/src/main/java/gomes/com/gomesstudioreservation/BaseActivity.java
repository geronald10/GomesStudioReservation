package gomes.com.gomesstudioreservation;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import gomes.com.gomesstudioreservation.data.ReservationContract;
import gomes.com.gomesstudioreservation.data.ReservationSessionManager;

public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    private DrawerLayout fullLayout;
    private Toolbar toolbar;
    private View navHeader;
    private ActionBarDrawerToggle drawerToggle;

    public ReservationSessionManager session;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        session = new ReservationSessionManager(getApplicationContext());
        session.checkLogin();
        /**
         * This is going to be our actual root layout.
         */
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        /**
         * {@link FrameLayout} to inflate the child's view. We could also use a {@link android.view.ViewStub}
         */
        FrameLayout activityContainer = (FrameLayout) fullLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        /**
         * Note that we don't pass the child's layoutId to the parent,
         * instead we pass it our inflated layout.
         */
        super.setContentView(fullLayout);

        HashMap<String, String> userDetail = session.getUserDetails();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorIcons));
        TextView textToolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        textToolbarTitle.setText(R.string.app_name);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        TextView textName = (TextView) navHeader.findViewById(R.id.tv_username);
        TextView textEmail = (TextView) navHeader.findViewById(R.id.tv_email);
        textName.setText(userDetail.get(ReservationSessionManager.KEY_NAME));
        textEmail.setText(userDetail.get(ReservationSessionManager.KEY_EMAIL));

        session = new ReservationSessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        if (useToolbar())
        {
            setSupportActionBar(toolbar);
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }

        setUpNavView();
    }

    /**
     * Helper method that can be used by child classes to
     * specify that they don't want a {@link Toolbar}
     * @return true
     */
    protected boolean useToolbar()
    {
        return true;
    }

    protected void setUpNavView()
    {
        navigationView.setNavigationItemSelectedListener(this);

        if( useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, fullLayout, toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);

            fullLayout.setDrawerListener(drawerToggle);
            drawerToggle.syncState();
        } else if(useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.ic_home));
        }
    }

    /**
     * Helper method to allow child classes to opt-out of having the
     * hamburger menu.
     * @return
     */
    protected boolean useDrawerToggle()
    {
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        fullLayout.closeDrawer(GravityCompat.START);
        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.nav_home:

                startActivity(new Intent(this, BookingActivity.class));
                finish();
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            case R.id.nav_booking_history:
                startActivity(new Intent(this, BookingHistoryActivity.class));
                finish();
                return true;
            case R.id.nav_about_us:
                startActivity(new Intent(this, AboutUsActivity.class));
                finish();
                return true;
            case R.id.nav_privacy_policy:
                startActivity(new Intent(this, PrivacyPolicyActivity.class));
                finish();
                return true;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                logoutUser();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        Uri uri = ReservationContract.UserEntry.CONTENT_URI;
        getApplicationContext().getContentResolver().delete(uri, null, null);
        session.logoutUser();
    }
}
