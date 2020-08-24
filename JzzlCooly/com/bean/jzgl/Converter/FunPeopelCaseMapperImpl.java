package com.bean.jzgl.Converter;

import com.bean.jzgl.DTO.FunPeopelCaseDTO;
import com.bean.jzgl.Source.FunPeopelCase;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-08-24T16:20:24+0800",
    comments = "version: 1.3.1.Final, compiler: BatchProcessingEnvImpl from aspectjtools-1.8.13.jar, environment: Java 1.8.0_251 (Oracle Corporation)"
)
public class FunPeopelCaseMapperImpl implements FunPeopelCaseMapper {

    @Override
    public FunPeopelCaseDTO pcToPcDTO(FunPeopelCase fpc) {
        if ( fpc == null ) {
            return null;
        }

        FunPeopelCaseDTO funPeopelCaseDTO = new FunPeopelCaseDTO();

        funPeopelCaseDTO.setId( fpc.getId() );
        funPeopelCaseDTO.setScbj( fpc.getScbj() );
        funPeopelCaseDTO.setState( fpc.getState() );
        funPeopelCaseDTO.setCreatetime( fpc.getCreatetime() );
        funPeopelCaseDTO.setUpdatetime( fpc.getUpdatetime() );
        funPeopelCaseDTO.setIdcard( fpc.getIdcard() );
        funPeopelCaseDTO.setName( fpc.getName() );
        funPeopelCaseDTO.setJqbh( fpc.getJqbh() );
        funPeopelCaseDTO.setAjbh( fpc.getAjbh() );

        funPeopelCaseDTO.setPersontype( personTypeToInt(fpc.getPersontype()) );

        return funPeopelCaseDTO;
    }

    @Override
    public FunPeopelCase pcDTOToPc(FunPeopelCaseDTO fpc) {
        if ( fpc == null ) {
            return null;
        }

        FunPeopelCase funPeopelCase = new FunPeopelCase();

        funPeopelCase.setId( fpc.getId() );
        funPeopelCase.setScbj( fpc.getScbj() );
        funPeopelCase.setState( fpc.getState() );
        funPeopelCase.setCreatetime( fpc.getCreatetime() );
        funPeopelCase.setUpdatetime( fpc.getUpdatetime() );
        funPeopelCase.setIdcard( fpc.getIdcard() );
        funPeopelCase.setName( fpc.getName() );
        funPeopelCase.setJqbh( fpc.getJqbh() );
        funPeopelCase.setAjbh( fpc.getAjbh() );

        funPeopelCase.setPersontype( personTypeToEnum(fpc.getPersontype()) );

        return funPeopelCase;
    }

    @Override
    public List<FunPeopelCase> pcDTOToPcs(List<FunPeopelCaseDTO> fpc) {
        if ( fpc == null ) {
            return null;
        }

        List<FunPeopelCase> list = new ArrayList<FunPeopelCase>( fpc.size() );
        for ( FunPeopelCaseDTO funPeopelCaseDTO : fpc ) {
            list.add( pcDTOToPc( funPeopelCaseDTO ) );
        }

        return list;
    }
}
