package umn.useit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

public class HomeFragment extends Fragment implements HomeCardAdapter.ItemClickListener{

    private TextView welcome;
    HomeCardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //GUI Init
        welcome = (TextView) Objects.requireNonNull(getView()).findViewById(R.id.welcome);

        //Firebase
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseUsers = database.getReference("users");
        String id = curr_user.getUid();
        DatabaseReference userRow = databaseUsers.child(id);

        userRow.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null) welcome.setText(String.format("Hi, %s %s!", user.getFirstname(), user.getLastname()));
            }
        }); //runTransaction()

        // POPULATE
        ArrayList<String> problemTitles = new ArrayList<>();
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");
        problemTitles.add("Poyo");

        //Setup RecyclerView
        RecyclerView recyclerView = getView().findViewById(R.id.rvHomeCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new HomeCardAdapter(getActivity(), problemTitles);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    } //onViewCreated()

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) + " on row number" + position, Toast.LENGTH_SHORT).show();
    }
}
