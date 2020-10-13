/**
 * @author Mrlu
 * @createTime 2020/10/12
 * @dependence 依赖js
 * @describe 卷宗整理 回收站
 *
 */
var recycleBin = (function () {
    let seqid;//送检次序id
    let recycleRecordsMap;//
    let archiveIndex;//loadArchiveIndex实例
    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    recycleRecordsMap = new Map();
                    utils.functional.forEach(reV.value, function (thisType) {
                        loadArchiveType(thisType);
                    });
                } else {
                    alert('文书信息无法加载');
                    // window.close();
                }
            }
        });
    }

    /**
     * 加载二级菜单
     * @author MrLu
     * @param thisType
     * @createTime  2020/10/9 11:28
     * @return    |
     */
    function loadArchiveType(thisType) {
        let li = document.createElement('li');
        li.id = 'recycleP' + thisType.id;
        let thisTypeEle = utils.createElement.createElement({
            tag: 'dt', attrs: {
                id: 'recycleTypeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a><p>' + thisType.archivetypecn + '</p>'
        });
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn)
        });
        li.append(thisTypeEle);
        $('#recycleBinArchiveIndex').append(li);
        //加载文书
        loadRecords(thisType.id, li.id)

    }

    /**
     * 加载具体文书目录
     * @author MrLu
     * @param typeId
     * @param liD 所属的li的Id
     * @createTime  2020/10/9 11:28
     */
    function loadRecords(typeId, liD) {
        $.post({
            url: '/ArrangeArchives/getRecordsIndex',
            data: {id: typeId, isDelete: 1},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    //索引信息
                    let indexing = {
                        i: 0,
                        f: reV.value.length
                    };
                    utils.functional.forEach(reV.value, function (thisRecord) {
                        $('#recycle' + liD).append(createRecordDD(thisRecord, indexing));
                        indexing.i++; //此行必须在$('#' + liD).append的后边
                    });
                } else {
                    throw  '(回收站)该卷文书加载失败：' + typeId;
                }
            }
        });
    }

    function createRecordDD(thisRecord) {
        if (!thisRecord.id) {
            throw  '(回收站)文书id未获取，无法加载该文书！！';
        }
        let key = 'recycleDd' + thisRecord.id;
        let dd = utils.createElement.createElement({
            tag: 'dd', attrs: {
                id: key
            }, arg: '<a><p class="recordname">' + thisRecord.recordname + '</p></a>'
        });
        //文书缓存至recordsMap
        recycleRecordsMap.set(key, {id:thisRecord.id,archivetypeid:thisRecord.archivetypeid,recordname: thisRecord.recordname, recordscode: thisRecord.recordscode});//缓存信息
        //加载文书
        dd.append(createButtons(key));
        return dd;
    }

     /**
     * 创建还原按钮
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/12 14:40
     * @return    |
      */
    function createButtons(ddId) {
        // const thisRecord = recycleRecordsMap.get(ddId);
        let div = document.createElement('div');
        div.setAttribute('class', 'hy_a');
        let a = document.createElement('a');
         a.innerHTML='还原';
        a.addEventListener('click', function () {
            restored(ddId)
            $(this).remove();
        })
        div.append(a);
        return div;
    }

    /**
     * 添加进回收站
     * @author MrLu
     * @param ddId 被删除的文书的ddid也是recordsMap的key
     * @param delObj loadArchiveIndex中的recordsMap中对应ddid的value
     * @createTime  2020/10/12 10:17
     * @return    |
     */
    function addRecycleBin(ddId, delObj) {
        console.log(delObj)
        delObj.id = ddId.replace('dd', '');
        let li = createRecordDD(delObj)
        $('#recycleP' + delObj.archivetypeid).append(li);
    }

    /**
     * 还原、恢复
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/12 14:36
     * @return    |
     */
    function restored(ddId) {
        //获取保存的文书对象
        const thisRecord = recycleRecordsMap.get(ddId);
        let typeFd=$('#P' + thisRecord.archivetypeid+' dd').last();//该类型文书的封底
        let lastRecord= typeFd.prev('dd');//该类型文书的最后一个文书
        //重返阳间
        //通过传递一个假的indexing  使createRecordDD方法不需要再查找dom元素 降低损耗
        let indexing = {
            i: 0,
            f: 0
        };
        let reArchive= archiveIndex.createRecordDD(thisRecord,indexing);//重塑肉身
        typeFd.before(reArchive);//返回人界
        console.log(reArchive)
        archiveIndex.reloadButton($(reArchive));//重新加载按钮
        archiveIndex.reloadButton(lastRecord);//重新加载按钮
        //把回收站的删除
        $('#'+ddId).remove();
        recycleRecordsMap.delete(ddId);
        //重新加载被还原文书的上一个文书的按钮
    }

    let _recycleBin = function (lai) {
        if (!lai){
            throw '未传入loadArchiveIndex实例，回收站功能无法加载！';
        }
        archiveIndex=lai;
    }
    _recycleBin.prototype = {
        loadIndex, addRecycleBin
    }
    return _recycleBin;
})();