package phoema.activiti;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import phoema.activiti.service.ActivitiService;
import phoema.activiti.web.DrawController;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActivitiServiceTest {

	@Autowired
	ActivitiService activitiService;
	@Autowired
	DrawController drawController;
	
	
	public ActivitiServiceTest() {


	}

	@Test
	public void findOne() throws Exception {
		activitiService.startProcessPatPre((long)20111111, "caseid");
		
		
		
		Thread.sleep(5000);
		assert(true);
	}

	
}
