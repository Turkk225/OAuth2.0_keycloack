package com.kse.core.authkeycloak.dao;

import com.kse.core.authkeycloak.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleDao extends JpaRepository<Role, Integer> {

    public Role findByNomAndEnableTrue(String nom);

    public Role findByNomIgnoreCaseAndEnableTrue(String nom); //

    public List<Role>  findByEnableTrue();

    public Role findByIdEqualsAndEnableTrue(Integer id);

}
