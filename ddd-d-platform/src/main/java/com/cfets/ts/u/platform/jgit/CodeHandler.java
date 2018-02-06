package com.cfets.ts.u.platform.jgit;

import java.util.Map;

import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;
/**
 * 代码处理器
 * @author zrp
 *
 */
public abstract class CodeHandler {
	protected CodeHandler successor = null;

	public CodeHandler getSuccessor() {
		return successor;
	}

	public void setSuccessor(CodeHandler successor) {
		this.successor = successor;
	}

	public abstract Map<String,Object>  handle(MergeObejectEntity object,Map<String,Object> flag);
}
