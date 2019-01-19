package com.billy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.billy.dao.WeatherResponse;
import com.billy.service.WeatherService;
import com.billy.vo.City;

@RestController
@RequestMapping("/weatherController")
public class WeatherController {

	@Autowired
	private WeatherService weatherService;
	
	/**
	 * 获取天气报告信息
	 */
	@GetMapping("/cityId/{cityId}")
	public ModelAndView getReportByCityId(@PathVariable("cityId")String cityId, Model model) {
		
		model.addAttribute("title", "Billy's 天气预报");
		model.addAttribute("cityId", cityId);
		try {
			model.addAttribute("cityList", weatherService.listCity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("report", weatherService.getDataByCityId(cityId).getData());
		
		return new ModelAndView("weather/report", "reportModel", model);
		
	}

	/**
	 * 根据城市ID获取天气信息
	 * @param cityId
	 * @return
	 */
	@RequestMapping("/getReportById")
	public WeatherResponse getReportByCityId(String cityId) {
		return weatherService.getDataByCityId(cityId);
	}

	/**
	 * 根据城市名称获取天气信息
	 * @param cityName
	 * @return
	 */
	@RequestMapping("/getReportByName")
	public WeatherResponse getReportByCityName(String cityName) {
		return weatherService.getDataByCityName(cityName);
	}

	/**
	 * 获取城市列表
	 * @return
	 */
	@RequestMapping("/getCityList")
	public List<City> getCityList() {
		
		List<City> listCity = null;
		try {
			
			listCity = weatherService.listCity();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("获取城市列表发生异常！");
		}
		return listCity;
	}
}
