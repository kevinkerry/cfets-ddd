package com.cfets.ddd.d.platform.action;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.cfets.ts.s.platform.bean.DependencyBean;
import com.cfets.ts.s.platform.util.PomParser;
import com.cfets.ts.s.platform.util.XMLWriter;

public class DoWrite {
	
	public static void doWrite(String fileName, PomParser pomParser){
		try {
			XMLWriter writeXML = new XMLWriter(fileName, "project");
			writeXML.init();
			writeXML.startDoc();
			writeXML.writeElementBeans(pomParser.getBeforeDependencyBeans());
			for (DependencyBean dependencyBean : pomParser.getDependencyBeanMap().keySet()) {
				writeXML.writePadStartElement("dependency");
				writeXML.writeWholeElement("groupId", dependencyBean.getGroupId(), null);
				writeXML.writeWholeElement("artifactId", dependencyBean.getArtifactId(), null);
				writeXML.writeWholeElement("version", dependencyBean.getVersion(), null);
				writeXML.writeWholeElement("scope", dependencyBean.getScope(), null);
				writeXML.writePadEndElement("dependency");
			}
			writeXML.writeElementBeans(pomParser.getAfterDependencyBeans());
			writeXML.endDoc();
			System.out.println("只合并com.cfets的依赖！");
			System.out.println("合并完毕！");
		} catch (IOException | TransformerException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
