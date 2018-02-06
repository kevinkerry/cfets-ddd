package com.cfets.ts.s.platform.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cfets.cwap.s.spi.GenericController;
import com.cfets.cwap.s.spi.paging.IPaging;
import com.cfets.cwap.s.spi.paging.Paging;
import com.cfets.cwap.s.spi.paging.PagingFactory;
import com.cfets.ts.s.log.TsLogger;
import com.cfets.ts.s.platform.PlatformHelper;
import com.cfets.ts.s.platform.bean.ComponentPO;
import com.cfets.ts.s.platform.bean.ComponentVO;
import com.cfets.ts.s.platform.bean.RueryPO;
import com.cfets.ts.s.platform.bean.SourceOrTarget;
import com.cfets.ts.s.platform.service.ComponentService;
import com.cfets.ts.s.platform.service.impl.ComponentServiceImpl;
import com.google.gson.Gson;

@Controller
@RequestMapping(PlatformHelper.PLUGIN_MAPPING)
public class PlatformController extends GenericController {
	private static final TsLogger logger = TsLogger
			.getLogger(PlatformController.class);
	private ComponentService componentService = new ComponentServiceImpl();

	
    @RequestMapping("/query.do")
    public ModelAndView query(HttpServletRequest req,HttpServletResponse rep){
    	
    	
    //	String id=cs.findId("cname");
    	// String sql="select * from tsbase.MAVEN_DATA where SR_NO_ID="+id+"";
    	
		return mmv("query").toMAV();
    }


    @RequestMapping("/search.do")
	public void search(HttpServletRequest req, HttpServletResponse rep) {		
		String cname=getParam(req,"filename");   	 
    	ComponentService cs=new ComponentServiceImpl();
    	List<RueryPO> listtwo=cs.findComponent(cname);
		try {
			rep.getWriter().print(new Gson().toJson(listtwo));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping("/searchtwo.do")
	public void searchtwo(HttpServletRequest req, HttpServletResponse rep) {		
		String cname=getParam(req,"filename");  	 
    	ComponentService cs=new ComponentServiceImpl();
    	List<RueryPO> listthree=cs.findComponenttwo(cname);
		try {
			rep.getWriter().print(new Gson().toJson(listthree));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// @RequestMapping("/load.do")
	// public ModelAndView load(HttpRequest req, HttpResponse rps) {
	// 	logger.debug("------load------");
	// 	ComponentGroup group = new ComponentGroup();
	// 	new PomFileComponentLoader().load(group);        
	// 	return mmv("load").toMAV();
	// }
	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest req, HttpServletResponse rep) {
		logger.debug("------index------");
		// 菜单列表
		Paging p = PagingFactory.paging(req, new IPaging() {
			@Override
			public Collection<?> fetch(HttpServletRequest req) {
				List<ComponentVO> list = componentService.findAll();
				return list;
			}
		});
		// 得到所有的node
		List<Map> depency = componentService.findMavenDepency();
		List<Map> l1 = new ArrayList<Map>();
		List<Map> l2 = new ArrayList<Map>();
		List<Map> l3 = new ArrayList<Map>();
		List<Map> l4 = new ArrayList<Map>();

		for (int i = 0; i < depency.size(); i++) {
			Map<String, String> mapNode = new HashMap<String, String>();
			mapNode.put("name", (String)depency.get(i).get("name"));
			String name =(String)depency.get(i).get("name");
			if(name.startsWith("ts-u")){
				mapNode.put("color", "#59A02E");
			}
			else if(name.startsWith("ts-s")){
				mapNode.put("color", "#E15FE2");
			}
			else {
				mapNode.put("color", "#0E0D0E");
			}
			l3.add(mapNode);
			Long id = (Long) depency.get(i).get("id");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("index", i);
			map.put("id", id);
			List<SourceOrTarget> list = componentService.findRalations(id);
			map.put("sourceList", list);
			l1.add(map);
		}
		for (Map map : l1) {
			int dd =  (int) map.get("index");
			List<SourceOrTarget> list = (List<SourceOrTarget>) map
					.get("sourceList");
			for (SourceOrTarget st : list) {
				for (Map map1 : l1) {
					if (st.getTarget().equals(map1.get("id")+"")) {
						Map<String, String> map44 = new HashMap<String, String>();
						map44.put("source", dd + "");
						map44.put("target", map1.get("index") + "");
						l2.add(map44);
					}
				}
			}
		}
		// 找到各自的位置
		String node = new Gson().toJson(l3);
		logger.debug("------node------" + node);
		// 得到所有的边缘edges
		for(Map mm :l2){
			String source=(String)mm.get("source");
			String target=(String)mm.get("target");
		  	mm.put("color", "#ccc");
			for(Map mm1 :l2){
				String source1=(String)mm1.get("source");
				String target1=(String)mm1.get("target");
				if(source.equals(target1)&&target.equals(source1)){
					mm.put("color", "#FB1C62");
					break;
				}
			}
			l4.add(mm);
		}
		String edges = new Gson().toJson(l4);
		logger.debug("------edges------" + edges);
		return mmv("listmaven").add("items", p.getItems())
				.add("paging", p.getParam()).add("nodes", node)
				.add("edges", edges).toMAV();
	}
    
   @RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest req, HttpServletResponse rep) {	  
        String idStr = getParam(req, "id");
		Long id = Long.parseLong(idStr);		
		String sql="update TSBASE.MAVEN_DATA set ISCHECK = '1' where SR_NO_ID="+id;		
		PlatformHelper.getJdbcManager().update(sql);		
		return mmv("view").toRedirect();
	}   
	@RequestMapping("/change.do")
	public ModelAndView change(HttpServletRequest req, HttpServletResponse rep) {	  
        String idStr = getParam(req, "id");
		Long id = Long.parseLong(idStr);		
		String sql="update TSBASE.MAVEN_DATA set ISCHECK = '0' where SR_NO_ID="+id;		
		PlatformHelper.getJdbcManager().update(sql);				
		return mmv("view").toRedirect();
	}   
	
	@RequestMapping("/view.do")
	public ModelAndView view(HttpServletRequest req, HttpServletResponse rep) {
	    ComponentService cs=new ComponentServiceImpl();
    	List<ComponentPO> listone=cs.findall();
		return mmv("loadtwo").add("listone",listone).toMAV();
	}
	
	@Override
	public String getModulePath() {
		return PlatformHelper.PLUGIN_MAPPING;
	}

}
