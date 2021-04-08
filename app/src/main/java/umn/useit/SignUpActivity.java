package umn.useit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    private ProgressBar spinner;

    public String firstname, lastname, email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Enable back button on ActionBar

        TextInputEditText rEmail = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText rPassword = (TextInputEditText) findViewById(R.id.password);
        TextInputEditText rFirstname = (TextInputEditText) findViewById(R.id.firstname);
        TextInputEditText rLastname = (TextInputEditText) findViewById(R.id.lastname);
        Button register_button = (Button) findViewById(R.id.register);

        spinner = (ProgressBar)findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();

                firstname = rFirstname.getText().toString().trim();
                lastname = rLastname.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    rEmail.setError("Email is required!");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    rEmail.setError("Invalid email address!");
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    rPassword.setError("Password is required!");
                    return;
                }

                //Copy pasta from Firebase Documentation
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userUid = mAuth.getUid();
                                    updateUI(user);
                                    storeNewUserData(userUid, firstname, lastname, email);
                                    finish();
                                    MainActivity.ac_main.finish(); //finish MainActivity so when loggedIn user in HomePage presses back, it quits the app.
                                    Toast.makeText(SignUpActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Register failed, please try again.", Toast.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    updateUI(null);
                                }
                            } //onComplete()
                        }); //addOnCompleteListener()
            } //onClick()
        }); //setOnClickListener()
    } //onCreate()

    private void storeNewUserData(String userUid, String firstname, String lastname, String email) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbUser = db.getReference().child("Users");

        User user = new User(firstname, lastname, email);

        dbUser.child(userUid).setValue(user);
    }

    private void updateUI(FirebaseUser user) {
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
