package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
        String name = email .substring(0, email .indexOf("@"));

        Button btn_logout = (Button) findViewById(R.id.btn_logout);
        Button btn_ask = (Button) findViewById(R.id.btn_ask);
        TextView text = (TextView) findViewById(R.id.text);
        text.setText("Welcome, " + name + "!");

        btn_logout.setOnClickListener(new View.OnClickListener() { // LOG OUT button clicked
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        btn_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AskActivity.class);
                startActivity(intent);
            }
        });
    } //onCreate()

    // In Home Activity, if back button is pressed, quit app.
    public void onBackPressed() {
        finish();
    }
}
