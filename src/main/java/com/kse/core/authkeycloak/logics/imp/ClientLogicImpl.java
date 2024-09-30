/**
 *
 */
package com.kse.core.authkeycloak.logics.imp;

import com.kse.core.authkeycloak.dao.RoleDao;
import com.kse.core.authkeycloak.dto.LoginRequestDto;
import com.kse.core.authkeycloak.dto.LoginResponseDto;
import com.kse.core.authkeycloak.dto.TokenDto;
import com.kse.core.authkeycloak.dto.UserRegistrationRecord;
import com.kse.core.authkeycloak.entities.Role;
import com.kse.core.authkeycloak.exceptions.ResourceAlreadyExistsException;
import com.kse.core.authkeycloak.exceptions.ResourceNotFoundException;
import com.kse.core.authkeycloak.logics.ClientLogic;
import com.kse.core.authkeycloak.tools.KeycloakUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import  com.kse.core.authkeycloak.dao.ClientDao;
import  com.kse.core.authkeycloak.entities.Client;
import  com.kse.core.authkeycloak.tools.CodegeneratorTools;
import  com.kse.core.authkeycloak.tools.DateTools;
import  com.kse.core.authkeycloak.tools.EncryptTools;
//import  com.kse.core.authkeycloak.tools.KeycloackTools;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.View;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author florentin
 *
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class ClientLogicImpl implements ClientLogic {

	@Autowired
	DateTools dateTools;

	@Autowired
	Environment env;

	@Autowired
	ClientDao clientDao;

	@Autowired
	RoleDao RoleDao;

	@Autowired
	EncryptTools encryptTools;

//	@Autowired
//	KeycloackTools keycloackTools;



//	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	CodegeneratorTools codegeneratorTools;
    @Autowired
    private View error;
    @Qualifier("resourceHandlerMapping")
    @Autowired
    private HandlerMapping resourceHandlerMapping;

	@Override
	public Client save(Client entity) {
		return clientDao.save(entity);
	}

	@Override
	public Client update(Client entity) {
		return clientDao.save(entity);
	}

	@Override
	public Client getById(int id) {
		return clientDao.findByClientidEqualsAndClientenableTrue(id);
	}

	@Override
	public List<Client> getAll() {
		return clientDao.findByClientenableTrue();
	}

	@Override
	public Boolean delete(Integer id) {
		return null;
	}

	@Override
	public Client getClientByContact(String clientcontact) {
		return clientDao.findByClientcontactAndClientenableTrue(clientcontact);
	}

	@Override
	public Client getClientByEmail(String clientemail) {
		return clientDao.findByClientemailAndClientenableTrue(clientemail);
	}

	@Value("${app.keycloak.realm}")
	private String realm;


	private String realm2 = "keycloak-trailer";


	@Value("${app.keycloak.serverUrl}")
	private String serverUrl;
	@Value("http://localhost:8080/auth")
	private String authServerUrl;

	@Value("${app.keycloak.auth.tokenUrl}")
	private String tokenUrl;

	@Value("${app.keycloak.auth.clientId}")
	private String clientId;

	@Value("${app.keycloak.auth.clientSecret}")
	private String clientSecret;


	@Value("${keycloak.client-id}")
	private String kcClientId;

	@Value("${keycloak.client-secret}")
	private String kcClientSecret;

	@Value("${keycloak.token-url}")
	private String kcGetTokenUrl;

	@Value("${keycloak.logout-url}")
	private String kcLogoutUrl;

	@Value("${keycloak.revoke-token-url}")
	private String kcRevokeTokenUrl;

	@Value("${keycloak.admin-token-url}")
	private String kcAdminUrl;

	@Value("${keycloak.update-url}")
	private String kcUpdateUrl;
//
	@Value("${keycloak.realm-container-id}")
	private String kcRealmContainerId;

	@Value("${keycloak.user-role-id}")
	private String kcUserRoleId;

	@Value("${keycloak.user-role-name}")
	private String kcUserRoleName;

	@Value("${keycloak.user-role-name-users}")
	private String RoleUsersname;

	@Value("${keycloak.user-role-id-users}")
	private String RoleUsersid;

	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

	private static final String ACCESS_TOKEN = "Access-Token";
	private static final String REFRESH_TOKEN = "Refresh-Token";
	private static final String EXPIRES_IN = "Expires-In";

	private static final String DEVICE_ID = "Device-Id";

	private final Keycloak keycloak;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private KeycloakUtil keycloakUtil;



//	@Autowired
//	private RestTemplate restTemplate;

	@Override
	public UserResource getUser(String userId) {
		UsersResource usersResource = getUsersResource();

		// Ajout de vérification pour s'assurer que l'utilisateur existe
		try {
			UserResource userResource = usersResource.get(userId);
			return userResource;
		} catch (Exception e) {
			log.error("Failed to fetch user with ID: " + userId, e);
			throw new RuntimeException("Could not find user with ID: " + userId);
		}
	}



	//	@Override
	public String createUser(UserRegistrationRecord newUserRecord) {

		UserRepresentation userRepresentation = new UserRepresentation();
		userRepresentation.setEnabled(true);
		userRepresentation.setFirstName(newUserRecord.firstName());
		userRepresentation.setLastName(newUserRecord.lastName());
		userRepresentation.setUsername(newUserRecord.username());
//		userRepresentation.setEmail("exemple1@kymann.com");
		userRepresentation.setEmail(newUserRecord.email());
		userRepresentation.setEmailVerified(true);

		log.info("User: " + userRepresentation.getUsername());
		log.info("User Email: " + userRepresentation.getEmail());
		log.info("User First Name: " + userRepresentation.getFirstName());
		log.info("User Last Name: " + userRepresentation.getLastName());

		CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
		credentialRepresentation.setValue(newUserRecord.password());
		credentialRepresentation.setType(CredentialRepresentation.PASSWORD);

		userRepresentation.setCredentials(List.of(credentialRepresentation));

		UsersResource usersResource = getUsersResource();

		Response response = usersResource.create(userRepresentation);

		log.info("Status Code " + response.getStatus());
		log.info("Response body: " + response.readEntity(String.class));


		if (!Objects.equals(201, response.getStatus())) {

			throw new RuntimeException("Status code " + response.getStatus());
		}

		log.info("New user has bee created");
		log.info("User id auth: " + response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));
		UserRepresentation createdUser = usersResource.get(response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1")).toRepresentation();
		log.info("User: " + createdUser.getUsername());
		log.info("User ID: " + createdUser.getId());
		log.info("User Email: " + createdUser.getEmail());
		log.info("User First Name: " + createdUser.getFirstName());
		log.info("User Last Name: " + createdUser.getLastName());
//		List<UserRepresentation> userRepresentations = usersResource.searchByUsername(newUserRecord.username(), true);
//        if (userRepresentations.isEmpty()) {
//            throw new RuntimeException("No user found with username: " + newUserRecord.username());
//        }
//        log.info("User found with username: " + newUserRecord.username());
//        log.info("User id: " + userRepresentations);
//        for (UserRepresentation userRepresentation1 : userRepresentations) {
//            log.info("User id: " + userRepresentation1.getId());
//        }
////        UserRepresentation userRepresentation1 = userRepresentations.get(0);
//        UserRepresentation userRepresentation1 = userRepresentations.get(0);
//
//        sendVerificationEmail(userRepresentation1.getId());
		return createdUser.getId();
	}

	public String register(UserRegistrationRecord registerRequest) {
    // Récupérer le jeton d'accès administrateur
    String adminAccessToken = keycloakUtil.getAdminAccessToken(GRANT_TYPE_PASSWORD, restTemplate, kcAdminUrl);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + adminAccessToken);

    // Créer le JSON dynamique pour les informations de l'utilisateur
  String dynamicJsonForUserInfo = String.format(
    "{\"firstName\":\"%s\",\"lastName\":\"%s\",\"username\":\"%s\",\"email\":\"%s\",\"enabled\":true,\"realmRoles\":[\"COMPANY\"]}",
    registerRequest.firstName(), registerRequest.lastName(), registerRequest.username(),registerRequest.email());
  log.info("dynamicJsonForUserInfo : {}", dynamicJsonForUserInfo);
    try {
        // Envoyer la requête pour créer l'utilisateur
        ResponseEntity<Object> response = restTemplate.exchange(
                kcUpdateUrl,
                HttpMethod.POST,
                new HttpEntity<>(dynamicJsonForUserInfo, headers),
                Object.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            // Récupérer l'ID du nouvel utilisateur
            String newUserID = keycloakUtil.getUserSub(registerRequest.username(), headers, restTemplate, kcUpdateUrl);

            // Créer le JSON dynamique pour le mot de passe
            String dynamicJsonForPassword = String.format(
                    "{\"type\":\"password\",\"temporary\":false,\"value\":\"%s\"}",
                    registerRequest.password());

			log.info("dynamicJsonForPassword : {}", dynamicJsonForPassword);

            // Envoyer la requête pour définir le mot de passe
            restTemplate.exchange(
                    kcUpdateUrl + "/" + newUserID + "/reset-password",
                    HttpMethod.PUT,
                    new HttpEntity<>(dynamicJsonForPassword, headers),
                    Object.class);

            // Créer le JSON dynamique pour le rôle
            String dynamicJsonForRole = String.format(
                    "[{\"id\": \"%s\",\"name\": \"%s\",\"description\": \"\",\"composite\": false,\"clientRole\": false,\"containerId\": \"%s\"}]",
                    RoleUsersid, RoleUsersname, kcRealmContainerId);

            // Envoyer la requête pour attribuer le rôle
            restTemplate.exchange(
                    kcUpdateUrl + "/" + newUserID + "/role-mappings/realm",
                    HttpMethod.POST,
                    new HttpEntity<>(dynamicJsonForRole, headers),
                    Object.class);

            // Retourner l'ID du nouvel utilisateur
            return newUserID;
        }

    } catch (Exception e) {
        // Gérer l'exception si l'utilisateur existe déjà
		log.error("Error creating user: " + e.getMessage());
        throw new ResourceAlreadyExistsException(e.getMessage());

    }

    // Retourner null si la création de l'utilisateur a échoué
    return null;
}

	private UsersResource getUsersResource() {

		return keycloak.realm(realm).users();
	}
	public void emailVerification(String userId){

		UsersResource usersResource = getUsersResource();
		usersResource.get(userId).sendVerifyEmail();
	}

	@Override
	public Client addClient(Client client) {

		try{

			log.info("Client: "+client.getClientcontact());
			UserRegistrationRecord newUserRecord = new UserRegistrationRecord(client.getClientcontact(), client.getClientemail(), client.getClientnom(), client.getClientprenom(), client.getClientmotdepasse());

			String keycloackUserId = register(newUserRecord);
			emailVerification(keycloackUserId);
			log.debug("keycloackUserId: "+keycloackUserId);
			if (keycloackUserId == null) {
				log.error("Erreur lors de la creation du client dans keycloak");
				return null;
			}else {
				log.info("Client créé dans keycloak avec succès");
				Timestamp date = dateTools.DateTimeStamp();
				String password = encryptTools.hashPassword(client.getClientmotdepasse());
				client.setClientenable(true);
				client.setClientdatecreation(date);
				client.setClientmotdepasse(password);
				client.setKeycloackuserid(keycloackUserId);
				Role role = RoleDao.findByNomAndEnableTrue(RoleUsersname);
				log.info("Role: "+role.getNom(), role.getId());
				client.setRole(role);
				Client result = this.save(client);
				log.info("Client créé dans la base de données avec succès");
				return  result;
//				return client;
			}
		}catch (Exception e){

			log.error("Erreur lors du process inscription client: contact = {} | error={}", client.getClientcontact(),e.getMessage());

			throw new ResourceAlreadyExistsException(e.getMessage());

		}


	}


	@Override
	public Client updateClient(Client client, Integer id) {

//		try{
//
//			Client client2 = this.getById(id);
//			Optional.ofNullable(client.getClientcontact()).ifPresent(client2::setClientcontact);
//			Optional.ofNullable(client.getClientnom()).ifPresent(client2::setClientnom);
//			Optional.ofNullable(client.getClientprenom()).ifPresent(client2::setClientprenom);
//			Optional.ofNullable(client.getClientemail()).ifPresent(client2::setClientemail);
//			Optional.ofNullable(client.getClientdatedenaissance()).ifPresent(client2::setClientdatedenaissance);
//			Optional.ofNullable(client.getClientgenre()).ifPresent(client2::setClientgenre);
////			Optional.ofNullable(client.getClientnomprenomparent()).ifPresent(client2::setClientnomprenomparent);
////			Optional.ofNullable(client.getClientnomprenomenfant()).ifPresent(client2::setClientnomprenomenfant);
//
//			UserDto userDto =  UserDto.builder().email(client2.getClientemail()).firstname(client2.getClientprenom()).lastname(client2.getClientnom()).username(client2.getClientcontact()).build();
//			keycloackTools.updateUser(client2.getKeycloackuserid(),userDto);
//			Client result = this.save(client2);
////			this.saveInOthersDB(result);
//
//			return result;
//
//		}catch (Exception e){
//
//			log.error("Erreur lors du process de modification client: contact = {} | error={}", client.getClientcontact(),e.getMessage());
//
//			return null;
//
//		}
		return null;
	}

	@Override
	public List<Client> getAllClient(Integer clientid) {

		try{
			List<Client> clients = new ArrayList<>();

			if(clientid != null){
				Client client = this.getById(clientid);
				clients.add(client);

			} else{

				clients = this.getAll();
			}
			return clients;

		}catch (Exception e){

			log.error("Erreur lors du process d'affichage de la liste de client: error={} "+e.getMessage());

			return null;
		}


	}

	@Override
	public Boolean updatePassword(Integer clientid, String lastpassword, String newpassword) {

//		try{
//
//
//			Client client = this.getById(clientid);
//
//			if(encryptTools.checkPassword(lastpassword, client.getClientmotdepasse())){
//
//				newpassword = encryptTools.hashPassword(newpassword);
//				client.setClientmotdepasse(newpassword);
//				UserDto userDto =  UserDto.builder().email(client.getClientemail()).firstname(client.getClientprenom()).lastname(client.getClientnom()).password(newpassword).username(client.getClientcontact()).build();
//				keycloackTools.updateUser(client.getKeycloackuserid(),userDto);
//
//				Client result = this.save(client);
////				this.saveInOthersDB(result);
//				return true;
//
//			}else{
//
//				return false;
//
//			}
//
//		}catch (Exception e){
//
//			log.error("Erreur lors du process modification de mot de passe de client: error={} "+e.getMessage());
//
//			return null;
//		}
		return null;

	}

