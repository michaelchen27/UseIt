package umn.useit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.HomeCardViewHolder> {
    private List<String> mData;
    private LayoutInflater mInflater;

    //Interface is defined at the bottom.
    private ItemClickListener mClickListener;

    //Constructor
    HomeCardAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //Inflates card layout(home_card.xml) to the ViewHolder (HomeCardViewHolder).
    @Override
    public HomeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.home_card, parent, false);
        return new HomeCardViewHolder(view);
    }

    //Binds data to the Card in each row
    @Override
    public void onBindViewHolder(@NonNull HomeCardViewHolder holder, int position) {
        String title_problem = mData.get(position);
        holder.tvTitleProblem.setText(title_problem);
    }

    //Total number of cards/rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Store and recycler the views as they are scrolled off screen.
    public class HomeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitleProblem;

        public HomeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleProblem = itemView.findViewById(R.id.title_problem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());

        }
    }

    String getItem(int id) {
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
}

