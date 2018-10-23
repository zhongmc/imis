/**
* RoleService.java
* @author ZHONGMC
* @description 
* @created Mon Oct 08 2018 10:59:15 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Oct 09 2018 14:27:59 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.service.menu;

import java.util.List;

import com.ynet.imis.domain.menu.Role;

public interface RoleService {
  
    public Role getRoleById(Long id);
    public int updateRole(Role role);
    public Role addRole(Role role );
    public int deleteRoleById(Long id );
    
    public int updateRoleMenus(Long rid, Long[]mids);
    public List<Role> getAllRoles();
    
}