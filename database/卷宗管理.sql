/*==============================================================*/
/* DBMS name:      ORACLE Version 11g                           */
/* Created on:     2020/8/24 11:11:49                           */
/*==============================================================*/


drop table SYS_BMB cascade constraints;

drop table SYS_LOGS cascade constraints;

drop table SYS_LOGS_JZCHNAGE cascade constraints;

drop table SYS_LOGS_WEBSOCKET cascade constraints;

drop table fun_PeopelCase cascade constraints;

drop table fun_archive_files cascade constraints;

drop table fun_archive_records cascade constraints;

drop table fun_archive_type cascade constraints;

drop table sys_logs_send cascade constraints;

/*==============================================================*/
/* Table: SYS_BMB                                               */
/*==============================================================*/
create table SYS_BMB 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   author               VARCHAR2(2000)       not null,
   type                 VARCHAR2(2000)       not null,
   code                 VARCHAR2(2000)       not null,
   codename             VARCHAR2(2000)       not null,
   constraint PK_SYS_BMB primary key (ID)
);

comment on table SYS_BMB is
'系统编码表';

comment on column SYS_BMB.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column SYS_BMB.STATE is
'运维标记';

comment on column SYS_BMB.CREATETIME is
'创建时间';

comment on column SYS_BMB.UPDATETIME is
'更新时间';

comment on column SYS_BMB.author is
'作者';

comment on column SYS_BMB.type is
'类型';

comment on column SYS_BMB.code is
'编码';

comment on column SYS_BMB.codename is
'编码名';

/*==============================================================*/
/* Table: SYS_LOGS                                              */
/*==============================================================*/
create table SYS_LOGS 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   MPARAMS              VARCHAR2(2000)       not null,
   REQUESTURL           VARCHAR2(2000)       not null,
   MNAME                VARCHAR2(2000)       not null,
   MRESULT              VARCHAR2(2000)       not null,
   OPERDESC             VARCHAR2(2000)       not null,
   OPERMODUL            VARCHAR2(2000)       not null,
   OPERTYPE             VARCHAR2(2000)       not null,
   IP                   VARCHAR2(2000)       not null,
   SYSUSERID            VARCHAR2(2000)       not null,
   SYSUSERNAME          VARCHAR2(2000)       not null,
   OPERATOR             VARCHAR2(2000)       not null,
   constraint PK_SYS_LOGS primary key (ID)
);

comment on table SYS_LOGS is
'系统日志表';

comment on column SYS_LOGS.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column SYS_LOGS.STATE is
'运维标记';

comment on column SYS_LOGS.CREATETIME is
'创建时间';

comment on column SYS_LOGS.UPDATETIME is
'更新时间';

comment on column SYS_LOGS.MPARAMS is
'方法参数 没有传0';

comment on column SYS_LOGS.REQUESTURL is
'方法url';

comment on column SYS_LOGS.MNAME is
'方法名';

comment on column SYS_LOGS.MRESULT is
'返回值 没有传0';

comment on column SYS_LOGS.OPERDESC is
'操作描述';

comment on column SYS_LOGS.OPERMODUL is
'操作模块';

comment on column SYS_LOGS.OPERTYPE is
'操作类型，枚举项';

comment on column SYS_LOGS.IP is
'IP地址';

comment on column SYS_LOGS.SYSUSERID is
'用户表id';

comment on column SYS_LOGS.SYSUSERNAME is
'用户名';

comment on column SYS_LOGS.OPERATOR is
'操作者（身份证号）';

/*==============================================================*/
/* Table: SYS_LOGS_JZCHNAGE                                     */
/*==============================================================*/
create table SYS_LOGS_JZCHNAGE 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   author               VARCHAR2(2000)       not null,
   authortype           number(9)            not null,
   ECN                  VARCHAR2(2000)       not null,
   constraint PK_SYS_LOGS_JZCHNAGE primary key (ID)
);

comment on table SYS_LOGS_JZCHNAGE is
'系统卷宗修改日志表';

comment on column SYS_LOGS_JZCHNAGE.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column SYS_LOGS_JZCHNAGE.STATE is
'运维标记';

