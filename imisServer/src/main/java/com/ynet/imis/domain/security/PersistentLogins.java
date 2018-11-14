package com.ynet.imis.domain.security;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotEmpty;

import com.ynet.imis.domain.AbstractEntity;

@Entity
public class PersistentLogins extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String username;
    @NotEmpty
    @PrimaryKeyJoinColumn
    private String series;
    private String token;
    private Date lastUsed;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastUsed() {
        return this.lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

}