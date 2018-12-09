/**
* PrjChanceCommBudget.java
* @author ZHONGMC
* @description 项目机会外采预算条目
* @created Tue Sep 18 2018 17:37:23 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Dec 07 2018 14:16:55 GMT+0800 (中国标准时间)
*/

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
import com.ynet.imis.domain.project.ProjectChance;

@Entity
@Table(name = "PRJ_CHANCE_COMM_BUDGET")
public class PrjChanceCommBudget extends CommBudget {

    private static final long serialVersionUID = 9003596827131602523L;

    @Column(name = "DEP_ID")
    private Long depId;

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    @Column(name = "PRJ_CHANCE_ID")
    private Long prjChanceId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // cascade = CascadeType.PERSIST,
    @JoinColumn(name = "PRJ_CHANCE_ID", nullable = false, updatable = false, insertable = false)
    private ProjectChance projectChance;

    public ProjectChance getProjectChance() {
        return this.projectChance;
    }

    public void setProjectChance(ProjectChance project) {
        this.projectChance = project;
        this.prjChanceId = project.getId();
    }

    public void setPrjChanceId(Long id) {
        prjChanceId = id;
    }

    public Long getPrjChanceId() {
        return prjChanceId;
    }

    public PrjChanceCommBudget() {

    }

}