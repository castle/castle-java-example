package io.castle.example.config;

/**
 * A single workflow exposed by the app. Each demo maps to a route (/&lt;url&gt;)
 * and a template (templates/&lt;url&gt;.html).
 */
public class Demo {

    private final String url;
    private final String friendlyName;
    private final String blurb;
    private final String wsd;

    public Demo(String url, String friendlyName, String blurb, String wsd) {
        this.url = url;
        this.friendlyName = friendlyName;
        this.blurb = blurb;
        this.wsd = wsd;
    }

    public String getUrl() {
        return url;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public String getBlurb() {
        return blurb;
    }

    public String getWsd() {
        return wsd;
    }
}
