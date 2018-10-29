/**
* CommBudget.java
* @author ZHONGMC
* @description 公共预算条目
* @created Tue Sep 18 2018 16:48:34 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Oct 26 2018 09:41:20 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.ynet.imis.domain.AbstractEntity;

@MappedSuperclass
public class CommBudget extends AbstractEntity {

    private static final long serialVersionUID = -8060466829366552782L;

    @Column(length = 64)
    protected String name;

    @Column(length = 128, name = "YC_DESC")
    protected String desc;

    @Column(name = "BG_DATE")
    protected Date bgDate;

    @Column(name = "END_DATE")
    protected Date endDate;

    protected BigDecimal amount;

    protected BigDecimal realAmount;

    public CommBudget() {

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

    public void setRealAmount(BigDecimal amount) {
        this.realAmount = amount;
    }

    public Date getBgDate() {
        return bgDate;
    }

    public void setBgDate(Date value) {
        this.bgDate = value;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date value) {
        this.endDate = value;
    }

    // //类别
    // public enum CostType{
    // DEPARTMENT,
    // PROJECT,
    // OTHER
    // }
    // private CostType costType;

    // public CostType getCostType()
    // {
    // return costType;
    // }

    // public void setCostType(CostType costType)
    // {
    // this.costType = costType;
    // }

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

}