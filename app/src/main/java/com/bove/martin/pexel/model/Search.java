package com.bove.martin.pexel.model;

/**
 * Created by Martín Bove on 12-Feb-20.
 * E-mail: mbove77@gmail.com
 */
public class Search {
    private String searchInEnglish;
    private String searchInSpanish;
    private String photo;

    public Search(String searchInEnglish, String searchInSpanish, String photo) {
        this.searchInEnglish = searchInEnglish;
        this.searchInSpanish = searchInSpanish;
        this.photo = photo;
    }

    public String getSearchInEnglish() {
        return searchInEnglish;
    }

    public void setSearchInEnglish(String searchInEnglish) {
        this.searchInEnglish = searchInEnglish;
    }

    public String getSearchInSpanish() {
        return searchInSpanish;
    }

    public void setSearchInSpanish(String searchInSpanish) {
        this.searchInSpanish = searchInSpanish;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
