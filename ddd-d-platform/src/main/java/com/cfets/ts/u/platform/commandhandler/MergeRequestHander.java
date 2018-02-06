package com.cfets.ts.u.platform.commandhandler;/*package com.cfets.ts.u.platform.commandhandler;

import org.apache.log4j.Logger;
import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cfets.ts.u.platform.api.commond.MergeCommond;
import com.cfets.ts.u.platform.model.mergemodel.MergeObject;

@Component
public class MergeRequestHander {
	private static final Logger logger = Logger.getLogger(MergeRequestHander.class);
	@Autowired
	private Repository<MergeObject> repository;
	
	public MergeRequestHander(){}
	
	@CommandHandler
	public void handle(MergeCommond mergeCommond) {
		
		 * DataSource ds = null; EventSourcingRepository eventSourcingRepository
		 * = new EventSourcingRepository(MergeObject.class, new
		 * JdbcEventStore(ds));
		 
		// eventSourcingRepository.setEventBus(clusteringEventBus());
		logger.info("----开始处理消息-----");
		String id=mergeCommond.getObject_attributes().getId();
		MergeObject r = repository.load(id);
		logger.info("----开始处理消息-----"+r);
		//r.setAssignee_id(mergeCommond.getObject_attributes().getAssignee_id());
		//r.setTarget_project_id(mergeCommond.getObject_attributes().getTarget_project_id());
		//logger.info("----开始处理消息success-----"+r.getTarget_project_id());
		//r=mergeCommond.getObject_attributes();
		 r.isTitleOrDescription(mergeCommond.getObject_attributes());
	}
}
*/