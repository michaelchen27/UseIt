package umn.useit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = findViewById(R.id.toolbar_layout);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        TextView problemDesc = findViewById(R.id.problem_desc);
        problemDesc.setText(desc);
        toolBarLayout.setTitle(title);

        ExtendedFloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(DetailActivity.this, ChatActivity.class));
        });
    }
}