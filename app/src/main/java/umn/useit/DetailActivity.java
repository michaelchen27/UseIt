package umn.useit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
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

import umn.useit.model.ChatMessage;
import umn.useit.model.Problem;
import umn.useit.model.Room;

public class DetailActivity extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseRooms = db.getReference().child("Rooms");
    private final DatabaseReference databaseProblems = db.getReference().child("Problems");
    private final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
    String curr_email = curr_user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);

//        String username = curr_email.substring(0, curr_email.indexOf('@'));

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String poster = intent.getStringExtra("poster");
        long time = intent.getLongExtra("timestamp", 0);

        TextView problemDesc = findViewById(R.id.problem_desc);
        problemDesc.setText(desc);
        toolBarLayout.setTitle(title);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        if(curr_email.equals(poster)) {
            fab.setText("Waiting for Solver");
            fab.setBackgroundColor(Color.LTGRAY);
            fab.setTextColor(Color.BLACK);
            fab.setIconResource(R.drawable.ic_clock);
            fab.setEnabled(false);
        }

        fab.setOnClickListener(view -> {
            List<ChatMessage> chatMessages = new ArrayList<>();
            /* Populate dummy data TODO: Delete this later*/

            ChatMessage cm = new ChatMessage("Can you help me with \""+ title + "\" ?", poster);
            chatMessages.add(cm);
            ChatMessage cm2 = new ChatMessage("Sure thing!", curr_email);
            chatMessages.add(cm2);

            /* Add room to Firebase DB */
            Room room = new Room(curr_email, poster, title, true);

            if (!curr_email.equals(poster)) {
                databaseRooms.push().setValue(room, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        String uniqueKey = ref.getKey();
                        databaseRooms.child(uniqueKey).child("chats").setValue(chatMessages);
                    }
                });
            }


            databaseProblems.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Problem problem = snapshot.getValue(Problem.class);
                        if (problem.getTime() == time) {
                            problem.setAvailable(false);
                            databaseProblems.child(snapshot.getKey()).setValue(problem);
                            databaseProblems.removeEventListener(this);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


            finish();
            startActivity(new Intent(DetailActivity.this, ChatActivity.class));
        });
    }
}