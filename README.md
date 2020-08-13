# 卷宗整理

## 1. 技术说明
  * 数据库：Oralce 11gR2
  * 连接池： druid
  * java版本：jdk-8u251
  * 框架技术： Spring boot(2.2.6)+Mybaits+Maven
  * 缓存技术： redis(5.0.5) （客户端为Lettuce）
  * 安全框架： Shiro(1.5.2)
  * 图片服务器： vsFtp+Nginx(代理)
  * 消息系统：   WebSocket
  * 因特殊时期开发考虑，该项目并没有使用前后端分离的架构，项目虽显臃肿，但部署开发环境非常简单
  * 程序正常发布后访问地址为  ： http://127.0.0.1:8080/index.html （端口号在正式发布时为80）
## 2. 开发说明
### 2.1 命名规范
1. 在Controller中@RequestMapping中的value必须与对方法同名
2. 使用驼峰命名法
3. 禁止使用aaa、abc、fuck 等意义不明私自泄愤的单词方式命名
4. Controller文件以Controller结尾，Service文件以Service结尾，Util文件以Util结尾
5. 在开发途中废弃的页面或文件请加上_discard尾缀  如原为a.html 废弃后改为 a_discard.html
6. java中废弃的类和方法请添加 `@Deprecated`注解，当一个类被标注@Deprecated则视为类中的所有方法全被废弃
7. 在被废弃的方法（有`@Deprecated`的）中要添加 `@describe` 注解， 说明废弃缘由并明确指出用于替代的代码
### 2.2 注释规范
> 注释非常重要，本规范着重重视注释问题，希望你的ide可以配置自动注释，反正我的能 <(￣ˇ￣)/
 1. 除项目配置文件外请确定每个文件第一行对应注释信息
 ```java
 /**
 * @author 作者
 * @createTime 创建时间
 * @describe 用途
 */
 ``` 
 2.除项目配置文件外请保证每个方法前都对应注释信息
  ```java
/**
  * 方法描述
  * @author 作者
  * @createTime 创建时间
  * @param 参数列表及标准值
  * @return 返回值描述
 */
 ``` 
 3.变量声明时请务必注释其用途
 
 4.在项目上线进行运维阶段后，当修改方法时，请添加注解`@version`和对应版本号并对该方法添加注释`@logs` 填写版本号和对应修改内容，版本号可自行酌情增加 如：

  ```java
/**
   * 方法描述
   * @author 作者
   * @createTime 创建时间
   * @param 参数列表及标准值
   * @return 返回值描述
   * @version  2
   * @logs 1.5 修改内容及原因
           2.0 修改内容及原因
  */
 
``` 
 
 ```java
String value="";//演示实例
```
 4.注释请务必清晰明朗，不允许夹带私货。保证每**5**行代码必出现一条注释
 
 5.判断或循环代码块需明确注释
 ```js
 if (0==gender){
 //情况1 例： 假如判断为男
 }else if (1==gender){
 //情况2  例 假如判断为女
 }else{
 // 其他情况
 .......
 }
 /*************************************************/
 for(){
 //标识循环作用  例： 循环拆分值
 }
 
 ```
 ### 2.3 数据库规范
 1. 数据中所有表名必须大写
 2. 项目中不允许出现delete语句（垃圾的关联数据可酌情删除）
 3. 数据库所有表必须带有以下字段
<table>
<tr><th>Name</th><th>数据类型</th><th>默认值</th><th>注释</th></tr>
<tr><td>id</td><td>number(9)</td><td>sequence</td><td>id</td></tr>
<tr><td>createtime</td><td>date</td><td>sysdate</td><td>创建时间</td></tr>
<tr><td>updatetime</td>
<td>date</td><td>sysdate</td><td>更新时间</td></tr>
<tr><td>state</td>
<td>number</td>
<td>0</td><td>状态</td></tr>
<tr><td>scbj</td><td>number</td><td>0</td><td>删除标记</td></tr>
</table>
 4.  数据库除以上5项外所有字段必须带有注释,且所有代码型字段必须标注对应含义
 <pre>
 例子  scbj number 0  删除标记 0默认 1已删除
 </pre>
 5. 数据库表名： 系统表以sys_开头，功能表以function_开头，例： <strong>function_test_question</strong>为test功能所对应的question表<br/>
 6. 数据库序列名 seq_表名_使用字段名 例：test表的id字段使用的序列即为   <strong>seq_test_id</strong><br>
 7. 码表数据可自行录入，不同类型的数据定义的不同的type类型，例如新闻类型的type字段为 news001 <br>
 8. 日志表所使用的索引高速缓存大小必须大于80
 
 
### 2.4 变量规范

