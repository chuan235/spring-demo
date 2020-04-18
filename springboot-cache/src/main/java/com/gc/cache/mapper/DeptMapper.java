package com.gc.cache.mapper;

import com.gc.cache.bean.Dept;
import org.apache.ibatis.annotations.*;

@Mapper
public interface DeptMapper {

    @Select("select * from dept where deptno = #{id}")
    public Dept getDeptById(Integer id);

    @Update("UPDATE dept SET dname=#{dname},loc=#{loc} WHERE deptno=#{deptno}")
    public void updateDept(Dept dept);

    @Delete("DELETE FROM dept WHERE deptno=#{id}")
    public void deleteDeptById(Integer id);

    @Insert("INSERT INTO dept(dname,loc,deptno) VALUES(#{dname},#{loc},#{deptno})")
    public void insertDept(Dept dept);

    @Select("SELECT * FROM dept WHERE dname = #{dname}")
    Dept getDeptByName(String dName);
}
