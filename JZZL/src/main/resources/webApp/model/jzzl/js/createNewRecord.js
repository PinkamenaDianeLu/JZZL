/**
 * @author Mrlu
 * @createTime 2020/12/22
 * @url webApp.model.jzzl.js
 * @describe 卷整理  新建文书
 */

/**
 * 上传图片
 * @author MrLu
 * @param
 * @createTime  2020/12/23 16:26
 * @return    |
 */
var dropUpload = (function () {
    let files = new Map();

    /**
     * 加载拖拽域
     * @author MrLu
     * @param zoneId 拖拽域id
     * @createTime  2020/11/1 15:03
     * @return    |
     */
    function loadDropZone(zoneId) {
        let dz = document.getElementById(zoneId);
        //点击上传区域等于点击了上传文本框
        dz.addEventListener('click', function () {
            document.getElementById('newFiles').click();
        });
        //添加拖拽事件  拖来拖去
        dz.addEventListener('dragover', function (e) {
            this.style.borderColor = 'red';//设置边框颜色
            e.stopPropagation();
            //阻止浏览器默认打开文件的操作
            e.preventDefault();

        });
        dz.ondragleave = function () {
            //恢复边框颜色
            this.style.borderColor = 'gray';
        };
        //释放鼠标
        dz.addEventListener("drop", function (e) {
            this.style.borderColor = 'gray';
            e.stopPropagation();
            //阻止浏览器默认打开文件的操作
            e.preventDefault();
            const files = e.dataTransfer.files;
            //拖文件进来了呢
            createThumbnailZone(files)

        });
        $('#newFiles').change(function () {
            createThumbnailZone($(this)[0].files)
        });
        //加载拖拽域
        $('#thumbnailZone').sortable().disableSelection();
    }

    /**
     * 生成缩略图
     * @author MrLu
     * @param thisImgs file数组
     * @createTime  2020/11/1 17:05
     */
    function createThumbnailZone(thisImgs) {
        if (thisImgs.length > 0) {
            let frag = document.createDocumentFragment();
            for (let thisImg of thisImgs) {
                let msg = '';//提示信息
                let isCool = true;//该图片是否符合规范
                let fileSize = +Math.round(thisImg.size * 100 / 1024) / 100;
                if (!/\/(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(thisImg.type)) {
                    msg += ' 不是可上传的类型！';
                    isCool = false;
                } else if (fileSize > (5 * 1024)) {
                    msg += '文件过大！请上传小于5M的图片';
                    isCool = false;
                }
                frag.append(createThumbnailDiv(thisImg, isCool, msg));
            }
            $('#thumbnailZone').append(frag);
        }
    }


    function createThumbnailDiv(thisImg, isCool, msg) {
        let key = 'img' + Math.floor(Math.random() * 1000);
        //记录
        if (isCool) {
            thisImg.keyForInput = key;
            files.set(key, thisImg);
        }
        let pDiv = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: isCool ? 'thumbnail' : 'redThumbnail',
            }
        });
        let imgDiv = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: window.URL.createObjectURL(thisImg),
                title: thisImg.name
            }
        });
        let inputDiv = utils.createElement.createElement({
            tag: 'input', attrs: {
                value: thisImg.name
            }
        });
        let msgDiv = utils.createElement.createElement({
            tag: 'span', attrs: {
                style: 'color:red', class: 'thumbnailMsg'
            }, arg: isCool ? '' : msg + ',请删除重传'
        });
        let delDiv = utils.createElement.createElement({
            tag: 'span', attrs: {
                class: 'cos01'
            }, arg: 'X'
        });
        //添加删除方法
        delDiv.addEventListener("click", function () {
            removeOne(key);
        })
        pDiv.append(imgDiv);
        pDiv.append(inputDiv);
        pDiv.append(msgDiv);
        pDiv.append(delDiv);
        return pDiv;
    }

    /**
     * 删除单个
     * @author MrLu
     * @param id
     * @createTime  2020/11/1 17:08
     */
    function removeOne(id) {
        $('#' + id).remove();
        files.delete(id);
    }

    /**
     * 全部清除
     * @author MrLu
     * @createTime  2020/11/1 16:51
     */
    function allRemove() {
        $('#thumbnailZone').empty();
        files.clear();
        $('#newFiles').val('');
    }

    /**
     * 上传
     * @author MrLu
     * @param recordId  要上传的文书id
     * @createTime  2020/11/1 17:12
     * @return    |
     */
    function upload(recordId) {
        console.log(recordId)
        const iterator1 = files[Symbol.iterator]();
        for (const item of iterator1) {
            let formData = new FormData();
            let pDiv = $('#' + item[0]);
            formData.append('fileName', pDiv.find('input').val());//文件名称
            formData.append('fileOrder', pDiv.index());//文件的顺序
            formData.append('newFile', item[1]);//文件本身
            formData.append('recordid', recordId);//文件所属文书的id
            console.log(pDiv.find('input').val());
            console.log(pDiv.index());
            console.log(item[1]);
            pDiv.find('.thumbnailMsg').html('正在上传');
            $.post({
                url: '/FileManipulation/upLoadRecordFiles',
                ache: false,
                processData: false,
                contentType: false,
                data: formData,
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ("success" === reV.message) {
                        pDiv.find('.thumbnailMsg').html('上传成功');
                        //刷新父页面
                        // parent.dqfxpgbgOut.reFreshTk();
                        //
                        files.delete(item[1].keyForInput);
                        if (files.size===0){
                            layer.msg('创建成功！');
                            //自我关闭
                               const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                               parent.layer.close(index);
                        }

                    }
                }
            })

            /*         if (files.size === 0) {
                         layer.alert('上传完成');
                         const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                         parent.layer.close(index);
                     }*/
            //如果有上传失败的话
            /*
            /  pDiv.find('.thumbnailMsg').html('上传失败');
              layer.alert('有部分文件未能完成上传，请检查文件并刷新后重试');
               const index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            / */
        }
    }


    function _dropUpload(zoneId) {
        loadDropZone(zoneId);
    }

    _dropUpload.prototype = {allRemove, upload};
    return _dropUpload;
})();

