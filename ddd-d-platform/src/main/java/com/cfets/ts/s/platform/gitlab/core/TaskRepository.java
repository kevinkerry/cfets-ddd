/**
 * 辅助交易系统ts-s-platform
 */
package com.cfets.ts.s.platform.gitlab.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * <b>Copyright 2016 中国外汇交易中心 All Rights Reserved</b>
 * 
 * @description：
 * @author pluto
 * @create on 2017年3月4日
 * 
 * @history
 * 
 */
public class TaskRepository {

	private static final Logger logger = Logger.getLogger(TaskRepository.class);

	public static final String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	public static final String AUTH_PASSWORD = "ts.gitlab.password";
	public static final String AUTH_USERNAME = "ts.gitlab.username";
	public static final String AUTH_TOKEN_VALUE = "ts.gitlab.token.value";
	public static final String AUTH_TOKEN_USED = "ts.gitlab.token.used";
	public static final String NO_VERSION_SPECIFIED = "unknown";
	private static final Object LOCK = new Object();
	private Map<String, String> properties;

	public TaskRepository(String kind) {
		this(kind, "unknown");
	}

	public TaskRepository(String kind, String version) {
		this(kind, version, null, "UTF-8", TimeZone.getDefault().getID());
	}

	public TaskRepository(String kind, String version, String serverUrl) {
		this(kind, version, serverUrl, "UTF-8", TimeZone.getDefault().getID());
	}

	public TaskRepository(String kind, String version, String serverUrl,
			String encoding, String timeZoneId) {
		this.properties = new LinkedHashMap<String, String>();
		this.properties.put("kind", kind);
		this.properties.put("version", version);
		this.properties.put("encoding", encoding);
		this.properties.put("timezone", timeZoneId);
		this.properties.put(AUTH_TOKEN_USED, "false");
	}

	public String getUrl() {
		return ((String) this.properties.get("url"));
	}

	public void setUrl(String newUrl) {
		this.properties.put("url", newUrl);
	}

	public boolean hasCredentials() {
		String username = getUsername();
		String password = getPassword();
		return ((username != null) && (username.length() > 0)
				&& (password != null) && (password.length() > 0));
	}

	public String getPassword() {
		return getAuthInfo(AUTH_PASSWORD);
	}

	public String getUsername() {
		return getAuthInfo(AUTH_USERNAME);
	}

	public void setAuthenticationCredentials(String url, String username,
			String password) {
		setUrl(url);
		setCredentials(username, password);
	}

	private void setCredentials(String username, String password) {
		if (username != null) {
			properties.put(AUTH_USERNAME, username);
		}
		if (password != null) {
			properties.put(AUTH_PASSWORD, password);
		}
	}

	public TaskRepository useToken() {
		this.properties.put(AUTH_TOKEN_USED, "true");
		return this;
	}

	public void flushAuthenticationCredentials() {
		synchronized (LOCK) {
			try {
				properties.clear();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e);
			}
		}
	}

	private String getAuthInfo(String property) {
		return ((properties == null) ? null : (String) properties.get(property));
	}

	public boolean equals(Object object) {
		if (object instanceof TaskRepository) {
			TaskRepository repository = (TaskRepository) object;
			if (getUrl() == null) {
				if (repository.getUrl() != null) {
					return false;
				}
			} else if (!(getUrl().equals(repository.getUrl()))) {
				return false;
			}

			if (getConnectorKind() == null) {
				return (repository.getConnectorKind() == null);
			}
			return getConnectorKind().equals(repository.getConnectorKind());
		}

		return super.equals(object);
	}

	public int hashCode() {
		int res = (getUrl() == null) ? 1 : getUrl().hashCode();
		return (res * 31 + ((getConnectorKind() == null) ? 1
				: getConnectorKind().hashCode()));
	}

	public String toString() {
		return getUrl();
	}

	public String getConnectorKind() {
		String kind = (String) this.properties.get("kind");
		if (kind != null) {
			return kind;
		}
		return "<unknown>";
	}

	public String getVersion() {
		String version = (String) this.properties.get("version");
		return (((version == null) || ("".equals(version))) ? "unknown"
				: version);
	}

	public void setVersion(String ver) {
		this.properties.put("version", (ver == null) ? "unknown" : ver);
	}

	public String getCharacterEncoding() {
		String encoding = (String) this.properties.get("encoding");
		return (((encoding == null) || ("".equals(encoding))) ? "UTF-8"
				: encoding);
	}

	public void setCharacterEncoding(String characterEncoding) {
		this.properties.put("encoding", (characterEncoding == null) ? "UTF-8"
				: characterEncoding);
	}

	public String getTimeZoneId() {
		String timeZoneId = (String) this.properties.get("timezone");
		return (((timeZoneId == null) || ("".equals(timeZoneId))) ? TimeZone
				.getDefault().getID() : timeZoneId);
	}

	public void setTimeZoneId(String timeZoneId) {
		this.properties.put("timezone", (timeZoneId == null) ? TimeZone
				.getDefault().getID() : timeZoneId);
	}

	public String getSynchronizationTimeStamp() {
		return ((String) this.properties.get("lastsynctimestamp"));
	}

	public void setSynchronizationTimeStamp(String syncTime) {
		this.properties.put("lastsynctimestamp", syncTime);
	}

	public void setRepositoryLabel(String repositoryLabel) {
		this.properties.put("label", repositoryLabel);
	}

	public String getRepositoryLabel() {
		String label = (String) this.properties.get("label");
		if ((label != null) && (label.length() > 0)) {
			return label;
		}
		return getUrl();
	}

	public Map<String, String> getProperties() {
		return new LinkedHashMap<String, String>(this.properties);
	}

	public String getProperty(String name) {
		return ((String) this.properties.get(name));
	}

	public void setProperty(String name, String value) {
		this.properties.put(name, value);
	}

	public boolean hasProperty(String name) {
		String value = getProperty(name);
		return ((value != null) && (value.trim().length() > 0));
	}

	public void removeProperty(String key) {
		this.properties.remove(key);
	}

}
