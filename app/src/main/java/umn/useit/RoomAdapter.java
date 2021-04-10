package umn.useit.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import umn.useit.R;
import umn.useit.model.Room;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private final List<Room> mData;
    private final LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    //Constructor
    public RoomAdapter(Context context, List<Room> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //Inflates card layout(home_card.xml) to the ViewHolder (HomeCardViewHolder).
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.room, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        Room room = mData.get(position);
        holder.senderName.setText(room.getSenderName());
        holder.previewMessage.setText(room.getPreviewMessage());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Convenient method for getting data at click position
    Room getItem(int id) {
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

        TextView senderName, previewMessage;
        List<Room> rooms;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name);
            previewMessage = itemView.findViewById(R.id.preview_message);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}