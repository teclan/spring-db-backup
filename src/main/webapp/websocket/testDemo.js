// JavaScript Document

var socket = null;
var tryTime = 0;
var isClose = 0;//是否主动断开，0否，1是
var userName = "tester-client";
var countMessage = 0;
function init(){
	if (!window.WebSocket) return -1;
	if(!userName)return -2;
	//alert(" "+turl);
	socket = new WebSocket('ws://'+window.location.host+'/WorkStation/websocket/login/' + userName);

	//接收消息
    socket.onmessage = function(evnt) {
		var data = evnt.data;
		printLog("收到: "+data);
       /* var json = $.parseJSON(data);
		var mode = json['mode'];
		var message = $.parseJSON(json['message'])
		switch(mode){
			case "news":showMessage(message);break;
			default : 
				 printLog("Unknown: "+data);
				 if(console)console.log(evnt);
		}*/
		socket.send(data);
    }
    // 异常
    socket.onerror = function(event) {
        printLog("happen error.");
		if(console)console.log(event);
    };

    // 建立连接
    socket.onopen = function(event) {
	printLog(userName + " is Go online.");
		if(console)console.log(event);
    };
    // 断线重连
    socket.onclose = function() {

		//如果不是主动断开，就重连
		if(isClose == 0){
			printLog(userName + " is Offline, reconnect(" + (tryTime+1) + "s)...");
			// 重试10次，每次之间间隔10秒
			if (tryTime < 10) {
				setTimeout(function() {
					socket = null;
					tryTime++;
					init();
				},
				500);
			} else {
				tryTime = 0;
				printLog("Connection failed ten times,this is over.");
			}
		}
    };
	return 1;
}

function printLog(str){
	var $li = $("<li></li>");
	$("#extraShow").append($li);
	var time = getTime(1);
	$li.html(time+",&nbsp;&nbsp;"+str);
}


function getTime(mode){
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth() +1;
	var date = myDate.getDate();
	var hour = myDate.getHours();
	var minute = myDate.getMinutes();
	var second = myDate.getSeconds();
	var millisecond = myDate.getMilliseconds();
	
	var time = year + "-" + month + "-" + date  + " " + hour + ":" + minute + ":" + second;
	if(mode == 1){
		time += "." + millisecond;
	}
	return time;
}

function loginFun(){
	
	var userNameInput = $('#userName').val();
	if(userName){
		userName = userNameInput;
	}
	var flag = init();
	if(flag == -1){
        printLog("This browser does not support WebSocket,please change the browser.");
	}
}

function showMessage(json){
	countMessage++;
	printLog("countMessage:" + countMessage);
	var sender = json.sender;
	var content = json.content;
	var time = json.time;
	var tip = "From " + sender + ": ";
	var info = content + "("+time+")";
	addChat(tip,info);
}
function addChat(tip,info){
	var $li = $("<li></li>");
	$("#messageShow").append($li);
	
	var $tip = $("<span></span>");
	$li.append($tip);
	$tip.addClass("chat-tip");
	$tip.text(tip);
	
	var $info = $("<span></span>");
	$li.append($info);
	$info.addClass("chat-Info");
	$info.text(info);
}

function ClearFun(idStr){
	$("#" + idStr).html("");
}

function ResetCount(){
	isClose=1;
	socket.close();
	//countMessage = 0;
}

