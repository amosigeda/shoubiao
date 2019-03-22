package com.bracelet.service.impl;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bracelet.datasource.DataSourceChange;
import com.bracelet.entity.Fence;
import com.bracelet.entity.FingerInfo;
import com.bracelet.entity.MemberInfo;
import com.bracelet.entity.PwdInfo;
import com.bracelet.entity.WatchPhoneBook;
import com.bracelet.entity.WhiteListInfo;
import com.bracelet.service.IMemService;
import com.bracelet.service.IPwdService;
import com.bracelet.service.ISosService;
import com.bracelet.util.Utils;

/**
 * 
 * 
 */
@Service
public class MemberServiceImpl implements IMemService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public boolean insert(Long user_id, String tel, String name, String imei,
			String head) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("insert into member_info (user_id, imei, phone, name,createtime, updatetime, head) values (?,?,?,?,?,?,?)",
						new Object[] { user_id, imei, tel, name, now, now , head},
						new int[] { Types.INTEGER, Types.VARCHAR,
								Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP,
								Types.TIMESTAMP, Types.VARCHAR });
		return i == 1;
	}

	@Override
	@DataSourceChange(slave = true)
	public List<MemberInfo> getMemberInfo(Long user_id, String imei) {
		String sql = "select * from member_info where user_id=? and imei =?";
		List<MemberInfo> list = jdbcTemplate
				.query(sql, new Object[] { user_id, imei },
						new BeanPropertyRowMapper<MemberInfo>(MemberInfo.class));
		return list;
	}

	@Override
	public boolean delete(Long user_id, Long id) {
		int i = jdbcTemplate.update(
				"delete from member_info where id = ? and user_id = ?",
				new Object[] { id, user_id }, new int[] { Types.INTEGER,
						Types.INTEGER });
		return i == 1;
	}

	@Override
	@DataSourceChange(slave = true)
	public MemberInfo getMemberInfobyTel(Long user_id, String imei, String tel) {
		String sql = "select * from member_info where user_id=? and imei =? and phone =? LIMIT 1";
		List<MemberInfo> list = jdbcTemplate.query(sql, new Object[] { user_id,
				imei, tel }, new BeanPropertyRowMapper<MemberInfo>(
				MemberInfo.class));
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getMemberInfobyTel null.user_id:" + user_id);
		}
		return null;
	}

	@Override
	public boolean deleteAll(Long user_id) {
		jdbcTemplate.update("delete from member_info where   user_id = ?",
				new Object[] { user_id }, new int[] { Types.INTEGER });
		return true;
	}

	@Override
	public boolean deleteByImei(String imei) {
		jdbcTemplate.update("delete from member_info where   imei = ?",
				new Object[] { imei }, new int[] { Types.VARCHAR });
		return true;
	}

	@Override
	public MemberInfo getMemberInfo(String username, String imei) {
		String sql = "select * from member_info where phone =? and imei =?  LIMIT 1";
		List<MemberInfo> list = jdbcTemplate.query(sql, new Object[] { username,
				imei }, new BeanPropertyRowMapper<MemberInfo>(
				MemberInfo.class));
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getMemberInfo null.user_id:" + username);
		}
		return null;
	}

	@Override
	public WatchPhoneBook getPhoneBookByImeiAndPhone(String imei, String tel) {
		String sql = "select * from watch_phonebook_info where imei =? and phone =? LIMIT 1";
		List<WatchPhoneBook> list = jdbcTemplate.query(sql, new Object[] { imei, tel }, new BeanPropertyRowMapper<WatchPhoneBook>(
						WatchPhoneBook.class));
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getPhoneBookByImeiAndPhone null.user_id:" + tel+","+imei);
		}
		return null;
	}

	@Override
	public boolean insertPhoneBookInfo(String imei, String name, String phone, String cornet, String headType,Integer status) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("insert into watch_phonebook_info ( imei, name, phone, cornet, headType, createtime, status, updatetime) values (?,?,?,?,?,?,?,?)",
						new Object[] { imei, name, phone, cornet, headType, now ,status, now},
						new int[] { Types.VARCHAR, Types.VARCHAR,
								Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
								Types.TIMESTAMP,Types.INTEGER ,
								Types.TIMESTAMP});
		return i == 1;
	}

	@Override
	public List<WatchPhoneBook> getPhoneBookByImei(String imei) {
		String sql = "select * from watch_phonebook_info where imei =?";
		List<WatchPhoneBook> list = jdbcTemplate
				.query(sql, new Object[] { imei },
						new BeanPropertyRowMapper<WatchPhoneBook>(WatchPhoneBook.class));
		return list;
	}

	@Override
	public boolean deletePhonebookById(Long id) {
		jdbcTemplate.update("delete from watch_phonebook_info where   id = ?",
				new Object[] { id }, new int[] { Types.INTEGER });
		return true;
	}

	@Override
	public boolean updatePhonebookById(Long id, String name, String phone, String cornet, String headType) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("update watch_phonebook_info set name=?, phone=?, cornet=?,headtype=?,updatetime=? where id = ?",
						new Object[] { name,phone,cornet,headType ,now, id }, new int[] {
								Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP,Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean updatePhonebookHeadImgById(Long deviceContactId, String photoImg) {
		Timestamp now = Utils.getCurrentTimestamp();
		int i = jdbcTemplate
				.update("update watch_phonebook_info set headImg=?,updatetime=? where id = ?",
						new Object[] { photoImg ,now, deviceContactId }, new int[] {
								Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER });
		return i == 1;
	}

	@Override
	public boolean deleteWatchMemberByImei(String imei) {
		jdbcTemplate.update("delete from watch_phonebook_info where   imei = ?",
				new Object[] { imei }, new int[] { Types.VARCHAR });
		return true;
	}

	@Override
	public WatchPhoneBook getPhoneBookByImeiAndStatus(String imei, Integer status) {
		String sql = "select * from watch_phonebook_info where imei =? and status =? order by id asc LIMIT 1";
		List<WatchPhoneBook> list = jdbcTemplate.query(sql, new Object[] { imei, status }, new BeanPropertyRowMapper<WatchPhoneBook>(
						WatchPhoneBook.class));
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		} else {
			logger.info("get getPhoneBookByImeiAndPhone null.user_id:" + imei+","+imei);
		}
		return null;
	}

}
