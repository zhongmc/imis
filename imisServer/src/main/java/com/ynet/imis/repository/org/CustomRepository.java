package com.ynet.imis.repository.org;

import java.util.List;

import com.ynet.imis.domain.org.Custom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomRepository extends JpaRepository<Custom, Long> {

    @Query("select a from Custom a where a.parentId is null")
    public List<Custom> getCustomTree();

    @Query("select a from Custom a where a.name == ?1")
    public Custom findByName(String name);

}