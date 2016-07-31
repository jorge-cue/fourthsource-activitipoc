package mx.fourthsource.activitipoc.activiti.listeners.sinpleAuthorizationProcess;

import org.activiti.engine.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.fourthsource.activitipoc.activiti.listeners.AbstractTaskListener;
import mx.fourthsource.activitipoc.activiti.models.simpleAuthorizationProcess.Request;

public class ControllerChoosesCoster extends AbstractTaskListener {

	private static final long serialVersionUID = -678288901040685893L;
	private static final Logger logger = LoggerFactory.getLogger(ControllerChoosesCoster.class);
	
	protected void onAssignment(DelegateTask delegateTask) {
		sendEmailNotification(delegateTask);		
	}
	
	protected void onDelegate(DelegateTask delegateTask) {
		sendEmailNotification(delegateTask);
	}
	
	protected void onResolve(DelegateTask delegateTask) {
		String coster = delegateTask.getVariable("coster", String.class);
		delegateTask.removeVariable("coster");
		resolve(delegateTask, coster);
		sendEmailNotification(delegateTask);
	}
	
	protected void onComplete(DelegateTask delegateTask) {
		logger.debug("onComplete");
		if (delegateTask.getDelegationState() == null) {
			String coster = delegateTask.getVariableLocal("coster", String.class);
			resolve(delegateTask, coster);
		}
	}
	
	private void sendEmailNotification(DelegateTask delegateTask) {
		String assignee = delegateTask.getAssignee();
		Request request = delegateTask.getVariable("request", Request.class);
		String creator = request.getCreator();
		logger.debug("Send Controller Chooses Coster notification to controller/assignee {} and creator {}; status {}", assignee, creator, delegateTask.getDelegationState());
	}
	
	private void resolve(DelegateTask delegateTask, String coster) {
		Request request = delegateTask.getVariable("request", Request.class);
		request.setCoster(coster);
		delegateTask.setVariable("request", request);		
	}
}
