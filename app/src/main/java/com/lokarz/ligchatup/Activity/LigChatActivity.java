package com.lokarz.ligchatup.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lokarz.ligchatup.R;
import com.lokarz.ligchatup.adapter.ChatMessageAdapter;
import com.lokarz.ligchatup.pojo.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class LigChatActivity extends BaseActivity implements View.OnClickListener {

    private Button sendBtn, logoutBtn;
    private EditText messageEt;
    private String userName;
    private RecyclerView chatMessageRv;
    private ChatMessageAdapter chatMessageAdapter;
    private DatabaseReference databaseReference;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lig_chat);

        chatMessageRv = (RecyclerView) findViewById(R.id.chat_messages_rv);
        logoutBtn = (Button) findViewById(R.id.logout_btn);
        sendBtn = (Button) findViewById(R.id.send_btn);
        messageEt = (EditText) findViewById(R.id.message_et);

        sendBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        messageEt.addTextChangedListener(textWatcher());

        userName = firebaseAuth.getCurrentUser().getDisplayName();

        databaseReference = FirebaseDatabase.getInstance().getReference("chatStorage");

        initChatMessageRv();
    }

    private TextWatcher textWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0){
                    sendBtn.setEnabled(false);
                }else{
                    sendBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    public void initChatMessageRv(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        chatMessageRv.setLayoutManager(layoutManager);

        chatMessages = new ArrayList<ChatMessage>();

        databaseReference.keepSynced(true);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    ChatMessage chatMessage = postSnapshot.getValue(ChatMessage.class);
                    chatMessages.add(chatMessage);
                }
                findViewById(R.id.progress).setVisibility(View.GONE);
                chatMessageAdapter.notifyDataSetChanged();
                chatMessageRv.smoothScrollToPosition(chatMessages.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatMessageAdapter = new ChatMessageAdapter(this, chatMessages, userName);
        chatMessageRv.setAdapter(chatMessageAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_btn:
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setSender(userName);
                chatMessage.setMessage(messageEt.getText().toString());
                databaseReference.push().setValue(chatMessage);

                messageEt.setText("");
                break;
            case R.id.logout_btn:
                logOutUser();
                break;
        }
    }

    private void logOutUser(){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_exit_message))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        logOutUser();
    }
}
