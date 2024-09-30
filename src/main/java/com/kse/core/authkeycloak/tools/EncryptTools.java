package com.kse.core.authkeycloak.tools;


import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class EncryptTools {
	
  	// Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.	
	private  int workload = 12;
	
	
	
		public  String hashPassword(String password) {
			String salt = BCrypt.gensalt(workload);
			String hashed_password = BCrypt.hashpw(password, salt);
			return hashed_password;
		}
		
		public  boolean checkPassword(String password, String stored_hash) {
			boolean password_verified = false;

			if(null == stored_hash || !stored_hash.startsWith("$2a$"))
				throw new IllegalArgumentException("Invalid hash provided for comparison");

			password_verified = BCrypt.checkpw(password, stored_hash);
			
			//return true ou false
			return password_verified;
		}

}
