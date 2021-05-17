package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Objects;

import umn.useit.model.User;
import umn.useit.prehome.MainActivity;

public class ProfileFragment extends Fragment {
    //Firebase
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseUsers = database.getReference("Users");
    private final DatabaseReference databaseProblems = database.getReference("Problems");

    User user;
    private TextView profile_name;
    private TextView exp;


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
        DatabaseReference userRow = databaseUsers.child(curr_userUid);

        profile_name = getView().findViewById(R.id.profile_name);


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

        btn_logout.setOnClickListener(new View.OnClickListener() { // LOG OUT button clicked
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));


            }
        });
    } //onViewCreated()
}
