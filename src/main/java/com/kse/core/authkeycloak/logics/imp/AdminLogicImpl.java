///**
// *
// */
//package com.kse.core.authkeycloak.logics.imp;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kse.core.authkeycloak.logics.AdminLogic;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.keycloak.representations.AccessTokenResponse;
//import  com.kse.core.authkeycloak.dao.AdminDao;
//import  com.kse.core.authkeycloak.dto.ChangepwdDto;
//import  com.kse.core.authkeycloak.dto.UserDto;
//import  com.kse.core.authkeycloak.entities.Admin;
//import  com.kse.core.authkeycloak.tools.CodegeneratorTools;
//import  com.kse.core.authkeycloak.tools.DateTools;
//import  com.kse.core.authkeycloak.tools.EncryptTools;
//import  com.kse.core.authkeycloak.tools.KeycloackTools;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
///**
// * @author florentin
// *
// */
////@AllArgsConstructor
//@RequiredArgsConstructor
////@NoArgsConstructor
//@Service
//@Slf4j
//public class AdminLogicImpl implements AdminLogic {
//
//
//	@Autowired
//	DateTools dateTools;
//
//	@Autowired
//	Environment env;
//
//	@Autowired
//	AdminDao adminDao;
//
//	@Autowired
//	EncryptTools encryptTools;
//
//	@Autowired
//	KeycloackTools keycloackTools;
//
//	@Autowired
//	CodegeneratorTools codegeneratorTools;
//
//	@Override
//	public Admin save(Admin entity) {
//		// TODO Auto-generated method stub
//		return adminDao.save(entity);
//	}
//
//	@Override
//	public Admin update(Admin entity) {
//		// TODO Auto-generated method stub
//		return adminDao.save(entity);
//	}
//
//	@Override
//	public Admin getById(int id) {
//		// TODO Auto-generated method stub
//		return adminDao.findByAdminidEqualsAndAdminenableTrue(id);
//	}
//
//	@Override
//	public List<Admin> getAll() {
//		// TODO Auto-generated method stub
//		return adminDao.findByAdminenableTrue();
//	}
//
//	@Override
//	public Boolean delete(Integer id) {
//		// TODO Auto-generated method stub
//		try {
//			adminDao.deleteById(id);
//
//			return true;
//
//		}catch (Exception e) {
//			e.printStackTrace();
//			return false;
//			// TODO: handle exception
//		}
//	}
//
//	@Override
//	public Admin getAdminByEmail(String adminemail) {
//		// TODO Auto-generated method stub
//		return adminDao.findByAdminemailIgnoreCaseAndAdminenableTrue(adminemail);
//	}
//
//	@Override
//	public Admin getByAdminemailIgnoreCase(String admin) {
//		// TODO Auto-generated method stub
//		return adminDao.findByAdminemailIgnoreCaseAndAdminenableTrue(admin);
//	}
//
//
//	@Override
//	public Admin updateAdmin(Admin admin, Integer id) {
//		Admin admin2 = this.getById(id);
//		Optional.ofNullable(admin.getAdmincontact()).ifPresent(admin2::setAdmincontact);
//		Optional.ofNullable(admin.getAdminnom()).ifPresent(admin2::setAdminnom);
//		Optional.ofNullable(admin.getAdminprenom()).ifPresent(admin2::setAdminprenom);
//		// TODO Auto-generated method stub
//		UserDto userDto =  UserDto.builder().email(admin2.getAdminemail()).firstname(admin2.getAdminprenom()).lastname(admin2.getAdminnom()).password(null).username(admin2.getAdminemail()).build();
//		keycloackTools.updateUser(admin2.getKeycloackuserid(),userDto);
//		Admin result = this.save(admin2);
//
//		return result;
//	}
//
//	@Override
//	public Boolean deleteAdmin(Integer id) {
//		try {
//			Admin admin = this.getById(id);
//			admin.setAdminenable(false);
//			keycloackTools.deleteUser(admin.getKeycloackuserid());
//			this.save(admin);
//			// TODO Auto-generated method stub
//			return true;
//		} catch (Exception e) {
//			// TODO: handle exception
//			return false;
//		}
//	}
//
//	@Override
//	public List<Admin> getAllAdmin(Integer adminid, Integer roleid) {
//		List<Admin> admins = new ArrayList<>();
//		if(adminid != null && roleid == null){
//			Admin admin = this.getById(adminid);
//			admins.add(admin);
//		}
//		else{
//			admins = this.getAll();
//		}
//		return admins;
//	}
//
//	@Override
//	public Admin updatePassword(Integer adminid, String newpassword) {
//
//		try{
//			Admin admin = this.getById(adminid);
//			String newpasswordEncrypted = encryptTools.hashPassword(newpassword);
//			admin.setAdminmotdepasse(newpasswordEncrypted);
//			admin.setAdmincodeotp(null);
//			keycloackTools.resetPassword(admin.getKeycloackuserid(),newpassword);
//			return this.save(admin);
//		}catch (Exception ex){
//
//			log.error("Error update password function : adminid={} | error={}", adminid, ex.getMessage());
//			return null;
//		}
//
//	}
//
//	@Override
//	public Map<String, Object> getAdminToken(String username, String password) {
////		AccessTokenResponse accessTokenResponse = keycloackTools.getTokenObject(username, password);
////		Admin admin = this.getAdminByEmail(username);
////
////		if( Objects.nonNull(admin) && Objects.nonNull(accessTokenResponse) ){
////			if(encryptTools.checkPassword(password, admin.getAdminmotdepasse()) ){
////				Map<String,Object> map = new HashMap<>();
////				map.put("admin", admin);
////				map.put("accessTokenResponse",accessTokenResponse);
////				return map;
////			}
////		}
//		return null;
//	}
//
//	@SneakyThrows
//	@Override
//	public Map<String, Object> refreshAdminToken(String refreshToken) {
//		AccessTokenResponse accessTokenResponse = keycloackTools.getrefreshTokenObject(refreshToken);
//
//		//Admin admin = this.getAdminByEmail(username);
//		if(Objects.nonNull(accessTokenResponse) ){
//			String[] res=accessTokenResponse.getToken().split("[.]",0);
//			String encodedPayload = res[1];
//			log.debug("encodedString: "+encodedPayload);
//			byte[] decodedBytes = Base64.getDecoder().decode(encodedPayload.getBytes(StandardCharsets.UTF_8));
//			String decodedString = new String(decodedBytes);
//			log.debug("Payload: "+decodedString);
//			Map<String,Object>  attributes = new ObjectMapper().readValue(decodedString, Map.class);
//			log.debug("attributes: "+attributes);
//
//			String preferred_username = (String) attributes.get("preferred_username");
//			log.debug("preferred_username: "+preferred_username);
//
//			Admin admin = this.getAdminByEmail(preferred_username);
//			if(Objects.isNull(admin)){
//				return null;
//			}
//			Map<String,Object> map = new HashMap<>();
//			map.put("admin", admin);
//			map.put("accessTokenResponse",accessTokenResponse);
//			return map;
//		}
//		return null;
//	}
//
//	@Override
//	public Integer forgotPassword(String email) {
//		String otp = codegeneratorTools.generateCode();
//		log.debug("code otp pour modification : "+otp);
//		Map<String, String> map = new HashMap<>();
//		map.put("${otp}", otp);
//
//		Admin admin = this.getAdminByEmail(email);
//		if(Objects.nonNull(admin)){
//			admin.setAdmincodeotp(otp);
//			this.save(admin);
//			return 200;
//
//		}else {
//			log.warn("Email incorrect: email={} ", email);
//			return 204;
//		}
//	}
//
//	@Override
//	public Admin changePassword(ChangepwdDto changepwdDto) {
//
//		try{
//			Admin admin = this.getByOtpAndemail(changepwdDto.getOtp(), changepwdDto.getUsername());
//			if(Objects.nonNull(admin)){
//				return this.updatePassword(admin.getAdminid(), changepwdDto.getNewpasswd());
//			}
//			return null;
//		}catch (Exception ex){
//			log.error("Error change password: infos={} | error={} ", changepwdDto.toString(), ex.getMessage());
//			return null;
//		}
//
//	}
//
//	@Override
//	public Admin getByOtpAndemail(String otp, String email) {
//		return adminDao.findByAdminemailIgnoreCaseAndAdmincodeotpAndAdminenableTrue(email, otp);
//	}
//
//}
