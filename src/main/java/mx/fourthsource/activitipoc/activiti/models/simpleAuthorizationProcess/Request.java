package mx.fourthsource.activitipoc.activiti.models.simpleAuthorizationProcess;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Request implements Serializable {

	private static final long serialVersionUID = 8213891569241501849L;
	private static final Logger logger = LoggerFactory.getLogger(Request.class);
	
	private String creator;
	private String controller;
	private String coster;
	private boolean valid;
	private boolean toCreator;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public String getCreator() {
		return creator;
	}

	public String getController() {
		return controller;
	}

	public String getCoster() {
		return coster;
	}

	public boolean isValid() {
		return valid;
	}

	public boolean isToCreator() {
		return toCreator;
	}

	public void setCreator(String creator) {
		if (this.creator == null || this.creator.equals(creator) == false) {
			logger.debug("Setting creator: old {}, new {}", this.creator, creator);
			this.creator = creator;
		}
	}

	public void setController(String controller) {
		if (this.controller == null || this.controller.equals(controller) == false) {
			logger.debug("Setting controller: old {}, new {}", this.controller, controller);
			this.controller = controller;
		}
	}

	public void setCoster(String coster) {
		if (this.coster == null || this.coster.equals(coster) == false) {
			logger.debug("Setting coster: old {}, new {}", this.coster, coster);
			this.coster = coster;
		}
	}

	public void setValid(boolean valid) {
		if (this.valid != valid) {
			logger.debug("Setting valid: old {}, new {}", this.valid, valid);
			this.valid = valid;
		}
	}

	public void setToCreator(boolean toCreator) {
		if (this.toCreator != toCreator) {
			logger.debug("Setting toCreator: old {}, new {}", this.toCreator, toCreator);
			this.toCreator = toCreator;
		}
	}
}
