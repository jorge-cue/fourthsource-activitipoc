package mx.fourthsource.activitipoc.activiti.listeners.sinpleAuthorizationProcess;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.fourthsource.activitipoc.activiti.listeners.AbstractTaskListener;
import mx.fourthsource.activitipoc.activiti.models.simpleAuthorizationProcess.Request;

public class ControllerValidatesCosting extends AbstractTaskListener implements TaskListener {

	private static final long serialVersionUID = -5296749324392729837L;
	private static final Logger logger = LoggerFactory.getLogger(ControllerValidatesCosting.class);

	@Override
	protected void onComplete(DelegateTask delegateTask) {
		logger.debug("onComplete");
		Boolean valid = delegateTask.getVariableLocal("valid", Boolean.class);
		delegateTask.removeVariablesLocal();
		
		Request request = delegateTask.getVariable("request", Request.class);
		request.setValid(valid);
		
		delegateTask.setVariable("request", request);
	}
}
