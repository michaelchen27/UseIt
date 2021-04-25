package umn.useit.prehome;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import umn.useit.home.DashboardActivity;
import umn.useit.R;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView signUpDirect = findViewById(R.id.sign_up_direct);
        Button login_button = findViewById(R.id.login_button);
        TextInputEditText mEmail = findViewById(R.id.email); //don't forget to change username -> email in XML too
        TextInputEditText mPassword = findViewById(R.id.password);
        TextView errormsg = findViewById(R.id.errormsg);

        // Enable back button on ActionBar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Spinner for loading Indicator
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        // FirebaseAuth instance init
        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required!");
                    return;
                }
                if (!email.matches(emailPattern)) {
                    mEmail.setError("Invalid email address!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required!");
                    return;
                }

                spinner.setVisibility(View.VISIBLE);
                login_button.setText("LOGGING IN...");
                //Copy pasta from Firebase Documentation
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            finish();
                            MainActivity.ac_main.finish(); //finish MainActivity so when loggedIn user in HomePage presses back, it quits the app.
                            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        } else {
                            spinner.setVisibility(View.GONE);
                            login_button.setText("LOGIN");
                            Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            errormsg.setText("Authentication failed");
                            updateUI(null);
                        }
                    } //onComplete()
                }); //addOnCompleteListener()
            } //onClick()
        }); //setOnClickListener()
    } //onCreate()

    @Override //Back Button
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Direct to Sign Up Page
    public void signUpDirect(View view) {
        startActivity(new Intent(this, SignUpActivity.class));
    }

    private void updateUI(FirebaseUser currentUser) {
    }

}
