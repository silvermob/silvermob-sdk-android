package com.silvermob.sdk.drprebid.model;

public class HelpScreen {
    private final String title;
    private final String htmlAsset;

    public HelpScreen(String title, String htmlAsset) {
        this.title = title;
        this.htmlAsset = htmlAsset;
    }

    public String getTitle() {
        return title;
    }

    public String getHtmlAsset() {
        return htmlAsset;
    }
}
