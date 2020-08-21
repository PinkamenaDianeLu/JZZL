import com.bean.jzgl.DTO.SysLogsDTO;
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
}
