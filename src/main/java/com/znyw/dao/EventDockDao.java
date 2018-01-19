package com.znyw.dao;

import java.util.List;
import java.util.Map;

public interface EventDockDao {

	boolean pushOwners(Map<String, Object> namesAndValues);

	boolean updateOwners(String userId, Map<String, Object> namesAndValues);

	boolean getOwnersByUserId(String userId);

	Map<String, Object> ownerslist(List<String> userIds, String userName, String userAddr, String areaName,
			String contact, String cPhone, String cMobile, String pnlTel, String pnlHdTel, String sort, int pageSize,
			int currPage);

	boolean pushAlarmEvent(Map<String, Object> namesAndValues);

	Map<String, Object> alarmEventslist(String eventNum, String eventTime, String evtWay, String eventDesc,
			String codeTypeId, String accountNum, String accountName, String handleStatus, String handleResult,
			String handleDesc, String handleTime, String sort, int pageSize,
			int currPage);

	boolean handleAlarmEvent(List<String> eventNums, String handleResult, String handleDesc, String handleTime);

	boolean hasAlarmEvent(String eventNum);

	List<String> getAllOwnerId();
	/**
	 * 指定事件列表中已经被处理的事件
	 * 
	 * @param eventNums
	 * @return
	 */
	List<String> getAlreadyHandledAlarmEventIds(List<String> eventNums);
	
	List<Map<String, Object>> getAlarmEventByEventNum(List<String> eventNum);
}
