/**
 * @author lijian
 */
package com.cfets.ts.s.platform;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.cfets.cwap.s.spi.SpiConfig;
import com.cfets.cwap.s.util.db.JdbcManager;
import com.cfets.cwap.s.util.db.JdbcProperty;

/**
 * @author lijian
 * 
 */
public final class PlatformHelper {

	private static final Logger logger = Logger.getLogger(PlatformHelper.class);

	public final static String PLUGIN_ID = "ts-s-platform";
	// 发布路径
	public final static String PLUGIN_MAPPING = "/" + PLUGIN_ID;

	private static JdbcManager jdbcManager = null;

	// /////////////////////////Message Send
	// Status////////////////////////////////
	public final static int TO_BE_SENT = 0; // 待发送状态
	public final static int SENT = 1; // 已发送状态
	public final static int CANCEL_SENT = 2; // 发送取消

	// ///////////////////////Message Persist
	// Status/////////////////////////////////
	public final static int DEL_STATUS = 0;// 删除状态
	public final static int NORMAL_STATUS = 1;// 正常状态

	/**
	 * 
	 * @return
	 */
	public static synchronized JdbcManager getJdbcManager() {
		if (jdbcManager == null) {
			// Spring环境下
			//jdbcManager = new JdbcManager(SpiConfig.getDataSource());
			// 本地调试
			 jdbcManager = new JdbcManager(getOracleJdbcProperty().buildDS());
		}
		logger.info("jdbcManager create successfully!!!");
		return jdbcManager;
	}

	protected static JdbcProperty getOracleJdbcProperty() {
		JdbcProperty oracle = new JdbcProperty();
		oracle.setDriver(JdbcProperty.DRIVER_ORACLE);
		oracle.setPassword("tsdev");
		oracle.setUsername("tsdev");
		oracle.setUrl("jdbc:oracle:thin:@200.31.154.128:1521:cfbmodb");
		return oracle;
	}

	/**
	 * 根据构件名称获取仓库地址
	 * 
	 * @description:[getRepositoryUrl]
	 * @param
	 * @return String
	 * @throws
	 * @author pluto 2017年3月6日
	 */
	public static String getRepositoryHttpUrl(String plugin) {
		return "http://200.31.147.77/TS/" + plugin + ".git";
	}

	public static String getRepositorySshUrl(String plugin) {
		return "git@gitlab.scm.cfets.com:TS/" + plugin + ".git";
	}
	public  static  void setDataSource(){
		jdbcManager=new JdbcManager(getOracleJdbcProperty().buildDS());
	}

}
