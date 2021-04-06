package umn.useit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcome = findViewById(R.id.welcome);

        //Bottom Nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch(menuItem.getItemId()) {
                        case R.id.nav_home:
                            transaction.setCustomAnimations(
                                    R.anim.enter_left_to_right, //enter
                                    R.anim.exit_left_to_right); //exit
                            changeFragment(transaction, new HomeFragment(), "HOME_FRAGMENT");

                            break;
                        case R.id.nav_ask:
                            transaction.setCustomAnimations(
                                    R.anim.enter_down_to_up,
                                    R.anim.exit_down_to_up);
                            changeFragment(transaction, new AskFragment(), "ASK_FRAGMENT");

                            break;
                        case R.id.nav_profile:
                            transaction.setCustomAnimations(
                                    R.anim.enter_right_to_left,
                                    R.anim.exit_right_to_left);
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

}