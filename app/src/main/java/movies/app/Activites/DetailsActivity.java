package movies.app.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import movies.app.Fragments.DetailsFragment;
import movies.app.R;

/**
 * Created by mohamed on 23/11/16.
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Toolbar toolbar=(Toolbar)findViewById(R.id.detailtoolbar);
        setSupportActionBar(toolbar);
        setTitle("Details");
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        DetailsFragment detailsFragment=new DetailsFragment();
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentdetailcontainer,detailsFragment,"").commit();
    }
}
