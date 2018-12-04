/**
* PrjBudget.java
* @author ZHONGMC
* @description 
* @created Tue Sep 18 2018 16:53:17 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 09:59:43 GMT+0800 (中国标准时间)
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
import com.ynet.imis.domain.project.Project;

@Entity
@Table(name = "PRJ_BUDGET")
public class PrjBudget extends AbstractEntity {

    private static final long serialVersionUID = -5971215572107380355L;

    private BigDecimal avgManMonthCost = new BigDecimal(0);

    // 递延到今年的人月与费用 （最近确权需要递延的成本？？？只作为导入时的用处，其它）
    // 统一计到上年的12月份！！！！
    // private double defferedManMonth;
    // private BigDecimal defferedAmount;

    private int confirmYear; // 确权年份，是否今年确权

    public int getConfirmYear() {
        return this.confirmYear;
    }

    public void setConfirmYear(int confirmYear) {
        this.confirmYear = confirmYear;
    }

    // public double getDefferedManMonth() {
    // return this.defferedManMonth;
    // }

    // public void setDefferedManMonth(double defferedManMonth) {
    // this.defferedManMonth = defferedManMonth;
    // }

    // public BigDecimal getDefferedAmount() {
    // return this.defferedAmount;
    // }

    // public void setDefferedAmount(BigDecimal defferedAmount) {
    // this.defferedAmount = defferedAmount;
    // }

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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_ID", nullable = false, updatable = false, insertable = false)
    private Project project;
    private String prjNo;

    // @Transient
    private String prjName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "PRJ_BUDGET_ID")
    @OrderBy("year, month ASC")
    List<PrjMonthBudget> monthBudgets = new ArrayList<PrjMonthBudget>();

    public PrjBudget() {

    }

    public BigDecimal getAvgManMonthCost() {
        return this.avgManMonthCost;
    }

    public void setAvgManMonthCost(BigDecimal avgManMonthCost) {
        this.avgManMonthCost = avgManMonthCost;
    }

    public Long getPrjId() {
        return this.prjId;
    }

    public void setPrjId(Long prjId) {
        this.prjId = prjId;
        for (PrjMonthBudget pmb : monthBudgets)
            pmb.setPrjId(prjId);
    }

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
        if (project != null) {
            this.setPrjId(project.getId());
            this.prjName = project.getName();
        }

    }

    public String getPrjNo() {
        return this.prjNo;
    }

    public void setPrjNo(String prjNo) {
        this.prjNo = prjNo;
    }

    public String getPrjName() {
        return this.prjName;
    }

    public void setPrjName(String prjName) {
        this.prjName = prjName;
    }

    public List<PrjMonthBudget> getMonthBudgets() {
        return this.monthBudgets;
    }

    public void addPrjMonthBudget(PrjMonthBudget monthBudget) {

        // 判断是否已存在, 导入数据时，处理递延成本
        for (PrjMonthBudget mb : monthBudgets) {
            if (mb.getYear() == monthBudget.getYear() && mb.getMonth() == monthBudget.getMonth()) {
                mb.setAmount(mb.getAmount().add(monthBudget.getAmount()));
                mb.setManMonth(mb.getManMonth() + monthBudget.getManMonth());
                return;
            }
        }
        monthBudgets.add(monthBudget);
        monthBudget.setPrjBudget(this);
    }

    public void setMonthBudgets(List<PrjMonthBudget> monthBudgets) {
        this.monthBudgets = monthBudgets;
    }

    // @OneToMany(cascade = CascadeType.PERSIST)
    // @JoinColumn(name = "PRJ_BUDGET_ID")
    // List<PrjCommBudget> commBudgets = new ArrayList<PrjCommBudget>();

    // public List<PrjCommBudget> getCommBudgets() {
    // return this.commBudgets;
    // }

    // public void addCommBudget(PrjCommBudget commBudget) {
    // commBudgets.add(commBudget);
    // commBudget.setPrjBudget(this);
    // }

    // public void setCommBudgets(List<PrjCommBudget> commBudgets) {
    // this.commBudgets = commBudgets;
    // }

    public PrjBudget(Long id, long depId, Long prjId, String prjName, String prjNo, BigDecimal avgMonthCost) {
        this.id = id;
        this.depId = depId;
        this.prjId = prjId;
        this.prjName = prjName;
        this.prjNo = prjNo;
        this.avgManMonthCost = avgMonthCost;

    }

    public PrjBudget clone() {
        PrjBudget prjBudget = new PrjBudget();

        prjBudget.id = id;
        prjBudget.depId = depId;
        prjBudget.prjId = prjId;
        prjBudget.prjName = prjName;
        prjBudget.prjNo = prjNo;
        prjBudget.confirmYear = confirmYear;
        // prjBudget.defferedAmount = defferedAmount;
        // prjBudget.defferedManMonth = defferedManMonth;

        prjBudget.avgManMonthCost = avgManMonthCost;

        return prjBudget;
    }

    public Map<String, Object> getMapedObject(int year) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        retMap.put("id", id);
        retMap.put("depId", depId);
        retMap.put("prjId", prjId);
        retMap.put("prjNo", prjNo);
        retMap.put("prjName", prjName);
        retMap.put("avgManMonthCost", avgManMonthCost);

        List<PrjMonthBudget> pmbs = new ArrayList<PrjMonthBudget>();
        retMap.put("monthBudgets", pmbs);

        for (PrjMonthBudget mb : this.monthBudgets) {
            if (mb.getYear() == year)
                pmbs.add(mb);
        }

        return retMap;
    }

    public void copyFrom(PrjChanceBudget prjChanceBudget) {

        this.depId = prjChanceBudget.getDepId();
        this.avgManMonthCost = prjChanceBudget.getAvgManMonthCost();
        this.confirmYear = prjChanceBudget.getConfirmYear();

        for (PrjChanceMonthBudget prjCMB : prjChanceBudget.getMonthBudgets()) {
            PrjMonthBudget prjMonthBudget = new PrjMonthBudget();
            prjMonthBudget.setPrjBudget(this);
            prjMonthBudget.setMonth(prjCMB.getMonth());
            prjMonthBudget.setYear(prjCMB.getYear());
            prjMonthBudget.setConfirmYear(prjCMB.getConfirmYear());
            prjMonthBudget.setAmount(prjCMB.getAmount());
            prjMonthBudget.setManMonth(prjCMB.getManMonth());
            prjMonthBudget.setDepId(this.depId);
        }

    }
}