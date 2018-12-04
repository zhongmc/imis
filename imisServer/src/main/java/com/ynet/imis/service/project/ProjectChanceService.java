package com.ynet.imis.service.project;

import java.util.List;

import com.ynet.imis.domain.project.Project;
import com.ynet.imis.domain.project.ProjectChance;

import org.springframework.data.domain.Page;

public interface ProjectChanceService {

    public ProjectChance addProjectChance(ProjectChance projectChance);

    public ProjectChance getProjectChanceById(Long id);

    public int deleteProjectChanceById(Long id);

    // 关闭机会
    public int closeProjectChanceById(Long id);

    // 转为项目
    public int transferProjectChanceToProjectById(Long id);

    public int updateProjectChance(ProjectChance projectChance);

    public Page<ProjectChance> getProjectChanceByPage(int page, int size, String keywords, Long customId,
            Long departmentId, Long prjType, Long prjClass, String beginDateScope, List<Long> depIds);

    public void deleteAll();
}