package com.cfets.ddd.d.platform.repository;

import com.cfets.ddd.d.core.logger.DLogger;
import com.cfets.ddd.d.core.model.AggregateContext;
import com.cfets.ddd.d.platform.PlatformComponent;
import com.cfets.ddd.d.platform.repository.domain.Branch;
import com.cfets.ddd.d.platform.repository.ext.ProjectService;
import org.axonframework.spring.stereotype.Aggregate;

/**
 * 开发者对应的一个具体的开发工程
 * Created by pluto on 23/01/2018.
 */
@Aggregate
public class Project implements AggregateContext {

    private static final DLogger logger = DLogger
            .getLogger(PlatformComponent.class);

    private ProjectService pi;

    private final String projectName;

    private Branch currentBranch;

    public Project(String projectName) {
        this.projectName = projectName;
    }

    public void registerProjectImplemention(ProjectService pi) {
        this.pi = pi;
    }

    public void cloneProject() {
        pi.clone(this.projectName);
    }

    public void switchBranch(String branchName) {
        pi.switchBranch(this.projectName, branchName);
    }


    @Override
    public String getAggregateIdentifier() {
        return null;
    }
}
