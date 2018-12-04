/**
* PrjChanceBudget.java
* @author ZHONGMC
* @description 
* @created Thu Nov 29 2018 10:45:18 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 10:54:37 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;
import com.ynet.imis.domain.project.ProjectChance;

@Entity
@Table(name = "PRJ_CHANCE_BUDGET")
public class PrjChanceBudget extends AbstractEntity {

    private static final long serialVersionUID = -4477383413878593371L;

    private BigDecimal avgManMonthCost = new BigDecimal(0);

    private BigDecimal contractAmount = new BigDecimal(0);

    public BigDecimal getContractAmount() {
        return this.contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    private short chance; // 概率 %

    private int confirmYear; // 确权年份，是否今年确权

    public int getConfirmYear() {
        return this.confirmYear;
    }

    public void setConfirmYear(int confirmYear) {
        this.confirmYear = confirmYear;
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_CHANCE_ID", nullable = false, updatable = false, insertable = false)
    private ProjectChance projectChance;
    // @Transient
    private String prjChanceName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PRJ_CHANCE_BUDGET_ID")
    @OrderBy("year, month")
    List<PrjChanceMonthBudget> monthBudgets = new ArrayList<PrjChanceMonthBudget>();

    public PrjChanceBudget() {

    }

    public BigDecimal getAvgManMonthCost() {
        return this.avgManMonthCost;
    }

    public void setAvgManMonthCost(BigDecimal avgManMonthCost) {
        this.avgManMonthCost = avgManMonthCost;
    }

    public Long getPrjChanceId() {
        return this.prjChanceId;
    }

    public void setPrjChanceId(Long prjCHanceId) {
        this.prjChanceId = prjCHanceId;
        for (PrjChanceMonthBudget pmb : monthBudgets)
            pmb.setPrjChanceId(prjCHanceId);
    }

    public ProjectChance getProjectChance() {
        return this.projectChance;
    }

    public void setProjectChance(ProjectChance projectChance) {
        this.projectChance = projectChance;
        if (projectChance != null) {
            this.setPrjChanceId(projectChance.getId());
            this.prjChanceName = projectChance.getName();
            this.contractAmount = projectChance.getContractAmount();
        }

    }

    public String getPrjChanceName() {
        return this.prjChanceName;
    }

    public void setPrjChanceName(String prjChanceName) {
        this.prjChanceName = prjChanceName;
    }

    public List<PrjChanceMonthBudget> getMonthBudgets() {
        return this.monthBudgets;
    }

    public void addPrjChanceMonthBudget(PrjChanceMonthBudget monthBudget) {

        // 判断是否已存在, 导入数据时，处理递延成本
        for (PrjChanceMonthBudget mb : monthBudgets) {
            if (mb.getYear() == monthBudget.getYear() && mb.getMonth() == monthBudget.getMonth()) {
                mb.setAmount(mb.getAmount().add(monthBudget.getAmount()));
                mb.setManMonth(mb.getManMonth() + monthBudget.getManMonth());
                return;
            }
        }
        monthBudgets.add(monthBudget);
        monthBudget.setPrjChanceBudget(this);
    }

    public void setMonthBudgets(List<PrjChanceMonthBudget> monthBudgets) {
        this.monthBudgets = monthBudgets;
    }

    public PrjChanceBudget clone() {
        PrjChanceBudget prjChanceBudget = new PrjChanceBudget();

        prjChanceBudget.id = id;
        prjChanceBudget.depId = depId;
        prjChanceBudget.prjChanceId = prjChanceId;
        prjChanceBudget.prjChanceName = prjChanceName;
        prjChanceBudget.confirmYear = confirmYear;
        prjChanceBudget.chance = chance;
        prjChanceBudget.contractAmount = contractAmount;
        prjChanceBudget.avgManMonthCost = avgManMonthCost;

        return prjChanceBudget;
    }

    public Map<String, Object> getMapedObject(int year) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("id", id);
        retMap.put("depId", depId);
        retMap.put("prjChanceId", prjChanceId);
        retMap.put("prjChanceName", prjChanceName);
        retMap.put("avgManMonthCost", avgManMonthCost);
        retMap.put("chance", chance);
        List<PrjChanceMonthBudget> pmbs = new ArrayList<PrjChanceMonthBudget>();
        retMap.put("monthBudgets", pmbs);

        for (PrjChanceMonthBudget mb : this.monthBudgets) {
            if (mb.getYear() == year)
                pmbs.add(mb);
        }

        return retMap;
    }
}