/**
* MonthCost.java
* @author ZHONGMC
* @description 费用预算的每月定义
* @created Tue Sep 18 2018 18:29:08 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Wed Oct 17 2018 11:40:20 GMT+0800 (中国标准时间)
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
@Table(name = "MONTH_COST")
public class MonthCost extends AbstractEntity {

    private static final long serialVersionUID = -8548023132071994481L;

    private int year;
    private short month;
    private BigDecimal amount;
    private BigDecimal realAmount;

    @Column(name = "BUDGET_TYPE_ID")
    private Long budgetTypeId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "BUDGET_TYPE_ID", nullable = false, updatable = false, insertable = false)
    private BudgetType budgetType;

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    private Department department;

    @Column(name = "COST_ITEM_ID")
    private long costItemId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "COST_ITEM_ID", nullable = false, updatable = false, insertable = false)
    private CostItem costItem;

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

    public Long getCostItemId() {
        return costItemId;
    }

    public void setCostItemId(Long id) {
        costItemId = id;
    }

    public CostItem getCostItem() {
        return costItem;
    }

    public void setCostItem(CostItem item) {
        costItem = item;
        this.costItemId = item.getId();
    }

    public Long getBudgetTypeId() {
        return budgetTypeId;
    }

    public void setBudgetTypeId(Long id) {
        budgetTypeId = id;
    }

    public BudgetType getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
        this.budgetTypeId = budgetType.getId();
    }
}