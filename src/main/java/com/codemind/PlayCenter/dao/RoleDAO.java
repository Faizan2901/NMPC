package com.codemind.PlayCenter.dao;

import com.codemind.PlayCenter.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role,Integer> {

    Role findByName(String name);

}
