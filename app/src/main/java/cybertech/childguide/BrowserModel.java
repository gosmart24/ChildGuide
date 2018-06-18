package cybertech.childguide;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 9/23/2017.
 */
public class BrowserModel {
    String searchWord;
    String searchSite;
    String searchDate;
    public BrowserModel(String searchWord, String searchSite, String searchDate) {
        this.searchWord = searchWord;
        this.searchSite = searchSite;
        this.searchDate = searchDate;
    }

    public BrowserModel() {
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSearchSite() {
        return searchSite;
    }

    public void setSearchSite(String searchSite) {
        this.searchSite = searchSite;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }
}
