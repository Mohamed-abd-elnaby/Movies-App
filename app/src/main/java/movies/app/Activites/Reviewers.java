package movies.app.Activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import movies.app.Adapters.ReviwerAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Model.Modelreviwer;
import movies.app.R;

/**
 * Created by mohamed on 21/11/16.
 */

public class Reviewers extends Activity {

    ArrayList<Modelreviwer>trailerlist;
    ReviwerAdapter reviwerAdapter;
    String filename;
    ProgressDialog progressDialog;
    ListView listView;
    TextView noreviwer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviwers);
        noreviwer=(TextView)findViewById(R.id.no_reviews);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        trailerlist=new ArrayList<>();
        listView=(ListView) findViewById(R.id.reviewers_recycle);
        progressDialog=ProgressDialog.show(Reviewers.this,"Please wait","looding");
        Intent intent=getIntent();
        filename=intent.getStringExtra("filename");
        loadReviwers();
        if(trailerlist!=null&&trailerlist.size()>0){
            reviwerAdapter=new ReviwerAdapter(trailerlist,Reviewers.this);
            listView.setAdapter(reviwerAdapter);
            noreviwer.setText("have one");
            noreviwer.setVisibility(View.GONE);
        }
        else {
            noreviwer.setText("No Reviewers For This Movie");
        }

    }

    void loadReviwers(){
        FileInputStream fileInputStream=null;
        ObjectInputStream objectInputStream=null;
        try{
            fileInputStream=getApplicationContext().openFileInput(filename);
            objectInputStream=new ObjectInputStream(fileInputStream);

                trailerlist=(ArrayList<Modelreviwer>)objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
            progressDialog.dismiss();
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
            progressDialog.dismiss();

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
            progressDialog.dismiss();

        }

    }


}
