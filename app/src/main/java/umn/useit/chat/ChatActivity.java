package umn.useit.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import umn.useit.R;
import umn.useit.home.DashboardActivity;
import umn.useit.model.ChatMessage;
import umn.useit.model.Problem;
import umn.useit.model.Room;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.ItemClickListener {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRooms = db.getReference().child("Rooms");
    private final DatabaseReference databaseProblems = db.getReference("Problems");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String curr_email = currentUser.getEmail();


    long time;
    String id;

    TextView poster;
    ExtendedFloatingActionButton fab;
    EditText input;

    ChatAdapter chatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Enable back button on ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //GUI
        input = findViewById(R.id.input);
        poster = findViewById(R.id.poster);
        fab = findViewById(R.id.fab);
        fab.setEnabled(false);

        /* Catch Intent */
        Intent intent = getIntent();
        time = intent.getLongExtra("timestamp", 0);
        id = intent.getStringExtra("id");

        /* Disable send button if edit text is empty */
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fab.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        storeMessage(time);

        // If an Android device is using keyboard, the Enter button will send the message.
        input.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) && (!input.getText().toString().equals(""))) {
                fab.callOnClick();
            }
            return false;
        });


    } //onCreate()

    /* Menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void showChats(List<ChatMessage> chats) {
        RecyclerView recyclerView = findViewById(R.id.rvChatList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        chatAdapter = new ChatAdapter(this, chats);
        recyclerView.setAdapter(chatAdapter);

        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                recyclerView.post(() -> recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1));
            }
        });


    } //showChats()

    public void storeMessage(long time) {
        databaseRooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    if (room.getProblemTime() == time) {

                        if (getSupportActionBar() != null) {
                            if (curr_email.equals(room.getSolver()))
                                getSupportActionBar().setTitle(room.getPoster().substring(0, room.getPoster().indexOf('@')));
                            else
                                getSupportActionBar().setTitle(room.getSolver().substring(0, room.getSolver().indexOf('@')));
                        }

                        String key = snapshot.getKey();

                        /* Send message button, if EditText is empty, the send button is disabled. */
                        fab.setOnClickListener(view -> {
                            databaseRooms.child(key).child("chats")
                                    .push()
                                    .setValue(new ChatMessage(input.getText().toString(), curr_email));
                            input.setText("");
                        });

                        DatabaseReference databaseChats = databaseRooms.child(key);

                        databaseChats.child("chats").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                List<ChatMessage> chatMessages = new ArrayList<>();
                                if (snapshot2.exists()) {
                                    for (DataSnapshot dataSnapshot2 : snapshot2.getChildren()) {
                                        ChatMessage chatMessage = dataSnapshot2.getValue(ChatMessage.class);
                                        chatMessages.add(chatMessage);
                                    }
                                    showChats(chatMessages);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
    }

    @Override //Back Button
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.nav_solved:
                removeRoom();
                break;
            case R.id.nav_not_solved:
                restorePost(id);
                removeRoom();

        }

        return super.onOptionsItemSelected(item);
    }

    public void removeRoom() {
        databaseRooms.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Room room = snapshot.getValue(Room.class);
                    if (room.getProblemTime() == time) {
                        room.setStatus(false);
                        databaseRooms.child(snapshot.getKey()).setValue(room);
                        databaseRooms.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        startActivity(new Intent(ChatActivity.this, DashboardActivity.class));
    }


    // Post shows up in Home again.
    public void restorePost(String id) {

        databaseProblems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for ( DataSnapshot snapshot2 : dataSnapshot.getChildren()){
                    Problem prob = snapshot2.getValue(Problem.class);
                    if(prob.getId() == id || prob.getTime() == time){
                        prob.setAvailable(true);
                        databaseProblems.child(snapshot2.getKey()).setValue(prob);
                        databaseProblems.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
