package movies.app.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import movies.app.Activites.MainActivity;
import movies.app.Adapters.TabsAdapter;
import movies.app.Application.ApplicationClass;
import movies.app.Cachs.FileCach;
import movies.app.Fragments.FragmentList;
import movies.app.Interfaces.DeatailLisener;
import movies.app.Interfaces.Mainlisener;
import movies.app.R;

public class MainFragment extends Fragment implements Mainlisener{
    public static final String toppopular="TOP";
    ViewPager viewPager;
    TabLayout tableLayout;
    TabsAdapter tabsAdapter;
    Toolbar toolbar;
    ViewPager.OnPageChangeListener onPageChangeListener;
    DeatailLisener deatailLisener;
    MainActivity mainActivity;

    public void setDeatailLisener(DeatailLisener deatailLisener) {
        this.deatailLisener = deatailLisener;
    }

    public MainFragment(){
        mainActivity=MainActivity.getInstance();
        mainActivity.setMainlisener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.activity_main_fragment,container,false);
        viewPager=(ViewPager)root.findViewById(R.id.viewpager);
        tableLayout=(TabLayout)root. findViewById(R.id.tabslayout);
        tableLayout.setupWithViewPager(viewPager);
        setupViewPager();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        FileCach fileCach=new FileCach(ApplicationClass.getInstance().getApplicationContext());
        fileCach.creat_table_setting();
        onPageChangeListener=new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


                if(position==0)
                    ((FragmentList)tabsAdapter.fragmentList.get(position)).startWebServiece(toppopular,deatailLisener);
                else if(position==1)
                    ((FragmentList)tabsAdapter.fragmentList.get(position)).startFav(deatailLisener);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageChangeListener.onPageSelected(0);

            }
        });
        return root;
    }

    void setupViewPager(){
        tabsAdapter=new TabsAdapter(getChildFragmentManager());
        tabsAdapter.addFragment(new FragmentList(),"MOVIES");
        tabsAdapter.addFragment(new FragmentList(),"FAVORATIES");
        viewPager.setAdapter(tabsAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        onPageChangeListener.onPageSelected(viewPager.getCurrentItem());
    }


    @Override
    public void onrefreshselected() {
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                onPageChangeListener.onPageSelected(0);
            }
        });
    }
}
