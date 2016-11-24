package movies.app.Application;

import android.app.Application;

/**
 * Created by mohamed on 16/11/16.
 */

public class ApplicationClass extends Application {

    private static ApplicationClass instance;

    public ApplicationClass(){

        instance=this;
    }

    public static ApplicationClass getInstance() {
        return instance;
    }
}