//	public List<RoleRepresentation> getUserRoles(String userId) {
//
//		return getUser(userId).roles().realmLevel().listAll();
////        return getUser(userId).roles().realmLevel().listAll();
//	}
		@Override
	public List<RoleRepresentation> getUserClientRoles(String userId) {
    UserResource userResource = getUser(userId);
    List<RoleRepresentation> clientRoles = userResource.roles().clientLevel(userId).listAll();

    return clientRoles;
}

	@Override
public Map<String, Object> getUserToken(String username, String password) {
		log.info("Récupération token pour l'utilisateur : username={}", username);
		LoginRequestDto loginRequest = new LoginRequestDto();
		loginRequest.setUsername(username);
		loginRequest.setPassword(password);
		log.info("loginRequest : {}", loginRequest);
		try {
			AccessTokenResponse accessTokenResponse = getAccessToken(loginRequest, restTemplate, GRANT_TYPE_PASSWORD, clientId, clientSecret, tokenUrl);
			Client client = getClientByContact(username);
			log.info("accessTokenResponse : {}", accessTokenResponse);

			if (Objects.nonNull(client) && Objects.nonNull(accessTokenResponse)) {
				if (encryptTools.checkPassword(password, client.getClientmotdepasse())) {
					Map<String, Object> response = new HashMap<>();
					response.put("client", client);
					response.put("accessTokenResponse", accessTokenResponse);
					return response;
				}
			}
		} catch (Exception e) {
			log.error("Failed to get user token: username={}; error={}", username, e.getMessage());
			throw new RuntimeException("Failed to get user token", e);
		}
        return null;
    }

