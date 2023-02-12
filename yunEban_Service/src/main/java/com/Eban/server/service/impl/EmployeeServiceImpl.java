package com.Eban.server.service.impl;

import com.Eban.server.mapper.EmployeeMapper;
import com.Eban.server.pojo.Employee;
import com.Eban.server.response.RespBean;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.IEmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public RespPageBean getAllEmployee(Integer currentPage, Integer size, Employee employee, LocalDate[] dateScope) {
//        开启分页
        Page<Employee> page = new Page<>(currentPage,size);
        IPage<Employee> allEmployeeByPage = employeeMapper.getAllEmployeeByPage(page, employee, dateScope);
        return new RespPageBean(allEmployeeByPage.getTotal(), allEmployeeByPage.getRecords());

    }

    @Override
    public RespBean maxWorkId() {
        // 直接内置函数快捷查询，但获取到的是键为string，值为Object的map列表
        List<Map<String, Object>> maps = employeeMapper.selectMaps(
                new QueryWrapper<Employee>().select("max(workID)"));
        // 因为max只会获取一个值，因此直接下标0获取对象并找到相应键，然后toString才能转为Int类型，
        Integer maxID = Integer.parseInt(maps.get(0).get("max(workID)").toString())+1;
        // 最后再格式化一下，八位数字长度，转回String
        return RespBean.success(null, String.format("%08d", maxID));
    }

    @Override
    public RespBean addEmp(Employee employee) {
        // 处理合同期限，保留两位小数
        LocalDate beginDate = employee.getBeginContract();
        LocalDate endDate = employee.getEndContract();
        // 选择计量单位为天，可选年月日时分等等
        long days = beginDate.until(endDate, ChronoUnit.DAYS);
        // 保留两位有效小数，00代表小数位数
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if (1 == employeeMapper.insert(employee)){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("添加失败!");
    }

    @Override
    public RespBean updateEmp(Employee employee) {
        // 处理合同期限，保留两位小数
        LocalDate beginDate = employee.getBeginContract();
        LocalDate endDate = employee.getEndContract();
        // 选择计量单位为天，可选年月日时分等等
        long days = beginDate.until(endDate, ChronoUnit.DAYS);
        // 保留两位有效小数，00代表小数位数
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        employee.setContractTerm(Double.parseDouble(decimalFormat.format(days/365.00)));
        if (1 == employeeMapper.updateById(employee)){
            return RespBean.success("更新成功!");
        }
        return RespBean.error("更新失败!");
    }

    @Override
    public List<Employee> getEmployee(Integer id) {
        return employeeMapper.getEmployee(id);
    }
}
