package movies.app.Fragments;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import movies.app.Activites.MainActivity;
import movies.app.Adapters.FragmentAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Interfaces.FragmentLisener;
import movies.app.Model.ModelFav;
import movies.app.Model.ModelUrl;
import movies.app.Model.ModelWebservice;
import movies.app.NetWork.WebServiece;
import movies.app.R;

/**
 * Created by mohamed on 16/11/16.
 */

public class FragmentList extends Fragment implements FragmentLisener {
    public static final int LIST_MODE = 0x0a;
    public static final int GRID_MODE = 0x0b;
    public static final String DDEFAULT = "Default";
    public static final String FAV = "fav";

    FragmentAdapter fragmentAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    DeatailLisener deatailLisener;
    String type;
    MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview= inflater.inflate(R.layout.fragement_list,container,false);
        recyclerView=(RecyclerView)rootview.findViewById(R.id.recycle_list);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("please wait...");


        return rootview;
    }

    public void onorentationchange(int oreantation,ArrayList<ModelWebservice>arrayList,List<ModelFav>favList,String type,DeatailLisener deatailLisener){



        if(oreantation== Configuration.ORIENTATION_PORTRAIT){

            fragmentAdapter=new FragmentAdapter(getActivity(),LIST_MODE,arrayList,favList,type,deatailLisener);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(fragmentAdapter);
        }
        else if(oreantation==Configuration.ORIENTATION_LANDSCAPE){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
            fragmentAdapter=new FragmentAdapter(getActivity(),GRID_MODE,arrayList,favList,type,deatailLisener);
            recyclerView.setAdapter(fragmentAdapter);

        }

    }

    public void startWebServiece(String type,DeatailLisener deatailLisener){
        this.deatailLisener=deatailLisener;
        mainActivity=new MainActivity();
        this.type=type;
        if(progressDialog!=null){

            progressDialog.show();
        }




        WebServiece webServiece=new WebServiece();
        webServiece.setFragmentLisener(this);
        webServiece.start();
    }

    public void startFav(DeatailLisener deatailLisener){
        this.deatailLisener=deatailLisener;
        FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
        List<ModelFav>List=new ArrayList<>();
        List=fileCach.getAllFav();
        if(List!=null){

            onorentationchange(getResources().getConfiguration().orientation,null,List,FAV,deatailLisener);
        }

    }

    @Override
    public void onWebServieceFinshed(ArrayList<ModelWebservice> List) {

        if(progressDialog!=null){
            progressDialog.dismiss();
        }
        if(List!=null){

            onorentationchange(getResources().getConfiguration().orientation,List,null,DDEFAULT,deatailLisener);

        }
        else {
            Log.d("Null","webservice finshed and list have no value");
        }
    }

    @Override
    public void onrefreshselected() {
        if(progressDialog!=null){

            progressDialog.show();
        }
        else{
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("please wait...");
            progressDialog.show();
        }

        WebServiece webServiece=new WebServiece();
        webServiece.setFragmentLisener(this);
        webServiece.start();
    }
}