//	@Override
//	public ResponseEntity<?> login(LoginRequestDto request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
//		return null;
//	}


	@Override
public List<RoleRepresentation> getUserRoles(String userId) {
    UserResource userResource = getUser(userId);
    List<RoleRepresentation> clientRoles = userResource.roles().clientLevel(userId).listAll();

    return clientRoles;
}
	@Override
	public ResponseEntity<?> login(LoginRequestDto loginRequest, HttpServletRequest servletRequest,
								   HttpServletResponse servletResponse) {
		String deviceId = servletRequest.getHeader(DEVICE_ID);

		TokenDto tokenDto = keycloakUtil.getAccessToken(loginRequest, restTemplate, GRANT_TYPE_PASSWORD,
				clientId,
				clientSecret, tokenUrl);
		servletResponse.addHeader(ACCESS_TOKEN, tokenDto.getAccess_token());
		servletResponse.addHeader(EXPIRES_IN, String.valueOf(tokenDto.getExpires_in()));
//		sessionStorage.putCache(REFRESH_TOKEN, deviceId, tokenDto.getRefresh_token(), 1800);

		return ResponseEntity.ok().body(LoginResponseDto.builder()
				.status("SUCCESS")
				.message("Login successful...")
				.accessToken(tokenDto.getAccess_token())
				.refreshToken(tokenDto.getRefresh_token())
				.build());
	}

	@Override
	public ResponseEntity<?> loginMobile(String username, String password) {
			// Récupérer le jeton d'accès administrateur
			String adminAccessToken = keycloakUtil.getAdminAccessToken(GRANT_TYPE_PASSWORD, restTemplate, kcAdminUrl);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", "Bearer " + adminAccessToken);
			String newUserID = keycloakUtil.getUserSub(username, headers, restTemplate, kcUpdateUrl);
			if (newUserID == null) {
				log.error("Erreur lors de la création du client dans keycloak");
				return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
			}
			if (getClientByEmail(username) != null  && getClientByEmail(username).getKeycloackuserid().equals(newUserID)) {
				log.error("Le client existe déjà");
				Client client = getClientByEmail(username);
//				throw new ResourceAlreadyExistsException("Le client existe déjà");
				return ResponseEntity.ok().body(client);
			}
        try {
            Timestamp date = dateTools.DateTimeStamp();
            String Clientpassword = encryptTools.hashPassword(password);
            Client client = new Client();
            client.setClientemail(username);
            client.setClientmotdepasse(Clientpassword);
            client.setClientdatecreation(date);
            client.setKeycloackuserid(newUserID);
            client.setRole(RoleDao.findByNomAndEnableTrue(RoleUsersname));
            client.setClientenable(true);
            Client result = this.save(client);
            log.info("Client créé dans la base de données avec succès");
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


	@Override
	public ResponseEntity<?> getUserTokenV2(String username, String password, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		LoginRequestDto loginRequest = new LoginRequestDto();
		loginRequest.setUsername(username);
		loginRequest.setPassword(password);
		Client client = getClientByContact(username);
		log.info("loginRequest : {}", loginRequest);
		// Vérifier le mot de passe localement
		if (!verifyPasswordLocally(username, password)) {
			log.error("Le mot de passe est incorrect");
			throw new ResourceNotFoundException("Le mot de passe est incorrect");
		}
		if (Objects.nonNull(client)) {
			try {
				TokenDto tokenDto = keycloakUtil.getAccessToken(loginRequest, restTemplate, GRANT_TYPE_PASSWORD,
						kcClientId,
						kcClientSecret, kcGetTokenUrl);
				servletResponse.addHeader(ACCESS_TOKEN, tokenDto.getAccess_token());
				servletResponse.addHeader(EXPIRES_IN, String.valueOf(tokenDto.getExpires_in()));
				Map<String, Object> response = new HashMap<>();
				response.put("client", client);
				response.put("accessTokenResponse", tokenDto);
				return ResponseEntity.ok().body(response);
			} catch (Exception e) {
				log.error("Failed to get user token: username={}; error={}", username, e.getMessage());
				throw new RuntimeException("Failed to get user token", e);
			}
		}throw new ResourceNotFoundException("Aucun client trouvé avec le contact: " + username);

	}

	private boolean verifyPasswordLocally(String username, String password) {
		// Implémentez ici la logique pour vérifier le mot de passe localement
		// Par exemple, vous pouvez récupérer l'utilisateur de la base de données et comparer les mots de passe
		Client client = clientDao.findByClientcontactAndClientenableTrue(username);
		if (client != null) {
			return encryptTools.checkPassword(password, client.getClientmotdepasse());
		}
		return false;
	}

	@Override
	public ResponseEntity<?> refreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String refreshToken) {
		TokenDto tokenDto = keycloakUtil.getRefreshToken(refreshToken, restTemplate, GRANT_TYPE_REFRESH_TOKEN,
				kcClientId, kcClientSecret, kcGetTokenUrl);

		// Ajoute le nouveau token d'accès et le temps d'expiration aux en-têtes de la réponse
		servletResponse.addHeader(ACCESS_TOKEN, tokenDto.getAccess_token());
		servletResponse.addHeader(EXPIRES_IN, String.valueOf(tokenDto.getExpires_in()));

		// Met à jour le cache de session avec le nouveau refresh token et son temps d'expiration
//		sessionStorage.putCache(REFRESH_TOKEN, deviceId, tokenDto.getRefresh_token(),
//				tokenDto.getRefresh_expires_in());

		// Retourne la réponse avec le nouveau token
		return ResponseEntity.ok().body(tokenDto);
	}

	@Override
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletRequest servletRequest, String refreshToken) {


		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
		requestBody.add("client_id", kcClientId);
		requestBody.add("client_secret", kcClientSecret);
		requestBody.add("refresh_token", refreshToken);

		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> logoutResponse = restTemplate.postForEntity(kcLogoutUrl, entity, String.class);

		requestBody = new LinkedMultiValueMap<>();
		requestBody.add("client_id", kcClientId);
		requestBody.add("client_secret", kcClientSecret);
		requestBody.add("token", refreshToken);

		HttpEntity<MultiValueMap<String, String>> revokeEntity = new HttpEntity<>(requestBody, headers);
		ResponseEntity<String> revokeResponse = restTemplate.postForEntity(kcRevokeTokenUrl, revokeEntity,
				String.class);

		if (logoutResponse.getStatusCode().is2xxSuccessful()
				&& revokeResponse.getStatusCode().is2xxSuccessful()) {
//			sessionStorage.removeCache(REFRESH_TOKEN, deviceId);
			return ResponseEntity.ok().body("Logout successful...");
		} else {
			return ResponseEntity.status(logoutResponse.getStatusCode()).body("Logout failed...");
		}
	}



	public AccessTokenResponse getTokenObject(String username, String password) {
		log.info("Récupération token pour l'utilisateur : username={}", username);
		log.info("Récupération token : username={}; password={}", username, password);
		log.info(clientId);
		log.info(clientSecret);
		log.info(tokenUrl);
		log.info(realm);

		// Vérification des paramètres nécessaires
		if (clientSecret == null || clientSecret.isEmpty()) {
			throw new IllegalArgumentException("Client secret is not provided");
		}
		if (tokenUrl == null || tokenUrl.isEmpty()) {
			throw new IllegalArgumentException("Token URL is not provided");
		}
		if (realm == null || realm.isEmpty()) {
			throw new IllegalArgumentException("Realm is not provided");
		}
		if (clientId == null || clientId.isEmpty()) {
			throw new IllegalArgumentException("Client ID is not provided");
		}Map<String, Object> clientCredentials = new HashMap<>();
		clientCredentials.put("grant_type", "password");
//		clientCredentials.put("client_id", clientId);
		clientCredentials.put("client_secret", clientSecret);
		log.info("clientCredentials : {}", clientCredentials);
		try {
			log.info("Récupération token : username={}; password={}", username, password);


			Configuration configuration = new Configuration(authServerUrl, realm, clientId, clientCredentials, null);
			log.debug("==========Step 2===========");

			// Création du client Authz
			AuthzClient authzClient = AuthzClient.create(configuration);
			log.debug("==========Step 3===========");

			// Obtention du token
			AccessTokenResponse response = authzClient.obtainAccessToken(username, password);
			log.debug("==========Step 4===========");
			log.debug("Token Response : {}", response);

			return response;

		} catch (Exception ex) {
			log.error("Erreur lors de la récupération du token pour l'utilisateur : username={}; error={}", username, ex.getMessage(), ex);
			return null;
		}
	}



public AccessTokenResponse getAccessToken(LoginRequestDto requestLogin, RestTemplate restTemplate,
                               String GRANT_TYPE_PASSWORD,
                               String kcClientId, String kcClientSecret, String kcGetTokenUrl) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    log.info("Récupération getAccessToken : username={}; password={}", requestLogin.getUsername(), requestLogin.getPassword());

    // Prépare le corps de la requête avec les informations de connexion et les détails du client
    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("grant_type", GRANT_TYPE_PASSWORD);
    requestBody.add("client_id", kcClientId);
    requestBody.add("client_secret", kcClientSecret);
    requestBody.add("username", requestLogin.getUsername());
    requestBody.add("password", requestLogin.getPassword());
    requestBody.add("scope", "openid");
	log.info("requestBody : {}", requestBody);
    // Ensure the URL is absolute
    String absoluteUrl =  kcGetTokenUrl;

    // Envoie la requête à Keycloak et obtient la réponse
    ResponseEntity<AccessTokenResponse> response = restTemplate.postForEntity(absoluteUrl,
            new HttpEntity<>(requestBody, headers), AccessTokenResponse.class);
	log.info("response : {}", response);
//	return response;
    // Retourne les détails du token
    return response.getBody();
}


}
