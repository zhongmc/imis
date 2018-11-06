package com.ynet.imis.service.project;

import java.io.File;
import java.util.List;

import com.ynet.imis.domain.project.Project;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ProjectService {

    public Project addProject(Project project);

    public Project getProjectById(Long id);

    public int deleteProjectById(Long id);

    public int updateProject(Project project);

    public Page<Project> getProjectByPage(int page, int size, String keywords, Long customId, Long departmentId,
            Long prjType, Long prjClass, String beginDateScope, List<Long> depIds);

}