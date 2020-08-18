
 /**
 * @author MrLu
 * @createTime  2020/4/30 18:29
 * @describe  js修改原型链请在此处
 * @version 1.0
  */

Function.prototype.addMethod = function (name, fn) {
    this[name] = fn;
    return this;
};

 Function.prototype.pValue="";