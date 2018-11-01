package com.bracelet.service.impl;

import com.bracelet.entity.IpAddressInfo;
import com.bracelet.entity.WatchDevice;
import com.bracelet.service.IDeviceService;
import com.bracelet.util.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Service
public class DeviceServiceImpl implements IDeviceService {
	@Autowired
	JdbcTemplate jdbcTemplate;
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public WatchDevice getDeviceInfo(String addimei) {
		String sql = "select * from device_watch_info where imei=? LIMIT 1";
		List<WatchDevice> list = jdbcTemplate.query(sql, new Object[] { addimei }, 
				new BeanPropertyRowMapper<WatchDevice>(WatchDevice.class));

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getDeviceInfo imei:" + addimei);
		}
		return null;
	}
	@Override
	public List<IpAddressInfo> getipinfo() {
		String sql = "select * from ip_info where status=1  LIMIT 5";
		List<IpAddressInfo> list = jdbcTemplate
				.query(sql, new Object[] {},
						new BeanPropertyRowMapper<IpAddressInfo>(IpAddressInfo.class));
		return list;
	}
	@Override
	public boolean insertParameter(String imei, String parameter) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("insert into watch_parameter_info (imei, parameter, createtime) values (?,?,?)",
						new Object[] { imei, parameter, now }, new int[] {
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,java.sql.Types.TIMESTAMP });
		return i == 1;
	}
	@Override
	public boolean insertNewImei(String imei, String phone, int typeOfOperator,
			String dv) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("insert into device_watch_info (imei, phone, nickname, dv, createtime, type) values (?,?,?,?,?,?)",
						new Object[] { imei, phone, imei, dv, now, typeOfOperator }, new int[] {
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.TIMESTAMP, java.sql.Types.INTEGER});
		return i == 1;
	}
	
	@Override
	public boolean updateImeiInfo(Long id, String phone, int typeOfOperator,
			String dv) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_info set phone=? ,dv=?,type=?,createtime=? where id = ?",
				new Object[] { phone, dv, typeOfOperator, now, id }, new int[] { Types.VARCHAR,Types.VARCHAR,
						Types.INTEGER,java.sql.Types.TIMESTAMP,java.sql.Types.INTEGER });
		return i == 1;
	}

	

}