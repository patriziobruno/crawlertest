package net.dstc.crawlertest.data;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity representing a optimizable URL
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
@Entity
public class Optimizable implements Serializable {

    @Id
    private String uuid;
    private String url;
    private String title;
    private long downloadTime;

    public Optimizable() {
    }

    public Optimizable(String url, String title, long downloadTime, String uuid) {
        this.url = url;
        this.title = title;
        this.downloadTime = downloadTime;
        this.uuid = uuid;
    }

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
