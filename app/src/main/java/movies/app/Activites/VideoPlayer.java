package movies.app.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import movies.app.Model.ModelUrl;
import movies.app.R;


/*
 * Created by mohamed on 21/11/16.
 */

public class VideoPlayer  extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{
    YouTubePlayerView youTubePlayerView;

    ModelUrl modelUrl=new ModelUrl();
    String key;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_player);
        Intent intent=getIntent();
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        key=intent.getStringExtra("key");
        youTubePlayerView=(YouTubePlayerView)findViewById(R.id.player);
        youTubePlayerView.initialize(modelUrl.getApi_key_youtub(),this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if(!b){
            youTubePlayer.cueVideo(key);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this,1).show();
        }
        else {
            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
        }
    }
}
