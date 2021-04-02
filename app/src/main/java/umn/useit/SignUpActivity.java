package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // Enable back button on ActionBar

        TextInputEditText rEmail = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText rPassword = (TextInputEditText) findViewById(R.id.password);
        Button register_button = (Button) findViewById(R.id.register);

        spinner = (ProgressBar)findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString();

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

                spinner.setVisibility(View.VISIBLE);
                //Copy pasta from Firebase Documentation
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                    finish();
                                    MainActivity.fa_main.finish(); //finish MainActivity so when loggedIn user in HomePage presses back, it quits the app.
                                    Toast.makeText(SignUpActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                                    spinner.setVisibility(View.GONE);
                                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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
