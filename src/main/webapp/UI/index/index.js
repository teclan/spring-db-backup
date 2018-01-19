/**
 * Created by ly on 2016/10/9.
 */
charset = "utf-8";
var checkboxArray = [];
$(document).ready(function () {
    checkUser(function () {
        //重绘一次布局，然后再设置页面的resize事件
        resizeDocment();        //重绘函数
        $(window).resize(function () {
            resizeDocment();    //重绘函数
        });
        init();
    })
});

;(function ($, window) {
    window.resizeDocment = _resizeDocment;  //修改页面大小
    window.init = _init;
    window.upPopusManager = _upPopusManager;
    window.setSelectedJsonData = _setSelectedJsonData;
    window.getSelectedJsonData = _getSelectedJsonData;
    window.okAndCancel = _okAndCancel;
    window.alertSuccess = _alertSuccess;
    window.alertFail = _alertFail;
    window.alertWarn = _alertWarn;
    window.getSysuserID = _getSysuserID;
    window.closeupPopus = _closeupPopus;        //关闭弹窗
    window.audioPlay = _audioPlay;
    //初始化

    var _config = {
        minWidth: 1280,
        minHeight: 765,
    };
    var _global = {
        selectedRowData: '',
        selectedJsonData: '',
        userInfo:{},
        iframeUrl:{
            alarmData: '../alarmData/alarmData.html',
            iframeAllManager:'../integratedQuery/integratedQueryRDPolice.html',
            iframepwdUpData:'resetPassword/resetPassword.html'
        },
    };
//重绘布局
    function _resizeDocment() {

        var $body = $('body');
        var $content = $('#content');
        var $divBottom = $('#divBottom');
        $body.css('overflow-y', 'hidden');
        $body.css('overflow-x', 'hidden');
        var DocHeight = $(window).height();
        var DocWidth = $(window).width();
        var main = {width: 0, height: 0};
        var nav = {widht: 0};
        var contentHeight = $content.height();
        var contentWidth = $content.width();
        var divBottomHeight = $divBottom.height();
        var divBottomWidth = $divBottom.width();

        DocHeight = parseInt(DocHeight);
        DocWidth = parseInt(DocWidth);

        $body.height(DocHeight);
        $body.width(DocWidth);
        contentHeight = DocHeight - 65 - divBottomHeight - 10;
        $content.height(contentHeight);
        if (DocWidth < _config.minWidth) {
            DocWidth = _config.minWidth;
            $body.css('overflow-x', 'scroll');
            $body.width(_config.minWidth);
            $content.height(contentHeight - 18);
            $divBottom.width(_config.minWidth);
            $("#show_up").css('left', '675px');
        }
        else {
            $body.css('overflow-x', 'hidden');
            $divBottom.css('width', '100%');
            var showUpLeft = (DocWidth - 90) / 2;
            showUpLeft = parseInt(showUpLeft);
            $("#show_up").css('left', showUpLeft + "px");
        }


    }

    function _init() {
        _initData();    //初始化数据
        _initEven();    //初始化事件
        //ywHeartbeat.setHeartBeatTimer();       //心跳
    }
    //获取服务器数据， 包含 报警信息 预处理信息 已处理信息
    function _initData() {
        var userName = $.cookie("userName");
        $("#userText").text(userName);
        $("#iframeRdPolice").attr("src", _global.iframeUrl.alarmData);
    }
    function _initEven() {
        //综合查询点击事件
        $("#allManager").bind('click', function () {
            $(this).addClass('nav-active').removeClass('nav-normal');
            $(this).siblings().removeClass('nav-active').addClass('nav-normal');
            _changePage("allManager");
        });
        //实时处警点击事件
        $("#rdPolice").bind('click', function () {
            $(this).addClass('nav-active').removeClass('nav-normal');
            $(this).siblings().removeClass('nav-active').addClass('nav-normal');
            _changePage("iframeRdPolice");
        });
        //修改密码点击事件
        $("#pwdUpData").bind('click', function () {
            _changePage("pwdUpData");
        });
        //退出系统
        $("#exitLabel").bind("click",function () {
            //删除cookie
            $.cookie("userName",null,{expires:-1,path:"/"});
            $.cookie("password",null,{expires:-1,path:"/"});
            location.href = "/WorkStation/UI/login/login.html";
        })
        //语音点击事件
        $("#labelAlert").bind('click', function () {
            var isAlert=$(this).hasClass("isAlert");
            if(isAlert){
                $(this).addClass('noAlert').removeClass('isAlert');
            }else{

                $(this).addClass('isAlert').removeClass('noAlert');
            }
        });
    }


    function _changePage(page) {
        switch (page) {
            case 'iframeRdPolice':
                $("#iframeRdPolice").css({
                    'width': '100%'
                }).siblings().css({
                    'width': '0px'
                });
                break;
            case 'allManager':
                $("#iframeAllManager").css({
                    'width': '100%'
                }).siblings().css({
                    'width': '0px'
                });
                if($("#iframeAllManager").attr('src')==''){
                    $("#iframeAllManager").attr('src',_global.iframeUrl.iframeAllManager);
                }
                break;
            case 'pwdUpData':
                $("#iframepwdUpData").css({
                    'width': '100%'
                }).siblings().css({
                    'width': '0px'
                });
                if($("#iframepwdUpData").attr('src')==''){
                    $("#iframepwdUpData").attr('src',_global.iframeUrl.iframepwdUpData);
                }
                break;
            default:
                break;
        }
    }

    function _upPopusManager(url){
        _openUpPopups($('body'),url , {
            width: 1000,
            height: 600
        });
    }

    function _closeupPopus() {
        $("#upMainDiv").remove();
        $("#upBottomDiv").remove();
        //刷新数据
        iframeRdPolice.searchEventInfo();
    }
    function _getSelectedJsonData() {
        return _global.selectedJsonData;
    }

    function _setSelectedJsonData(jsonData) {
        _global.selectedJsonData = jsonData;
    }

    function _okAndCancel(tipStr,confirmCallback,cancelCallback) {
        $.fn.alertOKorCancel({
            "title": '确认信息',//标题设置
            'src': 'img/title.png', //图标路径
            'tip': tipStr, //提示语
            'cancelBtnLbl': '取消',//取消按钮名称
            'confirmBtnLbl': '确定',//确定按钮名称
            cancelCallback:cancelCallback,
            confirmCallback: confirmCallback
        });

    }

    function _alertSuccess(tipStr,sec,callbackFun) {
        $.fn.alertSure({
            "sec": sec,
            "title": '提示信息',
            'src': 'img/success.png',
            'tip': tipStr,
            confirmCallback: callbackFun
        });
    }

    function _alertFail(tipStr,sec,callbackFun) {
        $.fn.alertSure({
            "sec": sec,
            "title": '提示信息',
            'src': 'img/fail.png',
            'tip': tipStr,
            confirmCallback: callbackFun
        });
    }

    function _alertWarn(tipStr,sec,callbackFun) {
        $.fn.alertSure({
            "sec": sec,
            "title": '警告信息',
            'src': 'img/warn.png',
            'tip': tipStr,
            confirmCallback: callbackFun
        });
    }

    function _setSysuserID(sysuserID) {
        _global.userInfo.sysuserID=sysuserID;
    }
    function _getSysuserID() {
        return _global.userInfo.sysuserID;
    }

    //播放报警声音
    function _audioPlay() {
        var audio = document.getElementById('audioMusic');
        if($("#labelAlert").hasClass('isAlert')){
            audio.play();//audio.play();// 这个就是播放
        }
    }
})(jQuery, window);

