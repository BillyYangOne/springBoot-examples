package com.kyee.monitor.base.resultmodel;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;

/**
 * 结果模型
 */
public abstract class ResultModel extends AbstractView {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 上下文类型
	 */
	protected enum ContentType {
		
		/**
		 * 文本
		 */
		TEXT("text/plain"),
		
		/**
		 * javascript
		 */
		JAVASCRIPT("text/javascript"),
		
		/**
		 * json
		 */
		JSON("text/json"),
		
		/**
		 * xml
		 */
		XML("text/xml"),
		
		/**
		 * html
		 */
		HTML("text/html"),
		
		/**
		 * zip 压缩文件
		 */
		ZIP("application/zip"),
		
		/**
		 * 文件流
		 */
		STREAM("application/octet-stream");
		
		private String value;
		
		ContentType(String value){
			this.value = value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		response.setContentType(getResultContentType().toString());
		render(response);
	}
	
	/**
	 * 渲染
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected abstract void render(HttpServletResponse response) throws Exception;
	
	/**
	 * 获取上下文类型
	 * 
	 * @return
	 */
	protected abstract ContentType getResultContentType();
}
