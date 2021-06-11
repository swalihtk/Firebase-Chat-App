package io.swalitk.github.firebasechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;

import org.json.JSONObject;

import io.swalitk.github.firebasechatapp.Middlewares.MySingleton;

public class Register extends AppCompatActivity {

    EditText et_username, et_password, et_email;
    Button bt_register;
    TextView tw_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username=findViewById(R.id.et_rgstr_username);
        et_password=findViewById(R.id.et_rgstr_password);
        et_email=findViewById(R.id.et_rgstr_email);
        bt_register=findViewById(R.id.btn_rgstr_registerButton);
        tw_login=findViewById(R.id.tw_rgstr_login);

        Firebase.setAndroidContext(this);

        tw_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username_text=et_username.getText().toString();
                String password_text=et_password.getText().toString();
                String email_text=et_email.getText().toString();

                if(TextUtils.isEmpty(username_text) || TextUtils.isEmpty(password_text) || TextUtils.isEmpty(email_text)){
                    Toast.makeText(Register.this, "Please fill all fields..", Toast.LENGTH_SHORT).show();
                }else{
                    ProgressDialog pd=new ProgressDialog(Register.this);
                    pd.setMessage("Registering...");
                    pd.show();

                    String url="https://messenger-clone-3775a-default-rtdb.firebaseio.com/users.json";
                    StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equals("null")){
                                Firebase reference=new Firebase("https://messenger-clone-3775a-default-rtdb.firebaseio.com/users");
                                reference.child(username_text).child("email").setValue(email_text);
                                reference.child(username_text).child("password").setValue(password_text);
                                Toast.makeText(Register.this, "Registerd", Toast.LENGTH_SHORT).show();
                            }else{
                                try{
                                    JSONObject obj=new JSONObject(response);
                                    if(!obj.has(username_text) && !obj.has(email_text)){
                                        Firebase reference=new Firebase("https://messenger-clone-3775a-default-rtdb.firebaseio.com/users");
                                        reference.child(username_text).child("email").setValue(email_text);
                                        reference.child(username_text).child("password").setValue(password_text);
                                        Toast.makeText(Register.this, "Registerd", Toast.LENGTH_SHORT).show();
                                    }else if(obj.has(username_text)){
                                        Toast.makeText(Register.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                                    }else if(obj.getJSONObject(username_text).getString("email").equals(email_text)){
                                        Toast.makeText(Register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Register.this, "Something error happened", Toast.LENGTH_SHORT).show();
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
                        }
                    });
                    MySingleton.getInstance(Register.this).addToRequestQueue(request);
                }
            }
        });

    }
}