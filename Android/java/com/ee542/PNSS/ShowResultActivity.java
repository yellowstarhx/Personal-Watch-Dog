package com.ee542.PNSS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);

        recyclerView = findViewById(R.id.photos_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        try{
            Log.d("ShowResultActivity", bundle.getString("data"));
            JSONArray jsonArray = new JSONArray(bundle.getString("data"));
            List<Image> mImages = new ArrayList<>();
            for(int i=0; i<jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                Log.e("url: ", jsonObject.getString("url"));
                Image image = new Image();
                //TODO: modify the json key name
                image.setCameraId(jsonObject.getString("camera_id"));
                image.setCaptureTime(jsonObject.getString("capture_time"));
                image.setComment(jsonObject.getString("comment"));
                image.setImageURL(jsonObject.getString("url"));
                image.setMachineLearningResult(jsonObject.getString("mlResult"));
                mImages.add(image);
            }
            recyclerAdapter = new RecyclerViewAdapter(mImages);
            recyclerView.setAdapter(recyclerAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
