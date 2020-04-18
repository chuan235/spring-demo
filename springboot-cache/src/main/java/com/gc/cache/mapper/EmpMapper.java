package com.gc.cache.mapper;

import com.gc.cache.bean.Emp;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EmpMapper {

    @Select("select * from emp where empno = #{id}")
    public Emp getEmpById(Integer id);

    @Update("UPDATE emp SET ename=#{ename},job=#{job},mgr=#{mgr},hiredate=#{hiredate},sal=#{sal},comm=#{comm},deptno=#{deptno} WHERE empno=#{empno}")
    public void updateEmp(Emp employee);

    @Delete("DELETE FROM emp WHERE empno=#{id}")
    public void deleteEmpById(Integer id);

    @Insert("INSERT INTO emp(empno,ename,job,mgr,hiredate,sal,comm,deptno) VALUES(#{empno},#{ename},#{job},#{mgr},#{hiredate},#{sal},#{comm},#{deptno})")
    public void insertEmployee(Emp employee);

    @Select("SELECT * FROM emp WHERE ename = #{eName}")
    Emp getEmpByEmpName(String eName);
}
