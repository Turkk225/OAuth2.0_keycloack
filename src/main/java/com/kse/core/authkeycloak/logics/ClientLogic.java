package com.kse.core.authkeycloak.logics;

import com.kse.core.authkeycloak.dto.LoginRequestDto;
import com.kse.core.authkeycloak.entities.Client;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.ResponseEntity;
//import com.kse.core.authkeycloak.Crud;

import java.util.List;
import java.util.Map;

public interface ClientLogic extends Crud<Client> {

    Client getClientByContact(String clientcontact);

    Client getClientByEmail(String clientemail);

    Client addClient(Client client);

    Client updateClient(Client client, Integer id);

    List<Client> getAllClient(Integer clientid);

    Boolean updatePassword(Integer clientid, String lastpassword, String newpassword);

    UserResource getUser(String userId);


    //    Object getUserRoles(String id);
    List<RoleRepresentation> getUserRoles(String userId);

    List<RoleRepresentation> getUserClientRoles(String userId);

    Map<String, Object> getUserToken(@NotNull(message = "Username obligatoire") String username, @NotNull(message = "Mot de passe obligatoire") String password);

    ResponseEntity<?> login(LoginRequestDto request, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    ResponseEntity<?> loginMobile(@NotNull(message = "Username obligatoire") String username, @NotNull(message = "Mot de passe obligatoire") String password);

    ResponseEntity<?> getUserTokenV2(@NotNull(message = "Username obligatoire") String username, @NotNull(message = "Mot de passe obligatoire") String password, HttpServletRequest servletRequest, HttpServletResponse servletResponse);

    ResponseEntity<?> refreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String refreshToken);

    ResponseEntity<?> logout(HttpServletRequest request, HttpServletRequest servletRequest, String refreshToken);
}
