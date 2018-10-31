/**
* PrjMonthBudget.java
* @author ZHONGMC
* @description 项目月预算条目
* @created Tue Sep 18 2018 16:53:38 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Oct 30 2018 17:47:20 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;
import com.ynet.imis.domain.org.Department;

@Entity
@Table(name = "PRJ_MONTH_BUDGET")
public class PrjMonthBudget extends AbstractEntity {

    private static final long serialVersionUID = 1399722498180677635L;

    private int year;
    private short month;
    private BigDecimal amount;
    private BigDecimal realAmount;

    private float manMonth;
    private float realManMonth;

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    private Department department;

    @Column(name = "PRJ_BUDGET_ID")
    private Long prjBudgetId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_BUDGET_ID", nullable = false, updatable = false, insertable = false)
    private PrjBudget prjBudget;

    @Column(name = "PRJ_ID")
    private Long prjId;

    public Long getPrjId() {
        return this.prjId;
    }

    public void setPrjId(Long prjId) {
        this.prjId = prjId;
    }

    public PrjMonthBudget() {

    }

    public PrjMonthBudget(Long id, int year, short month, BigDecimal amount, float manMonth) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.amount = amount;
        this.manMonth = manMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public short getMonth() {
        return month;
    }

    public void setMonth(short month) {
        this.month = month;
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

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    public Long getDepId() {
        return depId;
    }

    public void setDepId(Long id) {
        depId = id;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department dep) {
        this.department = dep;
        this.depId = dep.getId();
    }

    public float getManMonth() {
        return manMonth;
    }

    public void setManMonth(float value) {
        manMonth = value;
    }

    public float getRealManMonth() {
        return realManMonth;
    }

    public void setRealManMonth(float value) {
        realManMonth = value;
    }

    public Long getPrjBudgetId() {
        return this.prjBudgetId;
    }

    public void setPrjBudgetId(Long id) {
        this.prjBudgetId = id;
    }

    public PrjBudget getPrjBudget() {
        return this.prjBudget;
    }

    public void setPrjBudget(PrjBudget prjBudget) {
        this.prjBudget = prjBudget;
        this.prjBudgetId = prjBudget.getId();
    }

}