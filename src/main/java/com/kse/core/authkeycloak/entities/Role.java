package com.kse.core.authkeycloak.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the admin database table.
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column
	private String nom;

	@Column(columnDefinition = "text")
	private String description;

	private Boolean enable;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	@Temporal(TemporalType.TIMESTAMP)
	private Date datecreation;

	//bi-directional many-to-one association to Admin
	@OneToMany(mappedBy="role")
	@JsonIgnore
	private List<Admin> admins;


	@OneToMany(mappedBy="role")
	@JsonIgnore
	private List<Client> clients;


	public List<Admin> getAdmins() {
		return this.admins;
	}

	public void setAdmins(List<Admin> admins) {
		this.admins = admins;
	}

	public Admin addAdmin(Admin admin) {
		getAdmins().add(admin);
		admin.setRole(this);

		return admin;
	}

	public Admin removeAdmin(Admin admin) {
		getAdmins().remove(admin);
		admin.setRole(null);
		return admin;
	}

}