package com.cfets.ts.s.platform.util;

public class CompareUtil {

	public static String compareVersion(String standardVersion, String beCheckedVersion) throws Exception {
		if (beCheckedVersion.contains("SNAPSHOT") || beCheckedVersion.contains("LATEST")) {
			return standardVersion;
		}
		String[] standard = standardVersion.split("\\.");
		String[] beChecked = beCheckedVersion.split("\\.");
		if (beChecked.length != standard.length) {
			throw new Exception("这版本号是瞎搞的嘛！ --> 受检件号：" + beCheckedVersion + " --> 标准件号：" + standardVersion);
		}
		for (int i = 0; i < beChecked.length; i++) {
			if (!standard[i].equals(beChecked[i])) {
				return standardVersion;
			}
		}
		return null;
	}

}
