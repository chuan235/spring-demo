package com.gc.cache.service;

import com.gc.cache.bean.Emp;
import com.gc.cache.mapper.EmpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "emp")
@Service
public class EmpService {

    @Autowired
    private EmpMapper empMapper;


    @Cacheable(/*value = "emp",*/keyGenerator = "myKeyGenerator",/*key = "'emp-'+#a0",*/condition = "#id<7839 || #id>7839",unless = "#id==7839")
    public Emp queryEmp(Integer id){
        System.out.println("查询的员工编号是："+id);
        Emp emp = empMapper.getEmpById(id);
        return emp;
    }

    //@Cacheable(/*value = "emp",*/keyGenerator = "myKeyGenerator")
    public Emp insertEmp(Emp emp){
        System.out.println("添加的员工编号是："+emp.getEmpno());
        empMapper.insertEmployee(emp);
        return emp;
    }

    @CachePut(/*value = "emp",*/keyGenerator = "myKeyGenerator")
    public Emp updateEmp(Emp emp){
        System.out.println("修改的员工编号是："+emp.getEmpno());
        empMapper.updateEmp(emp);
        return emp;
    }

    /**
     * allEntries = true：指定清除这个缓存中所有的数据
     * beforeInvocation：缓存的清除是否在方法之前执行
     *   默认(false)代表缓存清除操作是在方法执行之后执行;如果出现异常缓存就不会清除
     *   true代表清除缓存操作是在方法运行之前执行，无论方法是否出现异常，缓存都清除
     * @param id
     */
    @CacheEvict(/*value = "emp",key="#id"*/keyGenerator = "myKeyGenerator",beforeInvocation = true)
    public void deleteEmp(Integer id){
        int i = 1/0;
        System.out.println("删除的员工编号是："+id);
        empMapper.deleteEmpById(id);
    }

    @Caching(
        cacheable = {
            @Cacheable(/*value = "emp",*//*key = "#a0"*/keyGenerator = "myKeyGenerator")
        },
        put = {
            @CachePut(/*value = "emp",*//*key = "#result.ename"*/keyGenerator = "myKeyGenerator")
        }
    )
    public Emp getEmpByName(String empName){
        System.out.println("查询的员工姓名是："+empName);
        Emp emp = empMapper.getEmpByEmpName(empName);
        return emp;
    }

}
