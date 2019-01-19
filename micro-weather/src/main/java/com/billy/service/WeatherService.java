package com.billy.service;

import java.util.List;

import com.billy.dao.WeatherResponse;
import com.billy.vo.City;

public interface WeatherService {

	WeatherResponse getDataByCityId(String cityId);
	
	WeatherResponse getDataByCityName(String cityName);
	
	/**
	 * 获取城市列表
	 */
	List<City> listCity() throws Exception;
	
	/**
	 * 根据ID同步天气数据
	 */
	void syncWeatherDataById(String cityId);
}