1. Controller中所有的整数使用Integer类型
2. 所有的时间使用date类型
3. 想要定义枚举类请提前与其它开发者交流

### 2.5 开发规范

#### 后端

1. 禁止使用引用的全局变量
2. **所有Controller方法必须添加@OperLog注解以记录日志**，详情请看3.2部分
3. 所有Controller方法对前台Ajax返回的数据格式统一为json类型（bootstrap分页除外）,统一格式为：

```java

 JSONObject reValue = new JSONObject();
        try {
            reValue.put("message", "success");
            reValue.put("value", "需要携带的返回值");
        } catch (Exception e) {
            e.printStackTrace();
            reValue.put("message", "error");
        }
        return reValue.toJSONString();

```
4. 所有查询必须加上一条  `SCBJ=0`
5. 所有更新必须加上 `set UPDATETIME=SYSDATE`
    
#### 前端

> 禁止使用cookie和localStorage！ 向前端禁止存储如：id、身份证号等涉密信息

1. 禁止使用全局变量
2. 尽量使用es6定义变量 （let、const等）
3. js中字符串一律使用单引号 
4. 注意删除开发时使用的alert、console.log等输出语句
5. 在对dom元素赋值时必须标明该dom元素用途
6. 本项目没有设置cookie也禁止使用cookie！
7. **禁止在交互数据的编译中使用eval()方法!**
8. 用户信息使用sessionStorage存储，会随着页面关闭而失效。值存储为：username账号、xm姓名、agencyname单位名称、agencycode单位代码
9. 父窗体向子窗体传值使用原型链中的pValue属性，详情请看 /utils/prototype.js 文件
10. 时间控件使用laydate，请不要在子窗体再次引用js，否则会造成laydate控件不正常显示的bug

## 3.使用
### 3.1 开发使用
* 项目在开发时使用application-dev.yml，打包时使用项目在开发时使用application-prod.yml，通过更改项目在开发时使用application.yml的spring.profiles.active属性更改
* 项目配置了热部署,前后端更改后都会自动重启项目,但是当新添加文件或文件路径改动时仍需手动重启项目
* redis使用redisTemplate操作，Template在开发中可以增加修改
* redis存储两个字段 分别是用户信息和用户权限 具体信息请查看代码
* 注意： 在数据库中加密的字段在redis中并不是加密的！
* 拦截器已配置完毕，但没有实际使用，详情请看com.config.Interceptor下的配置
* session已配置完毕，session中存储了序列化的UserSession，UserSession中存储了对应redis中的key
### 3.2 日志记录

* 日志使用spring boot AOP，详情请看com.config.aop文件夹下配置

  在需要记录日志的方法上添加注释`@OperLog`(operModul = "功能名称",operDesc = "功能描述",operType = OperLog.type.SELECT)
> @OperLog(operModul = "功能名称",operDesc = "功能描述",operType = OperLog.type.SELECT)
>
> //注意operType是一个枚举类型，包含Select、Insert、Update 三个值 默认值为Select

会自动记录日志并插入数据库，除了注解中的三个值外还会记录以下内容
* 方法名
* 返回值
* 参数 Json类型字符串
* 请求路径
* ip地址

**请自觉在所有Controller方法上添加注释** 部分Service方法可选择添加
### 3.3 权限验证与拦截
本项目使用shiro进行安全验证，配置在com.config.shiro下
* 默认拦截所有请求，需要设置请在com.config.shiro.ShiroConfig中设置并填写详细注释
* O5权限为管理员权限，拥有全部权限
* 权限部分与数据库中表sys_pemissions、sys_userar 有紧密关系，错误录入数据将会导致无法进行正确的权限验证
* 在所有需要权限验证的页面引用 
```js
<!-- 该js需要utils.js依赖 -->
 <script src="/utils/permissionsUtil.js"></script>
```
之后再将所有需要验证权限的元素添加属性 `pCode=""` 值为数据库中设置的对应权限即可
### 3.4 工具包使用
* StringUtil 使用commons-lang3
* DateUtil使用Joda-Time
* md5使用 commons-codec
* 加密使用ThreeDes 并对数据添加了salt
* ftp连接使用 jcraft


### 3.5 服务器配置
* vsftp设置 ： 参考vsftp.conf
* nginx设置 ： 参考nginx.conf
* 防火墙 关闭 selinux 关闭
* redis服务器内存 至少12G

## 4. 开发时间
  * 起始时间： --
  * 预计完成时间： --
    
    
----------------------------------
* @**Author:** Mrlu008
* @**version:** 0.01 Alpha 
* @**CreateTime** 2020年8月13日17:19:16
* @**UpdateTime** 2020年8月13日17:19:21
* @**State** 开发中
* &copy; 哈尔滨市盛世华博科技发展有限公司
