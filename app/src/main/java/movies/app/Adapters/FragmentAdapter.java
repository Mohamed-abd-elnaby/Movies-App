package movies.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import movies.app.Activites.DetailsActivity;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Model.ModelFav;
import movies.app.Model.ModelWebservice;
import movies.app.R;
import movies.app.Managers.ImageManager;

import static movies.app.Fragments.FragmentList.DDEFAULT;
import static movies.app.Fragments.FragmentList.FAV;
import static movies.app.Fragments.FragmentList.GRID_MODE;
import static movies.app.Fragments.FragmentList.LIST_MODE;

/**
 * Created by mohamed on 17/11/16.
 */

public class FragmentAdapter extends RecyclerView.Adapter<FragmentAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ModelWebservice>List=new ArrayList<ModelWebservice>();
    List<ModelFav>favList=new ArrayList<ModelFav>();
    int orentation;
    private  int height;
    private  int width;
    private DeatailLisener deatailLisener;
    DisplayMetrics displayMetrics;
    String type;
    public FragmentAdapter(Activity activity, int orentation, ArrayList<ModelWebservice>List, List<ModelFav>favList , String type,DeatailLisener deatailLisener){
        this.deatailLisener=deatailLisener;
        this.type=type;
        this.favList=favList;
        this.activity=activity;
        this.List=List;
        this.orentation=orentation;
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    }

    @Override
    public FragmentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=activity.getLayoutInflater().inflate(R.layout.item,parent,false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.relativeLayout.getLayoutParams();
        ModelWebservice modelWebservice=null;
        if(type==DDEFAULT){
             modelWebservice=List.get(position);
        }
        holder.imageView.setImageResource(R.drawable.placeholder);
        if(orentation == LIST_MODE){

            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            params.height = height /3;

            holder.relativeLayout.setLayoutParams(params);
            if(type==DDEFAULT){
                ImageManager.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,modelWebservice.getPoster_path(),modelWebservice.getId(),DDEFAULT);
            }else if(type==FAV){
                ImageManager.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,favList.get(position).getImage(),favList.get(position).getId(),FAV);
            }

        }else if(orentation == GRID_MODE){

            height = displayMetrics.heightPixels;
            width = displayMetrics.widthPixels;
            //params.width = width /3;
            params.height = height /3;

            holder.relativeLayout.setLayoutParams(params);
            if(type==DDEFAULT){
                ImageManager.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,modelWebservice.getPoster_path(),modelWebservice.getId(),DDEFAULT);
            }else if(type==FAV){
                ImageManager.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,favList.get(position).getImage(),favList.get(position).getId(),FAV);
            }
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id=null;
                if(type==DDEFAULT){
                    ModelWebservice modelWebservice=List.get(position);
                    id=modelWebservice.getId();
                }
                else if(type==FAV){
                    id=favList.get(position).getId();
                }
                deatailLisener.onselectitem(id);


            }
        });

    }

    @Override
    public int getItemCount() {

        if(type==DDEFAULT){
            return List.size();
        }
        else if(type==FAV){
            return favList.size();
        }
        else
            return 0;

    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ProgressBar progressBar;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.cat_image);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.item_container);

        }
    }
}
