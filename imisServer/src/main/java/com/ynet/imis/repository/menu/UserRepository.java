package com.ynet.imis.repository.menu;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.data.jpa.repository.Query;

import com.ynet.imis.domain.menu.User;

public interface UserRepository extends JpaRepository<User, Long>,
	JpaSpecificationExecutor<User> {

	 @Query("select a from User a where a.id = ?1") 
	 public User findUserById(Long Id); 
	
	 @Query("select a from User a where a.userName=?1 AND a.status != 'DELETE'")
	 public User findByUserName(String value );
	 
	 @Query("from User") 
	 public List<User> listUser();
	 

	
}
