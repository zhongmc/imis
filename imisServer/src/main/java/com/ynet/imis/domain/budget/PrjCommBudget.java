/**
* PrjCommBudget.java
* @author ZHONGMC
* @description 项目公共预算条目
* @created Tue Sep 18 2018 17:37:23 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 01 2018 11:06:31 GMT+0800 (中国标准时间)
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
import com.ynet.imis.domain.project.Project;

@Entity
@Table(name = "PRJ_COMM_BUDGET")
public class PrjCommBudget extends CommBudget {

    private static final long serialVersionUID = 9003596827131602523L;

    @Column(name = "DEP_ID")
    private Long depId;

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    // private Project project;

    @Column(name = "PRJ_ID")
    private Long prjId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_ID", nullable = false, updatable = false, insertable = false)
    private Project project;

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

    public PrjCommBudget() {

    }

    public PrjCommBudget(Long id, Long prjId, BigDecimal amount, String name, Date expectDate, String desc) {
        this.id = id;
        this.prjId = prjId;
        this.amount = amount;
        this.name = name;
        this.expectDate = expectDate;
        this.desc = desc;
    }

}