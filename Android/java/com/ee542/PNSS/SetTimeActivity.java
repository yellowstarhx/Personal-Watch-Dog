package com.ee542.PNSS;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class SetTimeActivity extends AppCompatActivity {

    EditText year;
    EditText month;
    EditText day;
    EditText hour;
    EditText minute;
    EditText second;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time);

        year = findViewById(R.id.year);
        month = findViewById(R.id.month);
        day = findViewById(R.id.day);
        hour = findViewById(R.id.hour);
        minute = findViewById(R.id.minute);
        second = findViewById(R.id.second);
        button = findViewById(R.id.send_request);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parameter = year.getText()+"-"+month.getText()+"-"+day.getText();
                parameter += " "+hour.getText() + ":" +minute.getText()+":"+second.getText();

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                //TODO:modify the url
                // local: 10.0.2.2
                String url = "http://54.215.225.213:8080/HouseWatchDog/history?camera_id=0001&start_time="+
                        parameter;
                url = url.replaceAll(" ","%20");
                Log.i("SetTimeActivity:",url);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                Bundle bundle = new Bundle();
                                bundle.putString("data", response.toString());
                                Intent intent = new Intent(getApplicationContext(), ShowResultActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Log.e("volley error",error.toString());
                            }
                        });

                queue.add(jsonArrayRequest);
            }
        });
    }

}
