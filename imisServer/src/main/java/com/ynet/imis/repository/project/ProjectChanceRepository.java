package com.ynet.imis.repository.project;

import com.ynet.imis.domain.project.ProjectChance;
import com.ynet.imis.domain.project.ProjectChance.ChanceState;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProjectChanceRepository
                extends JpaRepository<ProjectChance, Long>, JpaSpecificationExecutor<ProjectChance> {

        @Modifying
        @Query("update ProjectChance a set a.chance=:chance where a.id=:id")
        public void updatePrjChanceChance(@Param(value = "id") Long id, @Param(value = "chance") short chance);

        // @Modifying
        // @Query("update ProjectChance a set a.chanceState = ChanceState.CLOSE where
        // a.id=?1")
        // public void closeProjectChance(long id);

        @Modifying
        @Query("update ProjectChance a set a.chanceState=?2 where a.id=?1")
        public void updateChanceState(long id, ChanceState state);
}