package com.example.rci.rci;

/**
 * Created by Dawson on 7/16/14.
 */
public class LinkItem {

    private String mName;
    private String mLink;

    public LinkItem(String name, String link) {

        this.mName = name;
        this.mLink = link;
    }

    public String getLink() {

        return mLink;
    }

    public void setLink(String link) {

        this.mLink = link;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {

        this.mName = name;
    }

    @Override
    public String toString() {

        return this.mName;
    }
}
