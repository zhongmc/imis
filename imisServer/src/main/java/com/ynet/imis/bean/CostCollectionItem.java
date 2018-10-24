package com.ynet.imis.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class CostCollectionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long costItemId;
    private String costItemName;
    private Long costGroupId;
    private String costGroupName;
    private BigDecimal amounts[];
    private BigDecimal sum;

    public CostCollectionItem() {
        amounts = new BigDecimal[12];
        sum = BigDecimal.valueOf(0);
        for (int i = 0; i < 12; i++)
            amounts[i] = BigDecimal.valueOf(0);

    }

    public CostCollectionItem(String groupName, String itemName) {
        amounts = new BigDecimal[12];
        sum = BigDecimal.valueOf(0);
        for (int i = 0; i < 12; i++)
            amounts[i] = BigDecimal.valueOf(0);

    }

    public Long getCostItemId() {
        return this.costItemId;
    }

    public void setCostItemId(Long costItemId) {
        this.costItemId = costItemId;
    }

    public String getCostItemName() {
        return this.costItemName;
    }

    public void setCostItemName(String costItemName) {
        this.costItemName = costItemName;
    }

    public Long getCostGroupId() {
        return this.costGroupId;
    }

    public void setCostGroupId(Long costGroupId) {
        this.costGroupId = costGroupId;
    }

    public String getCostGroupName() {
        return this.costGroupName;
    }

    public void setCostGroupName(String costGroupName) {
        this.costGroupName = costGroupName;
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

        amounts[month] = amounts[month].add(amount);
        sum = sum.add(amount);
    }

}