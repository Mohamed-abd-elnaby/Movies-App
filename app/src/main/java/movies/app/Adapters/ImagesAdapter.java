package movies.app.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import movies.app.Managers.ImagesManagerDetail;
import movies.app.Model.ModelImage;
import movies.app.R;

import static movies.app.Fragments.FragmentList.GRID_MODE;
import static movies.app.Fragments.FragmentList.LIST_MODE;

/**
 * Created by mohamed on 17/11/16.
 */

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ModelImage>List=new ArrayList<ModelImage>();
    int orentation;
    DisplayMetrics displayMetrics;
    String id;
    public ImagesAdapter(Activity activity, int orentation, ArrayList<ModelImage>List ,String id){
        this.id=id;
        this.activity=activity;
        this.List=List;
        this.orentation=orentation;
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    @Override
    public ImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=activity.getLayoutInflater().inflate(R.layout.image_item,parent,false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.relativeLayout.getLayoutParams();
        holder.imageView.setImageResource(R.drawable.placeholder);
        if(orentation == LIST_MODE){
            int width=displayMetrics.widthPixels;
            int height=displayMetrics.heightPixels;
            params.width=width;
            params.height = (height*4)/5 ;


            holder.relativeLayout.setLayoutParams(params);

            ImagesManagerDetail.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,List.get(position).getUrl(),id);


        }else if(orentation == GRID_MODE){


            int width=displayMetrics.widthPixels;
            int height=displayMetrics.heightPixels;
            params.width = width ;
            params.height = (height*4)/5;

            holder.relativeLayout.setLayoutParams(params);

            ImagesManagerDetail.getInstince().diplayimage(activity,holder.imageView,holder.progressBar,holder.relativeLayout,List.get(position).getUrl(),id);

        }


    }

    @Override
    public int getItemCount() {


            return List.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        ProgressBar progressBar;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.image_image);
            progressBar=(ProgressBar)itemView.findViewById(R.id.progressBar_image);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.image_container);

        }
    }
}
