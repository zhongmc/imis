package com.ynet.imis.domain.budget;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;
import com.ynet.imis.domain.project.Project;

@Entity
@Table(name = "PRJ_RIGHTS_CONFIRM")
public class PrjRightsConfirm extends AbstractEntity {

    private static final long serialVersionUID = -5724326134628837609L;

    @Column(length = 64)
    protected String name;

    @Column(length = 128, name = "YC_DESC")
    protected String desc;

    @Column(name = "BG_DATE")
    protected Date bgDate;

    private Date realDate; // 确权日期

    private Date expectDate; // 预计日期

    @Column(name = "BEG_DATE")
    private Date begDate; // 确权范围
    @Column(name = "END_DATE")
    protected Date endDate;

    protected BigDecimal amount;

    protected BigDecimal realAmount;

    @Column(name = "DEP_ID")
    private Long depId;

    @Column(name = "PRJ_ID")
    private Long prjId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_ID", nullable = false, updatable = false, insertable = false)
    private Project project;

    public Date getRealDate() {
        return this.realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }

    public Date getExpectDate() {
        return this.expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Date getBegDate() {
        return this.begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal amount) {
        this.realAmount = amount;
    }

    public Date getBgDate() {
        return bgDate;
    }

    public void setBgDate(Date value) {
        this.bgDate = value;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date value) {
        this.endDate = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        desc = value;
    }

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.prjId = project.getId();
    }

    public void setPrjId(Long id) {
        prjId = id;
    }

    public Long getPrjId() {
        return prjId;
    }
}