package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class AskFragment extends Fragment {

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Problems");

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

                HashMap<String, String> problemMap = new HashMap<>();

                problemMap.put("title", title_problem);
                problemMap.put("desc", problem_desc);

                root.push().setValue(problemMap);

                Intent intent = new Intent(getActivity(), SucceedActivity.class);
                startActivity(intent);
            }
        });
    }
}
