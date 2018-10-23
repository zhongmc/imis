package com.ynet.imis.repository.menu;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ynet.imis.domain.menu.Menu;

public interface MenuRepository extends CrudRepository<Menu, Long> {

	
	 @Query("select a from Menu a where a.parentId is null")
	 public List<Menu> listRootMenu();
	
	 @Query("select a from Menu a where a.parentId=?1")
	 public List<Menu> findChild(Long id);
	 
	// Menu(Long id, Long parentId, String name, String path, String component, boolean hidden)
	 @Query("select new Menu(a.id, a.parentId, a.name, a.path, a.component, a.hidden) from Menu a")
	 public List<Menu> listAllMenu();
	 
	//  @Query("select new Menu(a.id, a.parentId, a.name, a.url, a.desc, a.number, a.systemFunc) from Menu a where a.id=?1")
	//  public Menu getMenuByID(Long id);
	 

}