package net.dstc.crawlertest.rest.model;

import net.dstc.crawlertest.data.Optimizable;

/**
 * A DTO to send information about optimizable sites through REST endpoints.
 * It maps 1:1 to {@link Optimizable}.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class OptimizableDTO {

    private String uuid;
    private String url;
    private String title;
    private long downloadTime;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
