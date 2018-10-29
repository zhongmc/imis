/**
* PrjIncomeForecast.java
* @author ZHONGMC
* @description 
* @created Thu Oct 18 2018 14:40:43 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Oct 25 2018 15:38:28 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

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
@Table(name = "PRJ_INCOME_FORECAST")
public class PrjIncomeForecast extends CommBudget {

    private static final long serialVersionUID = -2507847790693324169L;

    @Column(name = "DEP_ID")
    private Long depId;

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

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

}