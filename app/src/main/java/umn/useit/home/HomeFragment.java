package umn.useit.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import umn.useit.DetailActivity;
import umn.useit.R;
import umn.useit.model.Problem;
import umn.useit.model.User;

public class HomeFragment extends Fragment implements HomeCardAdapter.ItemClickListener {

    //Firebase Database
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseUsers = database.getReference("Users");
    private final DatabaseReference databaseProblems = database.getReference("Problems");
    private final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();

    String email = curr_user.getEmail();

    //Adapter
    HomeCardAdapter adapter;

    //Models
    User user;

    int askedCounter;
    boolean askedCount = true;


    //GUI
    private TextView welcome;
    private TextView level;
    private TextView asked;
    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // GUI Init
        welcome = Objects.requireNonNull(getView()).findViewById(R.id.welcome);
        level = Objects.requireNonNull(getView()).findViewById(R.id.level);
        asked = Objects.requireNonNull(getView()).findViewById(R.id.asked);
        nestedScrollView = getView().findViewById(R.id.n_scrollview);
        swipeRefresh = getView().findViewById(R.id.swipeRefreshLayout);

        email = email.substring(0, email.indexOf("@"));

        /* Get current user firstname and lastname */
        String curr_userUid = curr_user.getUid();
        DatabaseReference userRow = databaseUsers.child(curr_userUid);
        userRow.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                User user = mutableData.getValue(User.class);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null)
                    welcome.setText(String.format("Hi, %s!", user.getFirstname()));
            }
        });

        /* Retrieve updated data on swipe refresh */
        swipeRefresh.setColorSchemeResources(R.color.teal);
        swipeRefresh.setOnRefreshListener(() -> {
            databaseProblems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Problem> problems = new ArrayList<>();

                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Problem problem = dataSnapshot.getValue(Problem.class);
                            if(problem.isAvailable()) problems.add(problem);

                        }
                        showCards(problems);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
            new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false), 2000);
        });

        shimmerLoading(3);
    } //onViewCreated()

    public void shimmerLoading(int count) {
        /*Add shimmer loading effect on start*/
        LinearLayout linearLayout = getView().findViewById(R.id.ll_shimmer);
        if (count < 3) {
            for (int i = 0; i < count; i++) {
                View child = getLayoutInflater().inflate(R.layout.home_card_shimmer, null);
                linearLayout.addView(child);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                View child = getLayoutInflater().inflate(R.layout.home_card_shimmer, null);
                linearLayout.addView(child);
            }
        }
    }// shimmerLoading

    /*  Show cards on Homescreen using RecyclerView. Firebase doesn't provide descending order query,
        so I will have to reverse the list order. */
    public void showCards(List<Problem> problems) {
        for (Problem problem : problems) {
            if (problem.getPoster().equals(email) && askedCount) {
                askedCounter++;
            }
        }
        askedCount = false;

        asked.setText(String.valueOf(askedCounter));
        RecyclerView recyclerView = getView().findViewById(R.id.rvHomeCard);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HomeCardAdapter(getActivity(), problems);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    } //showCards()

    /* Increment view if a post is viewed. Send title and desc to detail via intent. */
    @Override
    public void onItemClick(View view, int position) {
        incrementView(position);

        // Send data to DetailActivity
        Intent i = new Intent(getActivity(), DetailActivity.class);
        i.putExtra("title", adapter.getItem(position).getTitleProblem());
        i.putExtra("desc", adapter.getItem(position).getDescProblem());
        i.putExtra("poster", adapter.getItem(position).getPoster());
        i.putExtra("timestamp", adapter.getItem(position).getTime());
        startActivity(i);
    }

    public void incrementView(int position) {
        long time = adapter.getItem(position).getTime();
        int seen = adapter.getItem(position).getSeen() + 1;

        databaseProblems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Problem problem = snapshot.getValue(Problem.class);
                    if (problem.getTime() == time) {
                        problem.setSeen(seen);
                        databaseProblems.child(snapshot.getKey()).setValue(problem);
                        databaseProblems.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

    } //incrementView();

    public void scrollToTop() {
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
    }

}
