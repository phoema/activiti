package phoema.activiti.service;

import lombok.extern.slf4j.Slf4j;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

@Slf4j
public class PatPreTaskListenerImpl implements TaskListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 指定个人任务和组任务的办理人
     * <activiti:taskListener>元素的event属性，
     * 它一共包含三种事件："create"、"assignment"、"complete"，
     * 分别表示结点执行处理逻辑的时机为：在处理类实例化时、在结点处理逻辑被指派时、在结点处理逻辑执行完成时，可以根据自己的需要进行指定。 
     * 这里根据activiti示例可能不能得到正确结果，示例显示在event=create时，可以设置assignee，但实际数据库未生效，act_hi_taskinst.ASSIGNEE_为空
     * 同时，当event=assignment，流程没有指定assignee监听器不生效，所以，需满足流程指定assignee，见name="案件分配"，且event="assignment"
     */
    @Override
    public void notify(DelegateTask delegateTask) {
    	// 这两个任务默认管理员执行
    	if(delegateTask.getName().equals("案件分配") || delegateTask.getName().equals("案件归档") ){   		
    		delegateTask.setAssignee("管理员");
    		
    	}
    	log.info("event=" +delegateTask.getEventName());
    	log.info("name=" +delegateTask.getName());
    	log.info("assignee=" +delegateTask.getAssignee());
    	log.info("variavles=" +delegateTask.getVariables());
    	log.info("TransientVariables=" +delegateTask.getTransientVariables());
    	
    }
}