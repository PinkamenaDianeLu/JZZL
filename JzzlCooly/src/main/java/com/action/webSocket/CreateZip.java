package com.action.webSocket;

import com.alibaba.fastjson.JSONObject;
import com.bean.zfba.Source.*;
import com.mapper.zfba.XtJcyAzSjdzbMapper;
import com.mapper.zfba.XtJcyXMLJdpzbMapper;
import com.mapper.zfba.XtJcylcjdWspzbMapper;
import com.util.GlobalUtil;
import com.util.MapUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.annotation.Resource;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author pl
 * @createTime 2020/8/28
 * @describe 生成zip包
 */
public class CreateZip {
    private static final String zipfiletemp = GlobalUtil.getGlobal("zipfiletemp");
    private static final String httpws = GlobalUtil.getGlobal("wsdown");
    private static final String ziptemp = GlobalUtil.getGlobal("ziptemp");
    private static final String zipfile = GlobalUtil.getGlobal("zipfile");
    private static final String jzwjdown = GlobalUtil.getGlobal("jzwjdown");
    private static final String jzfjdown = GlobalUtil.getGlobal("jzfjdown");
    @Resource
    private XtJcyXMLJdpzbMapper xtJcyXMLJdpzbMapper;
    @Resource
    private XtJcyAzSjdzbMapper xtJcyAzSjdzbMapper;
    @Resource
    private XtJcylcjdWspzbMapper xtJcylcjdWspzbMapper;

