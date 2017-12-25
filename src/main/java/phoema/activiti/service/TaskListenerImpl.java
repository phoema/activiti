package phoema.activiti.service;

import lombok.extern.slf4j.Slf4j;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class TaskListenerImpl implements TaskListener {

    /**
     * 专利预审流程监听：简单化多少个监听合并到一个类里了，实际业务每个任务可以设置不同的监听器
     * <activiti:taskListener>元素的event属性，
     * 它一共包含三种事件："create"、"assignment"、"complete"，
     * 分别表示结点执行处理逻辑的时机为：在处理类实例化时、在结点处理逻辑被指派时、在结点处理逻辑执行完成时，可以根据自己的需要进行指定。 
     */
    @Override
    public void notify(DelegateTask delegateTask) {
    	log.info("name=" +delegateTask.getName());
    	log.info("assignee=" +delegateTask.getAssignee());
    	log.info("variavles=" +delegateTask.getVariables());
    	log.info("TransientVariables=" +delegateTask.getTransientVariables());
    	
    	
    }
}