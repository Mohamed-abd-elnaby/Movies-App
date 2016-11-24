package movies.app.NetWork;

import android.content.Context;
import android.net.ConnectivityManager;
import movies.app.Application.ApplicationClass;

/**
 * Created by mohamed on 04/11/16.
 */

public  class InternetConnection {
    public static boolean ifNetworkconnected(){
        ConnectivityManager connectivityManager= (ConnectivityManager) ApplicationClass.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null;
    }
}
