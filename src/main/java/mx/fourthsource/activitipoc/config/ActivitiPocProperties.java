package mx.fourthsource.activitipoc.config;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mx.fourthsource.activitipoc")
public class ActivitiPocProperties {
	
	private final String tenantId = "4th_SOURCE_ACTIVITIPOC";
	
	@NotNull
	private String adapterWsURL;

	public String getTenantId() {
		return tenantId;
	}

	public String getAdapterWsURL() {
		return adapterWsURL;
	}

	public void setAdapterWsURL(String adapterWsURL) {
		this.adapterWsURL = adapterWsURL;
	}
}
