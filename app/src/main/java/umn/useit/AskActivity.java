package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AskActivity extends AppCompatActivity {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Problems");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);


        EditText mtitle_problem = (EditText) findViewById(R.id.TitleProblem);
        EditText mproblem_desc = (EditText) findViewById(R.id.ProblemDesc);
        Button msubmit_btn = (Button) findViewById(R.id.SubmitProblem);


        msubmit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_problem = mtitle_problem.getText().toString();
                String problem_desc = mproblem_desc.getText().toString();

                HashMap<String, String> problemMap = new HashMap<>();

                problemMap.put("title", title_problem);
                problemMap.put("desc", problem_desc);

                root.push().setValue(problemMap);

                Intent intent = new Intent(AskActivity.this, SucceedActivity.class);
                startActivity(intent);
            }
        });

    }
}