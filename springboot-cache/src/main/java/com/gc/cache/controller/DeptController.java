package com.gc.cache.controller;

import com.gc.cache.bean.Dept;
import com.gc.cache.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DeptController {

    private static int no = 100;
    @Autowired
    private DeptService deptService;

    @RequestMapping("/dept/{id}")
    public Dept queryDept(@PathVariable Integer id){
        return deptService.queryDept(id);
    }

    @RequestMapping("/dept/insert")
    public Dept insertEmp(){
        Dept dept = deptService.queryDept(10);
        dept.setDname("ps");
        dept.setLoc("shanghai");
        dept.setDeptno(no);
        return deptService.insertDept(dept);
    }

    @RequestMapping("/dept/delete/{id}")
    public void deleteDept(@PathVariable Integer id){
        deptService.deleteDept(id);
    }

    @RequestMapping("/dept/update/{id}")
    public Dept updateEmp(@PathVariable Integer id){
        Dept dept = deptService.queryDept(id);
        dept.setDname("beijing"+no);
        return deptService.updateDept(dept);
    }

}
