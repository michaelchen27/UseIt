package umn.useit.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import umn.useit.R;
import umn.useit.model.Room;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private final List<Room> mData;
    private final LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    private final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
    String curr_email = curr_user.getEmail();

    //Constructor
    public RoomAdapter(Context context, List<Room> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = mData.get(position);
        holder.problem_title.setText(room.getProblemTitle());
        if(room.getPoster().equals(curr_email)) {
            holder.poster.setText("me");
        } else {
            holder.poster.setText(room.getPoster().substring(0, room.getPoster().indexOf("@")));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Convenient method for getting data at click position
    public Room getItem(int id) {
        return mData.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    //ItemClicKListener
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    //Store and recycler the views as they are scrolled off screen.
    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView problem_title, poster;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            problem_title = itemView.findViewById(R.id.problem_title);
            poster = itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}