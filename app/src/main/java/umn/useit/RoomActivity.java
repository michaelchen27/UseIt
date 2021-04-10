package umn.useit;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import umn.useit.home.RoomAdapter;
import umn.useit.model.Room;

public class RoomActivity extends AppCompatActivity implements RoomAdapter.ItemClickListener {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRooms = database.getReference("Rooms");

    RoomAdapter adapter;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        List<Room> rooms = new ArrayList<>();
//        rooms = getDB(databaseRooms, rooms);
        Room room = new Room("Michael", "Hello, dis is harcoded!");
        rooms.add(room);
        room = new Room("William", "Hello, dis is william hard-coded!");
        rooms.add(room);

        showRooms(rooms);
    } //onCreate()

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

    //Get data from DB with ListenerForSingleValueEvent
    public List<Room> getDB(Query query, List<Room> list) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Room room = dataSnapshot.getValue(Room.class);
                        list.add(room);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }); //Get User's Post
        return list;
    } //getDB()

    @Override
    public void onItemClick(View view, int position) {
        //TODO: When room chat is clicked, show all messages between 2 users.

    }
}