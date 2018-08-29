package com.terrier.finances.gestion.ui.communs.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.services.parametrages.business.ParametragesService;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
import com.terrier.finances.gestion.ui.login.business.UserSessionsService;


/**
 * Facade des services pour appels depuis l'IHM
 * @author vzwingma
 *
 */
@Controller
public class FacadeServices {

	
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FacadeServices.class);

	// Gestionnaire des composants UI
	private static FacadeServices facadeServices;

	public FacadeServices(){
		LOGGER.debug("[INIT] UI FacadeServices");
		synchronized (FacadeServices.class) {
			facadeServices = this;	
		}
	}
	/**
	 * 
	 * Liens vers les services métiers
	 */
	@Autowired
	private OperationsService serviceOperations;

	@Autowired
	private ParametragesService serviceParams;

	@Autowired
	private AuthenticationService serviceAuth;
	
	@Autowired 
	private UserSessionsService serviceUserSessions;

	/**
	 * @return l'instance de la façade des services
	 */
	public static FacadeServices get(){
		return facadeServices;
	}
	
	

	/**
	 * @return the serviceDepense
	 */
	public OperationsService getServiceOperations() {
		return serviceOperations;
	}

	/**
	 * @param serviceOperations the serviceDepense to set
	 */
	public void setServiceOperations(OperationsService serviceOperations) {
		LOGGER.info("Injection du service métier Operations");
		this.serviceOperations = serviceOperations;
	}

	/**
	 * @return the serviceParams
	 */
	public ParametragesService getServiceParams() {
		return serviceParams;
	}

	/**
	 * @param serviceParams the serviceParams to set
	 */
	public void setServiceParams(ParametragesService serviceParams) {
		LOGGER.info("Injection de ParametragesService");
		this.serviceParams = serviceParams;
	}

	/**
	 * @return the serviceAuth
	 */
	public AuthenticationService getServiceAuth() {
		return serviceAuth;
	}

	/**
	 * @param serviceAuth the serviceAuth to set
	 */
	public void setServiceAuth(AuthenticationService serviceAuth) {
		LOGGER.info("Injection de AuthenticationService");
		this.serviceAuth = serviceAuth;
	}



	/**
	 * @return the serviceUserSessions
	 */
	public UserSessionsService getServiceUserSessions() {
		return serviceUserSessions;
	}



	/**
	 * @param serviceUserSessions the serviceUserSessions to set
	 */
	public void setServiceUserSessions(UserSessionsService serviceUserSessions) {
		LOGGER.info("Injection de UserSessionsManager");
		this.serviceUserSessions = serviceUserSessions;
	}

}
