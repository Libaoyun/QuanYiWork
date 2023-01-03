package com.Eban.server.service.impl;

import com.Eban.server.mapper.DepartmentMapper;
import com.Eban.server.pojo.Department;
import com.Eban.server.response.RespBean;
import com.Eban.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartments() {
        // 这里传直接-1是因为部门中最大的id就是1，而它父部门就是-1，因此直接从上往下进行递归查询即可
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    public RespBean addDepartment(Department department) {
        department.setEnabled(true);
        departmentMapper.addDepartment(department);
        if (department.getResult() == 1){
            return RespBean.success("添加成功!",department);
        }
        return RespBean.error("添加失败");
    }

    @Override
    public RespBean deleteDepartmentById(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.deleteDepartmentById(dep);
        if (dep.getResult() == -3){
            return RespBean.error("该部门不存在，请重新选择!");
        }
        if (dep.getResult() == -2){
            return RespBean.error("该部门下还有子部门，删除失败!");
        }
        if (dep.getResult() == -1){
            return RespBean.error("该部门还有关联的员工，删除失败!");
        }
        if (dep.getResult() == 1) {
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }
}
