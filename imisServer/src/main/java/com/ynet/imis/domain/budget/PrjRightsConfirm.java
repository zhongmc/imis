package com.ynet.imis.domain.budget;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ynet.imis.domain.project.Project;

@Entity
@Table(name = "PRJ_RIGHTS_CONFIRM")
public class PrjRightsConfirm extends CommBudget {

    private static final long serialVersionUID = -5724326134628837609L;

    @Column(name = "PRJ_ID")
    private Long prjId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRJ_ID", nullable = false, updatable = false, insertable = false)
    private Project project;

    public Project getProject() {
        return this.project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.prjId = project.getId();
    }

    public void setPrjId(Long id) {
        prjId = id;
    }

    public Long getPrjId() {
        return prjId;
    }
}