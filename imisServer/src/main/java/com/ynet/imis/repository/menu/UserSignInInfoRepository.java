package com.ynet.imis.repository.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ynet.imis.domain.menu.UserSignInInfo;

public interface UserSignInInfoRepository extends JpaRepository<UserSignInInfo, Long>,
		JpaSpecificationExecutor<UserSignInInfo> {

	@Query( "select new UserSignInInfo(a.id, a.userId, a.successCount, a.failedCount, a.lastIP, a.currentIP, a.signInTime, a.lastSignInTime, a.loginTime) "
			+ "from UserSignInInfo a where a.userId =?1" ) 
	UserSignInInfo getUserSignInInfo(Long userId );
	
	
}
