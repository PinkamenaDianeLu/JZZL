var elements = document.getElementsByClassName('list');
let oriSx = [];
for (var i = 0; i < elements.length; i++) {
    new Sortable(elements[i], {
        group: {
            string: 'shared',
            pull: function (to, from, dragEl, evt) {
                return true
            },
            put: function (to, from, dragEl, evt) {
                let oriJz = dragEl.classList[0] + dragEl.classList[1];//源卷宗
                let putJz = to.el.classList[0] + to.el.classList[1];//目标卷宗
                //判断拖回去时候是否是原地址
                //只能拖入回收站
                return 'recycle' === to.el.id || putJz === oriJz;

            }
        },
        filter: ".ignore-elements",
        invertSwap: true

        , onUpdate: function (/**Event*/evt) {
            console.log('*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-')
            const thisAry = this.toArray();
            //想取代封皮 不可以
            if (evt.newIndex === 0) {
                //相当第一个
                const first = thisAry[0];
                const ori = thisAry[evt.oldIndex];
                console.log(first)
                console.log(ori)
                console.log(thisAry)
                thisAry[0]=ori;
                //1  +0
                //2  ?
                //last  +1
                let ts=0;
                if (1===evt.oldIndex){
                    ts=0;
                }else  if (thisAry.length-1===evt.oldIndex){
                    ts=1
                }else {
                    console.log(thisAry.length)

                    ts=-(thisAry.length-3);
                }
                thisAry[evt.oldIndex+ts]=first;
                console.log(thisAry)
                //
                //   neworiAry.splice(0, 1, ori);
                // console.log(neworiAry)
                //  neworiAry.splice(evt.oldIndex, 1, first);
                // console.log(neworiAry)
                this.sort(thisAry);

            }
            // if (evt.newIndex === (thisAry.length - 1) || evt.newIndex === 0) {
            //     this.sort(oriSx);
            // }
        }
    });
}


let recycle = document.getElementById('recycle');
