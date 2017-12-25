package phoema.activiti.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;
import com.google.common.io.Files;

@RestController
//@RequestMapping(value="/activiti",method={RequestMethod.POST,RequestMethod.GET})
public class DrawController {
	  @Autowired  
	    RepositoryService repositoryService;  
	    @Autowired  
	    ManagementService managementService;  
	    @Autowired  
	    protected RuntimeService runtimeService;  
	    @Autowired  
	    ProcessEngineConfiguration processEngineConfiguration;  
	    @Autowired  
	    ProcessEngineFactoryBean processEngine;  
	    @Autowired  
	    HistoryService historyService;  
	    @Autowired  
	    TaskService taskService;  
	
	//开启流程实例
	@RequestMapping(value = "/draw", method = RequestMethod.GET)
	public byte[] draw(String processid,String filename) throws IOException {
		if(Strings.isNullOrEmpty(filename)){
			filename="test1";
		}
	     String processInstanceId = processid;  
	        //获取历史流程实例  
	        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();  
	        //获取流程图  
	        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());  
	        processEngineConfiguration = processEngine.getProcessEngineConfiguration();  
	        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);  
	  
	        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();  
	        ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());  
	  
	        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();  
	        //高亮环节id集合  
	        List<String> highLightedActivitis = new ArrayList<String>();  
	          
	        //高亮线路id集合  
	        List<String> highLightedFlows = getHighLightedFlows(processid);  
	  
	        for(HistoricActivityInstance tempActivity : highLightedActivitList){  
	            String activityId = tempActivity.getActivityId();  
	            highLightedActivitis.add(activityId);  
	        }  
	  
	        //中文显示的是口口口，设置字体就好了  
	        //diagramGenerator.generateDiagram(arg0, arg1, arg2)
	        //InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis,highLightedFlows); 
	        //生成流图片  
//	        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "PNG", highLightedActivitis, highLightedActivitis,    
//	                processEngine.getProcessEngineConfiguration().getActivityFontName(),    
//	                processEngine.getProcessEngineConfiguration().getLabelFontName(),    
//	                processEngine.getProcessEngineConfiguration().getActivityFontName(),  
//	                processEngine.getProcessEngineConfiguration().getProcessEngineConfiguration().getClassLoader(), 1.0);    
	        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis,highLightedActivitis,"宋体","微软雅黑","黑体",null,2.0);
	        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();  
	        byte[] buff = new byte[100];  
	        int rc = 0;  
	        while ((rc = inputStream.read(buff, 0, 100)) > 0) {  
	            swapStream.write(buff, 0, rc);  
	        }  
	        byte[] in2b = swapStream.toByteArray();  
	        //生成本地图片
	          File file = new File("D:/"+filename+".png");
	          Files.write(in2b, file);
	        
	        
	          
	        return in2b;  
//	        //单独返回流程图，不高亮显示  
////	        InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);  
//	        imageStream.
//	        // 输出资源内容到相应对象  
//	        byte[] b = new byte[1024];  
//	        int len;  
//	        while ((len = imageStream.read(b, 0, 1024)) != -1) {  
//	            response.getOutputStream().write(b, 0, len);  
//	        }  
	          
	    }  
	/* 
	    * 递归查询经过的流 
	    */  
	   private List<String> getHighLightedFlows(String processid) {  
		   List<String> ret = null;
		   Task task = taskService.createTaskQuery().processInstanceId(processid).singleResult();  
		   if(task != null) ret = runtimeService.getActiveActivityIds(task.getExecutionId());
		   return ret;
//	       for (ActivityImpl activity : activityList) {  
//	           if (activity.getProperty("type").equals("subProcess")) {  
//	               // get flows for the subProcess  
//	               getHighLightedFlows(activity.getActivities(), highLightedFlows, historicActivityInstanceList);  
//	           }  
//	  
//	           if (historicActivityInstanceList.contains(activity.getId())) {  
//	               List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();  
//	               for (PvmTransition pvmTransition : pvmTransitionList) {  
//	                   String destinationFlowId = pvmTransition.getDestination().getId();  
//	                   if (historicActivityInstanceList.contains(destinationFlowId)) {  
//	                       highLightedFlows.add(pvmTransition.getId());  
//	                   }  
//	               }  
//	           }  
//	       }  
	   }  
}
