package com.wang.springboot.controller;

import com.wang.springboot.dao.DepartmentDao;
import com.wang.springboot.dao.EmployeeDao;
import com.wang.springboot.entities.Department;
import com.wang.springboot.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    DepartmentDao departmentDao;

    // 查询所有员工返回列表页面
    @GetMapping(value = "/emps")
    public String list(Model model) {
        // 查询所有员工信息
        Collection<Employee> employees = employeeDao.getAll();

        // 把员工信息放在请求域中
        model.addAttribute("emps", employees);

        return "emp/list";
    }

    // 跳转员工添加页面
    @GetMapping(value = "/emp")
    public String toAddPage(Model model) {
        // 查询所有部门
        Collection<Department> departments = departmentDao.getDepartments();

        // 把部分信息放到请求域中
        model.addAttribute("depts", departments);

        return "emp/add";
    }

    /**
     * 员工添加请求
     * SpringMVC 自动将请求参数和入参对象的属性进行一一绑定；
     *      | 要求请求参数的名字和 javaBean 入参的对象里面的属性名是一样的
     */
    @PostMapping(value = "emp")
    public String addEmp(Employee employee) {
        // 保存员工
        employeeDao.save(employee);

        // 跳转到员工列表页面
        // redirect: 表示重定向到一个地址  /代表当前项目路径
        // forward: 表示转发到一个地址
        return "redirect:/emps";
    }

    // 跳转到修改员工页面
    @GetMapping(value = "/emp/{id}")
    public String toEditPage(@PathVariable("id") Integer id, Model model) {
        // 根据ID查询员工信息
        Employee employee = employeeDao.get(id);
        model.addAttribute("emp", employee);

        // 查询所有部门，并把部分信息放到请求域中
        Collection<Department> departments = departmentDao.getDepartments();
        model.addAttribute("depts", departments);

        // 修改和添加页面共用
        return "emp/add";
    }

    // 员工修改：需要提交员工id
    @PutMapping("/emp")
    public String updateEmployee(Employee employee) {
        // 保存员工信息
        employeeDao.save(employee);

        return "redirect:/emps";
    }

    // 员工删除：需要提交员工id
    @DeleteMapping("/emp/{id}")
    public String deleteEmployee(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/emps";
    }
}