    /**
     *
     * @desc:创建外部节点。生成xml字符串
     * @author  潘磊
     * @Date 2020年8月9日下午4:34:43
     * @param ajbh
     * @param jqbh
     * @param lcdm
     * @param fsdwdm
     * @param jsdwdm
     * @param ywdm
     * @param xyrbh
     * @param wjbm
     * @param wjbid
     * @return String    DOM对象
     */
    public String selectCreateXML(String ajbh,String jqbh,String lcdm,String fsdwdm,String jsdwdm,String ywdm,String xyrbh,String wjbm,String wjbid){
        JSONObject result = null;
        StringBuffer sb = new StringBuffer();
        try {
            result = new JSONObject();
            Map<String,Object> pzmap = new HashMap<String, Object>();
            pzmap.put("fjdid", "0");
            pzmap.put("ssywjd", lcdm);
            List<XtJcyXMLJdpzb> mlist =this.xtJcyXMLJdpzbMapper.selectByFjdid(pzmap);
            //判断是否有父节点
            if (mlist.size()>0) {
                //循环父节点
                for (int i = 0; i < mlist.size(); i++) {
                    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    sb.append("<"+mlist.get(i).getFjdmc()+">");
                    //判断是否包含数据集
                    if (!"NO".equals(mlist.get(i).getZjdmc())) {
                        //不包含数据集
//						if (mlist.get(i).getSfdtsj()==1) {
//
//							for (int k = 0; k < 2; k++) {
//								sb.append("<R>");
//								if ("1".equals(mlist.get(i).getSfbhsjj())) {
//									//输出数据集
//
//									sb.append(this.createSJJ(mlist.get(i).getId().toString()));
//
//								}
//								pzmap.put("fjdid", mlist.get(i).getId());
//								List<XtJcyXMLJdpzb> zlist =this.xtJcyXMLJdpzbMapper.selectByFjdid(pzmap);
//								if (zlist.size()>0) {
//									for (int j = 0; j < zlist.size(); j++) {
//										sb.append(this.createJd(zlist.get(i)));
//									}
//								}
//								sb.append("</R>");
//							}
//
//						}else{
//							if ("1".equals(mlist.get(i).getSfbhsjj())) {
//								//输出数据集
//
//								sb.append(this.createSJJ(mlist.get(i).getId().toString()));
//
//							}
                        pzmap.put("fjdid", mlist.get(i).getId());
                        List<XtJcyXMLJdpzb> zlist =this.xtJcyXMLJdpzbMapper.selectByFjdid(pzmap);
                        Map<String,Object> wheremap = new HashMap<String, Object>();
                        wheremap.put("AJBH", ajbh);
                        wheremap.put("JQBH", jqbh);
                        wheremap.put("WJBM", wjbm);
                        wheremap.put("WJBID", wjbid);
                        //
                        if (zlist.size()>0) {
                            for (int j = 0; j < zlist.size(); j++) {
                                sb.append(this.createJd(zlist.get(j),wheremap,xyrbh));
                            }
                        }
//						}
                    }
//					else{
//						if (mlist.get(i).getSfdtsj()==1) {
//							for (int j = 0; j < 2; j++) {
//								sb.append("<R>");
//								//输出数据集
//
//								sb.append(this.createSJJ(mlist.get(i).getId().toString()));
//
//								sb.append("</R>");
//							}
//						}else{
//							//输出数据集
//
//							sb.append(this.createSJJ(mlist.get(i).getId().toString()));
//
//						}
//					}
                    sb.append("</"+mlist.get(i).getFjdmc()+">");
                }
            }
            String uid = UUID.randomUUID().toString();
            uid = uid.replace("-", "");
            String ret =  this.createXML(uid, sb.toString());
            if ("true".equals(ret)) {
                String rew = this.downWS(uid, jqbh, lcdm, xyrbh);
                if ("true".equals(rew)) {
                    String[] jzfl = null;
                    if ("0101A".equals(lcdm)) {
                        jzfl = new String[]{"1","2"};
                    }else if ("0104A".equals(lcdm)) {
                        jzfl = new String[]{"1","2"};
                    }
                    String jzre = this.downJzws(uid, jqbh, jzfl, xyrbh);
                    if ("true".equals(jzre)) {
                        String filenamet = fsdwdm+"_"+jsdwdm+"_"+lcdm+"_"+ywdm+"_";
                        String filenamed = "_"+sb.substring(sb.indexOf("<SJBS>")+6,sb.indexOf("</SJBS>"));
                        String rez = this.createZip(uid, filenamet, filenamed);
                    }

                }
            }
//			if ("true".equals(ret)) {
//				String reet = getFlwsForJqbh(uid,jqbh);
//				System.out.println("第二个接口返回值："+reet);
//				if ("true".equals(reet)) {
//					Map<String, Object> jdwsmap = new HashMap<String, Object>();
//					jdwsmap.put("lcjd", lcdm);
//					jdwsmap.put("jqbh", jqbh);
//					List<XtJcylcjdWspzb> lcjdws = this.xtJcylcjdWspzbMapper.selectByLcjd(jdwsmap);
//					String wres = "false";
//					for (XtJcylcjdWspzb xtJcylcjdWspzb : lcjdws) {
//						jdwsmap.put("wjbm", xtJcylcjdWspzb.getWjbm());
//						jdwsmap.put("wjdm", xtJcylcjdWspzb.getWjdm());
//						List<WjJzsjb> list = this.xtJcylcjdWspzbMapper.selectWjsjByJqbh(jdwsmap);
//
//						for (WjJzsjb wjJzsjb : list) {
//
//							if ("XT_FJZB".equals(xtJcylcjdWspzb.getWjbm())) {
//								wres = this.getOneWord("1", uid, wjJzsjb.getFjzbid().toString(),"");
//							}else{
//								wres = this.getOneWord("0", uid, "", wjJzsjb.getWjdz());
//							}
//							if ("false".equals(wres)) {
//								break;
//							}
//						}
//						if ("false".equals(wres)) {
//							break;
//						}
//					}
//					if ("false".equals(wres)) {
//
//					}else{
//						String sjdb = "_"+sb.substring(sb.indexOf("<SJBS>")+6,sb.indexOf("</SJBS>"));
//						String name = fsdwdm+"_"+jsdwdm+"_"+lcdm+"_"+ywdm+"_";
//						String zipres = this.setZip(uid, name, sjdb);
//						System.out.println(zipres);
//
//					}
//				}
//
//			}
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return result.toString();
    }





    /**
     *
     * @desc:创建子节点
     * @author  潘磊
     * @Date 2020年8月9日下午4:35:31
     * @param jxyXML
     * @param wheremap
     * @param xyrbh
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String createJd(XtJcyXMLJdpzb jxyXML,Map<String, Object> wheremap,String xyrbh){
        StringBuffer stb = new StringBuffer();
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            stb.append("<"+jxyXML.getFjdmc()+">");
            //是否包含数据集
            if (!"NO".equals(jxyXML.getZjdmc())) {
                //是否多条数据
                if (jxyXML.getSfdtsj() == 1) {
                    //查询数量sql
                    if (!"".equals(jxyXML.getCountsql())&&jxyXML.getCountsql()!=null) {
                        String sql = jxyXML.getCountsql();
                        String wheresql = jxyXML.getCountsqlwhere();
                        if (!"".equals(wheresql)&&wheresql!=null) {
                            if (wheresql.indexOf(",")!=-1) {
                                String[] temwheresql = wheresql.split(",");
                                for (int i = 0; i < temwheresql.length; i++) {
                                    //p@特殊处理
                                    if (temwheresql[i].startsWith("p@")) {
                                        String tp = temwheresql[i].substring(temwheresql[i].indexOf("@")+1);
                                        sql = sql + " and (" +tp+" is null or " +tp+"='"+xyrbh+"'";
                                        if (xyrbh.indexOf(",")!=-1) {
                                            String[] tps = xyrbh.split(",");
                                            for (int j = 0; j < tps.length; j++) {
                                                sql = sql + " or " +tp +"='"+tps[j]+"'";
                                            }
                                            sql = sql + ")";
                                        }else{
                                            sql = sql + ")";
                                        }
                                    }else{
                                        sql = sql + " and " + temwheresql[i] + " = '" + wheremap.get(temwheresql[i])+"'";

                                    }

                                }
                            }else{
                                if (wheresql.startsWith("p@")) {
                                    String tp = wheresql.substring(wheresql.indexOf("@")+1);
                                    sql = sql + " and (" +tp+" is null or " +tp+"='"+xyrbh+"'";
                                    if (xyrbh.indexOf(",")!=-1) {
                                        String[] tps = xyrbh.split(",");
                                        for (int j = 0; j < tps.length; j++) {
                                            sql = sql + " or " +tp +"='"+tps[j]+"'";
                                        }
                                        sql = sql + ")";
                                    }else{
                                        sql = sql + ")";
                                    }
                                }else{
                                    sql = sql + " and " + wheresql + " = '" + wheremap.get(wheresql)+"'";
                                }

                            }
                        }

                        Map<String, Object> semap = new HashMap<String, Object>();
                        semap.put("sql", sql);
                        List<Map<String, Object>> countlist = this.xtJcyXMLJdpzbMapper.selectObject(semap);
                        for (int i = 0; i < countlist.size(); i++) {
                            stb.append("<R>");
                            if ("1".equals(jxyXML.getSfbhsjj())) {
                                //输出数据集

                                stb.append(this.createSJJ(jxyXML,countlist.get(i),xyrbh));

                            }
                            map.put("fjdid", jxyXML.getId());
                            List<XtJcyXMLJdpzb> zlist =this.xtJcyXMLJdpzbMapper.selectByFjdid(map);
                            if (zlist.size()>0) {
                                for (int j = 0; j < zlist.size(); j++) {
                                    stb.append(this.createJd(zlist.get(j),countlist.get(i),xyrbh));
                                }
                            }

                            stb.append("</R>");
                        }
                    }else{
                        stb.append("<R>");
                        //输出数据集

                        stb.append(this.createSJJ(jxyXML,null,xyrbh));

                        stb.append("</R>");
                    }

                }else{
                    if ("1".equals(jxyXML.getSfbhsjj())) {
                        //输出数据集

                        stb.append(this.createSJJ(jxyXML,wheremap,xyrbh));

                    }
                    map.put("fjdid", jxyXML.getId());
                    List<XtJcyXMLJdpzb> zlist =this.xtJcyXMLJdpzbMapper.selectByFjdid(map);
                    if (zlist.size()>0) {
                        for (int j = 0; j < zlist.size(); j++) {
                            stb.append(this.createJd(zlist.get(j),wheremap,xyrbh));
                        }
                    }
                }
            }else{
                if (jxyXML.getSfdtsj() == 1) {
                    if (!"".equals(jxyXML.getCountsql())&&jxyXML.getCountsql()!=null) {
                        String sql = jxyXML.getCountsql();
                        String wheresql = jxyXML.getCountsqlwhere();
                        if (!"".equals(wheresql)&&wheresql!=null) {
                            if (wheresql.indexOf(",")!=-1) {
                                String[] temwheresql = wheresql.split(",");
                                for (int i = 0; i < temwheresql.length; i++) {
                                    if (temwheresql[i].startsWith("p@")) {
                                        String tp = temwheresql[i].substring(temwheresql[i].indexOf("@")+1);
                                        sql = sql + " and (" +tp+" is null or " +tp+"='"+xyrbh+"'";
                                        if (xyrbh.indexOf(",")!=-1) {
                                            String[] tps = xyrbh.split(",");
                                            for (int j = 0; j < tps.length; j++) {
                                                sql = sql + " or " +tp +"='"+tps[j]+"'";
                                            }
                                            sql = sql + ")";
                                        }else{
                                            sql = sql + ")";
                                        }
                                    }else{
                                        sql = sql + " and " + temwheresql[i] + " = '" + wheremap.get(temwheresql[i])+"'";

                                    }

                                }
                            }else{
                                if (wheresql.startsWith("p@")) {
                                    String tp = wheresql.substring(wheresql.indexOf("@")+1);
                                    sql = sql + " and (" +tp+" is null or " +tp+"='"+xyrbh+"'";
                                    if (xyrbh.indexOf(",")!=-1) {
                                        String[] tps = xyrbh.split(",");
                                        for (int j = 0; j < tps.length; j++) {
                                            sql = sql + " or " +tp +"='"+tps[j]+"'";
                                        }
                                        sql = sql + ")";
                                    }else{
                                        sql = sql + ")";
                                    }
                                }else{
                                    sql = sql + " and " + wheresql + " = '" + wheremap.get(wheresql)+"'";
                                }
                            }
                        }

                        Map<String, Object> semap = new HashMap<String, Object>();
                        semap.put("sql", sql);
                        List<Map<String, Object>> countlist = this.xtJcyXMLJdpzbMapper.selectObject(semap);
                        for (int i = 0; i < countlist.size(); i++) {
                            stb.append("<R>");
                            //输出数据集

                            stb.append(this.createSJJ(jxyXML,countlist.get(i),xyrbh));

                            stb.append("</R>");
                        }
                    }else{
                        stb.append("<R>");
                        //输出数据集

                        stb.append(this.createSJJ(jxyXML,null,xyrbh));

                        stb.append("</R>");
                    }

                }else{
                    //输出数据集

                    stb.append(this.createSJJ(jxyXML,wheremap,xyrbh));

                }
            }
            stb.append("</"+jxyXML.getFjdmc()+">");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        return stb.toString();
    }
    /**
     *
     * @desc:创建数据集
     * @author  潘磊
     * @Date 2020年8月9日下午4:35:49
     * @param jxyXML
     * @param wheremap
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String createSJJ(XtJcyXMLJdpzb jxyXML,Map<String, Object> wheremap,String xyrbh){
        StringBuffer sjb = new StringBuffer();
        try {
            List<Map<String, Object>> countlist = new ArrayList<Map<String,Object>>();
            if (!"".equals(jxyXML.getSjsql())&&jxyXML.getSjsql()!=null) {
                String sjsql = jxyXML.getSjsql();
                String sjsqlwhere = jxyXML.getSjsqlwhere();
                if (!"".equals(sjsqlwhere)&&sjsqlwhere!=null) {
                    if (sjsqlwhere.indexOf(",")!=-1) {
                        String[] temwheresql = sjsqlwhere.split(",");
                        for (int i = 0; i < temwheresql.length; i++) {
                            if (temwheresql[i].startsWith("p@")) {
                                String tp = temwheresql[i].substring(temwheresql[i].indexOf("@")+1);
                                sjsql = sjsql + " and (" +tp+" is null or " +tp+"='"+xyrbh+"'";
                                if (xyrbh.indexOf(",")!=-1) {
                                    String[] tps = xyrbh.split(",");
                                    for (int j = 0; j < tps.length; j++) {
                                        sjsql = sjsql + " or " +tp +"='"+tps[j]+"'";
                                    }
                                }else{
                                    sjsql = sjsql + ")";
                                }
                            }else{
                                sjsql = sjsql + " and " + temwheresql[i] + " = '" + wheremap.get(temwheresql[i])+"'";
                            }

                        }
                    }else{
                        sjsql = sjsql + " and " + sjsqlwhere + " = '" + wheremap.get(sjsqlwhere)+"'";
                    }
                }

                Map<String, Object> semap = new HashMap<String, Object>();
                semap.put("sql", sjsql);
                //System.out.println(jxyXML.getFjdmc()+":"+sjsql);
                countlist = this.xtJcyXMLJdpzbMapper.selectObject(semap);
                for (int i = 0; i < countlist.size(); i++) {
                    Map<String,Object> temp = MapUtil.nullToEmpty(countlist.get(i));
                    countlist.set(i, temp);
                }
            }

            List<XtJcyAzSjdzb> list = this.xtJcyAzSjdzbMapper.selectSjjByFjdid(jxyXML.getId().toString());
            if (list.size()>0) {
                for (XtJcyAzSjdzb xtJcyAzSjdzb : list) {
                    if (!"".equals(xtJcyAzSjdzb.getDefua())&&xtJcyAzSjdzb.getDefua()!=null) {
                        if ("UUID".toUpperCase().equals(xtJcyAzSjdzb.getDefua())) {
                            String uuid = UUID.randomUUID().toString();
                            uuid = uuid.replace("-", "");
                            sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+">"+uuid+"</"+xtJcyAzSjdzb.getJcyjdmc()+">");
                        }else if(xtJcyAzSjdzb.getDefua().startsWith("_")){
                            //含有下划线为sql
                            Map<String, Object> sqlMap = new HashMap<String, Object>();
                            sqlMap.put("sql", xtJcyAzSjdzb.getDefua().substring(1));
                            List<Map<String, Object>> defList =  this.xtJcyXMLJdpzbMapper.selectObject(sqlMap);
                            if (defList.size()>0) {
                                Map<String, Object> valMap  = defList.get(0);
                                for (String key : valMap.keySet()) {
                                    Object val = valMap.get(key);
                                    sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+">"+val.toString()+"</"+xtJcyAzSjdzb.getJcyjdmc()+">");
                                }
                            }
                        }
                        else {
                            sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+">"+xtJcyAzSjdzb.getDefua()+"</"+xtJcyAzSjdzb.getJcyjdmc()+">");
                        }

                    }else {
                        if (countlist.size()>0) {
                            if (!"".equals(xtJcyAzSjdzb.getAzzdmc())&&xtJcyAzSjdzb.getAzzdmc()!=null) {
                                if (countlist.get(0).containsKey(xtJcyAzSjdzb.getAzzdmc())) {
                                    String temp = countlist.get(0).get(xtJcyAzSjdzb.getAzzdmc()).toString();
                                    if (!"".equals(temp)&&temp!=null) {
                                        sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+">"+temp+"</"+xtJcyAzSjdzb.getJcyjdmc()+">");
                                    }else{
                                        sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+"></"+xtJcyAzSjdzb.getJcyjdmc()+">");
                                    }
                                }else{
                                    sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+"></"+xtJcyAzSjdzb.getJcyjdmc()+">");
                                }

                            }else{
                                sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+"></"+xtJcyAzSjdzb.getJcyjdmc()+">");
                            }


                        }else{
                            sjb.append("<"+xtJcyAzSjdzb.getJcyjdmc()+"></"+xtJcyAzSjdzb.getJcyjdmc()+">");
                        }

                    }
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return sjb.toString();
    }

    public XtJcyXMLJdpzbMapper getXtJcyXMLJdpzbMapper() {
        return xtJcyXMLJdpzbMapper;
    }


    public void setXtJcyXMLJdpzbMapper(XtJcyXMLJdpzbMapper xtJcyXMLJdpzbMapper) {
        this.xtJcyXMLJdpzbMapper = xtJcyXMLJdpzbMapper;
    }


    public XtJcyAzSjdzbMapper getXtJcyAzSjdzbMapper() {
        return xtJcyAzSjdzbMapper;
    }


    public void setXtJcyAzSjdzbMapper(XtJcyAzSjdzbMapper xtJcyAzSjdzbMapper) {
        this.xtJcyAzSjdzbMapper = xtJcyAzSjdzbMapper;
    }




    public XtJcylcjdWspzbMapper getXtJcylcjdWspzbMapper() {
        return xtJcylcjdWspzbMapper;
    }



    public void setXtJcylcjdWspzbMapper(XtJcylcjdWspzbMapper xtJcylcjdWspzbMapper) {
        this.xtJcylcjdWspzbMapper = xtJcylcjdWspzbMapper;
    }

    /**
     *
     * @desc:生成xml文件到临时目录
     * @author  潘磊
     * @Date 2020年8月9日下午4:36:05
     * @param uid
     * @param str
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String createXML(String uid,String str){
        FileOutputStream os = null;
        try {
            String url =zipfiletemp+uid;
            File file = new File(url);
            if (!file.exists()) {
                file.mkdirs();
            }
            String xmlstr=this.strToXmlstr(str);
            FileWriter fw = new FileWriter(url+"//ywxx.xml");
            fw.write(xmlstr);
            fw.flush();
            fw.close();

        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

            return "false";
        }
        return "true";
    }


    public String strToXmlstr(String str){
        XMLWriter writer = null;
        StringWriter writes = new StringWriter();
        Document document;
        try {
            document = DocumentHelper.parseText(str);
            OutputFormat format = OutputFormat.createPrettyPrint();

            writer = new XMLWriter(writes, format);
            writer.write(document);
        } catch (Exception e) {

            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally
        {
            if (writer != null)
                try {
                    writer.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }


        }
        return writes.toString();
    }
    /**
     *
     * @desc:下载word文书到指定目录
     * @author  潘磊
     * @Date 2020年8月9日下午4:36:22
     * @param uid
     * @param jqbh
     * @param lcdm
     * @param xyrbh
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String downWS(String uid,String jqbh,String lcdm,String xyrbh){
        String res = "";
        try {
            Map<String, Object> jdwsmap = new HashMap<String, Object>();
            jdwsmap.put("lcjd", lcdm);
            List<XtJcylcjdWspzb> lcjdws = this.xtJcylcjdWspzbMapper.selectByLcjd(jdwsmap);
            for (int i = 0; i < lcjdws.size(); i++) {
                List<WjJzsjb> list = new ArrayList<WjJzsjb>();
                //是否多人文书
                if ("1".equals(lcjdws.get(i).getSfdr().toString())) {
                    Map<String , Object> map = new HashMap<String, Object>();
                    map.put("xyrbh", xyrbh);
                    map.put("jqbh", jqbh);
                    map.put("wjbm", lcjdws.get(i).getWjbm());
                    map.put("wjdm", lcjdws.get(i).getWjdm());
                    List<XtWjflb> flblist = this.xtJcylcjdWspzbMapper.selectWjidByInfo(map);
                    for (int j = 0; j < flblist.size(); j++) {
                        map.put("wjbid", flblist.get(j).getWjbid());
                        list = this.xtJcylcjdWspzbMapper.selectWjsjByJqbh(map);

                        if (list.size()>0) {
                            res = this.downHttpURL(uid, list);
                        }else{
                            if ("1".equals(lcjdws.get(i).getSfgj().toString())) {
                                return "false@无关键法律文书";
                            }
                        }
                    }
//					map.put("wjbid", wjbid);
//					list = this.xtJcylcjdWspzbMapper.selectWjsjByJqbh(map);
                    //是否缺少必要文书

                }else{
                    if (xyrbh.indexOf(",")!=-1) {
                        String[] xyrte = xyrbh.split(",");
                        for (int j = 0; j < xyrte.length; j++) {
                            Map<String , Object> map = new HashMap<String, Object>();
                            map.put("xyrbh", xyrbh);
                            map.put("jqbh", jqbh);
                            map.put("wjbm", lcjdws.get(i).getWjbm());
                            map.put("wjdm", lcjdws.get(i).getWjdm());
                            List<XtWjflb> flblist = this.xtJcylcjdWspzbMapper.selectWjidByInfo(map);
                            for (int k = 0; k < flblist.size(); k++) {
                                map.put("wjbid", flblist.get(k).getWjbid());
                                list = this.xtJcylcjdWspzbMapper.selectWjsjByJqbh(map);

                                if (list.size()>0) {
                                    res = this.downHttpURL(uid, list);
                                }else{
                                    if ("1".equals(lcjdws.get(i).getSfgj().toString())) {
                                        return "false@无关键法律文书";
                                    }
                                }
                            }
                        }
                    }else{
                        Map<String , Object> map = new HashMap<String, Object>();
                        map.put("xyrbh", xyrbh);
                        map.put("jqbh", jqbh);
                        map.put("wjbm", lcjdws.get(i).getWjbm());
                        map.put("wjdm", lcjdws.get(i).getWjdm());
                        List<XtWjflb> flblist = this.xtJcylcjdWspzbMapper.selectWjidByInfo(map);
                        for (int k = 0; k < flblist.size(); k++) {
                            map.put("wjbid", flblist.get(k).getWjbid());
                            list = this.xtJcylcjdWspzbMapper.selectWjsjByJqbh(map);

                            if (list.size()>0) {
                                res = this.downHttpURL(uid, list);
                            }else{
                                if ("1".equals(lcjdws.get(i).getSfgj().toString())) {
                                    return "false@无关键法律文书";
                                }
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "false@文书下载失败";
        }
        return res;
    }
    /**
     *
     * @desc:根据httpurl下载文件word
     * @author  潘磊
     * @Date 2020年8月9日下午4:36:43
     * @param uid
     * @param list
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String downHttpURL(String uid,List<WjJzsjb> list){
        FileOutputStream fileout = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        URL url = null;

        try {
            for (int i = 0; i < list.size(); i++) {
                url = new URL(httpws+list.get(i).getWjdz());
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60*1000);
                inputStream = conn.getInputStream();
                byte[] byteData =readInputStream(inputStream);
                String saveur =zipfiletemp+uid+"//WS";
                File saveDir = new File(saveur);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                String temp = list.get(i).getWjdz();
                String fileName = temp.substring(temp.lastIndexOf("/")+1);
                File file = new File(saveDir+File.separator+fileName);
                fileout= new FileOutputStream(file);
                fileout.write(byteData);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "false@文书下载失败";
        }finally {
            try {
                if (fileout!=null) {
                    fileout.close();
                }
                if (inputStream!=null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
                return "false@文书下载失败";
            }
        }
        return "true";
    }
    /**
     *
     * @desc:流转二进制数组
     * @author  潘磊
     * @Date 2020年8月9日下午4:37:38
     * @param inputStream
     * @return
     * @throws Exception
     * @return byte[]    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private static byte[] readInputStream(InputStream inputStream) throws Exception{
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            while ((len = inputStream.read(buffer))!=-1) {
                bos.write(buffer,0,len);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            if (bos!=null) {
                try {
                    bos.close();
                } catch (Exception e2) {
                    // TODO: handle exception
                }
            }
        }
        return bos.toByteArray();
    }

    private String downJzws(String uid,String jqbh,String[] jzfl,String xyrbh){
        String res = "";
        try {
            for (int i = 0; i < jzfl.length; i++) {
                Map<String, Object> jzmap = new HashMap<String, Object>();
                String xyrsql = "";
                if (xyrbh.indexOf(",")!=-1) {
                    String[] xyrbhte = xyrbh.split(",");
                    xyrsql = "xyrbh is null or xyrbh = '"+xyrbh+"'";
                    for (int j = 0; j < xyrbhte.length; j++) {
                        xyrsql = xyrsql + " or xyrbh = '"+xyrbhte[j]+"'";
                    }
                }else{
                    xyrsql = "xyrbh is null or xyrbh = '"+ xyrbh+"'";
                }
                jzmap.put("xyrsql", xyrsql);
                jzmap.put("jqbh", jqbh);
                jzmap.put("jzfl", jzfl[i]);
                List<Map<String, Object>>  jzmllist = this.xtJcylcjdWspzbMapper.selectJzml(jzmap);
                for (int j = 0; j < jzmllist.size(); j++) {
                    Map<String, Object> jzwjmap = new HashMap<String, Object>();
                    jzwjmap.put("jqbh", jqbh);
                    jzwjmap.put("jzfl", jzfl[i]);
                    jzwjmap.put("wjbm", jzmllist.get(j).get("WJBM"));
                    jzwjmap.put("wjbid", jzmllist.get(j).get("WJBID"));
                    List<Map<String, Object>> jzwjlist = this.xtJcylcjdWspzbMapper.selectJzwj(jzwjmap);
                    res = this.downJZHttpURL(uid, jzwjlist);
                }

            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return "false@文书下载失败";
        }
        return res;
    }

    private String downJZHttpURL(String uid,List<Map<String, Object>> list){
        for (int i = 0; i < list.size(); i++) {
            FileOutputStream fileout = null;
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            URL url = null;
            try {
                String hturl = "";
                if ("XT_FJZB".equals(list.get(i).get("WJBM"))) {
                    hturl = jzfjdown+list.get(i).get("WJDZ");
                }else{
                    hturl = jzwjdown+list.get(i).get("WJDZ");
                }
                url = new URL(hturl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(60*1000);
                inputStream = conn.getInputStream();
                byte[] byteData =readInputStream(inputStream);
                String jzurl = "";
                String zipjzurl = list.get(i).get("JZWJDZ").toString();
                if ("2".equals(list.get(i).get("JZFL"))) {
                    jzurl = zipjzurl.substring(0,zipjzurl.lastIndexOf("/")+1);
                }else if ("1".equals(list.get(i).get("JZFL"))) {
                    jzurl = zipjzurl.substring(0,zipjzurl.lastIndexOf("/")+1);
                }else{
                    jzurl = "//temp";
                }
                String saveur =zipfiletemp+uid+"//"+jzurl.replace("/", "//");
                File saveDir = new File(saveur);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }

                String temp = list.get(i).get("WJDZ").toString();
                String fileName = temp.substring(temp.lastIndexOf("/")+1);
                File file = new File(saveDir+File.separator+fileName);
                fileout= new FileOutputStream(file);
                fileout.write(byteData);



            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return "false@文书下载失败";
            }finally {
                try {
                    if (fileout!=null) {
                        fileout.flush();
                        fileout.close();
                    }
                    if (inputStream!=null) {
                        inputStream.close();
                    }
                } catch (Exception e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                    return "false@文书下载失败";
                }
            }
        }
        return "true";
    }
    /**
     *
     * @desc:生成zip到临时目录，发送zip到指定目录
     * @author  潘磊
     * @Date 2020年8月9日下午4:38:02
     * @param uid
     * @param filenamet
     * @param filenamed
     * @return
     * @return String    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private String createZip(String uid,String filenamet,String filenamed){

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(ziptemp+uid+".zip"));){

            File file = new File(zipfiletemp+uid);
            File[] list = file.listFiles();
            for (int i = 0; i < list.length; i++) {
                compress(list[i], out, "");
            }

        } catch (Exception e) {
            // TODO: handle exception
            return "false@文件打包失败";
        }
        try {
            this.removeZip(uid, filenamet, filenamed);
        } catch (Exception e2) {
            // TODO: handle exception
            e2.printStackTrace();
            return "false@文件复制失败";
        }


        return "true";
    }
    /**
     *
     * @desc:判断目录是文件还是文件夹
     * @author  潘磊
     * @Date 2020年8月9日下午4:41:53
     * @param file
     * @param out
     * @param basedir
     * @throws Exception
     * @return void    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private void compress(File file, ZipOutputStream out, String basedir) throws Exception{
        if (file.isDirectory()) {
            this.zipDirectory(file, out, basedir);
        }else{
            this.zipFile(file, out, basedir);
        }
    }
    /**
     *
     * @desc:压缩单个文件
     * @author  潘磊
     * @Date 2020年8月9日下午4:42:21
     * @param srcfile
     * @param out
     * @param basedir
     * @throws Exception
     * @return void    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    private void zipFile(File srcfile,ZipOutputStream out,String basedir) throws Exception{
        if (!srcfile.exists()) {
            return;
        }
        byte[] buf = new byte[1024];

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(srcfile));
        ){
            int len = 0;
            out.putNextEntry(new ZipEntry(basedir+srcfile.getName()));
            while ((len = in.read(buf))>0) {
                out.write(buf,0,len);

            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return;
        }
    }
    /**
     *
     * @desc:压缩文件夹
     * @author  潘磊
     * @Date 2020年8月9日下午4:46:46
     * @param dir
     * @param out
     * @param basedir
     * @throws Exception
     * @return void    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    public void zipDirectory(File dir,ZipOutputStream out,String basedir) throws Exception{
        if (!dir.exists()) {
            return;
        }
        File[] file = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            compress(file[i], out,basedir+ dir.getName()+"/");
        }
    }
    /**
     *
     * @desc:文件复制到发送目录，并重命名
     * @author  潘磊
     * @Date 2020年8月9日下午4:46:55
     * @param uid
     * @param filenamet
     * @param filenamed
     * @return void    DOM对象
     * @throws
     * @since  CodingExample　Ver 1.1
     */
    public void removeZip(String uid,String filenamet,String filenamed ){

        //读取文件md5码，用于文件完整性校验
        String md5 = "";
        try(FileInputStream in = new FileInputStream(new File(ziptemp+uid+".zip"));) {
            md5 = DigestUtils.md5Hex(in);
        } catch (IOException e1) {

            // TODO Auto-generated catch block
            e1.printStackTrace();
            return;
        }
        //附
        try(FileInputStream in = new FileInputStream(new File(ziptemp+uid+".zip"));
            FileOutputStream out = new FileOutputStream(new File(zipfile+filenamet+md5+filenamed+".zip"));) {
            byte[] buff = new byte[1024];
            int len = 0;
            while((len=in.read(buff))!=-1){
                out.write(buff,0,len);
            }
            out.flush();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return;
        }finally {
            File delfile = new File(ziptemp+uid+".zip");
            if (delfile.exists()) {
                delfile.delete();
            }
        }
    }
    public static void main(String[] args) {
        try {
            String md5 = DigestUtils.md5Hex(new FileInputStream("D://1//230623000000_230623_0101A_0101_d717bc56515034f28b973e19abfb5b63_3b88bcc5926a4ae1b7fd88b12cf6c087.zip"));
            System.out.println(md5);
        }  catch (IOException e) {

            // TODO Auto-generated catch block
            e.printStackTrace();

        }
//		String x ="p@xyrbh";
//		x =x.substring(x.indexOf("@")+1);
//		System.out.println(x);
//		String uuid = UUID.randomUUID().toString();
//		System.out.println(uuid.replaceAll("-", ""));
//		String temp = "<SJBS>"+uuid+"</SJBS>";
//		String sjdb = temp.substring(temp.indexOf("<SJBS>")+6,temp.indexOf("</SJBS>"));
//		System.out.println(sjdb);
    }
}
