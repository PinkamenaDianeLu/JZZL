package com.mapper.jzgl;


public interface DeleteCaseMapper {

    int delFunarchiverecords(String ajbh);
    int delFunarchivesfc(String ajbh);
    int delFunarchiveseq(String ajbh);
    int delFunarchivetype(String ajbh);
    int delFunarchivefiles(String ajbh);
    int delFuncaseinfo(String ajbh);
    int delFuncasepeoplecase(String ajbh);
    int delFunsuspectrecord(String ajbh);
    int delFunsuspect(String ajbh);

}