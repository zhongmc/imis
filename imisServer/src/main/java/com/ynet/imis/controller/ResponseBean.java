package com.ynet.imis.controller;


public class ResponseBean {

    private String status;
    private String message;

    private Object retObject;

    public Object getRetObject() {
        return this.retObject;
    }

    public void setRetObject(Object retObject) {
        this.retObject = retObject;
    }

    public  ResponseBean(String status, String message)
    {
        this.status = status;
        this.message = message;
    }

    public  ResponseBean(String status, String message, Object retObj)
    {
        this.status = status;
        this.message = message;
        retObject = retObj;
    }


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}