package com.bracelet.service.impl;

import com.bracelet.entity.IpAddressInfo;
import com.bracelet.entity.WatchDevice;
import com.bracelet.entity.WatchDeviceHomeSchool;
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
		List<IpAddressInfo> list = jdbcTemplate.query(sql, new Object[] {},
				new BeanPropertyRowMapper<IpAddressInfo>(IpAddressInfo.class));
		return list;
	}

	@Override
	public boolean insertParameter(String imei, String parameter) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("insert into watch_parameter_info (imei, parameter, createtime) values (?,?,?)",
				new Object[] { imei, parameter, now },
				new int[] { java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean insertNewImei(String imei, String phone, int typeOfOperator, String dv) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into device_watch_info (imei, phone, nickname, dv, createtime, updatetime,type) values (?,?,?,?,?,?,?)",
				new Object[] { imei, phone, imei, dv, now, now,typeOfOperator },
				new int[] { java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.TIMESTAMP, java.sql.Types.TIMESTAMP, java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean updateImeiInfo(Long id, String phone, int typeOfOperator, String dv) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update("update device_watch_info set phone=? ,dv=?,type=?,updatetime=? where id = ?",
				new Object[] { phone, dv, typeOfOperator, now, id }, new int[] { Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER, java.sql.Types.TIMESTAMP, java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean updateImeiInfo(Long id, String imei, String phone, String nickname, Integer sex, String birday,
			String school_age, String school_info, String home_info, String weight, String height, String head) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_info set phone=? ,nickname=?,sex=?,birday=?, school_age=?, school_info=?, home_info=?, weight=?, height=?, head=? , updatetime=? where id = ?",
				new Object[] { phone, nickname, sex, birday, school_age, school_info, home_info, weight, height, head,
						now, id },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, java.sql.Types.TIMESTAMP,
						java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean insertDeviceImeiInfo(String imei, String phone, String nickname, Integer sex, String birday,
			String school_age, String school_info, String home_info, String weight, String height, String head) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into device_watch_info (imei, phone, nickname, sex, birday, school_age, school_info, home_info, head, weight, height, createtime, updatetime) values (?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { imei, phone, nickname, sex, birday, school_age, school_info, home_info, head, weight,height,now, now },
				new int[] { java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.INTEGER,
						java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.VARCHAR,java.sql.Types.TIMESTAMP,
						java.sql.Types.TIMESTAMP });
		return i == 1;
	}

	@Override
	public boolean updateImeiHeadInfo(Long id, String head) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_info set  head=? , updatetime=? where id = ?",
				new Object[] {  head, now, id },
				new int[] { Types.VARCHAR, java.sql.Types.TIMESTAMP,
						java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean updateImeiHomeAndFamilyInfo(Long id, String school_info, String home_info) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_info set  school_info=?, home_info=? updatetime=? where id = ?",
				new Object[] {  school_info, home_info, now, id },
				new int[] { Types.VARCHAR, Types.VARCHAR, java.sql.Types.TIMESTAMP,
						java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean updateImeiNotHomeAndFamilyInfo(Long id, String imei, String phone, String nickname, Integer sex,
			String birday, String school_age, String weight, String height, String head) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_info set phone=? ,nickname=?,sex=?,birday=?, school_age=?, weight=?, height=?, head=? , updatetime=? where id = ?",
				new Object[] { phone, nickname, sex, birday, school_age, weight, height, head,
						now, id },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, java.sql.Types.TIMESTAMP,
						java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public WatchDeviceHomeSchool getDeviceHomeAndFamilyInfo(Long id) {
		String sql = "select * from device_watch_hf_info where w_id=? LIMIT 1";
		List<WatchDeviceHomeSchool> list = jdbcTemplate.query(sql, new Object[] { id },
				new BeanPropertyRowMapper<WatchDeviceHomeSchool>(WatchDeviceHomeSchool.class));

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getDeviceInfo imei:" + id);
		}
		return null;
	}

	@Override
	public boolean insertDeviceHomeAndFamilyInfo(Long id, String imei, String schoolAddress, String classDisable1,
			String classDisable2, String weekDisable1, String schoolLat, String schoolLng, String latestTime,
			String homeAddress, String homeLng, String homeLat) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into device_watch_hf_info (w_id, imei, createtime, updatetime, schoolAddress, classDisable1, classDisable2, weekDisable1, schoolLat, schoolLng, latestTime, homeAddress, homeLng, homeLat) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
				new Object[] { id, imei, now, now, schoolAddress, classDisable1, classDisable2, weekDisable1, schoolLat, schoolLng, latestTime, homeAddress, homeLng, homeLat },
				new int[] { java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.TIMESTAMP, java.sql.Types.TIMESTAMP,
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.VARCHAR });
		return i == 1;
	}

	@Override
	public boolean updateImeiHomeAndFamilyInfoById(Long id, String classDisable1, String classDisable2,
			String weekDisable, String schoolAddress, String schoolLat, String schoolLng, String latestTime,
			String homeAddress, String homeLat, String homeLng) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"update device_watch_hf_info set updatetime=?, schoolAddress=?, classDisable1=?, classDisable2=?, weekDisable1=?, schoolLat=?, schoolLng=?, latestTime=?, homeAddress=?, homeLng=?, homeLat=? where w_id = ?",
				new Object[] { now, schoolAddress, classDisable1, classDisable2, weekDisable, schoolLat, schoolLng, latestTime, homeAddress, homeLng, homeLat, id},
				new int[] { Types.TIMESTAMP, 
						Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
						Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
						Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,
						java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean insertNewImeiCopy(Long id, String imei, String phone, int typeOfOperator, String dv) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate.update(
				"insert into device_watch_bak_info (d_id, imei, phone, nickname, dv, createtime, updatetime,type) values (?,?,?,?,?,?,?,?)",
				new Object[] { id, imei, phone, imei, dv, now, now,typeOfOperator },
				new int[] { java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR,
						java.sql.Types.VARCHAR, java.sql.Types.TIMESTAMP, java.sql.Types.TIMESTAMP, java.sql.Types.INTEGER });
		return i == 1;
	}

	@Override
	public WatchDevice getDeviceBakInfo(String imei) {
		String sql = "select * from device_watch_info where imei=? LIMIT 1";
		List<WatchDevice> list = jdbcTemplate.query(sql, new Object[] { imei },
				new BeanPropertyRowMapper<WatchDevice>(WatchDevice.class));

		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getDeviceInfo imei:" + imei);
		}
		return null;
	}
	

}
