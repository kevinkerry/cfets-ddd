package com.cfets.ts.s.platform.bean;

public class ArgumentBean {

	private boolean standardSpecified = false;

	private String standardPomPath;

	private boolean checkedSpecified = false;

	private String checkedPomPath;

	private boolean needUpdate = false;

	private boolean needMerge = false;

	public boolean isStandardSpecified() {
		return standardSpecified;
	}

	public void setStandardSpecified(boolean standardSpecified) {
		this.standardSpecified = standardSpecified;
	}

	public String getStandardPomPath() {
		return standardPomPath;
	}

	public void setStandardPomPath(String standardPomPath) {
		this.standardPomPath = standardPomPath;
	}

	public boolean isCheckedSpecified() {
		return checkedSpecified;
	}

	public void setCheckedSpecified(boolean checkedSpecified) {
		this.checkedSpecified = checkedSpecified;
	}

	public String getCheckedPomPath() {
		return checkedPomPath;
	}

	public void setCheckedPomPath(String checkedPomPath) {
		this.checkedPomPath = checkedPomPath;
	}

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public boolean isNeedMerge() {
		return needMerge;
	}

	public void setNeedMerge(boolean needMerge) {
		this.needMerge = needMerge;
	}

	@Override
	public String toString() {
		return "ArgumentBean [standardSpecified=" + standardSpecified + ", standardPomPath="
				+ standardPomPath + ", checkedSpecified=" + checkedSpecified + ", checkedPomPath=" + checkedPomPath
				+ ", needUpdate=" + needUpdate + ", needMerge=" + needMerge + "]";
	}

}
