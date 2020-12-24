package com.bean.jzgl.Source;

import com.config.annotations.CodeTableMapper;

import java.util.Date;

/**
 * @author MrLu
 * @createTime 2020/9/28 10:17
 * @describe
 */
public class FunArchiveFiles {
    private Integer id;

    private Integer scbj;

    private Integer state;

    private Date createtime;

    private Date updatetime;

    private String jqbh;

    private String ajbh;

    private Integer thisorder;

    private Integer archiverecordid;

    private Integer archivetypeid;

    private Integer filetype;
    @CodeTableMapper(sourceFiled = "filetype", codeTableType = "filetype")
    private String filetype_name;

    private String fileurl;

    private String originurl;

    private Integer isdowland;

    private String filename;

    private Integer archiveseqid;

    private Integer archivesfcid;

    private Integer isazxt;

    private String author;

    private Integer authorid;

    private Integer isshow;
    private  String filecode;
    private Integer isdelete;
    private String serverip;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScbj() {
        return scbj;
    }

    public void setScbj(Integer scbj) {
        this.scbj = scbj;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getJqbh() {
        return jqbh;
    }

    public void setJqbh(String jqbh) {
        this.jqbh = jqbh == null ? null : jqbh.trim();
    }

    public String getAjbh() {
        return ajbh;
    }

    public void setAjbh(String ajbh) {
        this.ajbh = ajbh == null ? null : ajbh.trim();
    }

    public Integer getThisorder() {
        return thisorder;
    }

    public void setThisorder(Integer thisorder) {
        this.thisorder = thisorder;
    }

    public Integer getArchiverecordid() {
        return archiverecordid;
    }

    public void setArchiverecordid(Integer archiverecordid) {
        this.archiverecordid = archiverecordid;
    }

    public Integer getArchivetypeid() {
        return archivetypeid;
    }

    public void setArchivetypeid(Integer archivetypeid) {
        this.archivetypeid = archivetypeid;
    }

    public Integer getFiletype() {
        return filetype;
    }

    public void setFiletype(Integer filetype) {
        this.filetype = filetype;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl == null ? null : fileurl.trim();
    }

    public String getOriginurl() {
        return originurl;
    }

    public void setOriginurl(String originurl) {
        this.originurl = originurl == null ? null : originurl.trim();
    }

    public Integer getIsdowland() {
        return isdowland;
    }

    public void setIsdowland(Integer isdowland) {
        this.isdowland = isdowland;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public Integer getArchiveseqid() {
        return archiveseqid;
    }

    public void setArchiveseqid(Integer archiveseqid) {
        this.archiveseqid = archiveseqid;
    }

    public Integer getArchivesfcid() {
        return archivesfcid;
    }

    public void setArchivesfcid(Integer archivesfcid) {
        this.archivesfcid = archivesfcid;
    }

    public Integer getIsazxt() {
        return isazxt;
    }

    public void setIsazxt(Integer isazxt) {
        this.isazxt = isazxt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public String getFiletype_name() {
        return filetype_name;
    }

    public void setFiletype_name(String filetype_name) {
        this.filetype_name = filetype_name;
    }

    public Integer getIsshow() {
        return isshow;
    }

    public void setIsshow(Integer isshow) {
        this.isshow = isshow;
    }

    public String getFilecode() {
        return filecode;
    }

    public void setFilecode(String filecode) {
        this.filecode = filecode;
    }

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }
}
