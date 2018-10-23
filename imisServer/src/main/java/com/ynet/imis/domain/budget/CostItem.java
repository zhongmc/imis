/**
* CostItem.java
* @author ZHONGMC
* @description 费用预算具体条目定义
* @created Tue Sep 18 2018 16:47:31 GMT+0800 (中国标准时间)
* @copyright 
* @last-modified Fri Oct 19 2018 16:08:08 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.AbstractEntity;

@Entity
@Table(name = "COST_ITEM")
public class CostItem extends AbstractEntity {

    private static final long serialVersionUID = -7440125235979232161L;

    public CostItem() {

    }

    public CostItem(Long id, Long groupId, String name, String amountFormula, boolean beAmount, String desc) {
        this.setId(id);
        this.groupId = groupId;
        this.name = name;
        this.amountFormula = amountFormula;
        this.beAmount = beAmount;
        this.desc = desc;
    }

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false, insertable = false)
    private CostGroup costGroup;// 分组

    @Column(name = "GROUP_ID")
    protected Long groupId;

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Column(length = 64)
    private String name;

    private boolean beAmount;

    @Column(name = "AMT_FORMULA", length = 64)
    private String amountFormula;

    public String getAmountFormula() {
        return this.amountFormula;
    }

    public void setAmountFormula(String amountFormula) {
        this.amountFormula = amountFormula;
    }

    @Column(length = 128, name = "YC_DESC")
    private String desc;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String value) {
        desc = value;
    }

    public CostGroup getGroup() {
        return costGroup;
    }

    public void setCostGroup(CostGroup value) {
        costGroup = value;
        groupId = value.getId();
    }

    public boolean getBeAmount() {
        return beAmount;
    }

    public void setBeAmount(boolean beAmount) {
        this.beAmount = beAmount;
    }

}