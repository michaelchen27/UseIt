package umn.useit;

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
import umn.useit.model.ChatMessage;
import umn.useit.model.Room;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> mData;
    private final LayoutInflater mInflater;

    private ItemClickListener mClickListener;

    private final FirebaseUser curr_user = FirebaseAuth.getInstance().getCurrentUser();
    String curr_email = curr_user.getEmail();

    //Constructor
    public ChatAdapter(Context context, List<ChatMessage> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = mData.get(position);
        holder.message_user.setText(chatMessage.getMessageUser());
        holder.message_text.setText(chatMessage.getMessageText());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // Convenient method for getting data at click position
    public ChatMessage getItem(int id) {
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
    public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView message_user, message_text;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            message_user = itemView.findViewById(R.id.message_user);
            message_text = itemView.findViewById(R.id.message_text);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}