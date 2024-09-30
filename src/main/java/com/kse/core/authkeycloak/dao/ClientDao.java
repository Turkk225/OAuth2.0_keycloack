package com.kse.core.authkeycloak.dao;


import com.kse.core.authkeycloak.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientDao extends JpaRepository<Client, Integer> {


	public List<Client>  findByClientenableTrue();

	public Client findByClientidEqualsAndClientenableTrue(Integer clientid);

	public Client findByClientcontactAndClientenableTrue(String clientcontact);


	Client findByClientemailAndClientenableTrue(String clientemail);
}
