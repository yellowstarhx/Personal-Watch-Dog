package com.ee542.PNSS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.btn_login);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
//
//                //TODO: modify the loginURL
//                String loginURL = "";
//                loginURL.replaceAll(" ","%20");
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                        (Request.Method.GET, loginURL, null, new Response.Listener<JSONObject>() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                // TODO: Handle error
//                            }
//                        });
//
//                queue.add(jsonObjectRequest);
                Log.e("username", "" + username.getText());
                Log.e("password", "" + password.getText());
                if (("" + username.getText()).equals("GOT6") && ("" + password.getText()).equals("jynjynjyn")) {
                    Intent intent = new Intent(getApplicationContext(), SetTimeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}

