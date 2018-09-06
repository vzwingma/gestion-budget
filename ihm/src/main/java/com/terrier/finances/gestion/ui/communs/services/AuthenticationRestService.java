package com.terrier.finances.gestion.ui.communs.services;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.communs.utilisateur.model.api.AuthentificationRestObject;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
import com.terrier.finances.gestion.ui.communs.abstrait.rest.AbstractHTTPClient;

/**
 * Service API vers {@link AuthenticationService}
 * @author vzwingma
 *
 */
@Controller
public class AuthenticationRestService extends AbstractHTTPClient {
	

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationRestService.class);
	
	/**
	 * Validation login/mdp
	 * @param login login
	 * @param motPasseEnClair mdp
	 * @return si valide
	 */
	public String authenticate(String login, String motPasseEnClair){
		final String URI = "http://localhost:8080/ihm/rest/authentification/v1/";

		Invocation.Builder invoque = getInvocation(URI, "authenticate");
		
		Entity<AuthentificationRestObject> auth = Entity.entity(new AuthentificationRestObject(login, motPasseEnClair), MediaType.APPLICATION_JSON_TYPE);
		LOGGER.info("Appel de {}", URI);
		Object resultat =  callHTTPPost(invoque, auth);
		LOGGER.info("Résultat : {}", resultat);
		return "result";
	}
}
