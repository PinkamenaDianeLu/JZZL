# 卷宗整理Cooly

## 1. 技术说明
  * 数据库：Oralce 11gR2 字符集：AMERICAN_AMERICA.AL32UTF8
  * 连接池： druid 1.1.23
  * java版本：jdk-8u251
  * 核心框架： Mybatis 3.5.5
  * bean映射处理器 : MapStruct 1.3.1.Final
  * 缓存技术： redis(5.0.5) （客户端为Lettuce）
  * 图片服务器： vsFtp+Nginx(代理)
  * 消息系统：   WebSocket
  * 日志记录 slf4j
  * 包管理： Maven
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

 1. 未完成的事项请使用 //TODO  注释  例： TODO MrLu 2020/8/18 16:39  今天还没摸鱼呢
 
 2. 变量声明时请务必注释其用途
   ```java
  String value="";//演示实例
  ```
 3. 注释请务必清晰明朗，不允许夹带私货。保证每**5**行代码必出现一条注释

 4. 除项目配置文件外请确定每个文件第一行对应注释信息
 ```java
 /**
 * @author 作者
 * @createTime 创建时间
 * @describe 用途
 */
 ``` 
 5.除项目配置文件外请保证每个方法前都对应注释信息
  ```java
/**
  * 方法描述
  * @author 作者
  * @createTime 创建时间
  * @param 参数列表及标准值
  * @return 返回值描述
 */
 ``` 
 
 6.在项目上线进行运维阶段后，当修改方法时，请对该方法添加注释`@logs` 填写版本号和对应修改内容，版本号可自行酌情增加 如：

  ```java
/**
   * 方法描述
   * @author 作者
   * @createTime 创建时间
   * @param 参数列表及标准值
   * @return 返回值描述
   * @logs 1.5 MrLu 修改内容及原因
           2.0 MrLu 修改内容及原因
  */
 
``` 
 
 
 7.判断或循环代码块需明确注释
 ```js
 //判断目的 如： 判断性别
 if (0==gender){
 //情况1 例： 假如判断为男
 }else if (1==gender){
 //情况2  例 假如判断为女
 }else{
 // 其他情况
 .......
 }
 /*************************************************/
//标示被循环的数据  如： 循环值为 nameArrary
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
<td>0</td><td>运维标记</td></tr>
<tr><td>scbj</td><td>number</td><td>0</td><td>删除标记 0 未删除 1 已删除</td></tr>
</table>
 4.  数据库除以上5项外所有字段必须带有注释,且所有代码型字段必须标注对应含义
 <pre>
 例子  scbj number 0  删除标记 0默认 1已删除
 </pre>
 5. 数据库表名： 系统表以sys_开头，功能表以fun_开头，例： <strong>fun_test_question</strong>为test功能所对应的question表<br/>
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
6. **单个方法刨除注释不可超过100行**
    
#### webSocket


## 3.使用
### 3.1 开发使用
* 项目配置了热部署,前后端更改后都会自动重启项目,但是当新添加文件或文件路径改动时仍需手动重启项目
* redis使用redisTemplate操作，Template在开发中可以增加修改
* redis存储两个字段 分别是用户信息和用户权限 具体信息请查看代码
* 注意： 在数据库中加密的字段在redis中并不是加密的！
* 拦截器已配置完毕，但没有实际使用，详情请看com.config.Interceptor下的配置
* session已配置完毕，session中存储了序列化的UserSession，UserSession中存储了对应redis中的key
* 部分字段使用枚举类，枚举类在 com.enums.Enums.java 下定义
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
### 3.3 枚举类

> 数据库中部分字段只有0、1、2等值用于区别标记，这些字段在本项目中使用枚举，并由MapStruct进行映射
* 枚举类在com.enums.Enums.java
* 定义枚举类需存储两个字段 (int)value和(String)name 并对应生成get set 方法
* 重写toString方法时返回name
* EnumsUtil中的getEnumByValue(final Class<E> , Object )方法可以通过value得到对应的枚举对象

### 3.4 工具包使用
* StringUtil 使用commons-lang3
* DateUtil使用Joda-Time
* md5使用 commons-codec
* 加密使用ThreeDes 并对数据添加了salt
* ftp连接使用 jcraft
* 水印工具使用 com.google.zxing 
* 文件压缩使用工具包 ant
* json处理使用 alibaba.fastjson
* 单元测试 junit
### 3.5 服务器配置
* vsftp设置 ： 参考vsftp.conf
* nginx设置 ： 参考nginx.conf
* 防火墙 关闭 selinux 关闭
* redis服务器内存 至少12G

## 4. 开发

### 4.1 开发时间
  * 起始时间： --
  * 预计完成时间： --
    
### 4.2 开发环境
  * idea：IntelliJ IDEA 2020.1.1
  * 数据库连接工具：plsqlDev 13.0.6.1911
  * maven： apache-maven-3.6.3（互联网仓库为：http://maven.aliyun.com/nexus/content/groups/public）
----------------------------------
* @**Author:** MrLu008
* @**Version:** 0.18 Beta 
* @**CreateTime** 2020年8月13日17:19:16
* @**UpdateTime** 2020年8月21日14:09:58
* @**State** 开发中
* &copy; 哈尔滨市盛世华博科技发展有限公司
