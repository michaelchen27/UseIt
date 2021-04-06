package umn.useit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        //Bottom Nav
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

    } //onCreate()

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