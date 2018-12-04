/**
* AdminController.java
* @author ZHONGMC
* @description 
* @created Mon Oct 08 2018 10:51:34 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 15:26:56 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ynet.imis.controller.ResponseBean;
import com.ynet.imis.domain.budget.BudgetType;
import com.ynet.imis.domain.budget.CostGroup;
import com.ynet.imis.domain.budget.CostItem;
import com.ynet.imis.domain.menu.Menu;
import com.ynet.imis.domain.menu.Role;
import com.ynet.imis.domain.menu.User;
import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.domain.org.Department;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.budget.DepBudgetService;
import com.ynet.imis.service.budget.PrjBudgetService;
import com.ynet.imis.service.menu.MenuService;
import com.ynet.imis.service.menu.RoleService;
import com.ynet.imis.service.menu.UserService;
import com.ynet.imis.service.org.CustomService;
import com.ynet.imis.service.org.DepartmentService;
import com.ynet.imis.service.project.ProjectService;
import com.ynet.imis.service.utils.DataImportService;
import com.ynet.imis.utils.ImisUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/system")
public class AdminController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private CustomService customService;

    @Autowired
    BudgetAdminService budgetAdminService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PrjBudgetService prjBudgetService;
    @Autowired
    private DepBudgetService depBudgetService;

    @Autowired
    private DataImportService dataImportService;

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    ////// Role

    @RequestMapping(value = "/basic/role/{rid}", method = RequestMethod.DELETE)
    public ResponseBean deleteRole(@PathVariable Long rid) {
        if (roleService.deleteRoleById(rid) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/basic/addRole", method = RequestMethod.POST)
    public ResponseBean addNewRole(String role, String roleZh) {

        Role aRole = new Role(role, roleZh);

        Role retRole = roleService.addRole(aRole);
        if (retRole != null) {
            return new ResponseBean("success", "添加成功!", retRole);
        }
        return new ResponseBean("error", "添加失败!");
    }

    /**
     * 菜单树，roleId 对应的可访问menuIds
     * 
     * @param rid
     * @return
     */
    @RequestMapping(value = "/basic/menuTree/{rid}", method = RequestMethod.GET)
    public Map<String, Object> menuTree(@PathVariable Long rid) {
        Map<String, Object> map = new HashMap<>();
        List<Menu> menus = menuService.menuTree();
        map.put("menus", menus);

        Role role = roleService.getRoleById(rid);
        List<Menu> rMs = role.getMenus();
        List<Long> selMids = new ArrayList<Long>();

        for (Menu menu : rMs)
            selMids.add(menu.getId());
        map.put("mids", selMids);
        return map;
    }

    /**
     * 更新Role 可访问的menu
     * 
     * @param rid
     * @param mids
     * @return
     */
    @RequestMapping(value = "/basic/updateMenuRole", method = RequestMethod.PUT)
    public ResponseBean updateMenuRole(Long rid, Long[] mids) {

        int ret = roleService.updateRoleMenus(rid, mids);
        if (ret == 1)
            return new ResponseBean("success", "更新成功!");
        return new ResponseBean("error", "更新失败!");
    }

    @RequestMapping("/basic/roles")
    public List<Role> allRoles() {
        return roleService.getAllRoles();
    }

    /// department

    @RequestMapping(value = "/basic/dep", method = RequestMethod.POST)
    public ResponseBean addDep(Department department) {

        logger.info("Add department: " + ImisUtils.objectJsonStr(department));

        Department aDep = departmentService.addDepartment(department);

        if (aDep != null) {
            return new ResponseBean("success", "add dep success!", aDep);
        }
        return new ResponseBean("error", "添加部门失败!");
    }

    @RequestMapping(value = "/basic/dep/{did}", method = RequestMethod.DELETE)
    public ResponseBean deleteDep(@PathVariable Long did) {
        if (departmentService.deleteDepartmentById(did) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/basic/depTree", method = RequestMethod.GET)
    public List<Department> getDepartmentTree() {
        return departmentService.getDepartmentTree();
    }

    @RequestMapping(value = "/basic/dep/{pid}", method = RequestMethod.GET)
    public Department getDepByPid(@PathVariable Long pid) {

        Department dep = departmentService.getDepartmentTreeByPid(pid);
        return dep;

        // if( dep != null )
        // return new ResponseBean("success", "", dep);

        // return new ResponseBean("error", "department with id: " + pid + " not
        // found!");
    }

    @RequestMapping(value = "/basic/deps", method = RequestMethod.GET)
    public List<Department> getAllDeps() {
        return departmentService.ListAllDepartments();
    }

    @RequestMapping(value = "/basic/myDeps", method = RequestMethod.GET)
    public List<Department> getMyDepartments() {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long pid = user.getDepId();
        return departmentService.listAllDepartmentByPid(pid);

    }

    @RequestMapping(value = "/basic/myDepTree", method = RequestMethod.GET)
    public List<Department> getMyDepartmentTree() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long pid = user.getDepId();

        Department department = departmentService.getDepartmentTreeByPid(pid);
        List<Department> deps = new ArrayList<Department>();
        deps.add(department);
        return deps;

    }

    ///////////////// Budget settings

    @RequestMapping(value = "/budget/settings")
    public Map<String, Object> getBudgetSettings() {
        Map<String, Object> map = new HashMap<>();

        map.put("bePrjBudget", "true");
        map.put("beCommonBudget", "true");
        Map<String, Object> aMap = budgetAdminService.getMyBudgetSettings();
        map.putAll(aMap);

        return map;
    }

    ///// budgetType
    @RequestMapping(value = "/budget/budgetType", method = RequestMethod.POST)
    public ResponseBean addBudgetType(BudgetType budgetType) {
        BudgetType aBudgetType = budgetAdminService.addBudgetType(budgetType);
        if (aBudgetType != null) {
            return new ResponseBean("success", "add budget type success!", aBudgetType);
        }
        return new ResponseBean("error", "添加预算类别失败!");
    }

    @RequestMapping(value = "/budget/budgetType", method = RequestMethod.PUT)
    public ResponseBean updateBudgetType(BudgetType budgetType) {
        int ret = budgetAdminService.updateBudgetType(budgetType);
        if (ret != 0) {
            return new ResponseBean("success", "update budgetType success!");
        }
        return new ResponseBean("error", "更新预算类别失败!");
    }

    @RequestMapping(value = "/budget/budgetType/{did}", method = RequestMethod.DELETE)
    public ResponseBean deleteBudgetTypeById(@PathVariable Long did) {
        if (budgetAdminService.deleteBudgetTypeById(did) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/budget/budgetType/{id}", method = RequestMethod.GET)
    public ResponseBean getBudgetTypeById(@PathVariable Long pid) {
        BudgetType bgp = budgetAdminService.getBudgetTypeById(pid);
        if (bgp != null)
            return new ResponseBean("success", "", bgp);

        return new ResponseBean("error", "budgettype with id: " + pid + " not found!");
    }

    /// costGroup
    @RequestMapping(value = "/budget/costGroup", method = RequestMethod.POST)
    public ResponseBean addCostGroup(CostGroup costGroup) {
        CostGroup costGrp = budgetAdminService.addCostGroup(costGroup);
        if (costGrp != null) {
            return new ResponseBean("success", "add cost group success!", costGrp);
        }
        return new ResponseBean("error", "添加费用分组失败!");
    }

    @RequestMapping(value = "/budget/costGroup", method = RequestMethod.PUT)
    public ResponseBean updateCostGroup(CostGroup costGroup) {
        int ret = budgetAdminService.updateCostGroup(costGroup);
        if (ret != 0) {
            return new ResponseBean("success", "update cost group success!");
        }
        return new ResponseBean("error", "更新费用分组失败!");
    }

    @RequestMapping(value = "/budget/costGroup/{did}", method = RequestMethod.DELETE)
    public ResponseBean deleteCostGroupById(@PathVariable Long did) {
        if (budgetAdminService.deleteCostGroupById(did) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/budget/costGroup/{id}", method = RequestMethod.GET)
    public ResponseBean getCostGroupById(@PathVariable Long pid) {
        CostGroup bgp = budgetAdminService.getCostGroupById(pid);
        if (bgp != null)
            return new ResponseBean("success", "", bgp);

        return new ResponseBean("error", "CostGroup with id: " + pid + " not found!");
    }

    ///// costItems
    @RequestMapping(value = "/budget/costItem", method = RequestMethod.POST)
    public ResponseBean addCostItem(CostItem costItem) {

        logger.info("add costItem:" + ImisUtils.objectJsonStr(costItem));

        CostItem costIt = budgetAdminService.addCostItem(costItem);
        if (costIt != null) {
            return new ResponseBean("success", "添加费用定义成功!", costIt);
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/budget/costItem", method = RequestMethod.PUT)
    public ResponseBean updateCostItem(CostItem costItem) {

        logger.info("add costItem:" + ImisUtils.objectJsonStr(costItem));

        int ret = budgetAdminService.updateCostItem(costItem);
        if (ret != 0) {
            return new ResponseBean("success", "添加费用定义成功!");
        }
        return new ResponseBean("error", "添加失败!");
    }

    @RequestMapping(value = "/budget/costItem/{did}", method = RequestMethod.DELETE)
    public ResponseBean deleteCostItemById(@PathVariable Long did) {
        logger.info("Delete cost item " + did);
        if (budgetAdminService.deleteCostItemById(did) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/budget/costItem/{id}", method = RequestMethod.GET)
    public ResponseBean getCostItemById(@PathVariable Long pid) {
        CostItem cit = budgetAdminService.getCostItemById(pid);
        if (cit != null)
            return new ResponseBean("success", "", cit);

        return new ResponseBean("error", "CostItem with id: " + pid + " not found!");
    }

    @RequestMapping(value = "/budget/clear", method = RequestMethod.POST)
    public ResponseBean clearBudgetData(String userName, String password) {
        logger.info("required to remove all budget data... user: " + userName);

        if (!userService.verifyUser(userName, password))
            return new ResponseBean("error", "你没有权限这么做!");
        logger.info("Remove prj budget data...");
        prjBudgetService.deleteAll();
        logger.info("Remove department cost data...");
        depBudgetService.deleteAll();
        logger.info("Remove project data...");
        projectService.deleteAll();

        return new ResponseBean("success", "成功清理预算数据!");
    }

    @RequestMapping(value = "/budget/import", method = RequestMethod.POST)
    public ResponseBean importBudgetData(Long depId, MultipartFile file, int realDataMonth) {
        logger.info("import budget data for dep:" + depId + " file: " + file.getOriginalFilename());

        if (depId == null)
            return new ResponseBean("error", "项目数据导入失败! null depId");

        try {
            int ret = dataImportService.importDepartmentBudgetInfo(file, depId, realDataMonth);
            if (ret == 0)
                return new ResponseBean("success", "项目数据导入成功!");
            else
                return new ResponseBean("error", "项目数据导入失败!");

        } catch (Exception e) {
            logger.error("项目数据导入失败!", e);
            return new ResponseBean("error", "项目数据导入失败!" + e.getMessage());

        }

    }

}