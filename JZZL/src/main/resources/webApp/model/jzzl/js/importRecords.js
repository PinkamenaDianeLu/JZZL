/**
 * @author Mrlu
 * @createTime 2020/12/28
 * @url webApp.model.jzzl.js
 * @describe
 */

var ImportRecords = new Map();
//<RecordId,Set<fileCode>>


var loadFiles = (function () {
    let dtree;//树形对象
    let recordId;//文书id
    let seqId;
    let checkFile = new Set();//复选功能中被选中的文件的filecode数组
    let imgMap = new Map();//文书图片
    /**
     * 查询文书的图片数据
     * @author MrLu
     * @param recordId 文书id
     // * @param selectedFile 被选中的filecode Set
     * @param callback  加载完文书图片后的回调函数
     * @createTime  2020/10/15 18:31
     */
    function loadFilesByRecord(recordId, callback) {
        $.post({
            url: '/ArrangeArchives/loadFilesByRecord',
            data: {
                seqId: seqId,
                recordId: recordId
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    $('#frontImg').empty();
                    const files = reV.value;
                    if (!(files && files.length > 0)) {
                        console.error('该文书下没有任何文件可供显示！'); //一页文书都没有还显示个p
                        return;
                    }
                    $('.recordImgBtn').show();
                    const selectedFiles = ImportRecords.get(recordId);
                    utils.functional.forEach(files, function (thisFile) {
                        let IsSelected = false;
                        //  = (selectedFiles && selectedFiles.has(thisFile.filecode)) || false;
                        if ('all' === selectedFiles) {
                            IsSelected = true;
                        } else if (!selectedFiles) {
                            IsSelected = false;
                        } else {
                            IsSelected = selectedFiles.has(thisFile.filecode);
                        }

                        $('#frontImg').append(loadFrontImg(thisFile, IsSelected));//加载平铺图

                    });

                    callback();//回调方法
                } else {
                    console.error('文书图片加载错误');
                    layer.alert('图片加载失败！');
                }
            }
        });
    }

    function getRecordId() {
        return recordId;
    }

    /**
     * 加载文书的平铺图
     * @author MrLu
     * @param file
     * @param IsSelected
     * @createTime  2020/10/16 11:03
     * @return    |
     */
    function loadFrontImg(file, IsSelected) {
        let div = document.createElement('div');
        div.id = 'front' + file.filecode;
        let divClass = 'div_a frontDiv';
        if (IsSelected) {
            divClass += '   active';
            checkFile.add(file.filecode);
        }
        div.setAttribute('class', divClass);
        //缩略图
        let front = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'frontImg' + file.filecode,
                src: file.serverip + file.fileurl,
                class: 'img_text',
                width: '150px', height: '192px'
            }
        });
        div.addEventListener('click', function () {
            const filecode = $(this).attr('id').replace('front', '');
            //判断是否被选中
            if ($(this).hasClass('active')) {
                //被选中 -> 取消选中
                $(this).removeClass('active');
                checkFile.delete(filecode);
            } else {
                //未被选中 选
                $(this).addClass('active');
                checkFile.add(filecode);//记录被选的文件id
            }
            if (checkFile.size > 0) {
                //有选中的文书
                if (checkFile.size === imgMap.size) {
                    //全选
                    dtree.checkStatus($('.dtree-theme-checkbox[data-id=' + recordId + ']')).check(); // 当前复选框选中
                } else {
                    //半选
                    dtree.checkStatus($('.dtree-theme-checkbox[data-id=' + recordId + ']')).noallCheck();
                }
                ImportRecords.set(recordId, checkFile);//将选中的东西记录下来
            } else {
                //没有选中的文书
                dtree.checkStatus($('.dtree-theme-checkbox[data-id=' + recordId + ']')).noCheck();
                ImportRecords.set(recordId, null);
            }

        })
        //大图
        let largeWrapper = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'preview' + file.filecode,
                style: 'display:none',
                class: 'larimg',

            }, arg: '<img width= "900px" height="1152px" src="' + file.serverip + file.fileurl + '">'
        });
        /////////放大镜效果
        //生成跟随鼠标的小方框
        let mov = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'mov' + file.filecode,
                class: 'movClass',
                style: 'display:none;pointer-events:none'
            }
        })

        front.onmouseover = function () {
            let pervDivLength = $('#front' + file.filecode).prevAll('.frontDiv').length;
            if (0 === pervDivLength % 2 && 0 !== pervDivLength) {
                $(largeWrapper).css('left', '-288px');
            }
            $(mov).show();//显示小框
            $(largeWrapper).show();//方大图显示
        }
        front.onmousemove = function (e) {
            let topY = e.offsetY;
            let topX = e.offsetX;
            mov.style.top = topY + 'px'
            mov.style.left = topX + 'px'
            largeWrapper.scrollLeft = (topX - 25) * 6;
            largeWrapper.scrollTop = (topY - 25) * 6;

        }
        front.addEventListener('mouseleave', function () {
            $(largeWrapper).removeAttr('style');
            $(mov).hide();
            $(largeWrapper).hide();
        })

        div.appendChild(front);
        div.appendChild(mov);
        div.appendChild(largeWrapper);
        imgMap.set(file.filecode, file);
        return div;
    }


    let _loadFiles = function ({
                                   seqIdP, recordIdP, dtreeP, callback = function () {
        }
                               }) {
        if (this instanceof _loadFiles) {

            //重置一些值
            imgMap = new Map();
            checkFile = new Set();
            seqId = seqIdP;
            recordId = recordIdP;
            dtree = dtreeP;
            //查询该文书下的文书图片
            //判断该文书是否被选中过
            if (!ImportRecords.get(recordId)) {
                ImportRecords.set(recordId, null);
            }

            loadFilesByRecord(recordId, callback)
        } else {

            return new _loadFiles({
                seqIdP: seqIdP,
                recordIdP: recordIdP,
                dtreeP: dtreeP,
                callback: callback,
            })
        }

    };
    _loadFiles.prototype = {getRecordId}
    return _loadFiles;
})()


