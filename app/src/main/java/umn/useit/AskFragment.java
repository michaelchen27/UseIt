package umn.useit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AskFragment extends Fragment {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private final DatabaseReference databaseProblems = db.getReference().child("Problems");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_ask, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etTitleProblem = Objects.requireNonNull(getView()).findViewById(R.id.TitleProblem);
        EditText etProblemDesc = Objects.requireNonNull(getView()).findViewById(R.id.ProblemDesc);
        Button btnSubmit = Objects.requireNonNull(getView()).findViewById(R.id.SubmitProblem);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_problem = etTitleProblem.getText().toString();
                String problem_desc = etProblemDesc.getText().toString();
                String user_email = mAuth.getCurrentUser().getEmail();
                DateFormat df = new SimpleDateFormat("d MMM yy · HH:mm ");
                String date = df.format(Calendar.getInstance().getTime());

                int index = user_email.indexOf('@');
                user_email = user_email.substring(0,index);

                int total = getPostAmount(); //buggy code
                storeProblemData(total ,title_problem, problem_desc, user_email, date);

//                HashMap<String, String> problemMap = new HashMap<>();
//
//                problemMap.put("title", title);
//                problemMap.put("desc", desc);
//                root.push().setValue(problemMap);

                startActivity(new Intent(getActivity(), SucceedActivity.class));
                getActivity().finish();
            }
        });
    } //onViewCreated()

    private void storeProblemData(int id, String title, String desc, String poster, String date) {
        Problem problem = new Problem(title, desc, poster, date, 0, id);
        databaseProblems.child(String.valueOf(id)).setValue(problem);
    }

    private int getPostAmount() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return prefs.getInt("postTotal",0);
    }
}
