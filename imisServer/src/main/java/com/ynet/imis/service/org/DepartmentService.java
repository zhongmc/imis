package com.ynet.imis.service.org;

import java.util.List;

import com.ynet.imis.domain.org.Department;

public interface DepartmentService {

    public Department getDepartmentById(Long id);

    public void updateDepartment(Department dep);

    public Department addDepartment(Department dep);

    public int deleteDepartmentById(Long id);

    public List<Department> getDepartmentTree();

    public Department getDepartmentTreeByPid(Long pId);

    public List<Department> listAllDepartmentByPid(Long pId);

    public List<Long> listAppDepartmentsIdByPid(Long pid);

    public List<Department> ListAllDepartments();

    public String getDepartmentName(Long id);

}