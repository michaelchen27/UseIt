package umn.useit.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import umn.useit.R;
import umn.useit.home.RoomAdapter;
import umn.useit.model.Room;

public class RoomActivity extends AppCompatActivity implements RoomAdapter.ItemClickListener {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRooms = database.getReference("Rooms");
    private final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
    String curr_email = curr_user.getEmail();

    RoomAdapter adapter;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        // Enable back button on ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        List<Room> rooms = new ArrayList<>();
        getDB(databaseRooms, rooms);


    } //onCreate()

    //Get data from DB with ListenerForSingleValueEvent
    public List<Room> getDB(Query query, List<Room> list) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        if (room.getSolver().equals(curr_email) || room.getPoster().equals(curr_email))
                            if (room.isStatus()) list.add(room);
                        query.removeEventListener(this);

                    }
                    showRooms(list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        }); //Get User's Post
        return list;
    } //getDB()

    public void showRooms(List<Room> rooms) {
        RecyclerView recyclerView = findViewById(R.id.rvRoom);

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llManager);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), llManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        adapter = new RoomAdapter(this, rooms);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    } //showRooms()


    @Override
    public void onItemClick(View view, int position) {

        // Send data to ChatActivity
        Intent i = new Intent(RoomActivity.this, ChatActivity.class);
        i.putExtra("timestamp", adapter.getItem(position).getProblemTime());
        startActivity(i);
    }

    @Override //Back Button
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}