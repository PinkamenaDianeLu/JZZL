/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence 依赖js
 * @describe 卷宗整理
 *
 */

var loadArchiveIndex = (function () {
    let seqid;

    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
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
        li.id = 'P' + thisType.id;
        let thisTypeEle = utils.createElement.createElement({
            tag: 'dt', attrs: {
                id: 'typeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a><p>' + thisType.archivetypecn + '</p>'
        })
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn)
        })
        li.append(thisTypeEle);
        $('#archiveIndex').append(li);
        loadRecords(thisType.id, li.id)

    }

    /**
     * 加载具体文书
     * @author MrLu
     * @param typeId
     * @param liD 所属的li的Id
     * @createTime  2020/10/9 11:28
     * @return    |
     */
    function loadRecords(typeId, liD) {
        $.post({
            url: '/ArrangeArchives/getRecordsIndex',
            data: {id: typeId},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    utils.functional.forEach(reV.value, function (thisRecord) {

                        //用文书代码分辨html还是图片
                      let up=  utils.createElement.createElement({
                            tag: 'a', attrs: {
                                title: '上移'
                            }, arg: '<i class="ico up"></i><a>'
                        })
                        let down=  utils.createElement.createElement({
                            tag: 'a', attrs: {
                                title: '下移'
                            }, arg: '<i class="ico Down"></i><a>'
                        })
                        let aedit=  utils.createElement.createElement({
                            tag: 'a', attrs: {
                                title: '编辑'
                            }, arg: '<i class="ico aedit"></i><a>'
                        })
                        let del=  utils.createElement.createElement({
                            tag: 'a', attrs: {
                                title: '删除'
                            }, arg: '<i class="ico delete"></i><a>'
                        })
                    })
                    /*
                    * <dd><a><p>接处警登记表</p></a>
						<div class="tools">
							<a href="" title="上移"><i class="ico up"></i></a>
							<a href="" title="下移"><i class="ico Down"></i></a>
							<a href="" title="编辑"><i class="ico aedit"></i></a>
							<a href="" title="删除"><i class="ico delete"></i></a>
						</div>
					</dd>*/
                } else {
                }
            }
        });
        console.log('加载文书' + typeId)
    }

    function _loadArchiveIndex() {

    }

    _loadArchiveIndex.prototype = {loadIndex}
    return _loadArchiveIndex;
})()

$(function () {
    $('#userHeart').load('/userHeart.html');
    //送检的id
    const seqId = utils.getUrlPar('id');
    let lai = new loadArchiveIndex();
    lai.loadIndex(seqId);

})