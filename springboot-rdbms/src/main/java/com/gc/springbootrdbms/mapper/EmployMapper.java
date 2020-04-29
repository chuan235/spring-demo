package com.gc.springbootrdbms.mapper;

import com.gc.springbootrdbms.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface EmployMapper {

    /**
     * 查询所有的员工信息
     * @return
     */
    List<Employee> findAllEmp();

    /**
     * 根据员工号查询员工信息
     * @param id
     * @return
     */
    Employee findEmpById(Integer id);
}
