/**
* CommBudget.java
* @author ZHONGMC
* @description 公共预算条目
* @created Tue Sep 18 2018 16:48:34 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Nov 01 2018 11:15:18 GMT+0800 (中国标准时间)
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

    protected Date expectDate;

    protected Date realDate;

    protected BigDecimal amount;

    protected BigDecimal realAmount;

    public CommBudget() {

    }

    public Date getExpectDate() {
        return this.expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public Date getRealDate() {
        return this.realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
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