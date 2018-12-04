/**
* AdminController.java
* @author ZHONGMC
* @description 
* @created Mon Oct 08 2018 10:51:34 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Tue Dec 04 2018 15:48:37 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.controller;

import java.util.List;

import com.ynet.imis.domain.org.Custom;
import com.ynet.imis.service.budget.BudgetAdminService;
import com.ynet.imis.service.org.CustomService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/custom")
public class CustomController {

    @Autowired
    private CustomService customService;

    @Autowired
    BudgetAdminService budgetAdminService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    // custom
    @RequestMapping(value = "/customTree")
    public List<Custom> customTree() {

        return customService.getCustomTree();

    }

    @RequestMapping(value = "/basic", method = RequestMethod.POST)
    public ResponseBean addCustom(Custom custom) {

        if (custom.getId() != null && custom.getId().intValue() == -1) {
            custom.setId(null);
        }
        Custom aCustom = customService.addCustom(custom);
        if (aCustom != null) {
            return new ResponseBean("success", "add custom success!", aCustom);
        }
        return new ResponseBean("error", "添加客户失败!");
    }

    @RequestMapping(value = "/basic/{did}", method = RequestMethod.DELETE)
    public ResponseBean deleteCustomById(@PathVariable Long did) {
        if (customService.deleteCustomById(did) == 1) {
            return new ResponseBean("success", "删除成功!");
        }
        return new ResponseBean("error", "删除失败!");
    }

    @RequestMapping(value = "/basic/{id}", method = RequestMethod.GET)
    public ResponseBean getCustomById(@PathVariable Long pid) {
        Custom custom = customService.getCustomById(pid);
        if (custom != null)
            return new ResponseBean("success", "", custom);

        return new ResponseBean("error", "custom with id: " + pid + " not found!");
    }

}