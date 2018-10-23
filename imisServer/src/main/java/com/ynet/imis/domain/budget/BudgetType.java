/**
* BudgetType.java
* @author ZHONGMC
* @description  预算类别，如；部门闲置、部门管理
* @created Tue Sep 18 2018 16:46:56 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Sep 18 2018 16:57:30 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ynet.imis.domain.AbstractEntity;



@Entity
@Table(name="BUDGET_TYPE")
public class BudgetType extends AbstractEntity{

    private static final long serialVersionUID = 5641654367103162689L;

    @Column(length = 64)
    private String name;

    @Column(length=128, name="YC_DESC")
    private String desc;

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String value )
    {
        desc = value;
    }
}