/**
 * 动态搜索
 * @author MrLu
 * @param
 * @createTime  2020/12/23 16:26
 * @return    |
 */
var searchSelect = (function () {
    let seqid;
    let xmSelect;

    /**
     * 加载下拉框
     * @author MrLu
     * @param elementId 加载位置id  是个div
     * @param isaccessory  是否是附件 1是0否
     * @createTime  2020/12/23 13:32
     * @return    |
     */
    function loadSelect(elementId, isaccessory) {
        xmSelect.render({
            el: '#' + elementId,//accessoriesCode //recordCode
            //配置搜索
            filterable: true,
            tips: '请选择类型',
            disable: true,
            //配置远程分页
            radio: true,
            clickClose: true,
            paging: true,
            pageRemote: true,
            //数据处理
            remoteMethod: function (val, cb, show, pageIndex) {
                axios({
                    method: 'get',
                    url: '/Records/getRecordCords',
                    params: {
                        offset: pageIndex,
                        limit: 5,
                        params: {
                            seqid: seqid,
                            recordname: val,
                            isaccessory: isaccessory
                        },
                    }
                }).then(response => {
                    //这里是success的处理
                    const res = response.data;
                    //回调需要两个参数, 第一个: 数据数组, 第二个: 总页码
                    cb(res.value, res.count);
                }).catch(err => {
                    //这里是error的处理
                    cb([], 0);
                });
            },
            model: {
                label: {
                    type: 'text', //自定义与下面的对应
                }
            },
            on: function (data) {
                const selectedRecordType = data.arr[0];//被选中的文件类型
                if (selectedRecordType) {
                    //记录下使用的文书代码
                    $('#sysRecordId').val(selectedRecordType.value);
                    axios({
                        method: 'get',
                        url: '/Records/selectRecordTypeSuspectByRid',
                        params: {
                            recordtypeid: selectedRecordType.value,
                            seqId: seqid,
                        }
                    }).then(response => {
                        $('.suspectTr').remove();
                        if ('success' === response.data.message) {
                            const suspects = response.data.value;
                            if (suspects) {
                                //这个文书是选嫌疑人的
                                let f = document.createDocumentFragment();
                                for (let thisSuspect of suspects) {
                                    let s = utils.createElement.createElement({
                                        tag: 'tr',
                                        attrs: {
                                            class: 'suspectTr'
                                        },
                                        arg: '<td>' + thisSuspect.suspectname + '</td><td>' + thisSuspect.suspectidcard + '</td>'
                                    });
                                    s.addEventListener('click', function () {
                                        //选择该人
                                        $('#suspectName').val(thisSuspect.suspectname);
                                        $('#suspectId').val(thisSuspect.id);

                                    });
                                    $(f).append(s);
                                }
                                $('#suspectDiv').show();
                                $('#suspectTable').append(f)
                            } else {
                                $('#suspectDiv').hide();
                                $('#suspectName').val('');
                                $('#suspectId').val('');
                            }
                        }

                    }).catch(err => {
                    });
                }


            }
        })
    }

    function _searchSelect(seqidP, xmSelectP) {
        xmSelect = xmSelectP;
        seqid = seqidP;
    }

    _searchSelect.prototype = {loadSelect}
    return _searchSelect;
})();

