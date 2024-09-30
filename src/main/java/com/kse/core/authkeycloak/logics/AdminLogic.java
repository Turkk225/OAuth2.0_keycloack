package com.kse.core.authkeycloak.logics;

import com.kse.core.authkeycloak.dto.ChangepwdDto;
import com.kse.core.authkeycloak.entities.Admin;

import java.util.List;
import java.util.Map;

public interface AdminLogic extends Crud<Admin> {
	Admin getAdminByEmail(String adminemail);
	
	public Admin getByAdminemailIgnoreCase(String adminemail);

	Admin updateAdmin(Admin admin,Integer id );

	Boolean deleteAdmin(Integer id);

	List<Admin> getAllAdmin(Integer adminid, Integer roleid);


	Admin updatePassword(Integer adminid, String newpassword);


	Map<String, Object> getAdminToken(String username, String password);

	Map<String, Object> refreshAdminToken(String refreshToken);

	public Integer forgotPassword(String email);

	public Admin changePassword(ChangepwdDto changepwdDto);

	public Admin getByOtpAndemail(String otp, String email);


}
