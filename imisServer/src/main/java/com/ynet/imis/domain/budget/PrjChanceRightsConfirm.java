package com.ynet.imis.domain.budget;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.project.ProjectChance;

@Entity
@Table(name = "PRJ_CHANCE_RIGHTS_CONFIRM")
public class PrjChanceRightsConfirm extends CommBudget {

    private static final long serialVersionUID = 5706330629093916741L;
    @Column(name = "BEG_DATE")
    private Date begDate; // 确权范围
    @Column(name = "END_DATE")
    protected Date endDate;

    @Column(name = "DEP_ID")
    private Long depId;

    @Column(name = "PRJ_CHANCE_ID")
    private Long prjChanceId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // cascade = CascadeType.PERSIST,
    @JoinColumn(name = "PRJ_CHANCE_ID", nullable = false, updatable = false, insertable = false)
    private ProjectChance projectChance;

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

    public ProjectChance getProjectChance() {
        return this.projectChance;
    }

    public void setProjectChance(ProjectChance projectChance) {
        this.projectChance = projectChance;
        this.prjChanceId = projectChance.getId();
    }

    public void setPrjChanceId(Long id) {
        prjChanceId = id;
    }

    public Long getPrjChanceId() {
        return prjChanceId;
    }
}