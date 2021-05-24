package umn.useit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import umn.useit.home.HomeCardAdapter;
import umn.useit.home.ProfileCardAdapter;
import umn.useit.model.Problem;
import umn.useit.model.User;
import umn.useit.prehome.MainActivity;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    //Firebase
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseUsers = database.getReference("Users");
    private final DatabaseReference databaseProblems = database.getReference("Problems");

    User user;
    private TextView profile_name;
    private TextView exp;
    public Uri imageUri;

    //Adapter
    ProfileCardAdapter adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {


        return inflater.inflate(R.layout.fragment_profile, container, false);
    } //onCreateView

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        // Init =============================================================================
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();

        String curr_userUid = curr_user.getUid();
        String email = curr_user.getEmail();
        DatabaseReference userRow = databaseUsers.child(curr_userUid);

        profile_name = getView().findViewById(R.id.profile_name);
        ImageView profilePic = Objects.requireNonNull(getView()).findViewById(R.id.profile_image);


        //Get user =======================================================================================
        userRow.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null){
                    profile_name.setText(String.format("%s %s", user.getFirstname(), user.getLastname()));

                }


            }
        }); //runTransaction()


        Button btn_logout = (Button) Objects.requireNonNull(getView()).findViewById(R.id.btn_logout);


        // LOG OUT button clicked
        btn_logout.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getActivity(), MainActivity.class));
            Objects.requireNonNull(getActivity()).finish();


        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        databaseProblems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Problem> problems = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Problem problem = dataSnapshot.getValue(Problem.class);
                        if (problem.getPoster().equals(email)) problems.add(problem);
                    }
                    showCards(problems);
                }
            }

            public void showCards(List<Problem> problems) {
                RecyclerView recyclerView = getView().findViewById(R.id.rvProfileCard);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setReverseLayout(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter = new ProfileCardAdapter(getActivity(), problems);
                recyclerView.setAdapter(adapter);
            } //showCards()

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    } //onViewCreated()

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView display = Objects.requireNonNull(getView()).findViewById(R.id.profile_image);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            display.setImageURI(imageUri);
        }
    }
}
