/**
 * 
 */
package com.terrier.finances.gestion.business.rest;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.terrier.finances.gestion.business.AuthenticationService;
import com.terrier.finances.gestion.business.BusinessDepensesService;
import com.terrier.finances.gestion.business.ParametragesService;
import com.terrier.finances.gestion.business.auth.UserAuthProvider;
import com.terrier.finances.gestion.data.transformer.DataTransformerBudget;
import com.terrier.finances.gestion.data.transformer.DataTransformerCategoriesDepense;
import com.terrier.finances.gestion.data.transformer.DataTransformerLigneDepense;
import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.model.business.parametrage.Utilisateur;
import com.terrier.finances.gestion.model.data.budget.BudgetMensuelDTO;
import com.terrier.finances.gestion.model.data.budget.BudgetMensuelXO;
import com.terrier.finances.gestion.model.data.budget.LigneDepenseDTO;
import com.terrier.finances.gestion.model.data.budget.LigneDepenseXO;
import com.terrier.finances.gestion.model.data.parametrage.CategorieDepenseXO;
import com.terrier.finances.gestion.model.data.parametrage.ContexteUtilisateurXO;
import com.terrier.finances.gestion.model.enums.EtatLigneDepenseEnum;
import com.terrier.finances.gestion.model.exception.BudgetNotFoundException;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;
import com.terrier.finances.gestion.model.exception.UserNotAuthorizedException;

/**
 * Controleur REST pour récupérer les budgets
 * @author vzwingma
 *
 */
