package com.cfets.ddd.d.platform.action;

import java.util.LinkedHashMap;
import java.util.Map;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.util.CompareUtil;

public class DoCompare {

	private static void doCompare(Map<DependencyBean, String> pomBeCheckedMap,
			Map<DependencyBean, String> pomBeStandardMap, boolean isNeedMerge) {
		Map<String, String> resultMap = new LinkedHashMap<>();
		for (Map.Entry<DependencyBean, String> pomBeChecked : pomBeCheckedMap.entrySet()) {
			DependencyBean beCheckedDependencyBean = pomBeChecked.getKey();
			if ("".equals(beCheckedDependencyBean.getScope()) || "compile".equals(beCheckedDependencyBean.getScope())) {
				System.err.println(beCheckedDependencyBean + "受检件的scope范围异常，需要强制provide");
				beCheckedDependencyBean.setScope("provide");
			}
			if (!beCheckedDependencyBean.getGroupId().contains("com.cfets.")) {
				System.out.println("跳过检测构件：" + beCheckedDependencyBean);
				continue;
			}
			String standardVersion = pomBeStandardMap.get(beCheckedDependencyBean);
			if (standardVersion == null) {
				resultMap.put(beCheckedDependencyBean.toString(), "标准pom中没有该构件");
				continue;
			}
			String result;
			try {
				result = CompareUtil.compareVersion(standardVersion, pomBeChecked.getValue());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println("该构件的版本号比较出错：" + beCheckedDependencyBean + "，将不作处理");
				continue;
			}
			if (result != null) {
				resultMap.put(beCheckedDependencyBean.toString(), result);
				if (isNeedMerge) {
					beCheckedDependencyBean.setVersion(result);
				}
			}
		}
		System.out.println();
		if (resultMap.size() != 0) {
			System.out.println("比较结果如下：");
			for (Map.Entry<String, String> aResult : resultMap.entrySet()) {
				System.out.println(aResult);
			}
		} else {
			System.out.println("所有构件的版本都是最新");
		}
	}

	public static void compareToStandard(Map<DependencyBean, String> pomBeCheckedMap,
			Map<DependencyBean, String> pomBeStandardMap) {
		doCompare(pomBeCheckedMap, pomBeStandardMap, false);
	}

	public static void compareToStandardAndMerge(Map<DependencyBean, String> pomBeCheckedMap,
			Map<DependencyBean, String> pomBeStandardMap) {
		doCompare(pomBeCheckedMap, pomBeStandardMap, true);
	}

}
