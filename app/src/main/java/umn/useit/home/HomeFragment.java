package umn.useit.home;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    FragmentManager fm = getFragmentManager();

    HomeCardAdapter adapter;
    User user;

    private TextView welcome;
    private TextView level;
    private TextView asked;


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

        //Get All User's Post ============================================================================
        Query query = databaseProblems.orderByChild("date");
        List<Problem> problems = new ArrayList<>();
        problems = getDB(query, problems);
        showCards(problems);
        sendTotal(problems.size());


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
        recyclerView.setAdapter(adapter);
    } //showCards()

    //Save total post in local data. AskFragment will use the total number as post ID
    public void sendTotal(int total) {
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .edit()
                .putInt("postTotal", total).apply(); //TODO: fix the cleared DB bug!
    } //sendTotal()

    //Get data from DB with ListenerForSingleValueEvent
    public List<Problem> getDB(Query query, List<Problem> list) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Problem problem = dataSnapshot.getValue(Problem.class);
                        list.add(problem);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }); //Get User's Post
        return list;
    } //getDB()


    //Increment view if a post is viewed. Send title and desc to detail via intent.
    @Override
    public void onItemClick(View view, int position) {
        String title = adapter.getItem(position).getTitleProblem();
        String desc = adapter.getItem(position).getDescProblem();
        int id = adapter.getItem(position).getId();
        int seen = adapter.getItem(position).getSeen();
        seen++;

        databaseProblems.child(String.valueOf(id)).child("seen").setValue(seen);
//        Toast.makeText(getActivity(), adapter.getItem(position).getTitleProblem() + " viewed", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        startActivity(intent);
    }

}