comment on column SYS_LOGS_JZCHNAGE.CREATETIME is
'创建时间';

comment on column SYS_LOGS_JZCHNAGE.UPDATETIME is
'更新时间';

comment on column SYS_LOGS_JZCHNAGE.author is
'作者';

comment on column SYS_LOGS_JZCHNAGE.authortype is
'作者类型0主办1辅办';

comment on column SYS_LOGS_JZCHNAGE.ECN is
'变更内容';

/*==============================================================*/
/* Table: SYS_LOGS_WEBSOCKET                                    */
/*==============================================================*/
create table SYS_LOGS_WEBSOCKET 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   sender               VARCHAR2(2000)       not null,
   messageType          number(9)            not null,
   receiver             VARCHAR2(2000)       not null,
   serverip             VARCHAR2(2000)       not null,
   clientip             VARCHAR2(2000)       not null,
   message              VARCHAR2(2000),
   constraint PK_SYS_LOGS_WEBSOCKET primary key (ID)
);

comment on table SYS_LOGS_WEBSOCKET is
'系统日志 webSocket信息表';

comment on column SYS_LOGS_WEBSOCKET.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column SYS_LOGS_WEBSOCKET.STATE is
'运维标记';

comment on column SYS_LOGS_WEBSOCKET.CREATETIME is
'创建时间';

comment on column SYS_LOGS_WEBSOCKET.UPDATETIME is
'更新时间';

comment on column SYS_LOGS_WEBSOCKET.sender is
'发送者';

comment on column SYS_LOGS_WEBSOCKET.messageType is
'消息类型';

comment on column SYS_LOGS_WEBSOCKET.receiver is
'接收者';

comment on column SYS_LOGS_WEBSOCKET.serverip is
'服务端IP地址';

comment on column SYS_LOGS_WEBSOCKET.clientip is
'客户端IP';

comment on column SYS_LOGS_WEBSOCKET.message is
'消息';

/*==============================================================*/
/* Table: fun_PeopelCase                                        */
/*==============================================================*/
create table fun_PeopelCase 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   idcard               VARCHAR2(19)         not null,
   name                 VARCHAR2(50),
   jqbh                 VARCHAR2(50),
   ajbh                 VARCHAR2(50)         not null,
   persontype           number(9)            not null,
   constraint PK_FUN_PEOPELCASE primary key (ID)
);

comment on table fun_PeopelCase is
'人案关联表';

comment on column fun_PeopelCase.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column fun_PeopelCase.STATE is
'运维标记';

comment on column fun_PeopelCase.CREATETIME is
'创建时间';

comment on column fun_PeopelCase.UPDATETIME is
'更新时间';

comment on column fun_PeopelCase.idcard is
'身份证号';

comment on column fun_PeopelCase.name is
'姓名';

comment on column fun_PeopelCase.jqbh is
'警情编号';

comment on column fun_PeopelCase.ajbh is
'案件编号';

comment on column fun_PeopelCase.persontype is
'人员类型0 主办 1辅办';

/*==============================================================*/
/* Table: fun_archive_files                                     */
/*==============================================================*/
create table fun_archive_files 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   JQBH                 VARCHAR2(2000)       not null,
   AJBH                 VARCHAR2(2000)       not null,
   thisorder            number(9)            not null,
   archivetrecordid     number(9)            not null,
   archivetypeid        number(9)            not null,
   filetype             number(9)            not null,
   fileurl              VARCHAR2(2000)       not null,
   originurl            VARCHAR2(2000)       not null,
   isdowland            number(9)            not null,
   filename             VARCHAR2(2000)       not null,
   constraint PK_FUN_ARCHIVE_FILES primary key (ID)
);

comment on table fun_archive_files is
'卷宗整理  卷宗文件表';

comment on column fun_archive_files.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column fun_archive_files.STATE is
'运维标记';

comment on column fun_archive_files.CREATETIME is
'创建时间';

comment on column fun_archive_files.UPDATETIME is
'更新时间';

comment on column fun_archive_files.JQBH is
'警情编号';

comment on column fun_archive_files.AJBH is
'案件编号';

comment on column fun_archive_files.thisorder is
'文件顺序';

