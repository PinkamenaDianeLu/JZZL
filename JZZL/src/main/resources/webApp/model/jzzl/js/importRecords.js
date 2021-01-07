/**
 * @author Mrlu
 * @createTime 2020/12/28
 * @url webApp.model.jzzl.js
 * @describe
 */

$(function () {
    const seqId = utils.getUrlPar('seqid');//需要上传的seqid
    layui.extend({
        dtree: '/Framework/layui/dtree/dtree'
    }).use(['layer', 'table', 'code', 'util', 'dtree', 'form'], function () {
        var element = layui.element,
            layer = layui.layer,
            table = layui.table,
            util = layui.util,
            dtree = layui.dtree,
            form = layui.form;
        //  初始化树
        dtree.render({
            elem: "#RecordTree",
            url: "/Records/selectBaseTypeForTree",
            request: {"seqId": seqId},//参数
            none: '无数据',
            checkbar: true,
            initLevel: 1,  // 指定初始展开节点级别
            icon: "-1", // 隐藏二级图标
            //cache: false,  // 当取消节点缓存时，则每次加载子节点都会往服务器发送请求
            method: "post",
            dataStyle: "layuiStyle",  //使用layui风格的数据格式
            dataFormat: "list",  //配置data的风格为list
            width: "100%"

        });
        dtree.on("chooseDone('RecordTree')", function (obj) {
            console.log(obj.seqId);//seqid
            console.log(obj.nodeId);//文书id
        });
    });
});