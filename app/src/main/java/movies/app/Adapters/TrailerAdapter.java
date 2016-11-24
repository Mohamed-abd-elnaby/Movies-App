package movies.app.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import movies.app.Activites.VideoPlayer;
import movies.app.R;

/**
 * Created by mohamed on 17/11/16.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    Activity activity;
    ArrayList<String>List=new ArrayList<String>();

    public TrailerAdapter(Activity activity, ArrayList<String>List){
        this.activity=activity;
        this.List=List;


    }
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=activity.getLayoutInflater().inflate(R.layout.trailer_item,parent,false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.trailImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, VideoPlayer.class);
                intent.putExtra("key",List.get(position));
                activity.startActivity(intent);
            }
        });

        int pos=position+1;

        holder.textView.setText(""+pos);
    }

    @Override
    public int getItemCount() {
        return List.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView textView;
        ImageButton trailImageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.trailerIndex);
            trailImageButton=(ImageButton) itemView.findViewById(R.id.trailerbutton);


        }
    }
}
