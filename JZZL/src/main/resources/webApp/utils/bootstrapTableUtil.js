/**
 * @author Mrlu
 * @createTime 2020/9/23
 * @url webApp.utils
 * @describe  bootstrapTable的工具方法
 * @dependence utils.js，jquery.js
 */

var createTable = (function () {
    //框架js文件位置
    let _tableId = '';
    let _column = [];
    let _param;
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
                pageSize: $('#' + _tableId).attr('colnum') || 13,
                sidePagination: 'server', // 服务端处理分页
                trimOnSearch: true,//自动去掉前后空格

                maintainSelected:true,//分页时记住其它页的checkbox

                queryParams: function queryParams(params) {
                    return {
                        offset: params.pageNumber,
                        limit: params.pageSize,
                        params: JSON.stringify(_param())
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
        })
    }

    /**
     * 加载js
     * @author MrLu
     * @createTime  2020/9/23 15:31
     */
    function loadFramework() {
        if (!document.getElementById('bootStrapJs')) {
            utils.heartJs('bootStrapJs', '/Framework/bootstrapTable/bootstrap-table.js',function(){
                utils.heartJs('bootStrapLocalJs', '/Framework/bootstrapTable/bootstrap-table-zh-CN.min.js', function () {
                    loadTable();
                })})
        }
    }

    let _createTable = function ({tableId, searchUrl, column = [], param}) {
        if (this instanceof _createTable) {
            _tableId = tableId;
            _column = column;
            _searchUrl = searchUrl;
            _param = param;
            utils.once(loadFramework());
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