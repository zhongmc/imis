package com.ynet.imis.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CostCollectionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    // private Long costGroupId;
    // private Long costItemId;

    private String name;
    private String groupName;

    private BigDecimal amounts[];
    private BigDecimal sum;

    public CostCollectionItem() {
        amounts = new BigDecimal[12];
        sum = BigDecimal.valueOf(0);
        for (int i = 0; i < 12; i++)
            amounts[i] = BigDecimal.valueOf(0);

    }

    public CostCollectionItem(String groupName, String itemName) {
        this.groupName = groupName;
        this.name = itemName;
        amounts = new BigDecimal[12];
        sum = BigDecimal.valueOf(0);
        for (int i = 0; i < 12; i++)
            amounts[i] = BigDecimal.valueOf(0);

    }

    public CostCollectionItem add(CostCollectionItem item) {
        CostCollectionItem newItem = new CostCollectionItem();
        BigDecimal amounts[] = new BigDecimal[12];
        BigDecimal ams[] = item.getAmounts();

        for (int i = 0; i < 12; i++) {
            amounts[i] = ams[i].add(this.amounts[i]);
        }

        newItem.setAmounts(amounts);
        BigDecimal sum = this.sum.add(item.getSum());
        newItem.setSum(sum);
        return newItem;
    }

    public CostCollectionItem subtract(CostCollectionItem item) {
        CostCollectionItem newItem = new CostCollectionItem();
        BigDecimal amounts[] = new BigDecimal[12];
        BigDecimal ams[] = item.getAmounts();

        for (int i = 0; i < 12; i++) {
            amounts[i] = this.amounts[i].subtract(ams[i]);
        }

        newItem.setAmounts(amounts);
        BigDecimal sum = this.sum.subtract(item.getSum());
        newItem.setSum(sum);
        return newItem;
    }

    public CostCollectionItem divide(CostCollectionItem item) {
        CostCollectionItem newItem = new CostCollectionItem();
        BigDecimal amounts[] = new BigDecimal[12];
        BigDecimal ams[] = item.getAmounts();

        for (int i = 0; i < 12; i++) {
            try {
                amounts[i] = this.amounts[i].divide(ams[i]);
            } catch (Exception e) {
                logger.error("failed to div", e.toString());
            }
        }

        newItem.setAmounts(amounts);
        try {
            BigDecimal sum = this.sum.divide(item.getSum());
            newItem.setSum(sum);
        } catch (Exception e) {
            logger.error("failed to div", e.toString());

        }
        return newItem;
    }

    public CostCollectionItem multiply(BigDecimal mult) {
        CostCollectionItem newItem = new CostCollectionItem();
        BigDecimal amounts[] = new BigDecimal[12];

        for (int i = 0; i < 12; i++) {
            try {
                amounts[i] = this.amounts[i].multiply(mult);
            } catch (Exception e) {
                logger.error("failed to div", e.toString());
            }
        }

        newItem.setAmounts(amounts);
        BigDecimal sum = this.sum.multiply(mult);
        newItem.setSum(sum);
        return newItem;
    }

    // public Long getCostItemId() {
    // return this.costItemId;
    // }

    // public void setCostItemId(Long costItemId) {
    // this.costItemId = costItemId;
    // }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // public Long getCostGroupId() {
    // return this.costGroupId;
    // }

    // public void setCostGroupId(Long costGroupId) {
    // this.costGroupId = costGroupId;
    // }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public BigDecimal[] getAmounts() {
        return this.amounts;
    }

    public void setAmounts(BigDecimal amounts[]) {
        this.amounts = amounts;
    }

    public BigDecimal getSum() {
        return this.sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    public void addAmount(int month, BigDecimal amount) {
        if (month < 0 || month > 11)
            return;
        if (amount == null)
            return;

        amounts[month] = amounts[month].add(amount);
        sum = sum.add(amount);
    }

}