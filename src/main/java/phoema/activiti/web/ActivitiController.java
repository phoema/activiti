package phoema.activiti.web;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import phoema.activiti.service.ActivitiService;

@RestController
//@RequestMapping(value="/activiti",method={RequestMethod.POST,RequestMethod.GET})
public class ActivitiController {
	@Autowired
	private ActivitiService myService;
	
	//开启流程实例
	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public void demo() {
		myService.startActivityDemo();
	}
	//开启流程实例
	@RequestMapping(value = "/process/{personId}/{compId}", method = RequestMethod.GET)
	public void startProcessInstance(@PathVariable Long personId, @PathVariable Long compId) {
		myService.startProcess(personId, compId);
	}
	
	//获取当前人的任务
	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
		List<Task> tasks = myService.getTasks(assignee);
		List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
		for (Task task : tasks) {
			dtos.add(new TaskRepresentation(task.getId(), task.getName()));
		}
		return dtos;
	}
	
	//完成任务
	@RequestMapping(value = "/complete/{joinApproved}/{taskId}", method = RequestMethod.GET)
	public String complete(@PathVariable Boolean joinApproved, @PathVariable String taskId) {
		myService.completeTasks(joinApproved, taskId);
		return "ok";
	}
	
	//Task的dto
	static class TaskRepresentation
	
	{
		private String id;
		private String name;
		
		public TaskRepresentation(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
