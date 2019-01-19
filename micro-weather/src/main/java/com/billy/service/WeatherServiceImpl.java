package com.billy.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.billy.dao.WeatherResponse;
import com.billy.util.XmlBuilder;
import com.billy.vo.City;
import com.billy.vo.CityList;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class WeatherServiceImpl implements WeatherService{
	
	private static Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	//获取天气数据API
	private final String WEATHER_API = "http://wthrcdn.etouch.cn/weather_mini";
	//缓存超时时间
	private final Long TIME_OUT = 1800L;
	
	@Override
	public WeatherResponse getDataByCityId(String cityId) {
		
		String url = WEATHER_API + "?citykey=" + cityId;
		
		return getWeatherData(url);
	}

	@Override
	public WeatherResponse getDataByCityName(String cityName) {
		
		String url = WEATHER_API + "?city=" + cityName;
		
		return getWeatherData(url);
	}
	
	private WeatherResponse getWeatherData(String url) {
		
		ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
		
		String strBody = "";
		String key = url; // 使用url作为缓存的key
		if(!this.stringRedisTemplate.hasKey(key)) {
			logger.info("没有找到key:" + key);
			
			ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
			if(responseEntity.getStatusCodeValue() == 200) {
				strBody = responseEntity.getBody();
			}
			ops.set(key, strBody, TIME_OUT, TimeUnit.SECONDS);
		}else {
			logger.info("找到key:" + key + ",value为：" + ops.get(key));
			strBody = ops.get(key);
		}
		
		ObjectMapper mapper = new ObjectMapper();
		WeatherResponse weatherResponse = null;
		
		try {
			weatherResponse = mapper.readValue(strBody, WeatherResponse.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return weatherResponse;
	}

	@Override
	public List<City> listCity() throws Exception {
		
		Resource resource = new ClassPathResource("cityList.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), "utf-8"));
		
		StringBuffer buffer = new StringBuffer();
		String line = "";
		
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		reader.close();
		
		CityList cityList = (CityList) XmlBuilder.xmStr2Object(CityList.class, buffer.toString());
		
		return cityList.getCityList();
	}

	@Override
	public void syncWeatherDataById(String cityId) {
		// TODO Auto-generated method stub
		String url = WEATHER_API + "?citykey=" + cityId;
		saveWeatherData(url);
	}
	
	/**
	 * 保存天气数据到缓存中
	 */
	public void saveWeatherData(String url) {
		
		ValueOperations<String, String> ops = this.stringRedisTemplate.opsForValue();
		
		String strBody = "";
		String key = url; // 使用url作为缓存的key
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
		if(responseEntity.getStatusCodeValue() == 200) {
			strBody = responseEntity.getBody();
		}
		ops.set(key, strBody, TIME_OUT, TimeUnit.SECONDS);
		
	}

}
