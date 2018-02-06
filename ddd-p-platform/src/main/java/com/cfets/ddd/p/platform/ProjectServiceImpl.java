package com.cfets.ddd.p.platform;

import com.cfets.ddd.d.core.logger.DLogger;
import com.cfets.ddd.d.platform.PlatformComponent;
import com.cfets.ddd.d.platform.repository.ext.ProjectService;
import com.cfets.ts.u.platform.contants.PlatData;
import org.eclipse.jgit.api.Git;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabBranch;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabProject;

import java.io.File;
import java.util.List;

/**
 * Created by pluto on 30/01/2018.
 */
public class ProjectServiceImpl implements ProjectService {

    private static final DLogger logger = DLogger
            .getLogger(PlatformComponent.class);

    @Override
    public void clone(String projectName) {

        try {
            //产生clone需要的url
            String address = getAddressByProject(projectName);
            //获取本地文件
            File file = getLocalFileByProject(projectName);
            if (!file.exists()) {
                //使用JGit API生成本地文件
                Git.cloneRepository().setURI(address).setDirectory(file).call();
                logger.info("clone the project of {} in file of {}", projectName, file.getPath());
            } else {
                logger.info("couldn't clone the project of {} because that the file or directory has existed in {}", projectName, file.getPath());
            }

        } catch (Exception e) {
            logger.error("failed to clone the project of {}", e, projectName);
        }
    }

    @Override
    public void switchBranch(String branchName) {

    }

    @Override
    public void createNewBranch(String projectName, String oldBranchName, String newBranchName) {
        try {
            GitlabAPI api = GitlabAPI.connect("http://200.31.147.77",
                    "4ZJPBCWdTNrJSRoQF6BT");
            //TODO:这个API需要验证
            GitlabProject gp = api.getProject("TS", projectName);
            if (gp == null) {
                logger.error("couldn't create the new branch of {} from the current branch of {} because of no found the project of {} ", newBranchName, oldBranchName, projectName);
                return;
            }
            api.createBranch(gp,newBranchName,oldBranchName);
            logger.info("the branch of {} for the project {} just being created successfully", newBranchName, gp.getName());
        } catch (Exception e) {
          logger.error("failed to create new branch of {}",e);
        }
    }

    @Override
    public String getAddressByProject(String projectName) {
        return null;
    }

    @Override
    public File getLocalFileByProject(String projectName) {
        File file = new File(PlatData.FILEPATH + File.separator + projectName);
        return file;
    }
}
