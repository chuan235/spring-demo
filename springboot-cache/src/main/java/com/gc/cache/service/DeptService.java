package com.gc.cache.service;

import com.gc.cache.bean.Dept;
import com.gc.cache.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

//@CacheConfig(cacheNames = "dept123",cacheManager = "cacheManager")
@Service
public class DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private RedisTemplate<Object,Dept> deptRedisTemplate;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

//    @Cacheable(keyGenerator = "gcKeyGenerator")
    public Dept queryDept(Integer id){
        System.out.println("查询的部门编号是："+id);
        Dept dept = deptMapper.getDeptById(id);
        /**
         * 保存部门
         * "ACCOUNTING"
         * {
         *   "deptno": 10,
         *   "dname": "ACCOUNTING",
         *   "loc": "NEW YORK"
         * }
         */
        //deptRedisTemplate.opsForValue().set(dept.getDname(), dept);
        redisTemplate.opsForValue().set("reids"+dept.getDname(), dept);
        System.out.println(dept);
        return dept;
    }

    public Dept insertDept(Dept dept){
        System.out.println("添加的部门是："+dept);
        deptMapper.insertDept(dept);
        return dept;
    }

    public Dept updateDept(Dept dept){
        System.out.println("修改的部门是："+dept);
        deptMapper.updateDept(dept);
        return dept;
    }

    public void deleteDept(Integer id){
        int i = 1/0;
        System.out.println("删除的部门编号是："+id);
        deptMapper.deleteDeptById(id);
    }

    public Dept getDeptByName(String deptName){
        System.out.println("查询的部门姓名是："+deptName);
        Dept dept = deptMapper.getDeptByName(deptName);
        return dept;
    }

}
