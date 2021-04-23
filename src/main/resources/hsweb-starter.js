//组件信息
var info = {
    groupId: "top.pippen",
    artifactId: "xn-cms",
    version: "1.0-SNAPSHOT",
    website: "https://pippen.top",
    comment: "xn-cms"
};

var menus = [];
var autzSettings = [{
    "id": "47e28ead7d747f91b31231888ae598f4",
    "permission": "access-logger",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query"),
    "priority": 10,
    "merge": true
}, {
    "id": "da018e7581cbcc123748703f2d6cc587",
    "permission": "system-logger",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query"),
    "priority": 10,
    "merge": true
}, {
    "id": "0918ee9da2b9b477f5f9024a03676593",
    "permission": "menu",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query", "save", "delete"),
    "priority": 10,
    "merge": true
}, {
    "id": "824317629a7149d8ce10fd8b9b26589c",
    "permission": "permission",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query", "save", "delete"),
    "priority": 10,
    "merge": true
}, {
    "id": "2814b327cf0a124682103a6fdb2ea6e3",
    "permission": "file",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("upload-static"),
    "priority": 10,
    "merge": true
}, {
    "id": "a50469c17d339548d1f3d49d5159e80a",
    "permission": "user",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query", "save", "delete"),
    "priority": 10,
    "merge": true
}, {
    "id": "06daea99c0d4fef7ebfda30a1acb8752",
    "permission": "dimension",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query", "save", "delete"),
    "priority": 10,
    "merge": true
}, {
    "id": "124262526a59e2e3a0045c0b2ca0bae9",
    "permission": "autz-setting",
    "dimensionType": "user",
    "dimensionTypeName": "用户",
    "dimensionTarget": "1199596756811550720",
    "dimensionTargetName": "1199596756811550720",
    "state": 1,
    "actions": java.util.Arrays.asList("query", "save", "delete"),
    "dataAccesses": [],
    "priority": 10,
    "merge": true
},
    {
        "id": "124262526a59e2e3a0045c0b2ca0baa1",
        "permission": "schema",
        "dimensionType": "user",
        "dimensionTypeName": "用户",
        "dimensionTarget": "1199596756811550720",
        "dimensionTargetName": "1199596756811550720",
        "state": 1,
        "actions": java.util.Arrays.asList("query", "save", "delete"),
        "dataAccesses": [],
        "priority": 10,
        "merge": true
    },
    {
        "id": "124262526a59e2e3a0045c0b2ca0bab1",
        "permission": "coll",
        "dimensionType": "user",
        "dimensionTypeName": "用户",
        "dimensionTarget": "1199596756811550720",
        "dimensionTargetName": "1199596756811550720",
        "state": 1,
        "actions": java.util.Arrays.asList("query", "save","add", "delete"),
        "dataAccesses": [],
        "priority": 10,
        "merge": true
    }


];

var users = [{
    "id": "1199596756811550720",
    "username": "admin",
    "password": "104ffe90cd840e08f7a79c7fddbe1699",
    "salt": "LmKOhcoB",
    "status": 1,
    "name": "超级管理员"
}];
//版本更新信息
var versions = [
    {
        version: "1.0-SNAPSHOT",
        upgrade: function (context) {

        }
    }
];

function initialize(context) {
    var database = context.database;

    database.dml().upsert("s_autz_setting_info").values(autzSettings).execute().sync();
    database.dml().upsert("s_user").values(users).execute().sync();
}

function install(context) {

}


//设置依赖
dependency.setup(info)
    .onInstall(install)
    .onUpgrade(function (context) { //更新时执行
        var upgrader = context.upgrader;
        upgrader.filter(versions)
            .upgrade(function (newVer) {
                newVer.upgrade(context);
            });
    })
    .onUninstall(function (context) { //卸载时执行

    }).onInitialize(initialize);