comment on column fun_archive_files.archivetrecordid is
'案卷表id';

comment on column fun_archive_files.archivetypeid is
'卷宗分类表id';

comment on column fun_archive_files.filetype is
'文件类型';

comment on column fun_archive_files.fileurl is
'图片文件地址';

comment on column fun_archive_files.originurl is
'图片文件源地址';

comment on column fun_archive_files.isdowland is
'是否已下载0 否 1 是';

comment on column fun_archive_files.filename is
'文件名称';

/*==============================================================*/
/* Table: fun_archive_records                                   */
/*==============================================================*/
create table fun_archive_records 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   JQBH                 VARCHAR2(2000)       not null,
   AJBH                 VARCHAR2(2000)       not null,
   thisorder            number(9)            not null,
   recordname           VARCHAR2(2000)       not null,
   archivetypeid        number(9)            not null,
   constraint PK_FUN_ARCHIVE_RECORDS primary key (ID)
);

comment on table fun_archive_records is
'卷宗整理  案卷 ';

comment on column fun_archive_records.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column fun_archive_records.STATE is
'运维标记';

comment on column fun_archive_records.CREATETIME is
'创建时间';

comment on column fun_archive_records.UPDATETIME is
'更新时间';

comment on column fun_archive_records.JQBH is
'警情编号';

comment on column fun_archive_records.AJBH is
'案件编号';

comment on column fun_archive_records.thisorder is
'文书顺序';

comment on column fun_archive_records.recordname is
'文书名';

comment on column fun_archive_records.archivetypeid is
'卷宗分类表id';

/*==============================================================*/
/* Table: fun_archive_type                                      */
/*==============================================================*/
create table fun_archive_type 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   JQBH                 VARCHAR2(2000)       not null,
   AJBH                 VARCHAR2(2000)       not null,
   archivetype          number(9)            not null,
   archivetypecn        VARCHAR2(2000)       not null,
   defaultOrder         number(9)            not null,
   constraint PK_FUN_ARCHIVE_TYPE primary key (ID)
);

comment on table fun_archive_type is
'卷宗整理  卷宗分类表';

comment on column fun_archive_type.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column fun_archive_type.STATE is
'运维标记';

comment on column fun_archive_type.CREATETIME is
'创建时间';

comment on column fun_archive_type.UPDATETIME is
'更新时间';

comment on column fun_archive_type.JQBH is
'警情编号';

comment on column fun_archive_type.AJBH is
'案件编号';

comment on column fun_archive_type.archivetype is
'卷宗类型';

comment on column fun_archive_type.archivetypecn is
'卷宗类型中文';

comment on column fun_archive_type.defaultOrder is
'默认顺序';

/*==============================================================*/
/* Table: sys_logs_send                                         */
/*==============================================================*/
create table sys_logs_send 
(
   ID                   number(9)            not null,
   SCBJ                 number(9)            not null,
   STATE                number(9)            not null,
   CREATETIME           DATE                 not null,
   UPDATETIME           DATE                 not null,
   author               VARCHAR2(2000)       not null,
   pushtimes            number(9)            not null,
   result               number(9)            not null,
   osurl                VARCHAR2(2000)       not null,
   ajbh                 VARCHAR2(2000)       not null,
   jqbh                 VARCHAR2(2000)       not null,
   constraint PK_SYS_LOGS_SEND primary key (ID)
);

comment on table sys_logs_send is
'系统推送日志表';

comment on column sys_logs_send.SCBJ is
'删除标记 0 未删除 1 已删除';

comment on column sys_logs_send.STATE is
'运维标记';

comment on column sys_logs_send.CREATETIME is
'创建时间';

comment on column sys_logs_send.UPDATETIME is
'更新时间';

comment on column sys_logs_send.author is
'作者';

comment on column sys_logs_send.pushtimes is
'推送次数';

comment on column sys_logs_send.result is
'结果0推送中 1成功 2失败';

comment on column sys_logs_send.osurl is
'操作系统中的地址';

comment on column sys_logs_send.ajbh is
'案件编号';

comment on column sys_logs_send.jqbh is
'警情编号';

