package com.kse.core.authkeycloak.controllers;

import com.kse.core.authkeycloak.dto.ClientDto;
import com.kse.core.authkeycloak.dto.LoginDto;
import com.kse.core.authkeycloak.dto.LoginRequestDto;
import com.kse.core.authkeycloak.dto.LoginmobileDto;
import com.kse.core.authkeycloak.entities.Client;
import com.kse.core.authkeycloak.logics.ClientLogic;
import com.kse.core.authkeycloak.tools.CodegeneratorTools;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*")
@Slf4j
@RequiredArgsConstructor
public class ClientController {
    //    	@Autowired
    private final ClientLogic clientLogic;

    @Autowired
    ModelMapper modelMapper;


    //	@Autowired
    CodegeneratorTools codegeneratorTools;

    @GetMapping
    public String test() {
        return "Hello";
    }

    @PostMapping("/test")
    public String test2() {
        return "Hello test";
    }


    /**
     * Cette Api permet l'inscription de client
     *
     * @param clientDto
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<?> addClient(@RequestBody @Valid ClientDto clientDto) {
        log.warn("Inscription client: contact={};", clientDto.getClientcontact());
        return new ResponseEntity<>(clientLogic.addClient(modelMapper.map(clientDto, Client.class)), HttpStatus.OK);

    }

    @PostMapping(value = "/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                                          @RequestHeader("RefreshToken") String refreshToken
    ) {
        return clientLogic.refreshToken(servletRequest, servletResponse, refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletRequest servletRequest, @RequestHeader("RefreshToken") String refreshToken) {
        return clientLogic.logout(request, servletRequest, refreshToken);
    }

    /**
     * Cette api permet d'avoir la liste de tous les clients
     *
     * @param clientid
     * @return
     */
    @GetMapping("/getall")
    public ResponseEntity<?> getAllClient(@RequestParam(value = "clientid", required = false) Integer clientid) {

        log.warn("Affichage de tous les clients");

        try {

            List<Client> clients = clientLogic.getAllClient(clientid);

            if (!clients.isEmpty()) {

                log.warn("Taille de la liste :" + clients.size());

                return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);

            } else {

                log.warn("Liste client vide");

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }
        } catch (Exception e) {

            log.error("Erreur affichage  liste de client: error={} " + e.getMessage());

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateClient(@PathVariable("id") Integer id, @RequestBody ClientDto clientDto) {

        log.warn("Modification client: id={} ", id);

        try {

            Client clientob1 = clientLogic.getById(id);

            if (Objects.isNull(clientob1)) {

                log.warn("Client inexistant: id={}");

                return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            }

            Client clientob2 = clientLogic.getClientByContact(clientDto.getClientcontact());

            if (Objects.isNull(clientob2) || (Objects.nonNull(clientob2) && clientob2.getClientid() == id)) {

                Client result = clientLogic.updateClient(modelMapper.map(clientDto, Client.class), id);
                log.warn("Modification client  terminée: id={} ", id);

                return new ResponseEntity<Client>(result, HttpStatus.OK);

            } else {

                log.warn("Client existe déjà: contact={}", clientDto.getClientcontact());

                return new ResponseEntity<Client>(HttpStatus.CONFLICT);

            }

        } catch (Exception e) {

            log.error("Erreur modification client: error={}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }


//    /**
//     * Cette api permet de modifier le mot de passe d'un compte client
//     * @param clientid
//     * @param lastpassword
//     * @param newpassword
//     * @return
//     */
//    @PutMapping("/updatepassword/{clientid}/{lastpassword}/{newpassword}")
//    public ResponseEntity updatePassword(@PathVariable("clientid") Integer clientid, @PathVariable("lastpassword") String lastpassword,  @PathVariable("newpassword") String newpassword){
//
//        try{
//
//            log.warn("Modification  mot de passe  client : id={} ", clientid);
//            Client client = clientLogic.getById(clientid);
//
//            if(client != null){
//
//                if(clientLogic.updatePassword(clientid, lastpassword, newpassword)){
//
//                    log.warn("Modification mot de passe client terminée: id={} ", clientid);
//
//                    return new ResponseEntity<>(HttpStatus.OK);
//
//                }else {
//
//                    log.warn("Ancien mot de passe incorrect: id={} ", clientid);
//
//                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//
//                }
//
//            }else{
//
//                log.warn("Client inexistant: id={}", clientid);
//
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//
//            }
//
//        } catch (Exception e) {
//
//            log.error("Erreur modification mot de passe client : id={}; error={} ",clientid, e.getMessage());
//
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//
//    }
//

    //    @GetMapping(value="/resendotpcode/{contact}")
//    public ResponseEntity<?> RenvoyerOtpCode(@PathVariable ("contact")  String contact){
//        try {
//            log.warn("Renvoie code otp :  contact={};", contact );
//            Client client = clientLogic.getClientByContact(contact);
//
//            if(client != null){
//                String codeOtp = codegeneratorTools.generateCode();
//                client.setClientcodeotp(codeOtp);
//                Client res = clientLogic.save(client);
//
//                log.warn("Renvoie code otp terminée : contact={} ", contact);
//                return new ResponseEntity<>(HttpStatus.OK);
//            }
//            else{
//                log.warn("Employé inexistant: contact={}", contact);
//                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//            }
//        } catch (Exception e) {
//            log.error("Erreur lors du renvoie du code otp:  contact={} | {} ", contact,e.getMessage());
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//    }
    @GetMapping("roles/{id}")
    public ResponseEntity<?> getClientRoles(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(clientLogic.getUserRoles(id));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<?> getUserRoles(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(clientLogic.getUserClientRoles(id));
    }

//
//    @PostMapping("/loginV2")
//    public ResponseEntity<?> loginV2(@RequestBody @Valid LoginDto login,
//                                     HttpServletRequest servletRequest,
//                                     HttpServletResponse servletResponse
//    ) {
//
//        log.warn("Tentative de connexion:" + login, servletRequest, servletResponse);
//        return clientLogic.getUserTokenV2(login.getUsername(), login.getPassword(), servletRequest, servletResponse);
//
//    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto login,
                                   HttpServletRequest servletRequest,
                                   HttpServletResponse servletResponse
    ) {

        log.warn("Tentative de connexion:" + login, servletRequest, servletResponse);
        return clientLogic.getUserTokenV2(login.getUsername(), login.getPassword(), servletRequest, servletResponse);

    }

    @PostMapping(value = "/mobile/login")
    public ResponseEntity<?> mobileLogin(@RequestBody @Valid LoginDto login

    ) {

        log.warn("Tentative de connexion:" + login  );
      return   clientLogic.loginMobile(login.getUsername(), login.getPassword()  );

//         return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
