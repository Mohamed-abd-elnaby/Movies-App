package movies.app.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import movies.app.R;

/**
 * Created by mohamed on 17/11/16.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    Activity activity;
    ArrayList<String>List=new ArrayList<String>();

    public CommentAdapter(Activity activity, ArrayList<String>List){
        this.activity=activity;
        this.List=List;


    }
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root=activity.getLayoutInflater().inflate(R.layout.comment,parent,false);

        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.textView.setText(List.get(position));
    }

    @Override
    public int getItemCount() {
        return List.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
       TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.commenttext);


        }
    }
}
