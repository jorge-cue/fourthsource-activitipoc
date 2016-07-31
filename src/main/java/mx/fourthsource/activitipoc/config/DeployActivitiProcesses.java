package mx.fourthsource.activitipoc.config;

import java.util.Arrays;

import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DeployActivitiProcesses implements ApplicationRunner {

	private final static Logger logger = LoggerFactory.getLogger(DeployActivitiProcesses.class);
	
	@Autowired
	private ActivitiPocProperties properties;
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		logger.info("DeployActivitiProcesses");
		Arrays.asList(
			"diagrams/simpleAuthorizationProcess.bpmn"
		).forEach( diagram -> {
			logger.info("deploying: '{}'", diagram);
			repositoryService
				.createDeployment()
				.enableDuplicateFiltering()
				.tenantId(properties.getTenantId())
				.name(properties.getTenantId())
				.addClasspathResource(diagram)
				.deploy();			
		});
	}
}
