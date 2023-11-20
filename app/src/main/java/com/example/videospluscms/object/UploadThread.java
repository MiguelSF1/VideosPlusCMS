package com.example.videospluscms.object;

import android.content.Context;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UploadThread extends Thread{
    Integer movieId;
    String filePathv, filename;
    Context context;
    public UploadThread(Integer movieId, String filePathv, Context context){
        this.movieId = movieId;
        this.filePathv = filePathv;
        this.context = context;
        getFilename();
    }

    public void run() {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://192.168.1.103:8080/movieVersions/upload",
                response -> Toast.makeText(context, "Upload Completed", Toast.LENGTH_SHORT).show(), Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("movieId", movieId.toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData(){
                Map<String, DataPart> params = new HashMap<>();
                try {
                    params.put("upload", new DataPart(filename, Files.readAllBytes(Paths.get(filePathv)), "multipart/form-data"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();
        requestQueue.add(multipartRequest);
    }

    public void getFilename() {
        int lastIndexOf = filePathv.lastIndexOf("/");
        filename =  filePathv.substring(lastIndexOf).substring(1);
    }
}