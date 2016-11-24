package movies.app.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import movies.app.Activites.Images;
import movies.app.Activites.Reviewers;
import movies.app.Adapters.CommentAdapter;
import movies.app.Adapters.TrailerAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Model.ModelDetails;
import movies.app.Model.ModelUrl;
import movies.app.Model.Modelreviwer;
import movies.app.NetWork.InternetConnection;
import movies.app.R;

/**
 * Created by mohamed on 18/11/16.
 */

public class DetailsFragment extends Fragment{
    public static final String comment="comment";
    public static final String trailer="trailer";
    public static final String Detailsname="DetailsFragment";
    public static final String Reviwersname="DetailsFragment";
    public ArrayList<String>trailerlist;
    ModelDetails modelDetails=new ModelDetails();
    ArrayList<String> listComment;
    ArrayList<Modelreviwer>reviwerlist;
    ProgressDialog progressDialog;
    FrameLayout linearLayout;
    ImageButton favforait;
    String id;
    LinearLayout relativeLayout;
    ImageView imageView;
    TextView title,overview,release_date,popularity,vote_count,vote_average,runtime,reviwers;
    RecyclerView recyclerView;
    TrailerAdapter trailerAdapter;
    ImageButton commentbutton;
    RecyclerView recyclerViewcomment;
    EditText commentEdit;
    CommentAdapter commentAdapter;
    FrameLayout frameLayout;
    int fav=0;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root=inflater.inflate(R.layout.details_fragment,container,false);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("please wait...");
        progressDialog.show();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        intionlizedComponent(root);



        getextras();

