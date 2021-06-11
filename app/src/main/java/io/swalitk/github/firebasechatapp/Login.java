package io.swalitk.github.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;

import org.json.JSONObject;

import io.swalitk.github.firebasechatapp.Middlewares.MySingleton;
import io.swalitk.github.firebasechatapp.Middlewares.UserDetails;

public class Login extends AppCompatActivity {

    EditText et_username, et_password;
    Button bt_login;
    TextView tw_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username=findViewById(R.id.et_lgin_username);
        et_password=findViewById(R.id.et_lgin_password);
        bt_login=findViewById(R.id.btn_lgn_loginButton);
        tw_register=findViewById(R.id.tw_lgn_register);

        Firebase.setAndroidContext(this);

        tw_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=et_username.getText().toString();
                String password=et_password.getText().toString();
                if(username.equals("") || password.equals("")){
                    Toast.makeText(Login.this, "Please fill..", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog pd=new ProgressDialog(Login.this);
                    pd.setMessage("Loging in..");
                    pd.show();
                    String url="https://messenger-clone-3775a-default-rtdb.firebaseio.com/users.json";
                    StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("null")){
                                Toast.makeText(Login.this,"User not found", Toast.LENGTH_SHORT).show();
                            }else{
                                try{
                                    JSONObject obj=new JSONObject(response);
                                    if(!obj.has(username)){
                                        Toast.makeText(Login.this, "Username not found", Toast.LENGTH_SHORT).show();
                                    }else if(obj.getJSONObject(username).getString("password").equals(password)){
                                        UserDetails.username=username;
                                        UserDetails.password=password;
                                        startActivity(new Intent(Login.this, Users.class));
                                    }else{
                                        Toast.makeText(Login.this, "Password error", Toast.LENGTH_SHORT).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("myKey", "Error: "+error.toString());
                            Toast.makeText(Login.this, "Something error happened", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                    MySingleton.getInstance(Login.this).addToRequestQueue(request);
                }

            }
        });
    }
}