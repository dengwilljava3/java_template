package com.ay.rbac.vo;

import java.util.List;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Role;

/**
 * 部门层级及角色
 * 
 * @author jackson
 *
 */
public class DepartmentVo extends Department {

    private static final long serialVersionUID = -1067494087107175136L;

    private List<Role> roleList;

    private List<DepartmentVo> childDepartment;

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<DepartmentVo> getChildDepartment() {
        return childDepartment;
    }

    public void setChildDepartment(List<DepartmentVo> childDepartment) {
        this.childDepartment = childDepartment;
    }

}
