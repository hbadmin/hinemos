/*

Copyright (C) 2017 NTT DATA Corporation

This program is free software; you can redistribute it and/or
Modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, version 2.

This program is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

 */

package com.clustercontrol.platform.selfcheck;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clustercontrol.HinemosManagerMain.StartupMode;
import com.clustercontrol.selfcheck.monitor.SelfCheckMonitor;

/**
 * 環境差分のある固定値を格納するクラス（Windows）<BR>
 *
 * @version 6.0.0
 * @since 6.0.0
 */
public class SelfCheckPertial {

	private static Log logger = LogFactory.getLog( SelfCheckPertial.class );

	private static final String DB_LONG_TRAN_VALIDATION_QUERY = "SELECT CAST((DATEDIFF (MS, start_time, GETDATE()) / 1000.0) AS DECIMAL(38,3)),"
			+ " 'query = [' + sql.text + '], start_time = ' + FORMAT(start_time, 'yyyy/MM/dd HH:mm:ss') + ', duration = '"
			+ " + CAST(CAST((DATEDIFF (MS, start_time, GETDATE()) / 1000.0) AS DECIMAL(38,3)) as VARCHAR) + ' sec'"
			+ " FROM sys.dm_exec_requests req"
			+ " CROSS APPLY sys.dm_exec_sql_text(req.sql_handle) sql"
			+ " WHERE GETDATE() > DATEADD( ss, 86400, start_time)";

	public static String getDbLongTranValidationQuery(){
		logger.debug("db longtran validationQuery(Windows) : " + DB_LONG_TRAN_VALIDATION_QUERY);
		return DB_LONG_TRAN_VALIDATION_QUERY;
	}
	
	public static SelfCheckMonitor[] getMonitors(StartupMode startupMode) {
		List<SelfCheckMonitor> monitors = new ArrayList<SelfCheckMonitor>();
		if (startupMode == StartupMode.NORMAL) {
			monitors.add(new DBSizeMonitor());
		}
		return monitors.toArray(new SelfCheckMonitor[0]);
	}
}