/**
 * @author Mrlu
 * @createTime 2020/9/23
 * @url webApp.utils
 * @describe  bootstrapTable的工具方法
 */

var createTable = (function () {
    //框架js文件位置
    const FRAMEWORKURL = '/Framework/bootstrapTable/bootstrap-table.min.js';
    let _tableId = '';
    let _column = [];
    let _param = {};
    let _searchUrl = '';

    /**
     * 创建表格
     * @author MrLu
     * @createTime  2020/9/23 15:50
     */
    function loadTable() {
        //生成table
        $('#' + _tableId).bootstrapTable('destroy', {}).bootstrapTable(
            {
                url: _searchUrl,
                dataType: 'json',
                method: 'post',
                striped: true,// 隔行变色
                contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                singleSelect: false,
                locale: 'zh-CN', // 表格汉化
                search: false, // 显示搜索框
                pagination: true, // 分页
                pageNumber: 1,
                pageSize: $('#' + _tableId).attr('colnum')||13,
                sidePagination: 'server', // 服务端处理分页
                trimOnSearch: true,//自动去掉前后空格
                queryParams: function queryParams(params) {
                    return {
                        offset: params.pageNumber,
                        limit: params.pageSize,
                        params: _param
                    };
                },
                queryParamsType: 'undefined',
                undefinedText: '-',//某个值为null时的替补值
                columns: _column
            });
    }

    /**
     * 刷新表格 多用于查询
     * @author MrLu
     * @param newParam 查询参数
     * @createTime  2020/9/23 15:51
     */
    function refreshTable(newParam) {
        $('#' + _tableId).bootstrapTable('refresh', {
            url: _searchUrl,
            silent: true,
            query: newParam
        })
    }

    /**
     * 加载js
     * @author MrLu
     * @createTime  2020/9/23 15:31
     */
    function loadFramework() {
        if (!document.getElementById('bootStrapJs')) {
            let script = document.createElement("script");
            script.type = 'text/javascript';
            script.id = 'bootStrapJs';
            if (!script.readyState) {
                script.onload = function () {
                    loadTable();
                }
            } else {
                //ie
                script.onreadystatechange = function () {
                    if ('loaded' === script.readyState || 'complete' === script.readyState) {
                        script.onreadystatechange = null;
                        loadTable();
                    }
                }
            }
            script.src = FRAMEWORKURL;
            document.getElementsByTagName("head")[0].appendChild(script);

        }
    }

    let _createTable = function ({tableId, searchUrl, column = [], param='{}'}) {
        if (this instanceof _createTable) {
            _tableId = tableId;
            _column = column;
            _searchUrl = searchUrl;
            _param = param;
            loadFramework();
        } else {

            return new _createTable({
                tableId: tableId,
                column: column,
                searchUrl: searchUrl,
                param: param
            })
        }
    }
    _createTable.prototype = {
        refreshTable
    }
    return _createTable;

})()