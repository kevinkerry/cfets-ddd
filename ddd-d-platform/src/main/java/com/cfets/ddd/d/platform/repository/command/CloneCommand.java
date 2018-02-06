package com.cfets.ddd.d.platform.repository.command;

import com.cfets.ddd.d.core.model.Command;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.model.mergemodel.Project;
import com.cfets.ts.u.platform.model.mergemodel.User;

/**
 * Clone工程的命令
 * Created by pluto on 23/01/2018.
 */
public class CloneCommand implements Command {

    private String projectName;

    public CloneCommand(String projectName) {
       this.projectName=projectName;
    }

    public String getProjectName() {
        return projectName;
    }


}
