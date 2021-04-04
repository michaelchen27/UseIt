package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AskActivity extends AppCompatActivity {
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Problems");

    private EditText mtitle_problem;
    public String title_problem;

    private EditText mproblem_desc;
    public String problem_desc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);

        mtitle_problem = (EditText) findViewById(R.id.TitleProblem);
        mproblem_desc = (EditText) findViewById(R.id.ProblemDesc);
        Button msubmit_btn = (Button) findViewById(R.id.SubmitProblem);


        msubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_problem = mtitle_problem.getText().toString();
                problem_desc = mproblem_desc.getText().toString();

                HashMap<String, String> problemMap = new HashMap<>();

                problemMap.put("title", title_problem);
                problemMap.put("desc", problem_desc);

                root.push().setValue(problemMap);

                Intent intent = new Intent(AskActivity.this, SucceedActivity.class);
                startActivity(intent);
            }
        });
        //Bottom Nav
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.ask);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                String title_problem = mtitle_problem.getText().toString();
//                String problem_desc = mproblem_desc.getText().toString();
                switch(menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.ask:
                        return true;
                }
                return false;
            }
        });
        //Bottom Nav
    } //onCreate()

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
