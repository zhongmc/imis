package com.ynet.imis.service.project;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.ynet.imis.domain.project.Project;
import com.ynet.imis.repository.project.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectDao;

    @Override
    public Project addProject(Project project) {

        return projectDao.save(project);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectDao.findById(id).get();
    }

    @Override
    public int deleteProjectById(Long id) {
        projectDao.deleteById(id);
        return 1;
    }

    @Override
    public int updateProject(Project project) {
        projectDao.save(project);
        return 1;
    }

    @Override
    public Page<Project> getProjectByPage(int page, int size, String keywords, Long customId, Long departmentId,
            Long prjType, Long prjClass, String beginDateScope, List<Long> depIds) {

        Specification<Project> spec = new Specification<Project>() {
            private static final long serialVersionUID = 3933287087564315019L;

            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                Predicate predicate = null;
                if (keywords != null && keywords.length() > 0) {
                    predicate = cb.like(root.<String>get("name"), "%" + keywords + "%");
                }

                if (customId != null && customId.longValue() != -1) {
                    Predicate pred = cb.equal(root.<Long>get("customId"), customId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }

                if (customId != null && customId.longValue() != -1) {
                    Predicate pred = cb.equal(root.<Long>get("customId"), customId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }
                if (departmentId != null && departmentId.longValue() != -1) {

                    Predicate pred = cb.equal(root.<Long>get("depId"), departmentId);
                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                } else if (depIds != null) {
                    Path<Long> path = root.get("depId");
                    CriteriaBuilder.In<Object> in = cb.in(path);
                    for (Long id : depIds)
                        in.value(id);

                    Predicate pred = cb.and(in);

                    if (predicate == null)
                        predicate = pred;
                    else
                        predicate = cb.and(predicate, pred);
                }

                return predicate;
            }
        };
        PageRequest pageReq = PageRequest.of(page, size);
        Page<Project> pageOfPrj = projectDao.findAll(spec, pageReq);

        return pageOfPrj;
    }

    // public Predicate toPredicate(Root<Person> root, CriteriaQuery<?>
    // criteriaQuery, CriteriaBuilder criteriaBuilder) {
    // List<Predicate> list = new ArrayList<>();
    // List<Integer> ids = personService.getIdList();
    // if (ids != null && ids.size() > 0) {
    // In<Object> in = criteriaBuilder.in(root.get("id"));
    // for (Integer id : ids) {
    // in.value(id);
    // }
    // list.add(in);
    // }

    // Predicate[] p = new Predicate[list.size()];
    // return criteriaBuilder.and(list.toArray(p));
    // }

}