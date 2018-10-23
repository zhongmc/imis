/**
* DepCommBudget.java
* @author ZHONGMC
* @description 部门公共预算条目
* @created Tue Sep 18 2018 17:37:09 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Fri Oct 19 2018 15:10:18 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.domain.budget;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.org.Department;

@Entity
@Table(name = "DEP_COMM_BUDGET")
public class DepCommBudget extends CommBudget {

    private static final long serialVersionUID = 2623896318518572970L;

    @Column(name = "DEP_ID")
    private Long depId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEP_ID", nullable = false, updatable = false, insertable = false)
    private Department department;

    public DepCommBudget() {

    }

    public DepCommBudget(Long id, Long depId, String name, BigDecimal amount, Date bgDate, String desc) {
        super.id = id;
        this.depId = depId;
        super.setName(name);
        super.setAmount(amount);
        super.setBgDate(bgDate);
        super.setDesc(desc);

    }

    public void setDepId(Long id) {
        depId = id;
    }

    public Long getDepId() {
        return depId;
    }

}