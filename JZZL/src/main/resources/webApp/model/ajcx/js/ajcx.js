/**
 * @author Mrlu
 * @createTime 2020/9/17
 * @url webApp.model.ajcx.js
 * @describe 送检记录js
 * @dependence [jquery,layer,utils,bootstrapTableUtil]
 */



var ajcxTable = (function () {

    let tableObject;

    const searchParam = function (zlrq,larq,ajbh, jqbh,casename, casetype,barxm,persontype,badwdwmc) {
        this.ajbh = ajbh;
        this.jqbh = jqbh;
        this.casename=casename;
        this.casetype = casetype;
        this.barxm = barxm;
        this.persontype = persontype;
        this.badwdwmc = badwdwmc;
        this.larq=larq;
        this.zlrq=zlrq;
        // this.casetype = casetype;
        // this.casetype = casetype;
        // this.casetype = casetype;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.ajbh = $('#ajbhHead').val().trim() + $('#ajbh').val().trim();
        reS.jqbh = $('#jqbh').val().trim();
        reS.casename=$('#casename').val().trim();
        reS.casetype = $('#casetype').val();
        reS.barxm=$('#barxm').val().trim();
        reS.persontype=$('#persontype').val().trim();
        reS.badwdwmc=$('#badwdwmc').val().trim();
        if(""!==$("#larq").val()&&null!==$("#larq").val()){
                var sj = $("#larq").val().split(' - ');
                reS.fciTimebegin = sj[0];
                reS.fciateTimeend = sj[1];
        }
        if(""!==$("#zlrq").val()&&null!=$("#zlrq").val()){
            var sjj = $("#zlrq").val().split(' - ');
            reS.sfcTimebegin = sjj[0];
            reS.sfcTimeend = sjj[1];
        }

        return reS;
    }
    /**
     * 查看送检记录
     * @author MrLu
     * @param id 案件信息表id
     * @createTime  2020/9/25 9:56
     * @return    |
     */
    this.submitHistory = function (id) {
        let urlP = window.btoa(id + sessionStorage.salt)
        window.open('/model/ajcx/sjjl.html?id=' + urlP);
    }
     /**
     * 合案
     * @author MrLu
     * @param id 案件信息表id
     * @createTime  2021/1/8 15:14
     * @return    |
      */
    this.combinationCase=function (id) {

    }
     /**
     * 拆案
     * @author MrLu
     * @param id 案件信息表id
     * @createTime  2021/1/8 15:14
     * @return    |
      */
    this.splitCase=function (id) {

    }

    function loadTable() {
        tableObject = createTable({
            tableId: 'ajcxTable',
            searchUrl: '/CaseSearch/selectPeopleCasePage',
            column: [{
                field: 'jqbh',
                title: '警情编号'
            }, {
                field: 'ajbh',
                title: '案件编号',

            }, {
                field: 'casename',
                title: '案件名称',
                formatter: (value, row) => {
                    return utils.beautifulTitle(value, 13);
                }
            }, {
                field: 'createtime',
                title: '整理时间', formatter: (value) => {
                    return utils.timeFormat.timestampToDate2(value)
                }
            }, {
                field: 'name',
                title: '主办人'
            }, {
                field: 'idcard',
                title: '主办人身份证号'
            }, {
                field: 'idcard',
                title: '送检状态'
            }, {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    let combinationCase = '<a class="b_but edit" onclick="combinationCase(\'' + row.caseinfoid + '\')">合案</a>';
                    let splitCase = '<a class="b_but edit" onclick="splitCase(\'' + row.caseinfoid + '\')">拆案</a>';
                    return '<a class="b_but edit" onclick="submitHistory(\'' + row.caseinfoid + '\')">进入卷宗</a>' + combinationCase + splitCase;
                }
            }], param: function () {
                return getSearchParam();
            }
        });
    }

    /**
     * 查询
     * @author MrLu
     * @createTime  2020/9/25 10:40
     * @return    |
     */
    function searchTable() {
        tableObject.refreshTable();
    }

    function _ajcxTable() {
        loadTable();
    }

    _ajcxTable.prototype = {
        searchTable
    }
    return _ajcxTable;
})()
$(function () {
    /**
     * 添加时间监听
     * @author zy
     * @createTime  2021/1/19 11.00
     */
    //日期范围
    laydate.render({
        elem: '#larq'
        , range: true
    });
    //日期时间选择器
    laydate.render({
        elem: '#zlrq'
        , range: true
    });
    let at = new ajcxTable();


    $('.caseTypeTab').click(function () {
        //样式转换
        $('.caseTypeTab').removeClass('active');
        $(this).addClass('active');
        //转换刑事行政
        $('#casetype').val(+$(this).attr('value'));
        at.searchTable();
    })
    $('#ajcxSearchBtn').click(function () {
        at.searchTable();
    });
    $('#userHeart').load('/userHeart.html');
})