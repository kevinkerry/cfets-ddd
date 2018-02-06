package com.cfets.ddd.d.platform.repository.ext;

import java.io.File;

/**
 * Created by pluto on 30/01/2018.
 */
public interface ProjectService {

    void clone(String projectName);

    void switchBranch(String projectName,String branchName);

    void createNewBranch(String projectName,String oldBranchName,String newBranchName);

    String getAddressByProject(String projectName);

    File getLocalFileByProject(String projectName);

}
