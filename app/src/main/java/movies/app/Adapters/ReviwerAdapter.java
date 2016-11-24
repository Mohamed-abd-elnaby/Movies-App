package movies.app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import movies.app.Model.Modelreviwer;
import movies.app.R;

/**
 * Created by mohamed on 21/11/16.
 */

public class ReviwerAdapter extends BaseAdapter{
    ArrayList<Modelreviwer>arrayList=new ArrayList<>();
    Context context;

    public ReviwerAdapter(ArrayList<Modelreviwer>modelreviwerslist,Context context){
        this.arrayList=modelreviwerslist;
        this.context=context;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder holder=null;
        if(convertView==null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.reviwer_item, parent,false);
            TextView author = (TextView) convertView.findViewById(R.id.author);
            TextView content = (TextView) convertView.findViewById(R.id.content);

            holder=new holder();
            holder.author=author;
            holder.content=content;
            convertView.setTag(holder);
        }
        else {

            holder= (ReviwerAdapter.holder) convertView.getTag();

        }
        Modelreviwer modelreviwer=arrayList.get(position);

        holder.author.setText(modelreviwer.getAuthor());
        holder.content.setText(modelreviwer.getContent());




        return convertView;
    }
    public static   class holder{

        TextView author;
        TextView content;


    }


}
