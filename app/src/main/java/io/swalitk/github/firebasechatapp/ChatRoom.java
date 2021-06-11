package io.swalitk.github.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

import io.swalitk.github.firebasechatapp.Middlewares.UserDetails;

public class ChatRoom extends AppCompatActivity {

    ScrollView scrollView;
    LinearLayout layout;
    TextView username_text;
    EditText et_message_text;
    ImageView img_sendButton;

    Firebase reference1, reference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        scrollView=findViewById(R.id.chat_scroll_view);
        layout=findViewById(R.id.chat_linearLayout);
        username_text=findViewById(R.id.chat_username);
        et_message_text=findViewById(R.id.chat_message_area);
        img_sendButton=findViewById(R.id.chat_sendButton);

        username_text.setText(UserDetails.chatWith);
        Firebase.setAndroidContext(ChatRoom.this);

        reference1=new Firebase("https://messenger-clone-3775a-default-rtdb.firebaseio.com/messages/"+UserDetails.username+"_"+UserDetails.chatWith);
        reference2=new Firebase("https://messenger-clone-3775a-default-rtdb.firebaseio.com/messages/"+UserDetails.chatWith+"_"+UserDetails.username);

        img_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message_text=et_message_text.getText().toString();

                if(!TextUtils.isEmpty(message_text)){
                    HashMap<String, String> map=new HashMap<>();
                    map.put("username", UserDetails.username);
                    map.put("message", message_text);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    et_message_text.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map=dataSnapshot.getValue(Map.class);
                String username=map.get("username").toString();
                String message=map.get("message").toString();

                if(username.equals(UserDetails.username)){
                    chatMessageSetup("You:\n"+message, 1);
                }else{
                    chatMessageSetup(UserDetails.chatWith+":\n"+message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void chatMessageSetup(String message, int pos){
       TextView textView=new TextView(ChatRoom.this);
       LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
       textView.setPadding(5,5,5,5);

       textView.setTextSize(16);
       if(pos == 1){
           textView.setBackgroundResource(R.drawable.username_border);
           lp.setMargins(580,0,0,10);
       }else{
           textView.setBackgroundResource(R.drawable.chat_with_border);
           lp.setMargins(3,0,0,10);
       }
        textView.setLayoutParams(lp);
        textView.setText(message);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}