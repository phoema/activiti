package phoema.activiti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import phoema.activiti.dao.CompRepository;
import phoema.activiti.dao.PersonRepository;
import phoema.activiti.domain.Comp;
import phoema.activiti.domain.Person;
import phoema.activiti.service.ActivitiService;


/**
 * springboot快速启动项
 * 可以使用其他方式启动，通过maven可以进行控制
 * @author jiahh
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableConfigurationProperties
@EnableScheduling  //配置和使用定时器
@EnableCaching  //配置和使用缓存
public class App {

	  @Autowired
	   private CompRepository compRepository;
	   @Autowired
	   private PersonRepository personRepository;
	public static void main(String[] args) throws Exception {
		SpringApplication.run(App.class, args);
	}
	
	 //初始化模拟数据
	   @Bean
	   public CommandLineRunner init(final ActivitiService myService) {
	      return new CommandLineRunner() {
	         public void run(String... strings) throws Exception {
	            if (personRepository.findAll().size() == 0) {
	               personRepository.save(new Person("wtr"));
	               personRepository.save(new Person("wyf"));
	               personRepository.save(new Person("admin"));
	            }
	            if (compRepository.findAll().size() == 0) {
	               Comp group = new Comp("great company");
	               compRepository.save(group);
	               Person admin = personRepository.findByPersonName("admin");
	               Person wtr = personRepository.findByPersonName("wtr");
	               admin.setComp(group); wtr.setComp(group);
	               personRepository.save(admin); personRepository.save(wtr);
	            }
	         }
	      };
	   }
	   
//		 //初始化模拟数据
//	   @Bean
//	   public CommandLineRunner init2(final RepositoryService repositoryService,
//               final RuntimeService runtimeService,
//               final TaskService taskService) {
//
//							return new CommandLineRunner() {
//							@Override
//							public void run(String... strings) throws Exception {
//							System.out.println("Number of process definitions : "
//								+ repositoryService.createProcessDefinitionQuery().count());
//							System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
//							runtimeService.startProcessInstanceByKey("oneTaskProcess");
//							System.out.println("Number of tasks after process start: " + taskService.createTaskQuery().count());
//							}
//							};
//	   }

}
