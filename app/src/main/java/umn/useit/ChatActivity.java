package umn.useit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        //Display message sender using stripped email.
//        int index = curr_email.indexOf('@');
//        String username = curr_email.substring(0, index);

        //Send message button, if EditText is empty, the send button is disabled.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseChat
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(), curr_email));
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

                int index = curr_email.indexOf('@');
                String username = curr_email.substring(0, index);

                //TODO: Color changed when content in scroll view is not visible.
                if (model.getMessageUser().equals(username))
                    messageText.setBackgroundColor(v.getResources().getColor(R.color.light_teal));

                messageUser.setText(model.getMessageUser());
                messageText.setText(model.getMessageText());
                messageTime.setText(DateFormat.format("hh:mm a", model.getMessageTime()));

            }
        };
        listOfMessages.setAdapter(adapter);
    } //displayChatMessages()

    @Override
    protected void onStart() {
        super.onStart();
        displayChatMessages();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayChatMessages();
    }
}