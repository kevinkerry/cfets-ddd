package com.cfets.ts.u.platform.api.event;

import com.cfets.ts.u.platform.model.mergemodel.MergeObejectEntity;

public class MergeRequestEvent {
private MergeObejectEntity mergeObject;
	public MergeRequestEvent(MergeObejectEntity mergeObject) {
		this.mergeObject=mergeObject;
	}
	public MergeObejectEntity getMergeObject() {
		return mergeObject;
	}
	public void setMergeObject(MergeObejectEntity mergeObject) {
		this.mergeObject = mergeObject;
	}

}
