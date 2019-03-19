package com.bracelet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bracelet.dto.HttpBaseDto;
import com.bracelet.dto.SocketLoginDto;
import com.bracelet.entity.Fence;
import com.bracelet.entity.Fencelog;
import com.bracelet.entity.LocationWatch;
import com.bracelet.entity.OddShape;
import com.bracelet.entity.SensitivePoint;
import com.bracelet.entity.SensitivePointLog;
import com.bracelet.entity.WatchPhoneBook;
import com.bracelet.exception.BizException;
import com.bracelet.service.IFenceService;
import com.bracelet.service.IFencelogService;
import com.bracelet.service.ISensitivePointService;
import com.bracelet.service.ISensitivePointlogService;
import com.bracelet.service.IUserInfoService;
import com.bracelet.util.ChannelMap;
import com.bracelet.util.RadixUtil;
import com.bracelet.util.RespCode;
import com.bracelet.util.Utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/security")
public class SecurityController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IFenceService fenceService;
	@Autowired
	IUserInfoService userInfoService;
	@Autowired
	IFencelogService fencelogService;
	@Autowired
	ISensitivePointService sensitivePointService;
	@Autowired
	ISensitivePointlogService sensitivePointlogService;

	/* 查找电子围栏 */
	@ResponseBody
	@RequestMapping(value = "/getwatchfence/{token}/{imei}", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	public String getwatchfence(@PathVariable String token, @PathVariable String imei) {
		
		JSONObject bb = new JSONObject();

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}

		List<Fence> fenoneList = fenceService.getWatchFenceList(imei);
		JSONArray jsonArray = new JSONArray();
		if (fenoneList != null) {
			for (Fence fenone : fenoneList) {
				JSONObject dataMap = new JSONObject();
				dataMap.put("GeofenceID", fenone.getId());
				dataMap.put("FenceName", fenone.getName()+"");
				dataMap.put("Entry", fenone.getIs_entry());
				dataMap.put("Exit", fenone.getIs_exit());
				dataMap.put("Enable", fenone.getIs_enable());
				dataMap.put("CreateTime", Utils.getLocationTime(System.currentTimeMillis()));
				dataMap.put("UpdateTime", "");
				dataMap.put("Description", "");
				dataMap.put("Lat", fenone.getLat());
				dataMap.put("Lng", fenone.getLng());
				dataMap.put("Radii", fenone.getRadius());
				dataMap.put("createtime", fenone.getCreatetime().getTime());
				dataMap.put("updatetime", fenone.getUpdatetime().getTime());
				dataMap.put("id",  fenone.getId());
				jsonArray.add(dataMap);
			}
			bb.put("Code", 1);
		}else{
			bb.put("Code", 0);
		}
		bb.put("GeoFenceList", jsonArray);
		return bb.toString();
	}

	/* 手表电子围栏 */
	/* 添加 */
	@ResponseBody
	@RequestMapping(value = "/addwatchfence", method = RequestMethod.POST)
	public String addwatchfence(@RequestBody String body) {
		JSONObject bb = new JSONObject();
		JSONObject jsonObject = (JSONObject) JSON.parse(body);
		String token = jsonObject.getString("token");

		String imei = jsonObject.getString("imei");
		String name = jsonObject.getString("fenceName");// 围栏名称
		String lat = jsonObject.getString("lat");
		String lng = jsonObject.getString("lng");
		String radius = jsonObject.getString("radius");
		
	
		
		Integer entry = jsonObject.getInteger("entry");
		Integer exit = jsonObject.getInteger("exit");
		Integer enable = jsonObject.getInteger("enable");
		
		
		/*
		 * ["{\"enable\":\"0\",\"radius\":\"500\",
		 * \"imei\":\"872018020142169\",
		 * \"token\":\"498016FC71B8D961E8E8FB8D8A6A8D55\",
		 * \"exit\":\"1\",\"entry\":\"1\",\"fenceName\":\"12\",
		 * \"lat\":\"22.533053142030756\",\"lng\":\"114.02305886149406\"}"]
		 * */
		

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		if (this.fenceService.insert(imei, name, lat, lng, radius, entry, exit, enable)) {
			bb.put("Code", 1);
		} else {
			bb.put("Code", 0);
		}
		return bb.toString();
	}

	/* 修改 */
	@ResponseBody
	@RequestMapping(value = "/updatewatchfence", method = RequestMethod.POST)
	public String udpateWatchfence(@RequestBody String body) {
		JSONObject bb = new JSONObject();
		JSONObject jsonObject = (JSONObject) JSON.parse(body);
		String token = jsonObject.getString("token");

		

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		String imei = jsonObject.getString("imei");
		String name = jsonObject.getString("name");// 围栏名称
		String lat = jsonObject.getString("lat");
		String lng = jsonObject.getString("lng");
		String radius = jsonObject.getString("radius");
		Long id = Long.valueOf(jsonObject.getString("id"));
		
		Integer entry = jsonObject.getInteger("entry");
		Integer exit = jsonObject.getInteger("exit");
		Integer enable = jsonObject.getInteger("enable");
		
		/*["{\"enable\":\"1\",\"radius\":\"500\",\"imei\":\"872018020142169\",
		 * \"id\":\"4\",\"token\":\"498016FC71B8D961E8E8FB8D8A6A8D55\",\
		 * "exit\":\"0\",\"name\":\"25889\",\"entry\":\"0\",
		 * \"lat\":\"22.535997554570656\",\"lng\":\"114.02577459812167\"}"]
		*/
		if (this.fenceService.updateWatchFence(id, imei, name, lat, lng, radius, entry, exit, enable)) {
			bb.put("Code", 1);
		} else {
			bb.put("Code", 0);
		}
		return bb.toString();
	}

	/* 删除 */
	@ResponseBody
	@RequestMapping(value = "/deletewatchfence/{token}/{id}", method = RequestMethod.GET)
	public String udpateWatchfence(@PathVariable String token, @PathVariable Long id) {
		JSONObject bb = new JSONObject();

		String userId = checkTokenWatchAndUser(token);
		if ("0".equals(userId)) {
			bb.put("Code", -1);
			return bb.toString();
		}
		if (this.fenceService.deleteWatchFence(id)) {
			bb.put("Code", 1);
		} else {
			bb.put("Code", 0);
		}
		return bb.toString();
	}

	/**
	 * 电子围栏
	 */
	@ResponseBody
	@RequestMapping(value = "/fence/{token}", method = RequestMethod.GET)
	public HttpBaseDto getFence(@PathVariable String token) {
		Long user_id = checkTokenAndUser(token);
		Fence fence = this.fenceService.getOne(user_id);
		HttpBaseDto dto = new HttpBaseDto();
		if (fence != null) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("id", fence.getId());
			dataMap.put("lat", fence.getLat());
			dataMap.put("lng", fence.getLng());
			dataMap.put("radius", fence.getRadius());
			dataMap.put("timestamp", fence.getUpdatetime().getTime());

			// fencelog
			List<Fencelog> fencelogList = this.fencelogService.findByCount(user_id, 2, 3);
			List<Map<String, Object>> fencelogDatalist = new LinkedList<Map<String, Object>>();
			if (fencelogList != null) {
				for (Fencelog fencelog : fencelogList) {
					Map<String, Object> fencelogDataMap = new HashMap<>();
					fencelogDataMap.put("id", fencelog.getId());
					fencelogDataMap.put("imei", fencelog.getImei());
					fencelogDataMap.put("lat", fencelog.getLat());
					fencelogDataMap.put("lng", fencelog.getLng());
					fencelogDataMap.put("radius", fencelog.getRadius());
					fencelogDataMap.put("lat1", fencelog.getLat1());
					fencelogDataMap.put("lng1", fencelog.getLng1());
					fencelogDataMap.put("status", fencelog.getStatus());
					fencelogDataMap.put("content", fencelog.getContent());
					fencelogDataMap.put("timestamp", fencelog.getUpload_time().getTime());
					fencelogDatalist.add(fencelogDataMap);
				}
			}
			dataMap.put("fencelog", fencelogDatalist);
			dto.setData(dataMap);
		}
		return dto;
	}

	@ResponseBody
	@RequestMapping(value = "/fence", method = RequestMethod.POST)
	public HttpBaseDto saveFence(@RequestParam String token, @RequestParam String lat, @RequestParam String lng,
			@RequestParam Integer radius) {
		if (StringUtils.isAllEmpty(lat, lng)) {
			throw new BizException(RespCode.NOTEXIST_PARAM);
		}
		if (radius == null || radius < 0) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.fenceService.insert(user_id, lat, lng, radius)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户设置电子围栏失败, token:" + token + ",userId:" + user_id + ",lat:" + lat + ",lng:" + lng + ",radius:"
					+ radius);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/fence/update", method = RequestMethod.POST)
	public HttpBaseDto updateFence(@RequestParam String token, @RequestParam Long id, @RequestParam String lat,
			@RequestParam String lng, @RequestParam Integer radius) {
		if (StringUtils.isAllEmpty(lat, lng)) {
			throw new BizException(RespCode.NOTEXIST_PARAM);
		}
		if (id == null || radius == null || radius < 0) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.fenceService.update(id, user_id, lat, lng, radius)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户修改电子围栏失败, token:" + token + ",userId:" + user_id + ",id:" + id + ",lat:" + lat + ",lng:"
					+ lng + ",radius:" + radius);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/fence/delete", method = RequestMethod.POST)
	public HttpBaseDto deleteFence(@RequestParam String token, @RequestParam Long id) {
		if (id == null) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.fenceService.delete(id, user_id)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户删除电子围栏失败, token:" + token + ",userId:" + user_id + ",id:" + id);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/fence/leavelog/head/{token}", method = RequestMethod.GET)
	public HttpBaseDto listFenceLeaveLog(@PathVariable String token) {
		Long user_id = checkTokenAndUser(token);
		List<Fencelog> fencelogList = this.fencelogService.findByCount(user_id, 2, 3);
		List<Map<String, Object>> fencelogDatalist = new LinkedList<Map<String, Object>>();
		if (fencelogList != null) {
			for (Fencelog fencelog : fencelogList) {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("id", fencelog.getId());
				dataMap.put("imei", fencelog.getImei());
				dataMap.put("lat", fencelog.getLat());
				dataMap.put("lng", fencelog.getLng());
				dataMap.put("radius", fencelog.getRadius());
				dataMap.put("lat1", fencelog.getLat1());
				dataMap.put("lng1", fencelog.getLng1());
				dataMap.put("status", fencelog.getStatus());
				dataMap.put("content", fencelog.getContent());
				dataMap.put("timestamp", fencelog.getUpload_time().getTime());
				fencelogDatalist.add(dataMap);
			}
		}
		HttpBaseDto dto = new HttpBaseDto();
		dto.setData(fencelogDatalist);
		return dto;
	}

	/**
	 * 敏感区域
	 */
	@ResponseBody
	@RequestMapping(value = "/sensitivepoint/{token}", method = RequestMethod.GET)
	public HttpBaseDto listSensitivePoint(@PathVariable String token) {
		Long user_id = checkTokenAndUser(token);
		List<SensitivePoint> splist = this.sensitivePointService.find(user_id);
		List<Map<String, Object>> dataList = new LinkedList<Map<String, Object>>();
		if (splist != null && splist.size() > 0) {
			for (SensitivePoint sp : splist) {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("id", sp.getId());
				dataMap.put("lat", sp.getLat());
				dataMap.put("lng", sp.getLng());
				dataMap.put("radius", sp.getRadius());
				dataMap.put("timestamp", sp.getUpdatetime().getTime());

				// sensitivepointlog
				List<SensitivePointLog> splogList = this.sensitivePointlogService.findByCount(user_id, sp.getId(), 0,
						3);
				List<Map<String, Object>> splogDatalist = new LinkedList<Map<String, Object>>();
				if (splogList != null) {
					for (SensitivePointLog splog : splogList) {
						Map<String, Object> splogDataMap = new HashMap<>();
						splogDataMap.put("id", splog.getId());
						splogDataMap.put("sp_id", splog.getSp_id());
						splogDataMap.put("imei", splog.getImei());
						splogDataMap.put("lat", splog.getLat());
						splogDataMap.put("lng", splog.getLng());
						splogDataMap.put("radius", splog.getRadius());
						splogDataMap.put("lat1", splog.getLat1());
						splogDataMap.put("lng1", splog.getLng1());
						splogDataMap.put("status", splog.getStatus());
						splogDataMap.put("content", splog.getContent());
						splogDataMap.put("timestamp", splog.getUpload_time().getTime());
						splogDatalist.add(splogDataMap);
					}
				}
				dataMap.put("sensitivepointlog", splogDatalist);
				dataList.add(dataMap);
			}
		}
		HttpBaseDto dto = new HttpBaseDto();
		dto.setData(dataList);
		return dto;
	}

	@ResponseBody
	@RequestMapping(value = "/sensitivepoint", method = RequestMethod.POST)
	public HttpBaseDto saveSensitivePoint(@RequestParam String token, @RequestParam String lat,
			@RequestParam String lng, @RequestParam Integer radius) {
		if (StringUtils.isAllEmpty(lat, lng)) {
			throw new BizException(RespCode.NOTEXIST_PARAM);
		}
		if (radius == null || radius < 0) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.sensitivePointService.insert(user_id, lat, lng, radius)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户设置敏感区域失败, token:" + token + ",userId:" + user_id + ",lat:" + lat + ",lng:" + lng + ",radius:"
					+ radius);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/sensitivepoint/update", method = RequestMethod.POST)
	public HttpBaseDto updateSensitivePoint(@RequestParam String token, @RequestParam Long id, @RequestParam String lat,
			@RequestParam String lng, @RequestParam Integer radius) {
		if (StringUtils.isAllEmpty(lat, lng)) {
			throw new BizException(RespCode.NOTEXIST_PARAM);
		}
		if (id == null || radius == null || radius < 0) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.sensitivePointService.update(id, user_id, lat, lng, radius)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户修改敏感区域失败, token:" + token + ",userId:" + user_id + ",id:" + id + ",lat:" + lat + ",lng:"
					+ lng + ",radius:" + radius);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/sensitivepoint/delete", method = RequestMethod.POST)
	public HttpBaseDto deleteSensitivePoint(@RequestParam String token, @RequestParam Long id) {
		if (id == null) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.sensitivePointService.delete(id, user_id)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户删除敏感区域失败, token:" + token + ",userId:" + user_id + ",id:" + id);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/sensitivepoint/inoutlog/head/{token}", method = RequestMethod.GET)
	public HttpBaseDto listSensitivePointLog(@PathVariable String token, @RequestParam Long sp_id) {
		Long user_id = checkTokenAndUser(token);
		List<SensitivePointLog> splogList = this.sensitivePointlogService.findByCount(user_id, sp_id, 0, 3);
		List<Map<String, Object>> splogDatalist = new LinkedList<Map<String, Object>>();
		if (splogList != null) {
			for (SensitivePointLog splog : splogList) {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("id", splog.getId());
				dataMap.put("sp_id", splog.getSp_id());
				dataMap.put("imei", splog.getImei());
				dataMap.put("lat", splog.getLat());
				dataMap.put("lng", splog.getLng());
				dataMap.put("radius", splog.getRadius());
				dataMap.put("lat1", splog.getLat1());
				dataMap.put("lng1", splog.getLng1());
				dataMap.put("status", splog.getStatus());
				dataMap.put("content", splog.getContent());
				dataMap.put("timestamp", splog.getUpload_time().getTime());
				splogDatalist.add(dataMap);
			}
		}
		HttpBaseDto dto = new HttpBaseDto();
		dto.setData(splogDatalist);
		return dto;
	}

	/* 异形电子围栏 */
	@ResponseBody
	@RequestMapping(value = "/otherFence", method = RequestMethod.POST)
	public HttpBaseDto saveOtherFence(@RequestParam String token, @RequestParam String point) {
		if (StringUtils.isAllEmpty(point)) {
			throw new BizException(RespCode.NOTEXIST_PARAM);
		}
		char[] chars = point.toCharArray();
		for (char aChar : chars) {
			boolean aacb = Utils.isChinesePunctuation(aChar);
			if (aacb) {
				throw new BizException(RespCode.ERR_PARAM);
			}
		}
		Long user_id = checkTokenAndUser(token);
		if (this.fenceService.insertOddShape(user_id, point)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户设置异形电子围栏失败, token:" + token + ",userId:" + user_id + ",point:" + point);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/OddShapefence/{token}", method = RequestMethod.GET)
	public HttpBaseDto getOddShapeFence(@PathVariable String token) {
		Long user_id = checkTokenAndUser(token);
		OddShape fence = this.fenceService.getOddShape(user_id);
		HttpBaseDto dto = new HttpBaseDto();
		if (fence != null) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("id", fence.getId());
			dataMap.put("point", fence.getPoint());
			dataMap.put("timestamp", fence.getCreatetime().getTime());
			dto.setData(dataMap);
		}
		return dto;
	}

	@ResponseBody
	@RequestMapping(value = "/OddShapefence/delete", method = RequestMethod.POST)
	public HttpBaseDto deleteOddFence(@RequestParam String token, @RequestParam Long id) {
		if (id == null) {
			throw new BizException(RespCode.ERR_PARAM);
		}
		Long user_id = checkTokenAndUser(token);
		if (this.fenceService.deleteOddShape(id, user_id)) {
			HttpBaseDto dto = new HttpBaseDto();
			return dto;
		} else {
			logger.info("用户删除异形电子围栏失败, token:" + token + ",userId:" + user_id + ",id:" + id);
			throw new BizException(RespCode.SYS_ERR);
		}
	}

}
