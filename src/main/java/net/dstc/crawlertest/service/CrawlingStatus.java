/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.dstc.crawlertest.service;

/**
 * Status of a crawling operation.
 *
 * @author Patrizio Bruno (desertconsulting@gmail.com)
 */
public class CrawlingStatus {

    public enum StatusCode {
        ERROR,
        RUNNING,
        DONE
    }

    private String id;
    private String url;
    private Exception error;
    private StatusCode code;
    private boolean optimizable;

    public String getUrl() {
        return url;
    }

    /**
     * Sets requested URL (the one received by a client request)
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Error message got from an exception
     *
     * @return
     */
    public String getError() {
        return error != null ? error.toString() : null;
    }

    /**
     * Sets an error message and change this.code to ERROR
     *
     * @param error
     */
    public void setError(Exception error) {
        this.error = error;
        code = StatusCode.ERROR;
    }

    public StatusCode getCode() {
        return code;
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    public boolean isOptimizable() {
        return optimizable;
    }

    void setOptimizable(boolean optimizable) {
        this.optimizable = optimizable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
