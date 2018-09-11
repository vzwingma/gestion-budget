/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.ui;

import com.terrier.finances.gestion.services.comptes.api.ComptesAPIService;
import com.terrier.finances.gestion.services.operations.api.OperationsAPIService;
import com.terrier.finances.gestion.services.parametrages.api.ParametragesAPIService;
import com.terrier.finances.gestion.services.utilisateurs.api.UtilisateurAPIService;
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
	public default OperationsAPIService getServiceOperations(){
		return FacadeServices.get().getServiceOperations();
	}
	
	/**
	 * @return service paramétrage
	 */
	public default ParametragesAPIService getServiceParams(){
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
	public default UtilisateurAPIService getServiceUtilisateurs(){
		return FacadeServices.get().getServiceUtilisateurs();
	}
	
	/**
	 * @return service auth
	 */
	public default ComptesAPIService getServiceComptes(){
		return FacadeServices.get().getServiceComptes();
	}
}