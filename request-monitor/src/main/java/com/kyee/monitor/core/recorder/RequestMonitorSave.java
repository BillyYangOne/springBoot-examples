package com.kyee.monitor.core.recorder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.util.CommonUtils.SecurityUtil;
import com.kyee.monitor.base.common.util.CommonUtils.TextUtil;
import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.core.monitor.bean.impl.ControllerMonitorResult;
import com.kyee.monitor.core.monitor.vo.RequestMonitorResult;


@Component("requestMonitorSave")
public class RequestMonitorSave  extends BaseComponent{

	@Autowired
    private JdbcTemplate jdbcTemplate;

	/**
	 * @describe 保存监控数据
	 */
	public void saveRequestMonitorData(Object data) {
		
		RequestMonitorResult requestResult = (RequestMonitorResult)data;
		logger.info("保存监控信息==========开始");
		try {
			saveMonitorInfo(requestResult);
		} catch (Exception e) {
			logger.error("保存监控信息时发生异常，异常信息为：" + e );
		}
		logger.info("保存监控信息==========结束");
	}
	
	/**
	 * @describe 插入监控记录 1、保存主信息 2、保存SQL信息
	 */
	@Transactional
	private int saveMonitorInfo(RequestMonitorResult requestResult) {
		
		ControllerMonitorResult monitorResult = requestResult.getControllerMonitorResult();
		
		logger.info("session信息为：" + monitorResult.getSessionParams());
		
		String sessionParams = monitorResult.getSessionParams();
		Map<String, String> userInfo = getUserInfo(sessionParams);
		
		// service 、 dao、 sql 的信息
		List<DetailMonitorResult> detailMonitorResult = requestResult.getDetailMonitorResult();
		
		// SQL 信息
		List<DetailMonitorResult> sqlMonitorResults = new ArrayList<>();
		
		detailMonitorResult.forEach(detailItem -> {
			if(detailItem.getTargetType() == MonitorTargetType.SQL) {
				sqlMonitorResults.add(detailItem);
			}
		});
		
		//遍历明细和SQL进行组装 request_detail 字段信息
		String requestDetail = serializer(detailMonitorResult);
		
		int result = 0;
		String sql = "insert into monitor_access_records("
				+ "record_id,request_url,params_data,result_info,request_s_time,request_e_time,"
				+"result_status,exception_msg,request_detail,user_id,hospital_id,request_ip,ecs_ip,ecs_port)"
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		if(TextUtil.isNotEmpty(monitorResult.getSourceUri())) {
			
			//获取主键ID
			Long recordId = jdbcTemplate.queryForObject("SELECT GET_MONITOR_ID FROM DUAL", Long.class);
			
			Object[] params = new Object[] {
					recordId,
					monitorResult.getSourceUri(),
					monitorResult.getRequestParams(),
					monitorResult.getResponse(),
					new Timestamp(monitorResult.getRunTime()),
					new Timestamp(monitorResult.getRunTime() + monitorResult.getSpendTime()),
					monitorResult.isHasException() ? 500 : 200,
					monitorResult.getException(),
					requestDetail,
					userInfo.get("user_id"),
					userInfo.get("hospital_id"),
					monitorResult.getRemoteAddr(),
					monitorResult.getIp(),
					monitorResult.getPort()
			};
			
			result = jdbcTemplate.update(sql, params);
			
			//保存SQL
			saveSqlInfo(sqlMonitorResults, recordId);
			
		}
		
//		//存储过程保存SQL
//		if(TextUtil.isNotEmpty(monitorResult.getSourceUri())) {
//			
//			String pSql = "insert into monitor_access_records("
//					+ "record_id,request_url,params_data,result_info,request_s_time,request_e_time,"
//					+"result_status,exception_msg,request_detail,user_id,hospital_id,request_ip,ecs_ip,ecs_port)"
//					+"values({id},"
//					+ "'" + monitorResult.getSourceUri() + "',"
//					+ "'" + monitorResult.getRequestParams() + "'," 
//					+ "'" + monitorResult.getResponse() + "',"
//				    + new Timestamp(monitorResult.getRunTime()) + ","
//					+ (new Timestamp(monitorResult.getRunTime() + monitorResult.getSpendTime())) + ","
//					+ "'" + (monitorResult.isHasException() ? 500 : 200) + "',"
//					+ "'" + monitorResult.getException() + "',"
//					+ "'" + requestDetail + "',"
//					+ "'" + userInfo.get("user_id") + "',"
//					+ "'" + userInfo.get("hospital_id") + "',"
//					+ "'" + monitorResult.getRemoteAddr() + "',"
//					+ "'" + monitorResult.getIp() + "',"
//					+ "'" + monitorResult.getPort()
//					+"')";
//			try {
//				
//				jdbcTemplate.execute("call P_MONITOR_DATA_SAVE('"+ pSql +"', '')");
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("保存存储发生异常----------------------");
//			}
//		}
		
		return result;
	}
	
	/**
	 * @describe 获取userId 和 hopitalId
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfo(String sessionStr){
		
		Map<String, String> result = new HashMap<>();
		try {
			
			if(TextUtil.isNotEmpty(sessionStr)) {
				
				if(TextUtil.isNotEmpty(sessionStr)) {
					
					Map<String, Object> sessionMap = JSON.parseObject(sessionStr, Map.class);
					String loginUserStr = sessionMap.get("login_user") + "";
					if(TextUtil.isNotEmpty(loginUserStr)) {
						Map<String, Object> loginUser = JSON.parseObject(loginUserStr, Map.class);
						result.put("user_id", loginUser.get("id") + "");
					}
					String hospitalStr = sessionMap.get("login_hospital") + "";
					if(TextUtil.isNotEmpty(hospitalStr)) {
						Map<String, Object> hospitalMap = JSON.parseObject(hospitalStr, Map.class);
						result.put("hospital_id", hospitalMap.get("hosnum") + "");
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("监控获取解析session信息时发生异常，异常信息为：" + e );
		}
		return result;
	}
	
	
	/**
	 * @describe 保存SQL信息
	 */
	public int saveSqlInfo(List<DetailMonitorResult> sqlResultList, Long recordId) {
		
		int result = 0;
		if(sqlResultList.size() > 0) {
			
			String sql = "insert into monitor_access_sql("
						+ "sql_id,record_id,sql_noparams,sql_params,sql_md5,start_time,end_time,result_status,exception_msg)"
						+ "values(get_monitor_id, ?, ?, ?, ?, ?, ?, ?, ?)";
			Object[] params = null;
			for (DetailMonitorResult sqlResult : sqlResultList) {
				
				params = new Object[] {
						recordId,
						sqlResult.getMethodContent(),
						sqlResult.getParams(),
						SecurityUtil.MD5_32(sqlResult.getMethodContent()),
						new Timestamp(sqlResult.getRunTime()),
						new Timestamp(sqlResult.getRunTime() + sqlResult.getSpendTime()),
						sqlResult.isHasException() ? 500 : 200,
						sqlResult.getException()
				};
				
				result += jdbcTemplate.update(sql, params);
			}
		}
		
		return result;
	}

	/**
	 * @describe 序列化对象
	 * @param data
	 * @return
	 */
	private String serializer(Object data) {

		return JSON.toJSONString(data);
	}
	
}
