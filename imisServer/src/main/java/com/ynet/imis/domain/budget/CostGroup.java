/**
* CostGroup.java
* @author ZHONGMC
* @description  四项费用分组
* @created Tue Sep 18 2018 16:47:11 GMT+0800 (中国标准时间)
* @copyright 
* @last-modified Mon Nov 05 2018 17:37:03 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ynet.imis.domain.AbstractEntity;

@Entity
@Table(name = "COST_GROUP")
public class CostGroup extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(length = 64)
    private String name;

    @Transient
    private List<CostItem> costItems = new ArrayList<CostItem>();

    public List<CostItem> getCostItems() {
        return this.costItems;
    }

    public void setCostItems(List<CostItem> costItems) {
        this.costItems = costItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        name = value;
    }

}