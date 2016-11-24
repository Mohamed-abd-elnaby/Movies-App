package movies.app.Model;

/**
 * Created by mohamed on 16/11/16.
 */

public class ModelUrl {
    private final static String api_key="3ce9738b489fa8101623a449aa151581";
    private final static String api_key_youtub="AIzaSyDy5Y8IfdBaFBvKEDgOfojDhWTGv8x7vdk";

    String language;
    String typeOfSearch;
    String pages;
    String sizeOfImage;
    String year;
    public   String photo_url="https://image.tmdb.org/t/p/w";

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTypeOfSearch() {
        return typeOfSearch;
    }

    public static String getApi_key() {

        return api_key;
    }

    public String getLanguage() {
        return language;
    }

    public String getPages() {
        return pages;
    }

    public String getPhoto_url() {
        return photo_url;
    }


    public void setSizeOfImage(String sizeOfImage) {

        this.sizeOfImage = sizeOfImage;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setTypeOfSearch(String typeOfSearch) {
        this.typeOfSearch = typeOfSearch;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getSizeOfImage() {
        return sizeOfImage;
    }
    public String getApi_key_youtub(){
        return api_key_youtub;
    }
}
