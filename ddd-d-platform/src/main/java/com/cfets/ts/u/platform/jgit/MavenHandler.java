package com.cfets.ts.u.platform.jgit;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cfets.ts.u.platform.ExploringAxonApplication;
import com.cfets.ts.u.platform.bean.MavenHandleBean;
import com.cfets.ts.u.platform.contants.PlatData;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity.Target;
import com.cfets.ts.u.platform.service.ComponentService;

/**
 * mava打包
 * 
 * @author zrp
 * 
 */
@Service("mavenHandler")
public class MavenHandler extends CodeHandler {

	private static final Logger logger = Logger.getLogger(MavenHandler.class);
	private static List<MavenHandleBean> lst  = new ArrayList<MavenHandleBean>();
	@Autowired
	private ComponentService componentService;
	@Override
	public Map<String, Object> handle(MergeObejectEntity object,
			Map<String, Object> flag) {
		if (getSuccessor() == null) {
			logger.info("第三个handler（MavenHandler）=========");
			try {
				logger.info("===================开始本地发布构件:"
						+ object.getTarget().getName());
				String filePath = PlatData.FILEPATH + File.separator
						+ object.getTarget().getName(); // 克隆文件的位置

				File refile = new File(filePath);
				Runtime runtime = Runtime.getRuntime();
				String[] cmd = { "/bin/sh", "-c",
						"mvn clean install -Dmaven.test.skip=true" };
				Process process = runtime.exec(cmd, null, refile);
				String outString;
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(process.getInputStream()));
				while ((outString = bufferedReader.readLine()) != null) {
					if (outString.indexOf("[ERROR]") != -1) {
						logger.info(outString);
						throw new Exception();
					}
				}
				process.waitFor();
				flag.put("flag", true);
				flag.put("msg", "maven发布成功");
				logger.info("====================maven发布成功");
				//flag=getSuccessor().handle(object, flag);
			} catch (Exception e) {
				flag.put("flag", false);
				flag.put("msg", "maven发布失败");
				logger.info("====================maven发布失败");
				MavenHandleBean bean = getMavenHandleBean();
				if(bean.allowExcute())
				{
					bean.excute();
					logger.info("重复发送：：：" +(3- bean.getNum()) + "次");
					try {
						Thread.sleep(600*1000);//10分钟
					} catch (Exception e1) {
						
						e1.printStackTrace();
					}
					flag=handle(object, flag);
					
				}
				logger.info("====================3次打包都失败");
				return flag;
			}
			

		} else {}
		return flag;
	}
	
	private MavenHandleBean getMavenHandleBean()
	{
		MavenHandleBean rbean = null;
		List<MavenHandleBean> removes = new ArrayList<MavenHandleBean>();
		Iterator<MavenHandleBean> it = this.lst.iterator();
		while(it.hasNext())
		{
			MavenHandleBean bean = it.next();
			if(bean.getObj() == Thread.currentThread())
			{
				rbean = bean;
			}
			if(!bean.allowExcute())
			{
				removes.add(bean);
			}
		}
		if(!removes.isEmpty())
		{
			for(MavenHandleBean bean:removes)
			{
				this.lst.remove(bean);
			}
		}
		if(rbean == null)
		{
			rbean = new MavenHandleBean(Thread.currentThread());
			this.lst.add(rbean);
		}
		return rbean;
	}


}
