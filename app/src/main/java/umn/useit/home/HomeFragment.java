package umn.useit.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
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

    //Firebase
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseUsers = database.getReference("Users");
    private final DatabaseReference databaseProblems = database.getReference("Problems");
    private final FragmentManager fm = getFragmentManager();
    //Adapter
    HomeCardAdapter adapter;
    //Models
    User user;
    Problem problem;
    String key;

    private SwipeRefreshLayout swipeRefresh;
    //GUI
    private TextView welcome;
    private TextView level;
    private TextView asked;
    private NestedScrollView nestedScrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //GUI Init
        welcome = Objects.requireNonNull(getView()).findViewById(R.id.welcome);
        level = Objects.requireNonNull(getView()).findViewById(R.id.level);
        asked = Objects.requireNonNull(getView()).findViewById(R.id.asked);
        nestedScrollView = getView().findViewById(R.id.n_scrollview);
        swipeRefresh = getView().findViewById(R.id.swipeRefreshLayout);

        // Add shimmer
        ShimmerFrameLayout shimmerFrameLayout = getView().findViewById(R.id.shimmerLayout);
        LinearLayout linearLayout = getView().findViewById(R.id.ll_shimmer);
        View child = getLayoutInflater().inflate(R.layout.home_card_shimmer, null);
        linearLayout.addView(child);

        // Init =============================================================================
        FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();

        String curr_userUid = curr_user.getUid();
        DatabaseReference userRow = databaseUsers.child(curr_userUid);

        //Get user =======================================================================================
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
                    welcome.setText(String.format("Hi, %s %s!", user.getFirstname(), user.getLastname()));
            }
        }); //runTransaction()

        swipeRefresh.setColorSchemeResources(R.color.teal);
        swipeRefresh.setOnRefreshListener(() -> {
            databaseProblems.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<Problem> problems = new ArrayList<>();
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Problem problem = dataSnapshot.getValue(Problem.class);
                            problems.add(problem);
                        }
                            showCards(problems);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
            new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false), 2000);
        });
    } //onViewCreated()

    /*  Show cards on Homescreen using RecyclerView. Firebase doesn't provide descending order query,
        so I will have to reverse the list order.*/
    public void showCards(List<Problem> problems) {
        RecyclerView recyclerView = getView().findViewById(R.id.rvHomeCard);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new HomeCardAdapter(getActivity(), problems);
        adapter.setClickListener(this);
        sendTotal(adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    } //showCards()

    //Save total post in local data. AskFragment will use the total number as post ID
    public void sendTotal(int total) {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("postTotal", total).apply(); //TODO: fix the cleared DB bug!
    } //sendTotal()

    //Increment view if a post is viewed. Send title and desc to detail via intent.
    @Override
    public void onItemClick(View view, int position) {
        incrementSeen(position);
        //Send data to DetailActivity
        String title = adapter.getItem(position).getTitleProblem();
        String desc = adapter.getItem(position).getDescProblem();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        startActivity(intent);
    }

    public void incrementSeen(int position) {
        long time = adapter.getItem(position).getTime();
        int seen = adapter.getItem(position).getSeen() + 1;

        databaseProblems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Problem problem = snapshot.getValue(Problem.class);
                    if (problem.getTime() == time) {
                        key = snapshot.getKey();
                        problem.setSeen(seen);
                        databaseProblems.child(key).setValue(problem);
                        databaseProblems.removeEventListener(this);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void scrollToTop() {
        nestedScrollView.fullScroll(NestedScrollView.FOCUS_UP);
    }

}
