package umn.useit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Objects;

import umn.useit.model.Problem;

public class AskFragment extends Fragment {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseProblems = db.getReference().child("Problems");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        btnSubmit.setEnabled(false);

        etTitleProblem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSubmit.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_problem = etTitleProblem.getText().toString();
                String problem_desc = etProblemDesc.getText().toString();
                String user_email = mAuth.getCurrentUser().getEmail();
                long date = System.currentTimeMillis();

                int index = user_email.indexOf('@');
                user_email = user_email.substring(0, index);

                Problem problem = new Problem(title_problem, problem_desc, user_email, date, 0);
                databaseProblems.push().setValue(problem);

                startActivity(new Intent(getActivity(), SucceedActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });
    } //onViewCreated()

    private void storeProblemData(String title, String desc, String poster, long date) {

    }
}
