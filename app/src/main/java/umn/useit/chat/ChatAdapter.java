package umn.useit.chat;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import umn.useit.R;
import umn.useit.model.ChatMessage;
import umn.useit.model.Room;

public class ChatAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
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

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = (ChatMessage) mData.get(position);

        if (message.getMessageUser().equals(curr_email)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = mInflater.inflate(R.layout.message_right, parent, false);
            return new SentMessageHolder(view);

        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = mInflater.inflate(R.layout.message, parent, false);
            return new ReceivedMessageHolder(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = mData.get(position);

        switch(holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:

                ((SentMessageHolder) holder).bind(message);

                break;

            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);

        }
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

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        TextView message_user, message_text, message_time;

        SentMessageHolder(View itemView) {
            super(itemView);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);

            //bind views
        }

        void bind(ChatMessage message) {
            message_text.setText(message.getMessageText());
            message_time.setText(DateFormat.format("hh:mm a", message.getMessageTime()));
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        TextView message_user, message_text, message_time;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            message_user = itemView.findViewById(R.id.message_user);
            message_text = itemView.findViewById(R.id.message_text);
            message_time = itemView.findViewById(R.id.message_time);

            //bind views
        }

        void bind(ChatMessage message) {
            //set values
            message_user.setText(message.getMessageUser().substring(0, message.getMessageUser().indexOf('@')));
            message_text.setText(message.getMessageText());
            message_time.setText(DateFormat.format("hh:mm a", message.getMessageTime()));
        }
    }
}