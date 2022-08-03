package com.ay.rbac.vo;

import java.util.List;
import com.ay.rbac.entity.Department;
import com.ay.rbac.entity.Role;
import com.ay.rbac.entity.User;

public class UserVo extends User {

	private static final long serialVersionUID = -8174585224470623663L;

	private List<Department> topDepartments;

	private List<Department> departments;

	private List<Role> roles;

	private String company;

	public List<Department> getTopDepartments() {
		return topDepartments;
	}

	public void setTopDepartments(List<Department> topDepartments) {
		this.topDepartments = topDepartments;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

}
