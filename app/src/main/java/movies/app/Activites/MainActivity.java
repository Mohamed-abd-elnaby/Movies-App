package movies.app.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import movies.app.Adapters.FragmentAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.MemoryCach;
import movies.app.Fragments.DetailsFragment;
import movies.app.Fragments.FragmentList;
import movies.app.Fragments.MainFragment;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Interfaces.FragmentLisener;
import movies.app.Interfaces.Mainlisener;
import movies.app.R;

/**
 * Created by mohamed on 23/11/16.
 */

public class MainActivity extends AppCompatActivity implements DeatailLisener {
    public boolean tabletmode=false;
    Mainlisener mainlisener;


    private static MainActivity instance;

    public static MainActivity getInstance() {

        if(instance==null){
            instance=new MainActivity();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        View view=findViewById(R.id.fl_detail);

        if(view!=null){
            tabletmode=true;


        }

        if(tabletmode==true){

            MainFragment mainFragment=new MainFragment();
            mainFragment.setDeatailLisener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main,mainFragment,"").commit();
        }
        else if(tabletmode==false){
            MainFragment mainFragment=new MainFragment();
            mainFragment.setDeatailLisener(this);
            getSupportFragmentManager().beginTransaction().add(R.id.fl_main,mainFragment,"").commit();
        }

    }


    public void setMainlisener(Mainlisener mainlisener) {
        instance.mainlisener = mainlisener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.setting){

            Intent intent = new Intent(MainActivity.this, Setting.class);
            startActivity(intent);
        }
        else  if(id==R.id.refresh){

            Handler handler=new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    instance.mainlisener.onrefreshselected();

                }
            });

        }
        else if (id==R.id.Clear){
            MemoryCach memoryCach=new MemoryCach();
            memoryCach.clearCach();
            File dir = new File(ApplicationClass.getInstance().getFilesDir().getPath());
            if (dir.isDirectory())
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    File file=new File(dir+"/"+children[i]);
                    file.delete();

                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onselectitem(String id) {
        if(tabletmode==true){

            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            DetailsFragment detailsFragment=new DetailsFragment();
            detailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_detail,detailsFragment,"").commit();
        }if (tabletmode==false){
            Intent intent=new Intent(MainActivity.this, DetailsActivity.class);
            if(id!=null)
            {
                intent.putExtra("id",id);
            }
            startActivity(intent);
        }
    }
}

