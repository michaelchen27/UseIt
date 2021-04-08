package umn.useit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import umn.useit.model.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseChat = db.getReference().child("Chats");
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String curr_email = currentUser.getEmail();


    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        int index = curr_email.indexOf('@');
        String username = curr_email.substring(0, index);

        //GUI
        EditText input = findViewById(R.id.input);
        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setEnabled(false);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fab.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseChat
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(), username));
                input.setText("");
            }
        });
        displayChatMessages();
    } //onCreate()

    private void displayChatMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.message, databaseChat) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText = v.findViewById(R.id.message_text);
                TextView messageUser = v.findViewById(R.id.message_user);
                TextView messageTime = v.findViewById(R.id.message_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("hh:mm a", model.getMessageTime()));

            }
        };

        listOfMessages.setAdapter(adapter);
    }


}