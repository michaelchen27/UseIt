package umn.useit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment {

    private TextView welcome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Firebase
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        //GUI
        welcome = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.welcome);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUsers = database.getReference("users");
        String id = curr_user.getUid();
        DatabaseReference firstname = databaseUsers.child(id).child("firstname");

        firstname.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                String name = mutableData.getValue(String.class);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.getValue(String.class);
                if(firstname != null)
                    welcome.setText("Hi, " + firstname + "!");
            }
        });
    } //onViewCreated()
}