$(function () {
    const seqId = utils.getUrlPar('seqid');//需要上传的seqid
    // const thisseqId = utils.getUrlPar('seqid');//需要上传的seqid
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
        let Dt = dtree.render({
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
        //图片加载对象
        let lf;
        //选中事件
        dtree.on("chooseDone('RecordTree')", function (obj) {
            let thisNode = obj.thisNode;
            const thisRecordId = $(thisNode).attr('data-id');//刚刚点的那个文书id
            if ($(thisNode).hasClass('dtree-icon-fuxuankuangxuanzhong')) {
                //全选
                ImportRecords.set(thisRecordId, 'all');
                if (lf) {
                    //判断是否是当前文书
                    if (thisRecordId === lf.getRecordId()) {
                        //全选
                        $('.frontDiv').addClass('active');
                    }
                }
            } else if ($(thisNode).hasClass('dtree-icon-fuxuankuang-banxuan')) {
                console.log('半选');
            } else {
                //不选
                ImportRecords.delete(thisRecordId);
                if (lf) {
                    //判断是否是当前文书
                    if (thisRecordId === lf.getRecordId()) {
                        //全选
                        $('.frontDiv').removeClass('active');
                    }
                }
            }
        });

        // 节点点击事件
        dtree.on("node('RecordTree')", function (obj) {
            if (1 !== +obj.param.level) {
                lf = loadFiles({
                    seqIdP: obj.param.seqId,
                    recordIdP: obj.param.nodeId,
                    dtreeP: Dt,
                    callback: function () {
                        // recordImgLoadObj.loadBtn();
                    }
                });
            }
        });
    });

    $('#saveBtn').click(function () {
        console.log(ImportRecords)
        //Integer seqId, Integer recordId, String fileCodes
        const iterator1 = ImportRecords[Symbol.iterator]();
        for (const item of iterator1) {
            if (item[1]) {
                console.log(item[1])
                let fileCodes = 'all';
                if ('all' !== item[1]) {
                    fileCodes = Array.from(item[1]).join(',');
                }
                $.post({
                    url: '/Records/importRecords',
                    data: {
                        seqId: seqId,
                        recordId: item[0],
                        fileCodes: fileCodes
                    },
                    success: (re) => {
                        const reV = JSON.parse(re);
                        if ('success' === reV.message) {

                            layer.msg("导入成功");
                            console.log('导入成功');
                            setTimeout(function () {
                                parent.location.reload();
                            },3000)
                            layer.msg("导入成功");
                            console.log('导入成功');

                        } else {
                            setTimeout(function () {
                                parent.location.reload();
                            },3000)
                            layer.msg("导入失败，无法确认该文书的相对位置！");
                            console.log('导入失败');
                        }
                    }
                });
            }


        }


    })
});