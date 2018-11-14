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
@Table(name = "COST_BUDGET_INFO")
public class CostBudgetInfo extends AbstractEntity {

    private static final long serialVersionUID = 1571466053840848576L;

    private int year;
    private short month;
    private BigDecimal amount;

    private BigDecimal realAmount;

    public BigDecimal getRealAmount() {
        return this.realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    private Department department;

    @Column(name = "BUDGET_TYPE_ID")
    private Long budgetTypeId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BUDGET_TYPE_ID", nullable = false, updatable = false, insertable = false)
    private BudgetType budgetType;

    @Column(name = "COST_ITEM_ID")
    private Long costItemId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "COST_ITEM_ID", nullable = false, updatable = false, insertable = false)
    private CostItem costItem;

    private Long costGroupId;

    public Long getCostGroupId() {
        return this.costGroupId;
    }

    public void setCostGroupId(Long costGroupId) {
        this.costGroupId = costGroupId;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public short getMonth() {
        return this.month;
    }

    public void setMonth(short month) {
        this.month = month;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Long getDepId() {
        return this.depId;
    }

    public void setDepId(Long depId) {
        this.depId = depId;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
        this.depId = department.getId();
    }

    public Long getBudgetTypeId() {
        return this.budgetTypeId;
    }

    public void setBudgetTypeId(Long budgetTypeId) {
        this.budgetTypeId = budgetTypeId;
    }

    public BudgetType getBudgetType() {
        return this.budgetType;
    }

    public void setBudgetType(BudgetType budgetType) {
        this.budgetType = budgetType;
        this.budgetTypeId = budgetType.getId();
    }

    public Long getCostItemId() {
        return this.costItemId;
    }

    public void setCostItemId(Long costItemId) {
        this.costItemId = costItemId;
    }

    public CostItem getCostItem() {
        return this.costItem;
    }

    public void setCostItem(CostItem costItem) {
        this.costItem = costItem;
        costItemId = costItem.getId();
        this.costGroupId = costItem.getGroupId();
    }
}