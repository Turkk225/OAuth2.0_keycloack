package com.kse.core.authkeycloak.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@Slf4j
public class CodegeneratorTools {
	
	@Autowired
	Environment env;


	
	static SecureRandom rnd = new SecureRandom();
	
	public String generateCode() {

	    log.trace("generating OTP ");

		try {
				
	    	    int len = Integer.valueOf(env.getProperty("otp.length")); 	    		
	    	    String values = "123456789012345678901234567890"; 
	    	    
	    	    StringBuilder key = new StringBuilder(len);
	    	    
	    	    for( int i = 0; i < len; i++ ) 
	    	        key.append( values.charAt( rnd.nextInt(values.length())) );

	    	    String token = key.toString();
	    	    	
	    	    log.trace("OTP generated");

	   		   return token;
	

		} catch (Exception e) {
			log.error("Error during OTP generation", e.toString());
			return null;
//			return "135420";

		}
	   
	}

}
