package com.kyee.monitor.base.resultmodel;

import javax.servlet.http.HttpServletResponse;

/**
 * 文本结果视图
 */
public abstract class TextBasedResultModel extends ResultModel {

	@Override
	public void render(HttpServletResponse response) throws Exception {
		
		String text = getText();
		if(text != null){
			response.getWriter().write(text);
		}
	}
	
	/**
	 * 获取待写入的文本
	 * 
	 * @return
	 */
	protected abstract String getText();
	
	@Override
	protected ContentType getResultContentType() {
		return ContentType.TEXT;
	}
}
