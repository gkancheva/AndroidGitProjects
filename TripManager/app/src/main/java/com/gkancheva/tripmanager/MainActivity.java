package com.gkancheva.tripmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.gkancheva.tripmanager.controller.expense.FragmentAddNewCategory;
import com.gkancheva.tripmanager.controller.expense.FragmentAddNewExp;
import com.gkancheva.tripmanager.controller.expense.FragmentSingleEventOrExpense;
import com.gkancheva.tripmanager.controller.trip.AddNewTripFragment;
import com.gkancheva.tripmanager.controller.trip.TripTabsFragment;
import com.gkancheva.tripmanager.controller.trip.TripsFragment;
import com.gkancheva.tripmanager.controller.user.LoginFragment;
import com.gkancheva.tripmanager.controller.user.RegisterFragment;
import com.gkancheva.tripmanager.model.expense.Event;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.repositories.UserManager;
import com.gkancheva.tripmanager.services.AlarmBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserManager mUserManager;
    private static final String USERNAME_KEY = "Username";
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;
    private TripsFragment mTripsFragment;
    private AddNewTripFragment mAddNewTripFragment;
    private TripTabsFragment mTabLayoutFragment;
    private TripManager mTripManager;
    private Trip mTrip;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        mTripManager = new TripManager();
        mTrip = mTripManager.findFirstUpcomingTrip();
        mUserManager = new UserManager(this);

        if(mUserManager.isLoggedIn()) {
            showHomeFragment();
        } else {
            showLogin();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int frCount = getSupportFragmentManager().getBackStackEntryCount();
            if (frCount == 0) {
                finish();
            } else {
                if (frCount > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_add && mUserManager.isLoggedIn()) {
            TripsFragment fr = (TripsFragment)getSupportFragmentManager().findFragmentByTag("Trips");
            if(fr != null && fr.isVisible()) {
                showAddNewTrip();
            } else {
                showAddNewCategoryExp(mTrip.getId());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if(mUserManager != null && mUserManager.isLoggedIn()) {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                showHomeFragment();
            }
        } else if (id == R.id.nav_trips) {
            if(mUserManager != null && mUserManager.isLoggedIn()) {
                showTrips();
            }
        } else if (id == R.id.nav_sign_out) {
            if(mUserManager != null) {
                mUserManager.logOut();
                Toast.makeText(this, R.string.sign_out, Toast.LENGTH_SHORT).show();
                showLogin();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showLogin() {
        mLoginFragment = new LoginFragment();
        updateToolbar(R.string.app_name, R.string.login);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mLoginFragment)
                .commit();
    }

    public void showHomeFragment() {
        removeAllFragmentsFromBackStack();
        mTrip = mTripManager.findFirstUpcomingTrip();
        Long tripId;
        if(mTrip != null) {
            tripId = mTrip.getId();
        } else {
            tripId = (long)-1;
        }
        mTabLayoutFragment = TripTabsFragment.newInstance(tripId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mTabLayoutFragment)
                .commit();
    }

    public void onLoggedIn(){
        showHomeFragment();
    }

    public void onRegisterClicked() {
        mRegisterFragment = new RegisterFragment();
        updateToolbar(R.string.app_name, R.string.register);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mRegisterFragment)
                .commit();
    }

    public void onUserRegistered() {
        mLoginFragment = new LoginFragment();
        showLogin();
    }

    public void showTrips() {
        if(mUserManager.isLoggedIn()) {
            removeAllFragmentsFromBackStack();
            mTripsFragment = new TripsFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mTripsFragment, "Trips")
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void showSingleTripInfo(long tripId) {
        if(mUserManager.isLoggedIn()) {
            mTabLayoutFragment = TripTabsFragment.newInstance(tripId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mTabLayoutFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void showAddNewTrip() {
        mAddNewTripFragment = new AddNewTripFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, mAddNewTripFragment)
                .addToBackStack(null)
                .commit();
    }

    public void showAddNewCategoryExp(long tripId) {
        FragmentAddNewCategory fragment = FragmentAddNewCategory.newInstance(tripId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showAddNewExpense(long tripId, String category) {
        FragmentAddNewExp fragment = FragmentAddNewExp.newInstance(tripId, category);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showSingleExpense(Expense expense) {
        FragmentSingleEventOrExpense fr
                = FragmentSingleEventOrExpense.newInstance(expense.getExpenseCategory().toString(), expense.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fr)
                .addToBackStack(null)
                .commit();
    }

    public void showSingleEvent(Event event) {
        FragmentSingleEventOrExpense fr
                = FragmentSingleEventOrExpense.newInstance(event.getExpenseCategory().toString(), event.getId());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fr)
                .addToBackStack(null)
                .commit();
    }

    public void updateToolbar(int stringTitleId, String titleStr , int strSubTitleId, String subTitle) {
        toolbar.setTitle(getResources().getString(stringTitleId) + titleStr);
        toolbar.setSubtitle(getResources().getString(strSubTitleId) + subTitle);
    }

    public void updateToolbar(int stringId, int strSubId) {
        toolbar.setTitle(getResources().getString(stringId));
        if(strSubId != -1) {
            toolbar.setSubtitle(getResources().getString(strSubId));
        }
    }

    public void removeAllFragmentsFromBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void scheduleNotificationForTrip(Date dateTime, String title, String body) {
        Intent notifyIntent = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
        notifyIntent.putExtra("Message", "Trip to " + title);
        notifyIntent.putExtra("Body", "Starting on " + body);
        PendingIntent pendIntent = PendingIntent
                .getBroadcast(MainActivity.this, 0, notifyIntent, 0);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Date date = new Date(dateTime.getYear(), dateTime.getMonth(), dateTime.getDate() - 1, 9, 0, 0);
        am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendIntent);
    }

    public void scheduleNotificationForEvent(Date dateTime, String category, String body) {
        SimpleDateFormat f = new SimpleDateFormat("HH:MM");
        Intent notifyIntent = new Intent(MainActivity.this, AlarmBroadcastReceiver.class);
        notifyIntent.putExtra("Message", category);
        notifyIntent.putExtra("Body", body + " at: " + f.format(dateTime));
        PendingIntent pendIntent = PendingIntent
                .getBroadcast(MainActivity.this, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        Date date = new Date(dateTime.getYear(), dateTime.getMonth(), dateTime.getDate(), dateTime.getHours(), dateTime.getMinutes() - 15, 0);
        am.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmBroadcastReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
