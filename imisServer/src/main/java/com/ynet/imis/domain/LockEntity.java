package com.ynet.imis.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class LockEntity extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Column(unique = true)
    private int lockId;
    private int lockValue;

    public LockEntity() {

    }

    public LockEntity(int lockId, int lockValue) {
        this.lockId = lockId;
        this.lockValue = lockValue;
    }

    public int getLockId() {
        return this.lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public int getLockValue() {
        return this.lockValue;
    }

    public void setLockValue(int lockValue) {
        this.lockValue = lockValue;
    }

}