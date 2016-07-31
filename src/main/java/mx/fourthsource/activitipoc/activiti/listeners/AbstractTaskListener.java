package mx.fourthsource.activitipoc.activiti.listeners;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTaskListener implements TaskListener {

	private static final long serialVersionUID = 7225929397468438699L;
	private static final Logger logger = LoggerFactory.getLogger(AbstractTaskListener.class);
	
	@Override
	public final void notify(DelegateTask delegateTask) {
		logger.trace("Notification for task {}, event {} delegate state {}.", 
				delegateTask.getName(), delegateTask.getEventName(), delegateTask.getDelegationState());
		switch (delegateTask.getEventName()) {
		case EVENTNAME_CREATE:
			onCreate(delegateTask);
			break;
		case EVENTNAME_ASSIGNMENT:
			if (delegateTask.getDelegationState() == null) {
				onAssignment(delegateTask);				
			} else {
				switch (delegateTask.getDelegationState()) {
				case PENDING:
					onDelegate(delegateTask);
					break;
				case RESOLVED:
					onResolve(delegateTask);
					break;
				}
			}
			break;
		case EVENTNAME_COMPLETE:
			onComplete(delegateTask);
			break;
		case EVENTNAME_DELETE:
			onDelete(delegateTask);
			break;
		}
	}
	
	/**
	 * The task is just created
	 * @param delegateTask
	 */
	protected void onCreate(DelegateTask delegateTask) {
	}
	
	/**
	 * Task is assigned and has not been delegated (owner and delegation state are null) 
	 * @param delegateTask
	 */
	protected void onAssignment(DelegateTask delegateTask) {
	}
	
	/**
	 * 
	 * @param delegateTask
	 */
	protected void onDelegate(DelegateTask delegateTask) {
	}
	
	/**
	 * 
	 * @param delegateTask
	 */
	protected void onResolve(DelegateTask delegateTask) {
	}
	
	/**
	 * 
	 * @param delegateTask
	 */
	protected void onComplete(DelegateTask delegateTask) {
	}
	
	/**
	 * 
	 * @param delegateTask
	 */
	protected void onDelete(DelegateTask delegateTask) {
	}
}
