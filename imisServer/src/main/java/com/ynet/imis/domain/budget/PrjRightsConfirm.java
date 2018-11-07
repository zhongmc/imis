package com.ynet.imis.domain.budget;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.project.Project;

@Entity
@Table(name = "PRJ_RIGHTS_CONFIRM")
public class PrjRightsConfirm extends CommBudget {

    private static final long serialVersionUID = -5724326134628837609L;

    @Column(name = "BEG_DATE")
    private Date begDate; // 确权范围
    @Column(name = "END_DATE")
    protected Date endDate;

    @Column(name = "DEP_ID")
    private Long depId;

    @Column(name = "PRJ_ID")
    private Long prjId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // cascade = CascadeType.PERSIST,
    @JoinColumn(name = "PRJ_ID", nullable = false, updatable = false, insertable = false)
    private Project project;

    public Date getBegDate() {
        return this.begDate;
    }

    public void setBegDate(Date begDate) {
        this.begDate = begDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date value) {
        this.endDate = value;
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