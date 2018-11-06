package com.ynet.imis.repository;

import com.ynet.imis.domain.LockEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LockRepository extends JpaRepository<LockEntity, Long> {

    @Query("select a from LockEntity a where a.lockId=?1")
    public LockEntity findByLockId(int lockId);

}