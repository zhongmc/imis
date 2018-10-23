package com.ynet.imis.repository.menu;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ynet.imis.domain.menu.Role;


/**
 * 
 * @see Role.RoleType (0 SYSTEM, 1 GLOBAL, 2 ORG_GLOBAL, 3 MER_GLOBAL, 4 PRIVATE)
 */
public interface RoleRepository extends JpaRepository<Role, Long>,
JpaSpecificationExecutor<Role> {


	
}
