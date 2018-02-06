package com.cfets.ddd.p.platform;

import com.cfets.ddd.d.core.logger.DLogger;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;

import java.io.IOException;
import java.util.List;

/**
 * Created by pluto on 01/02/2018.
 */
public class ApplicationService {

    private static final DLogger logger = DLogger
            .getLogger(ApplicationService.class);


    /**
     * 为Gitlab 指定Group下的所有工程创建指定分支
     *
     * @param groupName
     * @param oldBranchName
     * @param newBranchName
     */
    public void createNewBranchForAllProjectInGitlabGroup(String groupName, final String oldBranchName, final String newBranchName) {

        try {
            GitlabAPI api = GitlabAPI.connect("http://200.31.147.77",
                    "4ZJPBCWdTNrJSRoQF6BT");
            GitlabGroup currentGroup = new GitlabGroup();
            List<GitlabGroup> lll = api.getGroups();
            for (GitlabGroup gg : lll) {
                if (groupName.equals(gg.getName())) {
                    currentGroup = gg;
                }
            }
            if (currentGroup == null) {
                logger.info("the group of {} is not created in GitLab or no authorization for the current user", groupName);
                return;
            }
            List<GitlabProject> gps = api.getGroupProjects(currentGroup.getId());
            if (gps == null || gps.isEmpty()) {
                logger.info("there has not project for the group of {}", groupName);
                return;
            }
            gps.stream().forEach(gp -> {

                try {
                    List<GitlabBranch> branchs = api.getBranches(gp);
                    final BranchPredicateWrapper bpw = new BranchPredicateWrapper();
                    branchs.stream().forEach(gb -> {
                        if (newBranchName.equals(gb.getName())) {
                            bpw.existNewBranch();
                        }
                        if (oldBranchName.equals(gb.getName())) {
                            bpw.existOldBranch();
                        }
                    });
                    if (bpw.isNewBranchExisted()) {
                        logger.info("the new branch of {} for the project of {} has been created", newBranchName, gp.getName());
                        return;
                    }
                    if (!bpw.isOldBranchExisted()) {
                        logger.info("the old branch of {} for the project of {} has not been created", newBranchName, gp.getName());
                        return;
                    }
                    api.createBranch(gp, newBranchName, oldBranchName);
                    logger.info("the branch of {} for the project {} just being created successfully", newBranchName, gp.getName());

                } catch (IOException e) {
                    logger.error("failed to create the branch", e);
                }
            });
        } catch (Exception e) {
            logger.error("failed to create the branch for all project in group", e);
        }
    }

    class BranchPredicateWrapper {

        boolean newBranchExisted = false;
        boolean oldBranchExisted = false;

        public BranchPredicateWrapper() {
        }

        public boolean isNewBranchExisted() {
            return newBranchExisted;
        }

        public boolean isOldBranchExisted() {
            return oldBranchExisted;
        }

        void existNewBranch() {
            newBranchExisted = newBranchExisted == false ? true : true;
        }

        void existOldBranch() {
            oldBranchExisted = oldBranchExisted == false ? true : true;
        }


    }
}
