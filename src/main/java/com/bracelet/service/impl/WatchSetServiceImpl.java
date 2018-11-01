package com.bracelet.service.impl;

import java.sql.Timestamp;
import java.sql.Types;

import com.bracelet.service.WatchSetService;
import com.bracelet.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class WatchSetServiceImpl implements WatchSetService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean insertInfo(String imei, Integer setStatus, Integer type) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_controller_server ( imei, set_status, c_type,createtime) values (?,?,?,?)",
				new Object[] { imei, setStatus, type,now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertSmsSetLogInfo(String imei, Integer setStatus,
			String operatorNumber, String content) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setsms_log ( imei, set_status, o_number, content, createtime) values (?,?,?,?,?)",
				new Object[] { imei, setStatus, operatorNumber, content, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertIpSetInfo(String imei, String ip, String port,
			Integer setStatus) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setip_log ( imei, set_status, ip, port, createtime) values (?,?,?,?,?)",
				new Object[] { imei, setStatus, ip, port, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertApnSetLog(String imei, String apnName,
			String username, String password, String data, Integer setStatus) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setapn_log ( imei, set_status, apn_name, username, password, data, createtime) values (?,?,?,?,?,?,?)",
				new Object[] { imei, setStatus, apnName, username, password, data, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertMoniotrLog(String imei, Integer setStatus, String phone) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setmonio_log ( imei, set_status, phone, createtime) values (?,?,?,?)",
				new Object[] { imei, setStatus, phone, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertpushMessageLog(String imei, Integer setStatus,
			String message) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setmessage_log ( imei, set_status, message, createtime) values (?,?,?,?)",
				new Object[] { imei, setStatus, message, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertCaptLog(String imei, Integer setStatus, String come) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setcapt_log ( imei, set_status, come, createtime) values (?,?,?,?)",
				new Object[] { imei, setStatus, come, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertGuardLog(String imei, Integer setStatus, Integer type) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_setguard_log ( imei, set_status, type, createtime) values (?,?,?,?)",
				new Object[] { imei, setStatus, type, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertaddfriendLog(String imei, Integer setStatus,
			String role, String phone, String cornet, String headType) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_addfriend_log ( imei, set_status,role_name, phone, cornet, headtype, createtime) values (?,?,?,?,?,?,?)",
				new Object[] { imei, setStatus, role,phone,cornet,headType, now },
				new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP });
		return i == 1;
	}

	
}