/**
 * 保存创建
 * @author MrLu
 * @param
 * @createTime  2020/12/23 16:26
 * @return    |
 */
var createNewRecord = (function () {
    let seqId;
    const record = function (recordName, recordWh, recordType, sysRecordId, suspectId) {
        this.seqId = seqId;
        this.recordName = recordName;
        this.recordWh = recordWh;
        this.sysRecordId = sysRecordId;
        this.suspectId = -1;
    }

    function getRecordV() {
        let newRecord = new record();
        const isaccessory = +($('.recordTab.active').attr('value'));
        if (0 === isaccessory) {
            //0 不是附件
            newRecord.recordName = document.getElementById('recordName').value;
            newRecord.recordWh = document.getElementById('recordWh').value;
        } else {
            //1 附件
            newRecord.recordName = document.getElementById('accessoriesName').value;
        }
        newRecord.sysRecordId = document.getElementById('sysRecordId').value;
        newRecord.suspectId = document.getElementById('suspectId').value;
        return newRecord;
    }

    /**
     * 保存
     * @author MrLu
     * @param  zl 拖拽域对象
     * @createTime  2021/1/3 12:20
     */
    function saveRecord(zl) {
        if ($('#thumbnailZone').find('.redThumbnail').length > 0) {
            layer.alert('红色边框的图片无法上传，请删除后重试！')
        } else if ($('#thumbnailZone').find('.thumbnail').length === 0) {
            layer.alert('请上传文书图片！')
        } else {
            axios({
                method: 'post',
                url: '/Records/createNewRecord',
                params: {
                    record: JSON.stringify(getRecordV())
                }
            }).then(response => {
                const reV = response.data;
                if ('success' === reV.message) {
                    const record = reV.value;
                    console.log(record);
                    zl.upload(record.id);
                    parent.lai.addRecord(record,reV.prevRId);
                } else {
                    console.error('新建文书失败');
                }
                //文书新建完了  把文书图片传上去
            }).catch(err => {
            });
        }


    }

    function _createNewRecord(seqIdP) {
        seqId = seqIdP
    }

    _createNewRecord.prototype = {
        saveRecord
    };
    return _createNewRecord;

})();


$(function () {
    const seqId = utils.getUrlPar('seqid');//需要上传的seqid
    //加载组件
    layui.extend({
        xmSelect: '/Framework/layui/xm-select'
    }).use(['xmSelect', 'layer'], function () {
        let xmSelect = layui.xmSelect;//搜索下拉框
        let ss = new searchSelect(seqId, xmSelect);
        ss.loadSelect('accessoriesCode', 1);
        ss.loadSelect('recordCode', 0);

        //加载拖拽部分
        let zl = new dropUpload('dropZone');

        $('#submit-all').click(function () {
            // $(this).unbind();//防止重复点击
            let cnr = new createNewRecord(seqId);
            //判断当前是附件还是文书

            cnr.saveRecord(zl)
        });
        $('#removeAll').click(function () {
            zl.allRemove();
        });
    });


})