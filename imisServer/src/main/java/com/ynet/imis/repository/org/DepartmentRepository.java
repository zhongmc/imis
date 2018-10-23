package com.ynet.imis.repository.org;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ynet.imis.domain.org.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>,
		JpaSpecificationExecutor<Department> {

	@Query("select new Department(a.id,  a.parentId, a.name, a.fullId) from Department a where a.parentId=-1 or a.parentId is null")
	public List<Department> getRootDeps();
	
	@Query("select new Department(a.id, a.parentId, a.name, a.fullId) from Department a")
	public List<Department> getAllDeps();

	@Query("select a from Department a where a.parentId is null")
	public List<Department> getDepartmentTree();


	// public Department(Long id, Long pid, Long rootDepId, String name, int level, DepType type, String desc,
	// 		String orgNumber, String fullId) {
}
