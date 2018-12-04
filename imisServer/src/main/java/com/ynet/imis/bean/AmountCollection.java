/**
* AmountCollection.java
* @author ZHONGMC
* @description  报表用，总量表，如多个部门比较时的，某项合计报表
*
* @created Fri Nov 16 2018 17:10:44 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Mon Nov 26 2018 09:56:54 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmountCollection implements Serializable {

    private static final long serialVersionUID = 453362864144184456L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private int size;
    private String name;
    private BigDecimal amounts[];

    public int getSize() {
        return this.size;
    }

    public void setSize(int size) {
        this.size = size;
        amounts = new BigDecimal[size];
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal[] getAmounts() {
        return this.amounts;
    }

    public void setAmounts(BigDecimal amounts[]) {
        this.amounts = amounts;
    }

    public AmountCollection(int size) {
        if (size < 1) {
            logger.error("size out of bound!" + size);
            return;
        }
        this.size = size;
        amounts = new BigDecimal[size];
    }

    public void copySumFrom(CostCollectionItem item, int idx) {
        if (idx >= size) {
            logger.error("index out of bound! " + idx + ":" + size);
            return;
        }
        // this.name = item.getName();
        this.amounts[idx] = item.getSum();
    }

    public void setAmount(int idx, BigDecimal amount) {
        if (idx >= size) {
            logger.error("index out of bound! " + idx + ":" + size);
            return;
        }
        // this.name = item.getName();
        this.amounts[idx] = amount;

    }

    public static AmountCollection CopyFrom(CostCollectionItem item, int size) {
        AmountCollection amc = new AmountCollection(size);
        amc.name = item.getName();
        amc.setAmount(0, item.getSum());
        return amc;
    }
}