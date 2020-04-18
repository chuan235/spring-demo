package com.gc.cache.controller;

import com.gc.cache.bean.Emp;
import com.gc.cache.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 */
@Controller
public class EmpController {

    private static int no = 9906;
    @Autowired
    private EmpService empService;

    @RequestMapping("/cache/emp/{id}")
    @ResponseBody
    public Emp queryEmp(@PathVariable Integer id){
        return empService.queryEmp(id);
    }

    @RequestMapping("/cache/emp/insert")
    @ResponseBody
    public Emp insertEmp(){
        Emp emp = empService.queryEmp(7369);
        emp.setEmpno(no++);
        emp.setEname("tom1"+no);
        return empService.insertEmp(emp);
    }

    @RequestMapping("/cache/emp/delete/{id}")
    @ResponseBody
    public void deleteEmp(@PathVariable Integer id){
        empService.deleteEmp(id);
    }

    @RequestMapping("/cache/emp/update/{id}")
    @ResponseBody
    public Emp updateEmp(@PathVariable Integer id){
        Emp emp = empService.queryEmp(id);
        emp.setEname("jack"+no);
        return empService.updateEmp(emp);
    }

}
