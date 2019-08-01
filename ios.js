let
    Default = require("default"),
    AppClassName = "AppController",

    IOS = Default.extend({
        copyToClip: function (text,tipStr) {
            let methodName = "copyToClip:";
			jsb.reflection.callStaticMethod(AppClassName, methodName, text.toString());
            glGame.panel.showTip("复制成功！");
        },
		getMachineCode: function () {
			let methodName = "getMachineCode";
			let imeiCode = jsb.reflection.callStaticMethod(AppClassName, methodName);
			console.log("ios imeiCode", imeiCode);
			return imeiCode;
		},
        getPhoneType: function () {
			let methodName = "getPhoneType";
			let phoneType = jsb.reflection.callStaticMethod(AppClassName, methodName);
			console.log("ios phoneType", phoneType);
			return phoneType;
        },
        saveToLocal: function (url) {
            let methodName = "saveToLocal:";
            jsb.reflection.callStaticMethod(AppClassName, methodName, url.toString());
            glGame.panel.showTip("保存成功")
        },
        getRegisID: function () {
            let methodName = "getRegisID";
            let registerID = jsb.reflection.callStaticMethod(AppClassName, methodName);
            console.log("ios jpush registerID", registerID)
            return registerID;
        },
        loginWX: function () {
            jsb.reflection.callStaticMethod('WXController', "login");
        },
        jumpToApp: function (url) {
            let methodName = "jumpToApp:";
            jsb.reflection.callStaticMethod(AppClassName, methodName, url.toString());
        },
        loginCallBack(data){
            console.log('loginCallBack' + JSON.stringify(data))
            let msg = {
                username:data.openid,
                wx_headurl:data.headimgurl,
                wx_nickname:data.nickname,
                wx_sex:data.sex,
            }
            glGame.logon.reqWxLogin(msg);
        },
    }),
    g_instance = null;

module.exports.getInstance = function () {
    if (!g_instance) {
        g_instance = new IOS();
    }
    return g_instance;
}
