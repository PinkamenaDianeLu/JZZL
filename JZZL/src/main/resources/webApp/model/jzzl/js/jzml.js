/**
 * @author Mrlu
 * @createTime 2020/10/28
 * @dependence 依赖js
 * @describe 卷宗目录
 *
 */
const recordObj = function (id, name, pagenumber, index, author,recorddate,comment) {
    this.id = id;//文书id
    this.name = name;//文书名
    this.pagenumber = pagenumber;//页号
    this.index = index;//序号
    this.author = author;//责任人
    this.recorddate=recorddate;//文书开局日期
    this.comment=comment;//注释
};

$(function () {
    const thisCover = parent.recordImgLoad.pValue;
    //得到该文书类型对象
    let PEle = document.getElementById('P' + thisCover.archivetypeid);
    if (PEle){
        const typeDate = parent.lai.getRecordIndexSort(PEle);
        console.log(typeDate);

        //循环生成
        let i=1;//文书序号
        let records=[];
        for (let thisRecord of typeDate ){
            records[records.length]=new recordObj(thisRecord.id,
                thisRecord.name,
                0,
                i++,
                '责任人',
                'time'
                );
        }


        for (let thisRecord of records){
            let thisTr=document.createElement('tr');
            for (let thisCol in thisRecord){
              let thisTd=  utils.createElement.createElement({
                    tag:'td',
                    arg:'<input value="'+thisRecord[thisCol]+'" />'
                })
                thisTr.append(thisTd);
                $('#indexTable').append(thisTr);
            }
        }

    }else {
        alert('该文书信息无法获取，请刷新页面重试！');
    }


})