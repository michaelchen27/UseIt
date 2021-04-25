package umn.useit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

import umn.useit.model.Problem;
import umn.useit.prehome.MainActivity;

import static android.app.Activity.RESULT_OK;

//testing my github skills
public class AskFragment extends Fragment {

    private final FirebaseDatabase db = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseProblems = db.getReference().child("Problems");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public Uri imageUri;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView problemPic;
    private ImageView display;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_ask, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText etTitleProblem = Objects.requireNonNull(getView()).findViewById(R.id.TitleProblem);
        EditText etProblemDesc = Objects.requireNonNull(getView()).findViewById(R.id.ProblemDesc);
        Button btnSubmit = Objects.requireNonNull(getView()).findViewById(R.id.SubmitProblem);
        ImageView problemPic = Objects.requireNonNull(getView()).findViewById(R.id.AddPhoto);
        btnSubmit.setEnabled(false);

        etTitleProblem.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSubmit.setEnabled(s.toString().trim().length() != 0);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title_problem = etTitleProblem.getText().toString();
                String problem_desc = etProblemDesc.getText().toString();
                String user_email = mAuth.getCurrentUser().getEmail();
                long date = System.currentTimeMillis();

                //String key = databaseProblems.push().setValue(problem).getKey();

                if (imageUri != null) {
                    ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading");
                    progressDialog.show();


                    DatabaseReference pushedItem = databaseProblems.push();
                    String key = pushedItem.getKey();
                    StorageReference ref = storageReference.child("images/"+key+"img");
                    String imgUrl = String.valueOf(ref.getDownloadUrl());
                    Problem problem = new Problem(title_problem, problem_desc, user_email, date, 0, true, key);
                    problem.setImgUrl(imgUrl);
                    pushedItem.setValue(problem);

                    ref.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (int) progress + "%");
                        }
                    });
                }else{
                    DatabaseReference pushedItem = databaseProblems.push();
                    String key = pushedItem.getKey();
                    Problem problem = new Problem(title_problem, problem_desc, user_email, date, 0, true, key);
                    pushedItem.setValue(problem);
                }

                startActivity(new Intent(getActivity(), SucceedActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            }
        });

        problemPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
    } //onViewCreated()

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView display = Objects.requireNonNull(getView()).findViewById(R.id.displayPhoto);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            display.setImageURI(imageUri);
        }
    }
}
