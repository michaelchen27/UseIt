package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import umn.useit.home.DashboardActivity;

public class SucceedActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prob_sub_succeed);

        MaterialButton mbtn_go_home = findViewById(R.id.btn_go_home);
        TextView countdown = findViewById(R.id.countdown);

        mbtn_go_home.setOnClickListener(v -> {
            goBack();
        });

        new Handler().postDelayed(this::goBack, 3000);

        new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("Redirecting you back to home in " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                countdown.setText("0");
            }
        }.start();

    } //onCreate()

    public void goBack() {
        Intent mainIntent = new Intent(SucceedActivity.this, DashboardActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
