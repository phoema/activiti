package phoema.activiti.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.io.Files;

@Service
public class ActivitiService {
	//注入为我们自动配置好的服务
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
    @Autowired  
    ProcessEngineConfiguration processEngineConfiguration;  
    @Autowired  
    HistoryService historyService;  
    //开始流程，传入申请者的id以及公司的id
	@Transactional
	public void startProcess(Long personId, Long compId) {
		print();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("personId", personId);
		variables.put("compId", compId);
		runtimeService.startProcessInstanceByKey("joinProcess",variables);
		print();
	}
	
	//获得某个人的任务别表
	@Transactional
	public List<Task> getTasks(String assignee) {
		List<Task> list = taskService.createTaskQuery().taskAssignee(assignee).list();
		print();
		return list;
	}
	
	//完成任务
	public void completeTasks(Boolean joinApproved, String taskId) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("joinApproved", joinApproved);
		taskVariables.put("pass", joinApproved);
		taskService.complete(taskId, taskVariables);
	}
	public boolean startActivityDemo() {
		System.out.println("method startActivityDemo begin....");
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("apply","zhangsan");
        map.put("approve","lisi");
        Task task = null;
        String taskId = null;
//流程启动
        //ExecutionEntity pi1 = (ExecutionEntity) runtimeService.startProcessInstanceByKey("leave",map);
        ProcessInstance pi1 = runtimeService.startProcessInstanceByKey("leave",map);
        String processId = pi1.getId();
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId, map);//完成第一步申请
        
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        map.put("pass", false);
        taskService.complete(taskId, map);//驳回申请

        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId, map);//完成修改申请
        
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        map.put("pass", true);
        taskService.complete(taskId, map);//批准申请

        
        System.out.println("method startActivityDemo end....");
        return false;
	}
	/**
	 * 根据任务ID，获取流程实例
	 * @param taskId 任务ID
	 * @return 流程实例
	 */
	public ProcessInstance getProcessInstanceByTaskId(String taskId) {
		// 1.获取流程定义
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        // 2.获取流程实例
        ProcessInstance pi =runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult(); 
        return pi;
	}
	private void print(){
		
		System.out.println("Number of process definitions : "
				+ repositoryService.createProcessDefinitionQuery().count());
			System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
			System.out.println("Number of tasks after process start: " + taskService.createTaskQuery().count());

	}
	@Transactional
	public void startProcessPatPre(Long personId,String caseid) throws Exception {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("applicant", personId);
		variables.put("caseid", caseid);//与业务表的关联字段案件id
        ProcessInstance process = runtimeService.startProcessInstanceByKey("patpre",caseid,variables);
        String processId = process.getId();
        Task task = null;
        String taskId = null;

        //案件提交
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId);
        
        //案件分配（人工指定审查员）
        variables = new HashMap<String, Object>();
        variables.put("examinant", "审查员A");
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId,variables);
                
        //案件初审(complete是需追加pass参数判断是否通过) false
        variables = new HashMap<String, Object>();
        variables.put("pass", false);
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId,variables);
        draw(processId,taskId);
        
        
        //初审修改
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId);

        //案件初审(complete是需追加pass参数判断是否通过) true
        variables = new HashMap<String, Object>();
        variables.put("pass", true);
        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskId = task.getId();
        taskService.complete(taskId,variables);
        draw(processId,taskId);
//        
//        //复审提交
//        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        taskId = task.getId();
//        taskService.complete(taskId);
//        
//        //案件复审(complete是需追加pass参数判断是否通过) false
//        variables = new HashMap<String, Object>();
//        variables.put("pass", false);
//        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        taskId = task.getId();
//        taskService.complete(taskId,variables);
//        draw(processId,taskId);
//       
//        //复审修改
//        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        taskId = task.getId();
//        taskService.complete(taskId);
//        
//        //案件复审(complete是需追加pass参数判断是否通过) true
//        variables = new HashMap<String, Object>();
//        variables.put("pass", true);
//        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        taskId = task.getId();
//        taskService.complete(taskId,variables);

//        //案件归档
//        task=taskService.createTaskQuery().processInstanceId(processId).singleResult();
//        taskId = task.getId();
//        taskService.complete(taskId);


	}
	
	public byte[] draw(String processid,String filename) throws IOException {
		if(Strings.isNullOrEmpty(filename)){
			filename="test1";
		}
	     String processInstanceId = processid;  
	        //获取历史流程实例  
	        HistoricProcessInstance processInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();  
	        //获取流程图  
	        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());  
	  
	        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();  
	  
	        List<HistoricActivityInstance> highLightedActivitList =  historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();  
	        //高亮环节id集合  
	        List<String> highLightedActivitis = new ArrayList<String>();  
	          
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
}
