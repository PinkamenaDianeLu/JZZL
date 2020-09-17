/**
 * @author Mrlu
 * @createTime 2020/9/17
 * @url webApp.model.test.js
 * @describe 一个分页列表的框架
 */

var loadData=(function () {
    let offset = 1;
    let Pages = 0;
    let searchJson = function ( jqbh, startsj, endsj, ajbh,barxm,zt) {
        this.dwdm = dwdm;
        this.jqbh = jqbh;
        this.startsj = startsj;
        this.endsj = endsj;
        this.zt = zt;
        this.barxm=barxm;
    }

    function getSearchJson() {
        let thissearchJson = new searchJson();
        thissearchJson.jqbh = document.getElementById("jqbh").value.trim();
        thissearchJson.ajbh = document.getElementById("ajbh").value.trim();
        thissearchJson.startsj = document.getElementById("startsj").value;
        thissearchJson.endsj = document.getElementById("endsj").value;
        thissearchJson.zt =$("[name=zt]:checked").val();
        // thissearchJson.barxm=document.getElementById("bar").value.trim();

        return thissearchJson;

    }
    /**
     * 加载表格
     * @author Mrlu
     * @param offset 页码
     * @param fn 回调函数
     * @param limit 一夜多少
     * @createTime  2020/9/3 10:50
     */
    function createTable(offset, fn, limit = 12) {
        $.get({
            url: "/AjgCrk/getJzWrkList",
            data: {
                searchJson: JSON.stringify(getSearchJson()),
                offset,
                limit
            }, success: (re) => {
                let reObj = JSON.parse(re)
                //删除旧行
                $(".zfyspTr").remove();
                let i = 0,
                    values = reObj.rows,
                    allTr = "";
                //记录总页数
                Pages = Math.ceil(reObj.total / limit);
                $("#zfyspTotle").html("共:" + reObj.total + "条数据");//显示总数

                $("#zfyspPages").html("第  " + 1 + "/" + Pages + "  页");//显示总页数
                for (; i < values.length;) {
                    let thisV = values[i],
                        tr = "<tr class='zfyspTr'>";
                    if (i % 2 === 0) {
                        tr = "<tr class='zfyspTr' style='background:#063d76'>";
                    }
                    tr += "<tr class='zfyspTr' style='border-bottom: #6773c3  1px solid'>" +
                        "<td style='background:#062961;'>"+thisV.jqbh+"</td>" +
                        "<td >"+thisV.ajbh+"</td>" +
                        "<td style='background:#062961;'>"+utilL.beautifulTitle(thisV.ajmc,10)+"</td>" +
                        "<td >"+utilL.beautifulTitle(thisV.jyaq,18)+"</td>" +
                        "<td>" + thisV.barxm  + "</td>" +
                        "<td style='background:#062961;'>" + thisV.bargajgmc.replace('黑龙江省哈尔滨市公安局香坊分局', '') + "</td>" ;
                    tr += "</tr>";
                    i++;
                    allTr += tr;
                }

                $("#sjkbTable").append(allTr);
                fn();
            }
        });
    }

    /**
     * 导出excel
     * @author Mrlu
     * @createTime  2020/9/7 11:23
     */
    function outExcel() {
        window.open("/AjgCrk/exportJzExcel?searchJson="+BASE64.encoder(JSON.stringify(getSearchJson())), "_blank");

    }

    /**
     * 更改页码
     * @author Mrlu
     * @param f
     * @createTime  2020/9/3 15:45
     * @return    |
     */
    function tablePage(f) {
        let newOffset = 1;
        if ("next" === f) {
            //下一页
            newOffset = offset + 1 > Pages ? offset : offset + 1;

        } else if ("last" === f) {
            //上一页
            newOffset = offset - 1 < 1 ? 1 : offset - 1;
        } else  {
            //具体页数
            if (f>newOffset) {
                //往前跳页
                newOffset = (f + 1 > Pages ? Pages : f );
            }else if (f<newOffset){
                //往后跳页
                newOffset = f - 1 < 1 ? 1 : f ;
            }else {
                //原地蹦
                newOffset=f;
            }
        }
        createTable(newOffset,function () {
            $("#zfyspPages").html("第  " + newOffset + "/" + Pages + "  页");//显示总页数
            offset = newOffset;
        })
    }

    /**
     * 下一页
     * @author Mrlu
     * @createTime  2020/9/4 11:41
     */
    function nextPage() {
        tablePage('next');
    }
    /**
     * 上一页
     * @author Mrlu
     * @createTime  2020/9/4 11:41
     */
    function PreviousPage() {
        tablePage('last');
    }
    /**
     * 跳页
     * @author Mrlu
     * @param f 跳转的页数
     * @createTime  2020/9/4 11:41
     */
    function jumpPage(f) {
        if (f&&typeof f==='number'&&!isNaN(f)) {
            tablePage(+f);
        }
    }

    function _LoadData( Offset = 1) {
        createTable( Offset, function () {
        });
    }
    _LoadData.prototype={
        nextPage,PreviousPage,jumpPage,outExcel
    }
    return _LoadData;
})()