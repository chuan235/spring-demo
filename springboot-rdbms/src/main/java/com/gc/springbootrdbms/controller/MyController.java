package com.gc.springbootrdbms.controller;

import com.gc.springbootrdbms.bean.Employee;
import com.gc.springbootrdbms.mapper.DepartmentMapper;
import com.gc.springbootrdbms.mapper.EmployMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class MyController {

//    @Autowired
//    private DepartmentMapper departmentMapper;
    @Autowired
    private EmployMapper employMapper;


    @GetMapping("emp/{id}")
    @ResponseBody
    public String info(@PathVariable Integer id){
        Employee employee = employMapper.findEmpById(id);
        return employee.toString();
    }

    /**
     * 整合jdbc
     */
    //@Autowired
    //private JdbcTemplate template;

//    @GetMapping("/getData")
//    @ResponseBody
//    public String getData(){
//        List<Map<String, Object>> maps = template.queryForList("select * from employee");
//        System.out.println(maps);
//        return maps.toString();
//    }

}
