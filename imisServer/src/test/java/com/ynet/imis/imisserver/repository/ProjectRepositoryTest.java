package com.ynet.imis.imisserver.repository;

import static org.junit.Assert.assertEquals;

import com.ynet.imis.domain.project.Project;
import com.ynet.imis.imisserver.ImisApplicationTests;
import com.ynet.imis.repository.project.ProjectRepository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public class ProjectRepositoryTest extends ImisApplicationTests {

    @Autowired
    private ProjectRepository projectDao;

    @Test
    public void whenFindPagedProjectSuccess() throws Exception {

        PageRequest pageReq = PageRequest.of(0, 10);
        Page<Project> pageOfPrj = projectDao.findAll((Specification<Project>) null, pageReq);

        outputObject(pageOfPrj.getContent());

        // assertEquals("Total elements: ", 3, pageOfPrj.getTotalElements());

        // assertEquals("return elements of page 0:", 3, pageOfPrj.getContent().size());

    }

}