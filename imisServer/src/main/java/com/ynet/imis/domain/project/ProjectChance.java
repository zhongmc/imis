/**
* ProjectChance.java
* @author ZHONGMC
* @description  项目机会
* @created Tue Sep 18 2018 18:01:36 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Dec 07 2018 15:03:45 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.project;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;
import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.domain.project.Project.PrjType;

@Entity
public class ProjectChance extends AbstractEntity {

    private static final long serialVersionUID = -1566292962548348166L;

    @Column(length = 128)
    private String name;

    public String getName() {
        return this.name;
    }

    // 增值税税率
    private double taxRate = 0.06; //

    public double getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(double taxRate) {
        this.taxRate = taxRate;
    }

    /**
     * @return the chance
     */
    public short getChance() {
        return chance;
    }

    /**
     * @param chance the chance to set
     */
    public void setChance(short chance) {
        this.chance = chance;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "CHANCE_NO", length = 24)
    private String chanceNo;

    public String getChanceNo() {
        return this.chanceNo;
    }

    public void setChanceNo(String chanceNo) {
        this.chanceNo = chanceNo;
    }

    @Column(name = "CUSTOM_ID")
    private Long customId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOM_ID", nullable = false, updatable = false, insertable = false)
    Custom custom;

    @Transient
    private String customName;

    private short chance; // 概率 （%）

    public BigDecimal getContractAmount() {
        return this.contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    private BigDecimal contractAmount;

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    Department department;

    @Transient
    private String depName;

    public Custom getCustom() {
        return this.custom;
    }

    public void setCustom(Custom custom) {
        this.custom = custom;
        if (custom != null)
            this.customId = custom.getId();
        else
            customId = null;
    }

    public Long getCustomId() {
        return this.customId;
    }

    public void setCustomId(Long customId) {
        this.customId = customId;
    }

    public String getCustomName() {
        return this.customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        if (department != null)
            depId = department.getId();
        else
            depId = null;
    }

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public String getDepName() {
        return this.depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    private Date beginDate;
    private Date endDate;

    public Date getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(Date begDate) {
        this.beginDate = begDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // public enum PrjType {
    // TASKS, MAN_MONTH, PROJECT, OTHER
    // };

    private PrjType prjType;

    public PrjType getPrjType() {
        return this.prjType;
    }

    public void setPrjType(PrjType prjType) {
        this.prjType = prjType;
    }

    public enum ChanceState {
        NORMAL, TO_PRJ, CLOSED
    };

    private ChanceState chanceState = ChanceState.NORMAL;

    public ChanceState getChanceState() {
        return this.chanceState;
    }

    public void setChanceState(ChanceState chanceState) {
        this.chanceState = chanceState;
    }

}