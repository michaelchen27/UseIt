package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SucceedActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prob_sub_succeed);

        Button mbtn_go_home = (Button) findViewById(R.id.btn_go_home);

        mbtn_go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SucceedActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

    }
}
