package net.dstc.crawlertest.rest.model;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wrapper class to store a URL. The URL is checked and if it's malformed, the
 * error is stored.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class ParsedURL {

    private final String unparsed;
    private URL url;
    private Exception error;

    ParsedURL(String url) {
        this.unparsed = url;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            error = ex;
        }
    }

    public URL getUrl() {
        return url;
    }

    /**
     * This has a value only if the wrapped URL is malformed.
     *
     * @return null or a {@link MalformedURLException} instance
     */
    public Exception getError() {
        return error;
    }

    /**
     * Original URL (before the check)
     *
     * @return
     */
    public String getUnparsed() {
        return unparsed;
    }

    /**
     * True if error property is null.
     *
     * @return
     */
    public boolean isOK() {
        return error == null;
    }
}
