/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.ui;

import com.terrier.finances.gestion.services.budget.business.OperationsService;
import com.terrier.finances.gestion.services.parametrages.business.ParametragesService;
import com.terrier.finances.gestion.services.utilisateurs.business.AuthenticationService;
import com.terrier.finances.gestion.ui.communs.services.FacadeServices;
import com.terrier.finances.gestion.ui.login.business.UserUISession;
import com.terrier.finances.gestion.ui.login.business.UserUISessionsService;
import com.vaadin.ui.Window;

/**
 * Méthodes génériques à tous les controleurs UI
 * @author vzwingma
 *
 */
public interface IUIControleurService  {


	/**
	 * Set popup modale
	 * @param popupModale enregistre la popup
	 */
	public default void setPopupModale(Window popupModale){
		getUserSession().setPopupModale(popupModale);
	}


	/**
	 * @return la session de l'UI
	 */
	public default UserUISession getUserSession(){
		return getServiceUserSessions().getSession();
	}

	/**
	 * @return service métier dépense
	 */
	public default OperationsService getServiceOperations(){
		return FacadeServices.get().getServiceOperations();
	}
	
	/**
	 * @return service paramétrage
	 */
	public default ParametragesService getServiceParams(){
		return FacadeServices.get().getServiceParams();
	}
	/**
	 * @return service User
	 */
	default UserUISessionsService getServiceUserSessions(){
		return FacadeServices.get().getServiceUserSessions();
	}
	/**
	 * @return service auth
	 */
	@Deprecated
	public default AuthenticationService getServiceAuthentification(){
		return FacadeServices.get().getServiceAuth();
	}
	/**
	 * @return service auth
	 */
	public default com.terrier.finances.gestion.ui.communs.services.AuthenticationRestService getRestServiceAuthentification(){
		return FacadeServices.get().getServiceRestAuth();
	}
}