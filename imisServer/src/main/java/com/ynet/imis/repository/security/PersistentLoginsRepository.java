package com.ynet.imis.repository.security;

import java.util.List;

import com.ynet.imis.domain.security.PersistentLogins;

import org.apache.ibatis.annotations.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PersistentLoginsRepository extends JpaRepository<PersistentLogins, Long> {

    @Query("select a from PersistentLogins a where a.series=?1")
    public PersistentLogins findBySeries(String series);

    @Query("select a from PersistentLogins a where a.username=?1")
    public List<PersistentLogins> foundAllByUsername(String username);

    @Modifying
    @Query("delete from PersistentLogins a where a.username=?1")
    public void removeUserPersistents(String username);
}