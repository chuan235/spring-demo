package com.gc.crud.controller;

import com.gc.crud.dao.DepartmentDao;
import com.gc.crud.dao.EmployeeDao;
import com.gc.crud.entity.Department;
import com.gc.crud.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Map;

@Controller
public class DashController {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private DepartmentDao departmentDao;


    @PostMapping("/user/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        Map<String,Object> map, HttpSession session){
        if(!StringUtils.isEmpty(username) && password.equals("123456")){
            session.setAttribute("loginuser",username);
            return "redirect:/main.html";
        }else{
            // 密码或者账号错误处理
            map.put("msg","用户名或密码错误");
            return "/index";
        }
    }

    @GetMapping("/emps")
    public String getEmpList(Model model){
        Collection<Employee> employees = employeeDao.getAll();
        model.addAttribute("data",employees);
        return "emp/list";
    }

    @GetMapping("/emp")
    public String toAddPage(Model model){
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments", departments);
        return "emp/add";
    }

    /**
     * 自动封装Employee name与属性一一对应
     */
    @PostMapping("/emp")
    public String addEmp(Employee emp){
        employeeDao.save(emp);
        // 返回员工列表  forward:/emps
        // 原理：ThymeleafViewResolver.createView()  RedirectView/InternalResourceView
        return "redirect:/emps";
    }

    @GetMapping("emp/{id}")
    public String toEidtPage(@PathVariable Integer id,Model model){
        Employee employee = employeeDao.get(id);
        model.addAttribute("emp",employee);
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("departments", departments);
        return "emp/add";
    }


    @PutMapping("/emp")
    public String updateEmp(Employee emp){
        // put请求 修改
        employeeDao.save(emp);
        return "redirect:/emps";
    }

    @DeleteMapping("/emp/{id}")
    public String delEmp(@PathVariable Integer id){
        employeeDao.delete(id);
        return "redirect:/emps";
    }
}
