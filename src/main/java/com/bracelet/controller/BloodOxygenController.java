package com.bracelet.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bracelet.dto.HttpBaseDto;
import com.bracelet.dto.LatestBloodOxygenDto;
import com.bracelet.dto.LatestBloodSugarDto;
import com.bracelet.entity.BloodOxygen;
import com.bracelet.entity.BloodSugar;
import com.bracelet.entity.VersionInfo;
import com.bracelet.service.IBloodOxygenService;
import com.bracelet.service.IBloodSugarService;
import com.bracelet.util.HttpClientGet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bloodOxygen")
public class BloodOxygenController extends BaseController {
    @Autowired
    IBloodOxygenService bloodOxygenService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ResponseBody
    @RequestMapping("/search/latest/{token}")
    public HttpBaseDto getLatestBloodOxygen(@PathVariable String token) {
        Long user_id = checkTokenAndUser(token);
        BloodOxygen bloodOxygen= bloodOxygenService.getLatest(user_id);
        LatestBloodOxygenDto latestBloodOxygenDto = null;
        if (bloodOxygen != null) {
        	latestBloodOxygenDto = new LatestBloodOxygenDto();
        	latestBloodOxygenDto.setBloodOxygen(bloodOxygen.getBlood_oxygen());
        	latestBloodOxygenDto.setPulseRate(bloodOxygen.getPulse_rate());
        	latestBloodOxygenDto.setTimestamp(bloodOxygen.getUpload_time().getTime());
        }
        HttpBaseDto dto = new HttpBaseDto();
        dto.setData(latestBloodOxygenDto);
        return dto;
    }
    
    @ResponseBody
	@RequestMapping(value = "/heath", method = RequestMethod.POST)
	public String oldLocation(@RequestBody String json) {
		JSONObject bb = new JSONObject();

		bb.put("Code", 1);

		return bb.toString();
	}
    
}
