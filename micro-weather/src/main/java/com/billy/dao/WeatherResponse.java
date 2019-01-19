package com.billy.dao;

import com.billy.domain.Weather;

/**
 * 返回對象
 * 
 * @author BillyYang
 *
 */
public class WeatherResponse {

	private Weather data;
	private String status;
	private String desc;

	public Weather getData() {
		return data;
	}

	public void setData(Weather data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
