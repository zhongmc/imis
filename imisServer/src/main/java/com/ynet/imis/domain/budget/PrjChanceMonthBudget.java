
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
@Table(name = "PRJ_CHANCE_MONTH_BUDGET")
public class PrjChanceMonthBudget extends AbstractEntity {

    private static final long serialVersionUID = 8185136374594922384L;

    private int year; // 发生年份

    private short month;
    private BigDecimal amount = new BigDecimal(0);

    private float manMonth = 0;
    // private float realManMonth;
    // private BigDecimal realAmount;
    private int confirmYear = 0; // 确权年份（成本也相应计入） 0 代表未确权

    public int getConfirmYear() {
        return this.confirmYear;
    }

    public void setConfirmYear(int confirmYear) {
        this.confirmYear = confirmYear;
    }

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    private Department department;

    @Column(name = "PRJ_CHANCE_BUDGET_ID")
    private Long prjChanceBudgetId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_CHANCE_BUDGET_ID", nullable = false, updatable = false, insertable = false)
    private PrjChanceBudget prjChanceBudget;

    @Column(name = "PRJ_CHANCE_ID")
    private Long prjChanceId;

    public Long getPrjChanceId() {
        return this.prjChanceId;
    }

    public void setPrjChanceId(Long prjId) {
        this.prjChanceId = prjId;
    }

    public PrjChanceMonthBudget() {

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

    public Long getPrjChanceBudgetId() {
        return this.prjChanceBudgetId;
    }

    public void setPrjChanceBudgetId(Long id) {
        this.prjChanceBudgetId = id;
    }

    public PrjChanceBudget getPrjChanceBudget() {
        return this.prjChanceBudget;
    }

    public void setPrjChanceBudget(PrjChanceBudget prjChanceBudget) {
        this.prjChanceBudget = prjChanceBudget;
        this.prjChanceBudgetId = prjChanceBudget.getId();
    }

}