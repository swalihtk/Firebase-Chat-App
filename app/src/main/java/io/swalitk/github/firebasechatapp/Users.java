package io.swalitk.github.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import io.swalitk.github.firebasechatapp.Middlewares.MySingleton;
import io.swalitk.github.firebasechatapp.Middlewares.UserAdapter;
import io.swalitk.github.firebasechatapp.Middlewares.UserDetails;

public class Users extends AppCompatActivity {

    TextView tw_noUser, tw_demo;
    ListView lw_users;
    ArrayList<String> all_users=new ArrayList<>();
    int total_users=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        tw_noUser=findViewById(R.id.noUsersText);
        lw_users=findViewById(R.id.usersList);
        //tw_demo=findViewById(R.id.demo_id);

        ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Loading users..");
        pd.show();

        String url="https://messenger-clone-3775a-default-rtdb.firebaseio.com/users.json";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doOnSuccess(response);
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        });
        MySingleton.getInstance(Users.this).addToRequestQueue(request);
    }

    public void doOnSuccess(String response){
        try{
            JSONObject object=new JSONObject(response);
            Iterator i=object.keys();
            String keys="";

            while(i.hasNext()){
                keys=i.next().toString();
                if(!keys.equals(UserDetails.username)){
                    all_users.add(keys);
                }
                total_users++;
            }

            if(total_users <=1){
                tw_noUser.setVisibility(View.VISIBLE);
                lw_users.setVisibility(View.GONE);
            }else{
                tw_noUser.setVisibility(View.GONE);
                lw_users.setVisibility(View.VISIBLE);
                UserAdapter adapter=new UserAdapter(Users.this, R.layout.custom_users_layout, all_users);
                lw_users.setAdapter(adapter);
                //lw_users.setAdapter(new ArrayAdapter<String>(Users.this, android.R.layout.simple_list_item_1, all_users));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}