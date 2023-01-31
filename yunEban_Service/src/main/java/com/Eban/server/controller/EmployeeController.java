package com.Eban.server.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.IExcelExportServer;
import com.Eban.server.converter.DateUtil;
import com.Eban.server.pojo.*;
import com.Eban.server.response.RespBean;
import com.Eban.server.response.RespPageBean;
import com.Eban.server.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.Na;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LBY
 * @since 2022-08-13
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;

    @Autowired
    private IPoliticsStatusService politicsStatusService;

    @Autowired
    private IJoblevelService jobLevelService;

    @Autowired
    private INationService nationService;

    @Autowired
    private IPositionService positionService;

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation("分页获取所有员工")
    @GetMapping("/")
    public RespPageBean getAllEmployee(@RequestParam(defaultValue = "1") Integer currentPage,
                                       @RequestParam(defaultValue = "10") Integer size,
                                       Employee employee,
                                       String[] dateScope) throws Exception {
//        LocalDate[] arr = new LocalDate[2];
//        arr[0] = LocalDate.parse(beginDateScope[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        arr[1] = LocalDate.parse(beginDateScope[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        return employeeService.getAllEmployee(currentPage, size, employee, arr);
        return employeeService.getAllEmployee(currentPage, size, employee, DateUtil.dateConvert(dateScope));
    }

    @ApiOperation("获取所有政治面貌")
    @GetMapping("/politic-status")
    public List<PoliticsStatus> getAllPoliticStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation("获取所有职称")
    @GetMapping("/job-levels")
    public List<Joblevel> getAllJobLevels(){
        return jobLevelService.list();
    }

    @ApiOperation("获取所有民族")
    @GetMapping("/nations")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation("获取所有职位")
    @GetMapping("/positions")
    public List<Position> getAllPositions(){
        return positionService.list();
    }

    @ApiOperation("获取所有部门（层级）")
    @GetMapping("/departments")
    public List<Department> getAllDepartments(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation("获取工号（自增）")
    @GetMapping("/maxWorkId")
    public RespBean maxWorkId(){
        return employeeService.maxWorkId();
    }

    @ApiOperation("添加员工")
    @PostMapping("/")
    public RespBean addEmp(@RequestBody Employee employee){
        // 这里不使用快捷方式，是因为类似于合同期限的需要手动处理
        return employeeService.addEmp(employee);
    }

    @ApiOperation("更新员工")
    @PostMapping("/update")
    public RespBean updateEmp(@RequestBody Employee employee){
        // 这里不使用快捷方式，是因为类似于合同期限的需要手动处理
        return employeeService.updateEmp(employee);
    }

    @ApiOperation("删除员工")
    @DeleteMapping("/{id}")
    public RespBean removeById(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return RespBean.success("删除成功!");
        }
        return RespBean.error("删除失败!");
    }

    @ApiOperation("导出员工表")
    @GetMapping(value = "/exports", produces = "application/octet-stream")
    public void exportEmployee(HttpServletResponse response){
        List<Employee> list = employeeService.getEmployee(null);

        ExportParams params = new ExportParams("员工表", "员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        ServletOutputStream out = null;
        try {
            // 流的形式
            response.setHeader("content-type", "application/octet-stream");
            // 防止中文乱码
            response.setHeader("content-disposition", "attachment;filename="
                    + URLEncoder.encode("员工表.xls", "UTF-8"));
            out = response.getOutputStream();
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation("导入员工数据")
    @PostMapping("/import")
    public RespBean importEmployee(MultipartFile file){
        ImportParams params = new ImportParams();
        // 去除标题行(通过标识标题行是第几行来忽略)
        params.setTitleRows(1);
        List<Nation> nationList = nationService.list();
        List<Department> departmentList = departmentService.list();
        List<Joblevel> jobLevelList = jobLevelService.list();
        List<Position> positionList = positionService.list();
        List<PoliticsStatus> politicsStatusList = politicsStatusService.list();
        try {
            List<Employee> list = ExcelImportUtil.importExcel(
                    file.getInputStream(), Employee.class, params);
            list.forEach(employee -> {
                employee.setNationId(nationList.get(nationList.indexOf(
                        new Nation(employee.getNation().getName()))).getId());
                employee.setDepartmentId(departmentList.get(departmentList.indexOf(
                        new Department(employee.getDepartment().getName()))).getId());
                employee.setJobLevelId(jobLevelList.get(jobLevelList.indexOf(
                        new Joblevel(employee.getJoblevel().getName()))).getId());
                employee.setPosId(positionList.get(positionList.indexOf(
                        new Position(employee.getPosition().getName()))).getId());
                employee.setPoliticId(politicsStatusList.get(politicsStatusList.indexOf(
                        new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId());
            });
            if (employeeService.saveBatch(list)){
                return RespBean.success("导入成功！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
            return RespBean.error("导入失败！");
    }
}
