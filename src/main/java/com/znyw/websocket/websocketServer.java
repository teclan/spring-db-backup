package com.znyw.websocket;


import java.io.IOException;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.xmlbeans.impl.tool.Extension.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@ServerEndpoint(value = "/websocket/login/{param}")   //类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
public class websocketServer {
	
	private final static Logger log = LoggerFactory.getLogger(websocketServer.class);
	
	public static ConcurrentHashMap<String, Session> userList = new ConcurrentHashMap<String, Session>();
	
	//private IRDAesMysqlDao iRDAesMysqlDao=(IRDAesMysqlDao) ContextLoader.getCurrentWebApplicationContext().getBean("RDAesMysqlDao");

	@OnOpen
	public void OnOpen(Session session,
			@PathParam(value = "param") String param ) {
		/*Session oldSession = userList.get(param);
		if(oldSession!=null){//同一浏览器新标签栏session改变，单点并不退出登陆
			JSONArray list = iRDAesMysqlDao.querySockAlertList(userName2);
			Timer timer = new Timer();
			clearSock timerTask =new clearSock();
			timerTask.list = list;
			timer.schedule(timerTask, 1000);
			boolean rel = iRDAesMysqlDao.delsockAlert("",userName2,2);
			log.info("websocket: OnOpen :" + userName2 + " 异地登陆.  清理sock返回"+rel);
		}
		else{
			log.info("websocket: OnOpen :" + userName2 + " 首次登陆");
		}	*/
		log.info("websocket: OnOpen :"+param);
		userList.put(param, session);
	}

	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		log.info("websocket: OnMessage :"+message);
	}

	@OnClose
	/*  不要使用session发送消息给用户
      	不要手动调用close方法
      	这里面不能有任何异常抛出
                            以上三种情况都会引起Message will not be sent because the WebSocket session has been closed*/
	public void OnClose(Session session,   
			@PathParam(value = "param") String param) {
		Session oldSession = userList.get(param);
		if(oldSession!=null && oldSession.equals(session)){
			//JSONArray list = iRDAesMysqlDao.querySockAlertList(userName2);
			//boolean rel = iRDAesMysqlDao.delsockAlert("",userName2,2);
			//log.info("websocket: OnClose :" + userName2 + " 退出登陆.  清理sock返回"+rel);
			userList.remove(param);
			/*Timer timer = new Timer();
			clearSock timerTask =new clearSock();
			timerTask.list = list;
			timer.schedule(timerTask, 1000);*/
		}
		else{
			log.info("websocket: OnClose :" + param + " 重复退出登陆");
		}
	}
	
	/**注解@OnError是方法级别注解，该注解指定了被注解的方法处理错误事件 
	 * 这些方法的参数有可选的错误信息、可选的发送错误的会话、可选的一组路径参数，用于打开阶段握手时WebSocket端点匹配入站URI过程中获取的路径 
	 * @param t 错误信息 
	 */  
	@OnError  
	public void errorHandler(Throwable t) {  
		log.error("websocket: OnError : "+t.toString());
	}  
	
	public static void senToUserList(String message,JSONArray sendUserList) {
		for (int i = 0; i < sendUserList.size(); i++) {
			String userName = sendUserList.getString(i);
			Session session = userList.get(userName);
			if (session!=null) {
				boolean rel = send(userName,session,message);
				System.out.println("\n==send:"+userName);
				int count = 1;
				while(!rel && count<=3){
					rel = send(userName,session,message);
				}
			}
		}
	}
	
	public static void sendAll(String message) {
		for(Entry<String, Session> en: userList.entrySet() ){
			boolean rel = send(en.getKey(),en.getValue(),message);
			int count = 1;
			while(!rel && count<=3){
				rel = send(en.getKey(),en.getValue(),message);
			}
		}
	} 
	
	private static boolean send(String user,Session session,String message) {
		
		Future<Void> fut = null;  
		try  {  
			fut = session.getAsyncRemote().sendText(message); 
		    fut.get(2,TimeUnit.SECONDS);  
		} catch (ExecutionException | InterruptedException e)  {  
		    //e.printStackTrace();  
			log.error("websocket: sendAll : err ! userName={},e={}",user,e.toString());
			return false;
		} catch (TimeoutException e1) {  
		    // timeout  
			log.error("websocket: sendAll : timeout ! userName={},e={}",user,e1.toString());
		    if (fut != null){  
		        fut.cancel(true);  
		    }  
		    return false;
		}  
		return true;
	} 
	
	private class clearSock extends TimerTask { 
		public JSONArray list;
        public void run() {  
        	int size = list.size();
    		for(int i=0;i<size;i++){
    			JSONObject delInfo = new JSONObject();
    	    	delInfo.put("accountNum", list.getJSONObject(i).getString("accountNum"));
    	    	delInfo.put("sysuserID", "");
    	    	delInfo.put("websockType", "Alertsock");
    	    	sendAll(delInfo.toJSONString());
    		}
        }  
	}  
	


}
