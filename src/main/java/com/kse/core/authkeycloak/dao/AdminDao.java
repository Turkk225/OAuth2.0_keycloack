package com.kse.core.authkeycloak.dao;


import com.kse.core.authkeycloak.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDao extends JpaRepository<Admin, Integer> {
	

	public Admin findByAdminemailIgnoreCaseAndAdminenableTrue(String adminemail); //
	
	public List<Admin>  findByAdminenableTrue();
	
	public Admin findByAdminidEqualsAndAdminenableTrue(Integer adminid);




	public Admin findByAdminemailIgnoreCaseAndAdmincodeotpAndAdminenableTrue(String adminemail, String otp);


}
