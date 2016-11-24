package movies.app.Interfaces;

import java.util.ArrayList;
import java.util.List;

import movies.app.Model.ModelWebservice;

/**
 * Created by mohamed on 16/11/16.
 */

public interface FragmentLisener {

    void onWebServieceFinshed(ArrayList<ModelWebservice> List);
    void onrefreshselected();
}
