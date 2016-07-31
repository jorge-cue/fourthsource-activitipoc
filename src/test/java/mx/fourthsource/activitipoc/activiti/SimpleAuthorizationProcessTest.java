package mx.fourthsource.activitipoc.activiti;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.fourthsource.activitipoc.activiti.models.simpleAuthorizationProcess.Request;

@RunWith(BlockJUnit4ClassRunner.class)
public class SimpleAuthorizationProcessTest {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleAuthorizationProcessTest.class);
	
	private IdentityService identityService;
	private RuntimeService runtimeService;
	private TaskService taskService;
	
	private static final String USER_KERMIT = "kermit";
	private static final String USER_FOZZIE = "fozzie";
	private static final String USER_GONZO = "gonzo";
	private static final String USER_PEGGIE = "peggie";
	
	@Rule public ActivitiRule activitiRule = new ActivitiRule();
	
	@Rule public TestWatcher testWatcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			logger.trace("Starting {} test.", description.getMethodName());
		}

		@Override
		protected void succeeded(Description description) {
			logger.trace("Test {} succeeded.", description.getMethodName());
		}

		@Override
		protected void failed(Throwable e, Description description) {
			logger.trace("Test {} failed.", description.getMethodName());
		}
	};

	@Before
	public void before() {
		identityService = activitiRule.getIdentityService();
		runtimeService = activitiRule.getRuntimeService();
		taskService = activitiRule.getTaskService();
	}
	
	@Test
	@Deployment(resources = {"diagrams/simpleAuthorizationProcess.bpmn"})
	public void testHappyPath() {
		identityService.setAuthenticatedUserId(USER_KERMIT);

		logger.trace("Creating process ");
		Map<String, Object> variables = new HashMap<>();
		Request request = new Request();
		request.setCreator(USER_KERMIT);
		request.setController(USER_FOZZIE);
		variables.put("request", request);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleAuthorizationProcess", "HAPPY_PATH", variables);
		assertNotNull("Process started succesfully.", processInstance);

		Task task;
		TaskQuery taskQuery = taskService.createTaskQuery();

		logger.trace("Checking todo list for {}", USER_FOZZIE);
		task = taskQuery.taskAssignee(USER_FOZZIE).singleResult();
		assertNotNull("Todo list for controller (fozzie) contains one task.", task);
		assertEquals("Kermit tasks is 'Controller chooses coster'.", "Controller chooses coster", task.getName());
		
		logger.trace("Completing task '{}'", task.getName());
		variables.clear();
		variables.put("coster", USER_GONZO);
		taskService.complete(task.getId(), variables, true);

		logger.trace("Checking todo list for {}", USER_GONZO);
		task = taskQuery.taskAssignee(USER_GONZO).singleResult();
		assertNotNull("Todo list for coster (gonzo) contains one task", task);
		assertEquals("Gonzo's task is 'Coster enter Cost Details'.", "Coster enter Cost Details", task.getName());
		
		logger.trace("Completing task '{}'", task.getName());
		taskService.complete(task.getId());
		
		logger.trace("Checking todo list for {}", USER_FOZZIE);
		task = taskQuery.taskAssignee(USER_FOZZIE).singleResult();
		assertNotNull("Todo list for controller (fozzie) contains one task.", task);
		assertEquals("Kermit tasks is 'Controller validates costing'.", "Controller validates costing", task.getName());
		
		logger.trace("Completing task '{}'", task.getName());
		variables.clear();
		variables.put("valid", true);
		taskService.complete(task.getId(), variables, true);
		
		logger.trace("Process should have finish.");
		assertEquals("Process finished", 0, taskQuery.count());
		assertNull("And the process has gone", runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult());
	}
	
	@Test
	@Deployment(resources = {"diagrams/simpleAuthorizationProcess.bpmn"})
	@Ignore
	public void testHappyPathWithDelegationOnControllerAssignsCoster() {
		identityService.setAuthenticatedUserId(USER_KERMIT);

		logger.trace("Creating process ");
		Map<String, Object> variables = new HashMap<>();
		Request request = new Request();
		request.setCreator(USER_KERMIT);
		request.setController(USER_FOZZIE);
		variables.put("request", request);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("simpleAuthorizationProcess", "HAPPY_PATH_WITH_DELEGATION", variables);
		assertNotNull("Process started succesfully.", processInstance);

		Task task;
		TaskQuery taskQuery = taskService.createTaskQuery();

		logger.trace("Checking todo list for {}", USER_FOZZIE);
		task = taskQuery.taskAssignee(USER_FOZZIE).singleResult();
		assertNotNull("Todo list for controller (fozzie) contains one task.", task);
		assertEquals("Kermit tasks is 'Controller chooses coster'.", "Controller chooses coster", task.getName());
		
		logger.trace("Delegating task to {}", USER_PEGGIE);
		taskService.delegateTask(task.getId(), USER_PEGGIE);
		
		logger.trace("Checking todo list for {}", USER_PEGGIE);
		task = taskQuery.taskAssignee(USER_PEGGIE).singleResult();
		assertNotNull("Todo list for delegated controller (peggie) contains one task.", task);
		assertEquals("Peggie's tasks is 'Controller chooses coster'.", "Controller chooses coster", task.getName());
		
		logger.trace("Resolving task {}", task.getName());
		variables.clear();
		variables.put("coster", USER_GONZO);
		taskService.resolveTask(task.getId(), variables);
		
		task = taskQuery.taskAssignee(USER_FOZZIE).singleResult();		
		assertNotNull("Todo list for controller (fozzie) contains one task.", task);
		assertEquals("Kermit tasks is 'Controller chooses coster'.", "Controller chooses coster", task.getName());

		logger.trace("Completing task '{}'", task.getName());
		taskService.complete(task.getId());

		logger.trace("Checking todo list for {}", USER_GONZO);
		task = taskQuery.taskAssignee(USER_GONZO).singleResult();
		assertNotNull("Todo list for coster (gonzo) contains one task", task);
		assertEquals("Gonzo's task is 'Coster enter Cost Details'.", "Coster enter Cost Details", task.getName());
		
		logger.trace("Completing task '{}'", task.getName());
		taskService.complete(task.getId());
		
		logger.trace("Checking todo list for {}", USER_FOZZIE);
		task = taskQuery.taskAssignee(USER_FOZZIE).singleResult();
		assertNotNull("Todo list for controller (fozzie) contains one task.", task);
		assertEquals("Kermit tasks is 'Controller validates costing'.", "Controller validates costing", task.getName());
		
		logger.trace("Completing task '{}'", task.getName());
		variables.clear();
		variables.put("valid", true);
		taskService.complete(task.getId(), variables, true);
		
		logger.trace("Process should have finish.");
		assertEquals("Process finished", 0, taskQuery.count());
	}
}