@RestController
@RequestMapping(value="/rest/v2")
public class BudgetRestController2 {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetRestController2.class);


	/**
	 * Lien vers les services métier
	 */
	@Autowired
	private BusinessDepensesService businessDepenses;
	/**
	 * Lien vers les services métier
	 */
	@Autowired
	private ParametragesService businessParams;
	/**
	 * Lien vers les services métier
	 */
	@Autowired
	private AuthenticationService businessAuthentication;

	@Autowired @Qualifier("dataTransformerCategoriesDepense")
	private DataTransformerCategoriesDepense dataTransformerCategoriesDepense;
	@Autowired @Qualifier("dataTransformerBudget")
	private DataTransformerBudget dataTransformerBudget;
	@Autowired @Qualifier("dataTransformerLigneDepense")
	private DataTransformerLigneDepense dataTransformerLigneDepense;

	@Autowired
	private UserAuthProvider manager;

	/**
	 * @return contexte catégories
	 * @throws UserNotAuthorizedException utilisateur non trouvé
	 * @throws DataNotFoundException données non trouvées
	 */
	@RequestMapping(value="/categories/depenses", 
			method=RequestMethod.GET, produces = "application/json")
	public List<CategorieDepenseXO> getCategoriesDepenses(
			HttpServletRequest request) throws DataNotFoundException, UserNotAuthorizedException{

		Object userSpringSec = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LOGGER.debug("[REST][{}] Appel REST GetCategoriesDepenses", userSpringSec);
		if(userSpringSec != null && userSpringSec instanceof Utilisateur){
			try{
				return dataTransformerCategoriesDepense.transformBOstoXOs(businessParams.getCategories());
			}
			catch(Exception e){
				throw new DataNotFoundException(e.getMessage());
			}
		}
		else{
			throw new UserNotAuthorizedException();
		}
	}

	/**
	 * @param login login
	 * @param motpasse mot de passe
	 * @return contexte utilisateur
	 * @throws UserNotAuthorizedException utilisateur non trouvé
	 * @throws DataNotFoundException données non trouvées
	 */
	@RequestMapping(value="/utilisateur", 
			method=RequestMethod.GET, produces = "application/json")
	public ContexteUtilisateurXO getContexteUtilisateur(HttpServletRequest request) throws DataNotFoundException, UserNotAuthorizedException{
		Object userSpringSec = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		LOGGER.debug("[REST][{}] Appel REST getContexteUtilisateur", userSpringSec);		
		ContexteUtilisateurXO contexteUtilisateur = new ContexteUtilisateurXO();
		if(userSpringSec != null && userSpringSec instanceof Utilisateur){
			try{
				Utilisateur user = (Utilisateur)userSpringSec;
				contexteUtilisateur.setUtilisateur(user);
				List<CompteBancaire> comptes = businessParams.getComptesUtilisateur(user);
				contexteUtilisateur.setComptes(comptes);

				for (CompteBancaire compteBancaire : comptes) {
					List<BudgetMensuelDTO> listeBudget = businessDepenses.chargerBudgetsMensuelsConsultation(user, compteBancaire.getId());
					LOGGER.debug(" {} budget chargé pour {}", listeBudget != null ? listeBudget.size() : 0, compteBancaire.getLibelle());
					Calendar minBudget = null;
					Calendar maxBudget = null;
					// Calcul des min/max pour le compte
					for (BudgetMensuelDTO budgetDTO : listeBudget) {
						Calendar dateBudget = Calendar.getInstance();
						dateBudget.set(Calendar.DAY_OF_MONTH, 1);
						dateBudget.set(Calendar.MONTH, budgetDTO.getMois());
						dateBudget.set(Calendar.YEAR, budgetDTO.getAnnee());

						if(minBudget == null || dateBudget.before(minBudget)){
							minBudget = dateBudget;
						}
						if(maxBudget == null || dateBudget.after(maxBudget)){
							maxBudget = dateBudget;
						}
					}
					contexteUtilisateur.setIntervalleCompte(compteBancaire, minBudget, maxBudget);
				}
				return contexteUtilisateur;
			}
			catch(Exception e){
				throw new DataNotFoundException(e.getMessage());
			}
		}
		else{
			LOGGER.error("L'utilisateur n'a pas le droit d'utiliser cette ressource ");
			throw new UserNotAuthorizedException();
		}
	}

	/**
	 * @param idCompte compte
	 * @param strMois mois en chaine
	 * @param strAnnee année en chaine
	 * @return budget correspondant
	 * @throws DataNotFoundException  erreur de connexion à la BDD
	 * @throws BudgetNotFoundException  erreur budget introuvable
	 */
	@RequestMapping(value="/budget/{idCompte}/{strMois}/{strAnnee}", method=RequestMethod.GET, produces = "application/json",headers="Accept=application/json")
	public BudgetMensuelDTO getBudget(
			@PathVariable String idCompte, 
			@PathVariable String strMois,
			@PathVariable String strAnnee, 
			HttpServletRequest request) throws BudgetNotFoundException, DataNotFoundException, UserNotAuthorizedException{

		Object userSpringSec = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userSpringSec != null && userSpringSec instanceof Utilisateur){
			LOGGER.debug("[REST][{}] Appel REST getBudget : compte : {}, période : {}/{}", userSpringSec, idCompte, strMois, strAnnee);
			int mois = Calendar.getInstance().get(Calendar.MONTH);
			try{
				mois = Integer.parseInt(strMois);
			}
			catch(NumberFormatException e){
				LOGGER.error("Erreur dans le mois reçu : utilisation de la valeur courante {}", mois, e);
			}
			int annee = Calendar.getInstance().get(Calendar.YEAR);
			try{
				annee = Integer.parseInt(strAnnee);
			}
			catch(NumberFormatException e){
				LOGGER.error("Erreur dans l'année reçu : utilisation de la valeur courante {}", annee, e);
			}
			BudgetMensuel budgetBO = businessDepenses.chargerBudgetMensuel((Utilisateur)userSpringSec, idCompte, mois, annee);
			BudgetMensuelXO budget = dataTransformerBudget.transformBOtoXO(budgetBO);
			// Vérification que le buget est lisible par l'utilisateur
			for(Utilisateur proprietaire : budget.getCompteBancaire().getListeProprietaires()){
				if(proprietaire.getId().equals(((Utilisateur)userSpringSec).getId())){
					return budget;
				}
			}
			return null;
		}
		else{
			throw new UserNotAuthorizedException();
		}

	}



	/**
	 * Chargement de la liste des dépenses
	 * @param idbudget identifiant du budget
	 * @return liste des dépenses
	 * @throws UserNotAuthorizedException erreur d'authentification
	 * @throws DataNotFoundException  erreur de connexion à la BDD
	 */
	@RequestMapping(value="/depenses/{idbudget}", method=RequestMethod.GET, produces = "application/json",headers="Accept=application/json")
	public List<LigneDepenseXO> getLignesDepenses(@PathVariable String idbudget) 
			throws UserNotAuthorizedException, DataNotFoundException, BudgetNotFoundException{
		Object userSpringSec = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userSpringSec != null && userSpringSec instanceof Utilisateur){
			LOGGER.debug("[REST][{}] Appel REST getLignesDepenses : idbudget={}", userSpringSec, idbudget);
			List<LigneDepense> listeDepensesDTO = businessDepenses.chargerLignesDepenses(idbudget);
			return dataTransformerLigneDepense.transformBOtoXO(listeDepensesDTO);
		}
		throw new UserNotAuthorizedException();
	}

	
	
	/**
	 * Chargement de la liste des dépenses
	 * @param idbudget identifiant du budget
	 * @return liste des dépenses
	 * @throws UserNotAuthorizedException erreur d'authentification
	 * @throws DataNotFoundException  erreur de connexion à la BDD
	 * @throws BudgetNotFoundException erreur budget introuvable
	 */
	@RequestMapping(value="/depenses/{idBudget}/{idDepense}", method=RequestMethod.PUT, produces = "application/json",headers="Accept=application/json")
	public void updateDepense(@PathVariable String idBudget, @PathVariable String idDepense, @RequestBody LigneDepenseDTO depense) 
			throws UserNotAuthorizedException, DataNotFoundException, BudgetNotFoundException{

		Object userSpringSec = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(userSpringSec != null && userSpringSec instanceof Utilisateur){
			
			LOGGER.debug("[REST][{}] Appel REST updateDepense : idbudget={} et idDepense={} : [{}]", userSpringSec, idBudget, idDepense, depense);
			
			EtatLigneDepenseEnum nvEtat = null;
			if(depense.getEtat() != null && !depense.getEtat().equals("SUPPRIMER")){
				nvEtat = EtatLigneDepenseEnum.valueOf(depense.getEtat());
			}
			businessDepenses.majEtatLigneDepense(idBudget, idDepense, nvEtat, ((Utilisateur)userSpringSec).getLibelle());
		}
		else{
			throw new UserNotAuthorizedException();	
		}
		
	}



	/**
	 * Appel PING
	 * @return résultat du ping
	 */
	@RequestMapping(value="/ping", method=RequestMethod.GET)
	public String ping(){
		LOGGER.info("Appel ping du service");
		return "L'application est démarrée";
	}
}