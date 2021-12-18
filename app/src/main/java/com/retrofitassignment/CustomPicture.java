package com.retrofitassignment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomPicture extends AppCompatActivity {
    private PictureAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custompicture);

        String albumName = getIntent().getStringExtra("albumName");

        progressDialog = new ProgressDialog(CustomPicture.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

        Call<List<RetroPhoto>> call = service.getAllPhotos();
        call.enqueue(new Callback<List<RetroPhoto>>() {

            @Override
            public void onResponse(Call<List<RetroPhoto>> call, Response<List<RetroPhoto>> response) {
                progressDialog.dismiss();
                ArrayList<RetroPhoto> photoList = new ArrayList<>();
                assert response.body() != null;
                for (RetroPhoto item: response.body()) {
                    if (albumName.equals(item.getAlbumId())) {
                        photoList.add(item);
                    }
                }

                generateDataList(photoList);
            }

            @Override
            public void onFailure(Call<List<RetroPhoto>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CustomPicture.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateDataList(List<RetroPhoto> photoList) {
        recyclerView = findViewById(R.id.customRecyclerView1);
        adapter = new PictureAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CustomPicture.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
