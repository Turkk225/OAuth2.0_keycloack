package com.kse.core.authkeycloak.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the admin database table.
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="admin")
public class Admin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminid;

	private String admincodeotp;

	private String admincontact;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date admindatecreation;

	private String adminemail;

	private Boolean adminenable;

	private String adminmotdepasse;

	private String adminnom;

	private String adminnumeromatricule;

	private String adminprenom;

	private String keycloackuserid;

	//bi-directional many-to-one association to Role
	@ManyToOne
	@JoinColumn(name="roleid")
	private Role role;





	public String getKeycloackuserid() {
		return keycloackuserid;
	}

	public void setKeycloackuserid(String keycloackuserid) {
		this.keycloackuserid = keycloackuserid;
	}
}