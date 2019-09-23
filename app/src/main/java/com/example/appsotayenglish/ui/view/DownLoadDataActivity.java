package com.example.appsotayenglish.ui.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appsotayenglish.R;
import com.example.appsotayenglish.data.itf.APIService;
import com.example.appsotayenglish.data.model.api.CategoryResponse;
import com.example.appsotayenglish.data.model.api.CategoryResponseData;
import com.example.appsotayenglish.data.model.api.Phrase;
import com.example.appsotayenglish.utills.Constant;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownLoadDataActivity extends AppCompatActivity {

    private APIService apiService;
    CategoryResponse categoryResponse = new CategoryResponse();
    List<CategoryResponseData> responseData = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 1000;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load_data);

        //Permission
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "You should grant permission", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
            return;
        }
        else{
            AlertDialog dialog = new SpotsDialog(DownLoadDataActivity.this);
            dialog.show();
            dialog.setMessage("Data Downloading...");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://notebook.mesaenglish.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            APIService jsonPlaceHolderApi = retrofit.create(APIService.class);

            Call<CategoryResponse> call = jsonPlaceHolderApi.showCategory("en");
            call.enqueue(new Callback<CategoryResponse>() {
                @Override
                public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                    if (response.isSuccessful()) {
                        categoryResponse = response.body();
                        responseData = categoryResponse.getData();

                        for (CategoryResponseData data : responseData) {
                            String name_file = String.valueOf(data.getId());
                            Log.d("DATARESPONSE", data.getImage());
                            imageDownload(getApplication(), name_file, data.getName(), data.getImage());
                            for (Phrase phrase : data.getPhrase()) {
//                            Uri uri= Uri.fromFile(new File(downloadAudio("music","1")));
//                            MediaPlayer mp= MediaPlayer.create(getApplicationContext(),uri);
//                            mp.start();
                                String category = name_file;
                                downloadAudio(phrase.getTranslate().getSound(),
                                        category,
                                        String.valueOf(phrase.getName()),
                                        String.valueOf(phrase.getTranslate().getId()));
                            }
                        }

                    }
                }


                @Override
                public void onFailure(Call<CategoryResponse> call, Throwable t) {

                }
            });

        }


    }

    public void imageDownload(Context ctx, String name_file, String name_img, String url_img) {
        Picasso.with(ctx)
                .load(url_img)
                .into(getTargetImage(name_file, DownLoadDataActivity.this, name_img));
    }

    //target to save
    private static Target getTargetImage(final String name_file, final Context context, final String name_img) {
        Target target = new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                try {
                    File file = new File("/storage/emulated/0/mesa/", name_file);
                    if (!file.exists()) {
                        file.mkdirs();
                    }

                    FileOutputStream fileOutputStream = new FileOutputStream(new File(file, name_img + ".jpg"));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Toast.makeText(context, "Save success", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    private String downloadAudio(String uri, String name_category,String name_phrase, String name_audio) {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uri);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationInExternalPublicDir("/" + Constant.NAME_APP + "/" + name_category + "/" + name_category + "_" + name_phrase,name_audio + ".mp3");
        request.allowScanningByMediaScanner();

        File file = new File("/" + Constant.NAME_APP + name_category,name_audio + ".mp3");
        if (!file.exists()) {
            Long ref = downloadManager.enqueue(request);
        }
        return file.toString();
    }
}
