package umn.useit.home;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import umn.useit.R;
import umn.useit.model.Problem;

public class ProfileCardAdapter extends RecyclerView.Adapter<ProfileCardAdapter.HomeCardViewHolder> {
    private final List<Problem> mData;
    private final LayoutInflater mInflater;

    //Interface is defined at the bottom.
    private ItemClickListener mClickListener;

    //Constructor
    public ProfileCardAdapter(Context context, List<Problem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //Inflates card layout(home_card.xml) to the ViewHolder (HomeCardViewHolder).
    @NonNull
    @Override
    public HomeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.profile_card, parent, false);
        return new HomeCardViewHolder(view);
    }

    //Binds data to the Card in each row
    @Override
    public void onBindViewHolder(@NonNull HomeCardViewHolder holder, int position) {
        Problem problem = mData.get(position);
        holder.tvTitleProblem.setText(problem.getTitleProblem());
        holder.tvPoster.setText(problem.getPoster().substring(0, problem.getPoster().indexOf('@')));
        holder.tvDate.setText(DateFormat.format("dd MMM yy · hh:mm a", problem.getTime()));
        holder.tvSeen.setText(String.valueOf(problem.getSeen()));
    }

    //Total number of cards/rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    Problem getItem(int id) {
        return mData.get(id);
    }

    //Allow click events to be caught.
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    //ItemClicKListener
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    //Store and recycler the views as they are scrolled off screen.
    public class HomeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitleProblem, tvPoster, tvDate, tvSeen;

        public HomeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleProblem = itemView.findViewById(R.id.title_problem);
            tvPoster = itemView.findViewById(R.id.poster);
            tvDate = itemView.findViewById(R.id.date);
            tvSeen = itemView.findViewById(R.id.seen);
        }

        @Override
        public void onClick(View view) {

        }
    }
}

