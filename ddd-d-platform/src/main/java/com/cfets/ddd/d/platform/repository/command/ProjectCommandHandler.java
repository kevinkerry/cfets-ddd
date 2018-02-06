package com.cfets.ddd.d.platform.repository.command;

import com.cfets.ddd.d.core.logger.DLogger;
import com.cfets.ddd.d.core.model.CommandHandler;
import com.cfets.ddd.d.platform.PlatformComponent;
import com.cfets.ts.u.platform.contants.PlatData;
import org.eclipse.jgit.api.Git;

import java.io.File;

/**
 * Created by pluto on 23/01/2018.
 */
public class ProjectCommandHandler implements CommandHandler{

    private static final DLogger logger = DLogger
            .getLogger(PlatformComponent.class);


    void cloneProject(CloneCommand cloneCommand){
        logger.info("初始化克隆数据开始-----");
        try {
            File file = new File(PlatData.FILEPATH + File.separator + gitName);
            if (!file.exists()) {
                Git.cloneRepository().setURI(sshUrl).setDirectory(file).call();
            }

        } catch (Exception e) {
            logger.error("初始化clone数据失败-----", e);
        }
    }


}
