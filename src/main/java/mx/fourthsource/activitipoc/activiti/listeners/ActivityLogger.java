package mx.fourthsource.activitipoc.activiti.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.VariableScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class ActivityLogger implements TaskListener, ExecutionListener {

	private static final long serialVersionUID = -6755195565262997806L;
	private static final Logger logger = LoggerFactory.getLogger(ActivityLogger.class);

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		StringBuilder message = new StringBuilder();
		message
			.append(execution.getEventName())
			.append(" - ")
			.append(execution.getCurrentActivityName())
			.append(" (")
			.append(execution.getCurrentActivityId())
			.append(")")
			.append(System.lineSeparator());
		message
			.append(buildExecutionInfo(execution))
			.append(System.lineSeparator());
		message
			.append(buildVariablesInfo(execution));
		logger.debug(message.toString());
	}

	@Override
	public void notify(DelegateTask delegateTask) {
		StringBuilder message = new StringBuilder();
		message
			.append(delegateTask.getEventName())
			.append(" - ")
			.append(delegateTask.getName())
			.append(" (")
			.append(delegateTask.getId())
			.append(")")
			.append(System.lineSeparator());
		message
			.append("Assignee: ").append(delegateTask.getAssignee())
			.append(", Owner: ").append(delegateTask.getOwner())
			.append(", Delagtion State: ").append(delegateTask.getDelegationState())
			.append(System.lineSeparator());
		message
			.append("Description: ").append(StringUtils.quote(delegateTask.getDescription()))
			.append(System.lineSeparator());			
		message
			.append(buildExecutionInfo(delegateTask.getExecution()))
			.append(System.lineSeparator());
		message
			.append(buildVariablesInfo(delegateTask));
		logger.debug(message.toString());
	}

	private String buildVariablesInfo(VariableScope scope) {
		StringBuilder message = new StringBuilder();
		message.append("  Variables").append(System.lineSeparator());
		scope.getVariables().forEach( (name, value) -> message.append("    ").append(name).append(": ").append(value).append(System.lineSeparator()) );
		message.append("  Local Variables").append(System.lineSeparator());
		scope.getVariablesLocal().forEach( (name, value) -> message.append("    ").append(name).append(": ").append(value).append(System.lineSeparator()) );
		return message.toString();
	}
	
	private String buildExecutionInfo(DelegateExecution execution) {
		StringBuilder info = new StringBuilder();
		info
			.append("Process Instance id: ").append(execution.getProcessInstanceId())
			.append(", Business Key: ").append(execution.getProcessBusinessKey())
			.append(", Execution id:").append(execution.getId())
			.append(", Parent Execution id: ").append(execution.getParentId())
			.append(", Super Execution id: ").append(execution.getSuperExecutionId())
			.append(", Process Definition Id: ").append(execution.getProcessDefinitionId());
		return info.toString();
	}
}
