
import java.util.Date;

import com.bean.jzgl.Converter.FunPeopelCaseMapper;
import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.DTO.SysLogsDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import com.enums.Enums;
import com.mapper.jzgl.FunPeopelCaseDTOMapper;
import com.mapper.jzgl.SysLogsMapper;
import com.sqlsession.JzzlSqlSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * @author MrLu
 * @createTime 2020/8/21 15:32
 * @describe mybatis测试
 */
public class MybatisTest {

    @Test
    public void testConnect(){
        SqlSession sqlSession=null;
        try {
            sqlSession= JzzlSqlSessionFactory.openSqlSession();
            SysLogsMapper sysLogsMapper= sqlSession.getMapper(SysLogsMapper.class);

            SysLogsDTO SysLogsDTO=sysLogsMapper.selectByPrimaryKey(1);
            System.out.println(SysLogsDTO.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    // TODO: 2020/8/22  MrLu  上单位测试一下 
    @Test
    public  void testInsert(){
        SqlSession sqlSession=null;
        try {
            sqlSession= JzzlSqlSessionFactory.openSqlSession();
            FunPeopelCaseDTOMapper funPeopelCaseDTOMapper= sqlSession.getMapper(FunPeopelCaseDTOMapper.class);
            FunPeopelCase newF=new FunPeopelCase();
            newF.setIdcard("dddd");
            newF.setName("vvv");
            newF.setJqbh("bbb");
            newF.setAjbh("aaa");
            newF.setPersontype(Enums.PersonType.SECONDARY);
            funPeopelCaseDTOMapper.insertSelective(FunPeopelCaseMapper.INSTANCE.pcToPcDTO(newF));
            sqlSession.commit();
        }catch (Exception e){
            sqlSession.rollback();
            e.printStackTrace();
        }
    }
}
