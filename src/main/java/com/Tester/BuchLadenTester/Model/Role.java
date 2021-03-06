package com.Tester.BuchLadenTester.Model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;

    @NotNull
    @Column(name = "role")
    private String role;

    @ManyToMany(mappedBy = "userRoles")
    Set<User> roleUsers = new HashSet<>();

    @Column(name = "role_desc")
    private String desc;


    public Role() {
    }
    public Role(Role role)
    {
        this.roleId=role.getRoleId();
        this.role=role.getRole();
        this.desc=role.getDesc();
        this.roleUsers=role.getRoleUsers();
    }
    public Role(String role,String desc){
        this.role = role;
        this.desc = desc;
    }

    public Role(String role, String desc,String privilege) {
        this.role = role;
        this.desc = desc;
    }


    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<User> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(Set<User> roleUsers) {
        this.roleUsers = roleUsers;
    }


    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((role == null) ? 0 : role.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Role other = (Role) obj;
        if (role == null) {
            return other.role == null;
        } else return role.equals(other.role);
    }
}
