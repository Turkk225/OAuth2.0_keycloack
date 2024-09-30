package com.kse.core.authkeycloak.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the client database table.
 * 
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client")
public class Client implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
//	private Integer id;
	private Integer clientid;

	private String clientcodeotp;

	private String clientcontact;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date clientdatecreation;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date clientdatedenaissance;

	private String clientemail;

	private Boolean clientenable;

	private String clientgenre;

	private String clientmotdepasse;

	private String clientnom;



	private String clientprenom;

	private String clientstatut;

	private String keycloackuserid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleid")
	private Role role;


	//bi-directional many-to-one association to Evaluer


	public String getKeycloackuserid() {
		return keycloackuserid;
	}

	public void setKeycloackuserid(String keycloackuserid) {
		this.keycloackuserid = keycloackuserid;
	}

	public Role getRole() {
		return this.role;
	}
	public void setRole(Role role) {
		this.role = role;
	}




}