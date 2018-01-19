package com.znyw.dao.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.znyw.dao.EventDockDao;
import com.znyw.tool.Objects;
import com.znyw.tool.SqlGenerateUtils;

@Repository
public class EventDockDaoImp implements EventDockDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventDockDaoImp.class);

	private static final String INSERT_WS_OWNERS_SQL = "insert into ws_owners %s";
	private static final String UPDATE_WS_OWNERS_SQL = "update ws_owners set %s where userId='%s'";
	private static final String INSERT_WS_ALARM_EVENT_SQL = "insert into ws_alarm_event %s";
	private static final String UPDATE_WS_ALARM_EVENT_SQL = "update ws_alarm_event set handleStatus='已处理',handleResult=?,handleDesc=?,handleTime=? where eventNum in ('%s') ";

	private static List<String> ALARM_EVENT_COLUMNS;

	static {
		ALARM_EVENT_COLUMNS = new ArrayList<String>();

		ALARM_EVENT_COLUMNS.add("eventNum");
		ALARM_EVENT_COLUMNS.add("eventTime");
		ALARM_EVENT_COLUMNS.add("eventLevel");
		ALARM_EVENT_COLUMNS.add("evtWay");
		ALARM_EVENT_COLUMNS.add("eventDesc");
		ALARM_EVENT_COLUMNS.add("recieiveTime");
		ALARM_EVENT_COLUMNS.add("sysCode");
		ALARM_EVENT_COLUMNS.add("codeTypeId");
		ALARM_EVENT_COLUMNS.add("accountNum");
		ALARM_EVENT_COLUMNS.add("accountName");
		ALARM_EVENT_COLUMNS.add("accountAddr");
		ALARM_EVENT_COLUMNS.add("accountZone");
		ALARM_EVENT_COLUMNS.add("usrAlmType");
		ALARM_EVENT_COLUMNS.add("devSubSys");
		ALARM_EVENT_COLUMNS.add("userMonitorId");
		ALARM_EVENT_COLUMNS.add("cameraModelId");
		ALARM_EVENT_COLUMNS.add("alarmAddr");
		ALARM_EVENT_COLUMNS.add("atPos");
		ALARM_EVENT_COLUMNS.add("devId");
		ALARM_EVENT_COLUMNS.add("devZoneId");
		ALARM_EVENT_COLUMNS.add("devModelName");
		ALARM_EVENT_COLUMNS.add("zoneAtPos");
		ALARM_EVENT_COLUMNS.add("snType");
		ALARM_EVENT_COLUMNS.add("almType");
		ALARM_EVENT_COLUMNS.add("wantDo");
		ALARM_EVENT_COLUMNS.add("areaId");
		ALARM_EVENT_COLUMNS.add("areaName");
		ALARM_EVENT_COLUMNS.add("snModelName");
		ALARM_EVENT_COLUMNS.add("codeType");
		ALARM_EVENT_COLUMNS.add("eventSrc");
	}

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Override
	public boolean pushOwners(Map<String, Object> namesAndValues) {
		try {

			int added = jdbcTemplate.update(
					String.format(INSERT_WS_OWNERS_SQL, SqlGenerateUtils.generateSqlForInsert(namesAndValues)),
					SqlGenerateUtils.getInsertValues(namesAndValues));

			return added > 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	public boolean updateOwners(String userId, Map<String, Object> namesAndValues) {
		try {
			String updateSql = String.format(UPDATE_WS_OWNERS_SQL,
					SqlGenerateUtils.generateSqlForUpdate(namesAndValues), userId);
			Object[] paras = SqlGenerateUtils.getNewValuesForUpdate(namesAndValues);

			jdbcTemplate.update(updateSql, paras);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;

	}

	@SuppressWarnings("deprecation")
	public boolean getOwnersByUserId(String userId) {

		String sql = "select count(*) from ws_owners where userId=?";

		try {
			return jdbcTemplate.queryForInt(sql, userId) > 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> ownerslist(List<String> userIds, String userName, String userAddr, String areaName,
			String contact, String cPhone, String cMobile, String pnlTel, String pnlHdTel, String sort, int pageSize,
			int currPage) {

		userName = Objects.isNullString(userName) ? "" : String.format(" and locate('%s',userName)>0 ", userName);
		userAddr = Objects.isNullString(userAddr) ? "" : String.format(" and locate('%s',userAddr)>0 ", userAddr);
		areaName = Objects.isNullString(areaName) ? "" : String.format(" and locate('%s',areaName)>0 ", areaName);
		contact = Objects.isNullString(contact) ? "" : String.format(" and locate('%s',contact)>0 ", contact);
		cPhone = Objects.isNullString(cPhone) ? "" : String.format(" and locate('%s',cPhone)>0 ", cPhone);
		cMobile = Objects.isNullString(cMobile) ? "" : String.format(" and locate('%s',userName)>0 ", cMobile);
		pnlTel = Objects.isNullString(pnlTel) ? "" : String.format(" and locate('%s',pnlTel)>0 ", pnlTel);
		pnlHdTel = Objects.isNullString(pnlHdTel) ? "" : String.format(" and locate('%s',userName)>0 ", pnlHdTel);

		String queryCountSql = "";
		String queryDataSql = "";

		if (Objects.isNull(userIds)) {
			queryCountSql = "select count(*) from ws_owners where 1=1 "
					+ userName + userAddr + areaName + contact + cPhone + cMobile + cMobile + pnlTel + pnlHdTel;

			queryDataSql = "select * from ws_owners where 1=1 "
					+ userName + userAddr + areaName + contact + cPhone + cMobile + cMobile + pnlTel + pnlHdTel
					+ " order by userName " + sort + " limit " + (currPage * pageSize) + "," + pageSize;

		} else {
			queryCountSql = "select count(*) from ws_owners where userId in ('" + Objects.Joiner("','", userIds) + "') "
					+ userName + userAddr + areaName + contact + cPhone + cMobile + cMobile + pnlTel + pnlHdTel;

			queryDataSql = "select * from ws_owners where userId in ('" + Objects.Joiner("','", userIds) + "') "
					+ userName + userAddr + areaName + contact + cPhone + cMobile + cMobile + pnlTel + pnlHdTel
					+ " order by userName " + sort + " limit " + (currPage * pageSize) + "," + pageSize;

		}


		int totalNum = 0;
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		try {

			totalNum = jdbcTemplate.queryForInt(queryCountSql);
			lists = jdbcTemplate.queryForList(queryDataSql);

			Objects.setNull2EmptyString(lists);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			totalNum = 0;
			lists = new ArrayList<Map<String, Object>>();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		map.put("lists", lists);

		return map;
	}

	@Override
	public boolean pushAlarmEvent(Map<String, Object> namesAndValues) {

		Objects.removeUnnecessaryColumns(ALARM_EVENT_COLUMNS, namesAndValues);

		try {
			int added = jdbcTemplate.update(
					String.format(INSERT_WS_ALARM_EVENT_SQL, SqlGenerateUtils.generateSqlForInsert(namesAndValues)),
					SqlGenerateUtils.getInsertValues(namesAndValues));
			return added > 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public Map<String, Object> alarmEventslist(String eventNum, String eventTime, String evtWay, String eventDesc,
			String codeTypeId, String accountNum, String accountName, String handleStatus, String handleResult,
			String handleDesc, String handleTime,
			String sort, int pageSize, int currPage) {
		
		
		String[] times = Objects.isNullString(eventTime) ? new String[] { "", "" } : eventTime.split(";");
		String timeStart = times[0];
		String timeEnd = times[1];

		String timeSql = "";
		if (Objects.isNotNullString(timeStart)) {
			timeSql += " AND eventTime > DATE_SUB('" + timeStart + "',INTERVAL 1 DAY) ";
		}
		if (Objects.isNotNullString(timeEnd)) {
			timeSql += " AND eventTime < DATE_SUB('" + timeEnd + "',INTERVAL -1 DAY) ";
		}

		eventNum = Objects.isNullString(eventNum) ? "" : String.format(" and locate('%s',eventNum)>0 ", eventNum);
		evtWay = Objects.isNullString(evtWay) ? "" : String.format(" and evtWay='%s' ", evtWay);
		eventDesc = Objects.isNullString(eventDesc) ? "" : String.format(" and locate('%s',sysCode)>0 ", eventDesc);
		codeTypeId = Objects.isNullString(codeTypeId) ? "" : String.format(" and codeTypeId='%s' ", codeTypeId);
		accountNum = Objects.isNullString(accountNum) ? ""
				: String.format(" and locate('%s',accountNum)>0 ", accountNum);
		accountName = Objects.isNullString(accountName) ? ""
				: String.format(" and locate('%s',accountName)>0 ", accountName);
		handleStatus = Objects.isNullString(handleStatus) ? ""
				: String.format(" and locate('%s',handleStatus)>0 ", handleStatus);
		handleResult = Objects.isNullString(handleResult) ? ""
				: String.format(" and locate('%s',handleResult)>0 ", handleResult);
		handleDesc = Objects.isNullString(handleDesc) ? ""
				: String.format(" and locate('%s',handleDesc)>0 ", handleDesc);
		handleTime = Objects.isNullString(handleTime) ? "" : String.format(" and locate('%s',eventNum)>0 ", handleTime);

		sort = Objects.isNullString(sort) ? "ASC" : sort.contains("ASC") ? "ASC" : "DESC";

		String queryCountSql = "select count(*) from ws_alarm_event where 1=1 " + eventNum + evtWay
				+ eventDesc + codeTypeId + accountNum + accountName + handleStatus + handleResult + handleDesc
				+ handleTime + timeSql;

		String queryDataSql = "select * from ws_alarm_event where 1=1 " + eventNum + evtWay + eventDesc
				+ codeTypeId + accountNum + accountName + handleStatus + handleResult + handleDesc + handleTime
				+ timeSql
				+ " order by eventNum,eventTime " + sort + " limit " + (currPage * pageSize) + "," + pageSize;

		int totalNum = 0;
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		try {

			totalNum = jdbcTemplate.queryForInt(queryCountSql);
			lists = jdbcTemplate.queryForList(queryDataSql);

			Objects.setNull2EmptyString(lists);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			totalNum = 0;
			lists = new ArrayList<Map<String, Object>>();
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalNum", totalNum);
		map.put("lists", lists);

		return map;
	}

	@SuppressWarnings("deprecation")
	public boolean hasAlarmEvent(String eventNum) {
		String sql = "select count(*) from ws_alarm_event where eventNum=?";

		try {
			return jdbcTemplate.queryForInt(sql, eventNum) > 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}
	@Override
	public boolean handleAlarmEvent(List<String> eventNums, String handleResult, String handleDesc, String handleTime) {
		try {
			int added = jdbcTemplate.update(
					String.format(UPDATE_WS_ALARM_EVENT_SQL, Objects.Joiner("','", eventNums)), handleResult,
					handleDesc, handleTime);
			return added > 0;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}

	public List<String> getAllOwnerId() {

		String sql = "select userId from ws_owners";
		List<String> userIds = new ArrayList<String>();

		try {
			List<Map<String, Object>> lists = jdbcTemplate.queryForList(sql);

			if (Objects.isNotNull(lists)) {

				for (Map<String, Object> map : lists) {
					userIds.add(map.get("userId").toString());
				}
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return userIds;
	}

	public List<String> getAlreadyHandledAlarmEventIds(List<String> eventNums) {

		String sql = "select eventNum from ws_alarm_event where eventNum in ('%s') and handleStatus='已处理' ";

		List<String> alreadyHandledAlarmEvents = new ArrayList<String>();

		try {

			List<Map<String, Object>> lists = jdbcTemplate
					.queryForList(String.format(sql, Objects.Joiner("','", eventNums)));

			if (Objects.isNotNull(lists)) {

				for (Map<String, Object> map : lists) {
					alreadyHandledAlarmEvents.add(map.get("eventNum").toString());
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}

		return alreadyHandledAlarmEvents;
	}

	public List<Map<String, Object>> getAlarmEventByEventNum(List<String> eventNums) {
		String sql = "select * from ws_alarm_event where eventNum in ('%s')";

		try {

			List<Map<String, Object>> lists = jdbcTemplate
					.queryForList(String.format(sql, Objects.Joiner("','", eventNums)));

			if (Objects.isNotNull(lists)) {
				Objects.setNull2EmptyString(lists);
				return lists;
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return new ArrayList<Map<String, Object>>();
	}

}