        return root;
    }

    @Override
    public void onStart() {
        loading_data();

        super.onStart();
    }

    @Override
    public void onStop() {
        saveDataToFile(id,comment);

        super.onStop();
    }


    void resizeBitmap(final Bitmap bitmap){

        try{
            if(bitmap!=null){
                linearLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        int hight,width;
                        hight=linearLayout.getHeight();
                        width=linearLayout.getWidth();
                        Bitmap newBitmap=Bitmap.createScaledBitmap(bitmap, width,hight, true);
                        imageView.setImageBitmap(newBitmap);
                    }
                });
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


    public void loading_data(){
        loadDetails();
        readDataFromFile(id,trailer);
        readDataFromFile(id,comment);
        loadReviwers();
        if(listComment!=null){
            commentAdapter=new CommentAdapter(getActivity(),listComment);
            recyclerViewcomment.setAdapter(commentAdapter);
            commentAdapter.notifyDataSetChanged();
        }
        if(modelDetails!=null&&trailerlist!=null){
            setadapter();
            setDetails();
            if(progressDialog!=null){
                progressDialog.dismiss();
            }

        }else {
            if(InternetConnection.ifNetworkconnected()){
                fetchDetail fetchDetail=new fetchDetail(id,progressDialog);
                fetchDetail.start();
                Log.d("looding data","from internet");
            }
            else{
                Toast.makeText(getContext(),"no internet connection",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }



        }


    }

    public void setDetails( ){
        try
        {
            overview.setText(modelDetails.getOverview());
            release_date.setText(modelDetails.getRelease_date());
            title.setText(modelDetails.getOriginal_title());
            popularity.setText(modelDetails.getPopularity());
            vote_count.setText(modelDetails.getVote_count());
            vote_average.setText(modelDetails.getVote_average());
            runtime.setText(modelDetails.getRuntime());

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    void intionlizedComponent(View root){
        frameLayout =(FrameLayout)root.findViewById(R.id.images_detail_container);
        reviwers=(TextView)root.findViewById(R.id.reviwers);
        favforait=(ImageButton)root.findViewById(R.id.favoritbutton);
        linearLayout=(FrameLayout)root.findViewById(R.id.imagecontainer);
        trailerlist=new ArrayList<String>();
        listComment=new ArrayList<String>();
        commentAdapter=new CommentAdapter(getActivity(),listComment);
        recyclerViewcomment=(RecyclerView)root.findViewById(R.id.commentlist);
        recyclerViewcomment.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewcomment.setAdapter(commentAdapter);
        commentEdit =(EditText)root.findViewById(R.id.comment_edit);
        commentbutton=(ImageButton)root.findViewById(R.id.comment_button);
        recyclerView=(RecyclerView) root.findViewById(R.id.trailerlist);
        title=(TextView)root.findViewById(R.id.move_title);
        runtime=(TextView)root.findViewById(R.id.runtime);
        release_date=(TextView)root.findViewById(R.id.relased_date);
        overview=(TextView)root.findViewById(R.id.overview);
        vote_average=(TextView)root.findViewById(R.id.vote_avarage);
        vote_count=(TextView)root.findViewById(R.id.vote_count);
        popularity=(TextView)root.findViewById(R.id.popularity);
        Toolbar toolbar=(Toolbar)root.findViewById(R.id.detailtoolbar);
        relativeLayout=(LinearLayout) root.findViewById(R.id.relative_detail);
        imageView=(ImageView)root.findViewById(R.id.detail_iamge);
        //setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getContext(),MainFragment.class);
                startActivity(intent1);
            }
        });
        commentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recyclerViewcomment.removeAllViews();
                String comment=commentEdit.getText().toString();
                if(listComment==null){
                    listComment=new ArrayList<String>();
                    listComment.add(comment);
                }
                else{
                    listComment.add(comment);
                }

                commentAdapter.notifyDataSetChanged();

                commentEdit.setText("");
            }
        });
        favforait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
                try{
                    String fav=fileCach.getFav(id);
                    if(fav.equals("0")){
                        fileCach.update(id,"1");
                    }else if(fav.equals("1")){
                        fileCach.update(id,"0");
                    }
                }
                catch (Exception ex){
                    ex.printStackTrace();
                }
                finally {
                    String fav=fileCach.getFav(id);
                    if(fav.equals("0")){
                        favforait.setBackgroundResource(R.drawable.unpressed);
                    }
                    else if(fav.equals("1")){
                        favforait.setBackgroundResource(R.drawable.pressed);
                    }
                }


            }
        });
        reviwers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Reviewers.class);
                intent.putExtra("filename",id+Reviwersname);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frameLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getContext(),Images.class);


                        intent.putExtra("id",id);

                        startActivity(intent);
                    }
                });


            }
        });

    }

    Bitmap decodingImage(String image){
        try {
            if(image!=null){
                byte []bytes= Base64.decode(image,Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
              /*  int width=relativeLayout.getWidth();
                int height=relativeLayout.getHeight();
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, width,
                        height, true);*/
                return bitmap;

            }
            else {
                return null;
            }

        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }



    }

    void fetchimage(String id){
        try{
            FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
            String image=fileCach.Getimage(id);
            Bitmap bitmap=decodingImage(image);
            resizeBitmap(bitmap);
            String fav=fileCach.getFav(id);
            if(fav.equals("0")){
                favforait.setBackgroundResource(R.drawable.unpressed);
            }
            else if(fav.equals("1")){
                favforait.setBackgroundResource(R.drawable.pressed);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }

    void getextras(){

        Bundle bundle=getArguments();
        id=bundle.getString("id");
        fetchimage(id);

    }

    void saveDetails(){

        try {
            FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
            fileCach.updateall(id,modelDetails.getOverview(),modelDetails.getRelease_date(),modelDetails.getOriginal_title(),modelDetails.getPopularity(),modelDetails.getVote_count(),modelDetails.getVote_average());

        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    void loadDetails(){
        FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
        modelDetails=fileCach.getdata(id);
        if(modelDetails!=null){
        }

    }

    void saveReviwers(ArrayList<Modelreviwer> reviwerlist){

        FileOutputStream fileOutputStream=null ;
        ObjectOutputStream objectOutputStream=null;
        try{
            fileOutputStream=ApplicationClass.getInstance().getApplicationContext().openFileOutput(id+Reviwersname, Context.MODE_PRIVATE);
            objectOutputStream =new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(reviwerlist);
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            try{
                objectOutputStream.close();
                fileOutputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            try{
                objectOutputStream.close();
                fileOutputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    void loadReviwers(){
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream=null;
        try{
            fileInputStream=ApplicationClass.getInstance().getApplicationContext().openFileInput(id+Detailsname);
            objectInputStream=new ObjectInputStream(fileInputStream);
            ArrayList<Modelreviwer> reviwerlist = (ArrayList<Modelreviwer>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            this.reviwerlist=reviwerlist;
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            try{
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            try{
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    void saveDataToFile(String filename,String type){
        FileOutputStream fileOutputStream=null ;
        ObjectOutputStream objectOutputStream=null;
        try{
            fileOutputStream=ApplicationClass.getInstance().getApplicationContext().openFileOutput(filename+type, Context.MODE_PRIVATE);
            objectOutputStream =new ObjectOutputStream(fileOutputStream);
            if(type.equals(comment))
            {
                objectOutputStream.writeObject(listComment);
            }
            else if(type.equals(trailer)){
                objectOutputStream.writeObject(trailerlist);

            }

            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
            try{
                objectOutputStream.close();
                fileOutputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            try{
                objectOutputStream.close();
                fileOutputStream.close();

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    void readDataFromFile(String filname,String type){
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream=null;
        try{
            fileInputStream=ApplicationClass.getInstance().getApplicationContext().openFileInput(filname+type);
            objectInputStream=new ObjectInputStream(fileInputStream);
            if(type.equals(comment)) {
                listComment = (ArrayList<String>) objectInputStream.readObject();
                if(listComment.size()>0){
                    return;
                }else {
                    listComment=null;
                }
            }
            else if (type.equals(trailer)){
                trailerlist=(ArrayList<String>)objectInputStream.readObject();
                if(trailerlist.size()>0){
                    return;
                }else {
                    trailerlist=null;
                }
            }
            objectInputStream.close();
            fileInputStream.close();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();


            try{
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception ex){
            ex.printStackTrace();


            try{
                if(objectInputStream!=null){
                    objectInputStream.close();
                    fileInputStream.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void setadapter(){
        trailerAdapter=new TrailerAdapter(getActivity(),trailerlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(trailerAdapter);

    }

    class fetchDetail extends Thread{
        Handler handler;
        String id;
        String result=null;
        ArrayList<String>list;
        ProgressDialog progressDialog;
        public fetchDetail(String id ,ProgressDialog ProgressDialog){
            this.id=id;
            this.progressDialog=ProgressDialog;
            handler=new Handler();
            list=new ArrayList<>();

        }
        boolean connect(){
            String link="https://api.themoviedb.org/3/movie/"+id+"?api_key="+ModelUrl.getApi_key()+"&append_to_response=videos,reviews";
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

        ModelDetails parsing_details(){
            try {
                JSONObject alldata=new JSONObject(result);
                ArrayList<ModelDetails>detailslist=new ArrayList<>();
                ModelDetails modelDetails=new ModelDetails();
                modelDetails.setId(id);
                modelDetails.setOverview(alldata.getString("overview"));
                modelDetails.setRelease_date(alldata.getString("release_date"));
                modelDetails.setOriginal_title(alldata.getString("original_title"));
                modelDetails.setPopularity(alldata.getString("popularity"));
                modelDetails.setVote_count(alldata.getString("vote_count"));
                modelDetails.setVote_average(alldata.getString("vote_average"));
                modelDetails.setRuntime(alldata.getString("runtime"));

                return modelDetails;

            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        ArrayList<String> parsing_trailer(){
            ArrayList<String>arrayList=new ArrayList<String>();
            try
            {
                JSONObject alldata=new JSONObject(result);
                JSONObject allVideos=alldata.getJSONObject("videos");
                JSONArray trailersArray=allVideos.getJSONArray("results");
                for(int i=0;i<trailersArray.length();i++){
                    JSONObject trailer=trailersArray.getJSONObject(i);
                    String key=trailer.getString("key");
                    arrayList.add(key);

                }
                return arrayList;

            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        ArrayList<Modelreviwer> parsing_reviewers(){

            ArrayList<Modelreviwer>reviwerList=new ArrayList<Modelreviwer>();
            try
            {
                JSONObject alldata=new JSONObject(result);
                JSONObject allVideos=alldata.getJSONObject("reviews");
                JSONArray reviwersarray=allVideos.getJSONArray("results");
                for(int i=0;i<reviwersarray.length();i++){
                    JSONObject reviwers=reviwersarray.getJSONObject(i);
                    String author=reviwers.getString("author");
                    String content=reviwers.getString("content");
                    Modelreviwer modelreviwer=new Modelreviwer();
                    modelreviwer.setAuthor(author);
                    modelreviwer.setContent(content);
                    reviwerList.add(modelreviwer);

                }
                return reviwerList;

            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        void parsing_data(){
            modelDetails=parsing_details();
            trailerlist=parsing_trailer();
            reviwerlist=parsing_reviewers();

        }

        @Override
        public void run() {

            if (connect())
                parsing_data();


            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                    }
                    saveDataToFile(id,trailer);
                    saveDetails();
                    saveReviwers(reviwerlist);
                    setDetails();
                    setadapter();

                }
            });


        }

    }


}
