package umn.useit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {
    final Fragment fHome = new HomeFragment();
    final Fragment fAsk = new AskFragment();
    final Fragment fProfile = new ProfileFragment();
    final FragmentManager fm = getSupportFragmentManager();
    final FragmentTransaction ft = fm.beginTransaction();
    Fragment fCurr = fHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Bottom Nav
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        fm.beginTransaction().add(R.id.fragment_container, fProfile, "PROFILE_FRAGMENT").hide(fProfile).commit();
        fm.beginTransaction().add(R.id.fragment_container, fAsk, "ASK_FRAGMENT").hide(fAsk).commit();
        fm.beginTransaction().add(R.id.fragment_container,fHome, "HOME_FRAGMENT").commit();

//Previous BottomNav Fragment, kept for reference.
        /*
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment currFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

                switch(menuItem.getItemId()) {
                    case R.id.nav_home:

                        if (!(currFragment instanceof HomeFragment)) {
                            transaction.setCustomAnimations(
                                    R.anim.enter_left_to_right, //enter
                                    R.anim.exit_left_to_right); //exit
                        }

                        changeFragment(transaction, new HomeFragment(), "HOME_FRAGMENT");

                            break;
                        case R.id.nav_ask:

                            if (!(currFragment instanceof AskFragment)) {
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                            }
                            changeFragment(transaction, new AskFragment(), "ASK_FRAGMENT");

                            break;
                        case R.id.nav_profile:
                            if (!(currFragment instanceof ProfileFragment)) {
                                transaction.setCustomAnimations(
                                        R.anim.enter_right_to_left,
                                        R.anim.exit_right_to_left);
                            }
                            changeFragment(transaction, new ProfileFragment(), "PROFILE_FRAGMENT");

                            break;
                    }
                return true;
            }
        }); //Bottom Nav
         */

    } //onCreate()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_actionbar, menu);
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction ft = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_home:
                    ft.setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_left_to_right);
                    if (!(fCurr instanceof HomeFragment)) ft.hide(fCurr).show(fHome).commit();
                    fCurr = fHome;
                    return true;

                case R.id.nav_ask:
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    if (!(fCurr instanceof AskFragment)) ft.hide(fCurr).show(fAsk).commit();
                    fCurr = fAsk;
                    return true;

                case R.id.nav_profile:
                    ft.setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left);
                    if (!(fCurr instanceof ProfileFragment)) ft.hide(fCurr).show(fProfile).commit();
                    fCurr = fProfile;
                    return true;
            }
            return false;
        }
    };

    public void changeFragment(FragmentTransaction transaction, Fragment f, String tag) {
        transaction
        .replace(R.id.fragment_container, f, tag)
        .commit();
    }

    public void viewNotification(View view) {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    public void viewChat(View view) {
        startActivity(new Intent(this, ChatActivity.class));
    }
}