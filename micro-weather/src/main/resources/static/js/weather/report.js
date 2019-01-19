/*!
 * report.html 页面脚本.
 * 
 */
"use strict";
//# sourceURL=report.js

// DOM 加载完再执行
$(function() {
	$("#selectCityId").change(function(){
		var cityId=$("#selectCityId").val();  //获取Select选择的Value
		var url = '/weatherController/cityId/' + cityId;
		window.location.href= url;
	});
});