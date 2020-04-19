package com.gc.crud;

import com.gc.crud.dao.EmployeeDao;
import com.gc.crud.entity.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrudApplicationTests {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void contextLoads() {
        Collection<Employee> employees = employeeDao.getAll();
        System.out.println(employees);

    }

    @Test
    public void demo(){
        String str = "000000031021";
        String newStr = str.replaceAll("^(0+)","");
        System.out.println(newStr);
    }

}
