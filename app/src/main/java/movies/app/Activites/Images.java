package movies.app.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import movies.app.Adapters.FragmentAdapter;
import movies.app.Adapters.ImagesAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Model.ModelFav;
import movies.app.Model.ModelImage;
import movies.app.Model.ModelUrl;
import movies.app.Model.ModelWebservice;
import movies.app.NetWork.InternetConnection;
import movies.app.NetWork.Networkconnection;
import movies.app.R;

import static movies.app.Fragments.FragmentList.GRID_MODE;
import static movies.app.Fragments.FragmentList.LIST_MODE;

/**
 * Created by mohamed on 23/11/16.
 */

public class Images extends Activity {
    RecyclerView recyclerView;
    List<ModelImage>modelImageList=new ArrayList<ModelImage>();
    ProgressDialog progressDialog;
    ImagesAdapter imagesAdapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        recyclerView=(RecyclerView)findViewById(R.id.imagesrecycl);
        Intent intent=getIntent();
        id=intent.getStringExtra("id");
        progressDialog=ProgressDialog.show(Images.this,"please wait","looding");
        ArrayList<ModelImage>modelImageArrayList=loodfromfile();
        if(modelImageArrayList!=null&&modelImageArrayList.size()>0){
            onorentationchange(getResources().getConfiguration().orientation,modelImageArrayList);
            progressDialog.dismiss();
        }
        else{
            FetchUrlImages fetchUrlImages=new FetchUrlImages();
            fetchUrlImages.start();
        }

    }

    class FetchUrlImages extends Thread{

        Handler handler=new Handler();
        FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
        ModelUrl modelUrl=fileCach.getsetting();
        private String result=null;
        ArrayList<ModelImage> modelImageArrayList=new ArrayList<>();
        @Override
        public void run() {
            super.run();
            if(InternetConnection.ifNetworkconnected())
                if(connect())
                    parsingdata();
            if(modelImageArrayList!=null){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        savetofile(modelImageArrayList);
                        onorentationchange(getResources().getConfiguration().orientation,modelImageArrayList);
                    }
                });
            }

        }

        boolean connect(){
            Log.d("read images from net","done");
            String link="https://api.themoviedb.org/3/movie/"+id+"/images?api_key="+ModelUrl.getApi_key()+"";
            HttpURLConnection httpURLConnection=null;
            InputStream inputStream=null;
            try{
                URL url=new URL(link);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuilder stringBuilder=new StringBuilder();
                while ((line=bufferedReader.readLine())!=null){
                    stringBuilder.append(line+"/n");
                }
                result=stringBuilder.toString();
                inputStream.close();
                httpURLConnection.disconnect();
                return true;
            }catch (Exception ex){
                ex.printStackTrace();
                try{
                    inputStream.close();
                    httpURLConnection.disconnect();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }
        }

        ArrayList<ModelImage> parsingdata(){

            try {
                JSONObject alldata=new JSONObject(result);
                JSONArray dataarray=alldata.getJSONArray("backdrops");
                for(int i=0;i<dataarray.length();i++){
                    JSONObject row=dataarray.getJSONObject(i);
                    ModelImage modelImage=new ModelImage();
                    modelImage.setUrl(modelUrl.photo_url+"600"+row.getString("file_path"));
                    //Log.d("poster",modelUrl.photo_url+"600"+row.getString("file_path"));
                    modelImageArrayList.add(modelImage);

                }
                Log.d("result",alldata.toString());
                progressDialog.dismiss();
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return modelImageArrayList;
        }
    }

    void savetofile(ArrayList<ModelImage> modelImageList){
        FileOutputStream fileOutputStream=null;
        ObjectOutputStream objectOutputStream=null;
        try{
            fileOutputStream=ApplicationClass.getInstance().openFileOutput(id+"Images", Context.MODE_PRIVATE);
            objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(modelImageList);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();

            try {
                  if(objectOutputStream!=null){
                      objectOutputStream.close();
                      fileOutputStream.close();
                  }
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                if(objectOutputStream!=null){
                    objectOutputStream.close();
                    fileOutputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }

    }

    ArrayList<ModelImage>loodfromfile(){
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream=null;
        try {
            fileInputStream=ApplicationClass.getInstance().openFileInput(id+"Images");
            objectInputStream=new ObjectInputStream(fileInputStream);
            return (ArrayList<ModelImage>)objectInputStream.readObject();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            try {
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    public void onorentationchange(int oreantation,ArrayList<ModelImage>arrayList){



        FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
        fileCach.creat_images_tables();
        if(oreantation== Configuration.ORIENTATION_PORTRAIT){

            imagesAdapter=new ImagesAdapter(Images.this,LIST_MODE,arrayList,id);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
            recyclerView.setAdapter(imagesAdapter);
        }
        else if(oreantation==Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
            imagesAdapter=new ImagesAdapter(Images.this,GRID_MODE,arrayList,id);
            recyclerView.setAdapter(imagesAdapter);

        }

    }

    }
