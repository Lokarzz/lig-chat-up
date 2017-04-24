package com.lokarz.ligchatup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lokarz.ligchatup.R;
import com.lokarz.ligchatup.pojo.ChatMessage;

import java.util.List;

/**
 * Created by Lokarz on 4/24/2017.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatMessageViewHolder> {

    private List<ChatMessage> chatMessages;
    private String currentUser;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages, String currentUser) {
        this.context = context;
        this.chatMessages = chatMessages;
        this.currentUser = currentUser;
    }

    @Override
    public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_rv_chat_message, parent, false);
        ChatMessageViewHolder vh = new ChatMessageViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ChatMessageViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        holder.senderTv.setText(chatMessage.getSender());
        holder.messageTv.setText(chatMessage.getMessage());

        if(chatMessage.getSender().equals(currentUser)){
            holder.senderTv.setText("You");
            holder.messageContainerLl.setGravity(Gravity.RIGHT);
            holder.arrowLeftIv.setVisibility(View.GONE);
            holder.arrowRightIv.setVisibility(View.VISIBLE);
//            holder.messageTv.setBackground(context.getDrawable(R.drawable.message_out_bubble));
        }else{
            holder.messageContainerLl.setGravity(Gravity.LEFT);
            holder.arrowRightIv.setVisibility(View.GONE);
            holder.arrowLeftIv.setVisibility(View.VISIBLE);
//            holder.messageTv.setBackground(context.getDrawable(R.drawable.message_in_bubble));
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView senderTv, messageTv;
        private LinearLayout messageContainerLl;
        private ImageView arrowLeftIv, arrowRightIv;

        public ChatMessageViewHolder(View itemView) {
            super(itemView);

            senderTv = (TextView) itemView.findViewById(R.id.sender_tv);
            messageTv = (TextView) itemView.findViewById(R.id.message_tv);
            messageContainerLl = (LinearLayout) itemView.findViewById(R.id.message_container_ll);
            arrowLeftIv = (ImageView) itemView.findViewById(R.id.arrow_left_iv);
            arrowRightIv = (ImageView) itemView.findViewById(R.id.arrow_right_iv);

        }
    }
}
