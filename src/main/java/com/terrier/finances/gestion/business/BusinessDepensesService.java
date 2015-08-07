package com.terrier.finances.gestion.business;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.terrier.finances.gestion.data.DepensesDatabaseService;
import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.Utilisateur;
import com.terrier.finances.gestion.model.data.budget.BudgetMensuelDTO;
import com.terrier.finances.gestion.model.data.budget.LigneDepenseDTO;
import com.terrier.finances.gestion.model.enums.EntetesTableSuiviDepenseEnum;
import com.terrier.finances.gestion.model.enums.EtatLigneDepenseEnum;
import com.terrier.finances.gestion.model.enums.TypeDepenseEnum;
import com.terrier.finances.gestion.model.exception.BudgetNotFoundException;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;

/**
 * Service Métier : Dépenses
 * @author vzwingma
 *
 */
@Service
public class BusinessDepensesService {


	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessDepensesService.class);

	/**
	 * Lien vers les données
	 */
	@Autowired
	private DepensesDatabaseService dataDepenses;

	/**
	 * Paramétrages
	 */
	@Autowired
	private ParametragesService serviceParams;
	/**
	 * Auth
	 */
	@Autowired
	private AuthenticationService serviceAuth;

	public static final String ID_SS_CAT_TRANSFERT_INTERCOMPTE = "ed3f6100-5dbd-4b68-860e-0c97ae1bbc63";

	public static final String ID_SS_CAT_SALAIRE = "d005de34-f768-4e96-8ccd-70399792c48f";

	public static final String ID_SS_CAT_REMBOURSEMENT = "885e0d9a-6f3c-4002-b521-30169baf7123";

	public static final String ID_SS_CAT_RESERVE = "26a4b966-ffdc-4cb7-8611-a5ba4b518ef5";
	public static final String ID_SS_CAT_PREVISION_SANTE = "eeb2f9a5-49b4-4c44-86bf-3bd626412d8e";
	/**
	 * Chargement du budget du mois courant
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 */
	public BudgetMensuel chargerBudgetMensuel(Utilisateur utilisateur, String compte, int mois, int annee) throws BudgetNotFoundException, DataNotFoundException{
		LOGGER.debug("Chargement du budget {} de {}/{}", compte, mois, annee);
		BudgetMensuel budgetMensuel = null;
		try{
			budgetMensuel = this.dataDepenses.chargeBudgetMensuel(compte, mois, annee);
		}
		catch(BudgetNotFoundException e){
			budgetMensuel = initNewBudget(compte, utilisateur, mois, annee);
		}
		// Maj du budget ssi budhet actif
		if(budgetMensuel != null && budgetMensuel.isActif()){
			// Recalcul du résultat du mois précédent
			try{
				int moisPrecedent = 0;
				int anneePrecedente = annee;
				if(mois == Calendar.JANUARY){
					moisPrecedent = Calendar.DECEMBER;
					anneePrecedente = annee -1;
				}
				else{
					moisPrecedent = mois -1;
				}
				BudgetMensuel budgetPrecedent = this.dataDepenses.chargeBudgetMensuel(compte, moisPrecedent, anneePrecedente);
				if(budgetPrecedent.isActif()){
					calculBudget(budgetPrecedent);
				}
				budgetMensuel.setResultatMoisPrecedent(budgetPrecedent.getFinArgentAvance());
			}
			catch(BudgetNotFoundException e){ }
			// Résultat mensuel mis à jour
			calculBudgetEtSauvegarde(budgetMensuel);
		}

		return budgetMensuel;
	}
	
	/**
	 * Charge la date du premier budget déclaré pour ce compte pour cet utilisateur
	 * @param utilisateur utilisateur
	 * @param compte id du compte
	 * @return la date du premier budget décrit pour cet utilisateur
	 */
	public Calendar[] getDatePremierDernierBudgets(String compte) throws DataNotFoundException{
		return this.dataDepenses.getDatePremierDernierBudgets(compte);
	}

	/**
	 * Chargement du budget du mois courant
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 */
	/**
	 * @param utilisateur utilisateur
	 * @param compte compte
	 * @return liste des budget
	 * @throws DataNotFoundException erreur
	 */
	public List<BudgetMensuelDTO> chargerBudgetsMensuelsConsultation(Utilisateur utilisateur, String compte) throws DataNotFoundException{
		LOGGER.debug("Chargement des budgets {} de {}", compte, utilisateur);
		return this.dataDepenses.chargeBudgetsMensuelsDTO(compte);
	}




	/**
	 * Chargement du budget du mois courant en consultation
	 * @param compte compte 
	 * @param mois mois 
	 * @param annee année
	 * @return budget mensuel chargé et initialisé à partir des données précédentes
	 */
	public BudgetMensuelDTO chargerBudgetMensuelConsultation(String compte, int mois, int annee) throws BudgetNotFoundException, DataNotFoundException{
		LOGGER.debug("Chargement du budget {} de {}/{}", compte, mois, annee);
		BudgetMensuelDTO budgetMensuel = this.dataDepenses.chargeBudgetMensuelDTO(compte, mois, annee);
		// Résultat mensuel
		LOGGER.debug("(Calcul budget chargé) : " );
		// calculBudget(budgetMensuel);
		return budgetMensuel;
	}

	/**
	 * Chargement des lignes de dépenses en consultation
	 * @param idBudget idBudget
	 * @return lignes de dépenses du budget
	 * @throws DataNotFoundException erreur
	 */
	public List<LigneDepenseDTO> chargerLignesDepensesConsultation(String idBudget) throws DataNotFoundException{
		LOGGER.debug("Chargement des dépenses de {}", idBudget);
		List<LigneDepenseDTO> listeDepenses = this.dataDepenses.chargerLignesDepenses(idBudget);
		return listeDepenses;
	}

	/**
	 * Init new budget
	 * @param compte compte
	 * @param mois mois
	 * @param annee année
	 * @return budget nouvellement créé
	 * @throws BudgetNotFoundException erreur budhet
	 * @throws DataNotFoundException erreur données
	 */
	private BudgetMensuel initNewBudget(String compte, Utilisateur utilisateur, int mois, int annee) throws BudgetNotFoundException, DataNotFoundException{
		LOGGER.info("[INIT] Initialisation du budget {} de {}/{}", compte, mois, annee);
		BudgetMensuel budget = new BudgetMensuel();
		budget.setActif(true);
		budget.setAnnee(annee);
		budget.setMois(mois);

		budget.setCompteBancaire(serviceParams.getCompteById(compte, utilisateur));

		// Init si dans le futur par rapport au démarrage
		Calendar datePremierBudget = getDatePremierDernierBudgets(compte)[0];
		datePremierBudget.set(Calendar.DAY_OF_MONTH, 1);
		
		Calendar dateCourante = Calendar.getInstance();
		dateCourante.set(Calendar.MONTH, mois);
		dateCourante.set(Calendar.YEAR, annee);
		
		if(dateCourante.after(datePremierBudget)){
			// MAJ Calculs à partir du mois précédent
			// Mois précédent
			int moisPrecedent = 0;
			int anneePrecedente = annee;
			if(mois == Calendar.JANUARY){
				moisPrecedent = Calendar.DECEMBER;
				anneePrecedente = annee -1;
			}
			else{
				moisPrecedent = mois -1;
			}
			// Recherche du budget précédent 
			// Si impossible : BudgetNotFoundException
			BudgetMensuel budgetPrecedent = chargerBudgetMensuel(utilisateur, compte, moisPrecedent, anneePrecedente);
			initBudgetFromBudgetPrecedent(budget, budgetPrecedent);
		}
		else{
			LOGGER.error("[INIT] le buget {}/{} n'a jamais existé", mois, annee);
			throw new BudgetNotFoundException();
		}

		LOGGER.info("[INIT] Sauvegarde du Budget {}", budget);
		boolean save = this.dataDepenses.sauvegardeBudgetMensuel(budget);
		if(save){
			return budget;
		}
		else{
			return null;
		}
	}


	/**
	 * Réinitialiser un budget mensuel
	 * @param budgetMensuel budget mensuel
	 * @throws DataNotFoundException  erreur sur les données
	 * @throws BudgetNotFoundException budget introuvable
	 */
	public void reinitialiserBudgetMensuel(BudgetMensuel budgetMensuel, Utilisateur utilisateur) throws BudgetNotFoundException, DataNotFoundException{
		initNewBudget(budgetMensuel.getCompteBancaire().getId(), utilisateur, budgetMensuel.getMois(), budgetMensuel.getAnnee());
	}
	/**
	 * Initialisation du budget à partir du budget du mois précédent
	 * @param budget
	 * @param budgetPrecedent
	 */
	private void initBudgetFromBudgetPrecedent(BudgetMensuel budget, BudgetMensuel budgetPrecedent){
		// Calcul
		calculBudget(budgetPrecedent);
		budget.setCompteBancaire(budgetPrecedent.getCompteBancaire());
		budget.setMargeSecurite(budgetPrecedent.getMargeSecurite());
		budget.setResultatMoisPrecedent(budgetPrecedent.getFinArgentAvance());
		for (LigneDepense depenseMoisPrecedent : budgetPrecedent.getListeDepenses()) {
			if(depenseMoisPrecedent.isPeriodique() || depenseMoisPrecedent.getEtat().equals(EtatLigneDepenseEnum.REPORTEE)){
				budget.getListeDepenses().add(depenseMoisPrecedent.cloneDepenseToMoisSuivant());	
				budget.getSetLibellesDepensesForAutocomplete().add(depenseMoisPrecedent.getLibelle());
			}

		}
	}


	/**
	 * Ajout d'une ligne transfert intercompte
	 * @param ligneDepense ligne de dépense de transfert
	 * @param compteCrediteur compte créditeur
	 * @param auteur auteur de l'action
	 * @throws BudgetNotFoundException erreur budget introuvable
	 * @throws DataNotFoundException erreur données
	 */
	public void ajoutLigneTransfertIntercompte(BudgetMensuel budget, LigneDepense ligneDepense, String compteCrediteur, Utilisateur utilisateur) throws BudgetNotFoundException, DataNotFoundException{

		LOGGER.info("Ajout d'un transfert intercompte de {} vers {} > {} ", budget.getCompteBancaire().getLibelle(), compteCrediteur, ligneDepense);
		/**
		 *  Si transfert intercompte : Création d'une ligne dans le compte distant
		 */
		BudgetMensuel budgetTransfert = chargerBudgetMensuel(utilisateur, compteCrediteur, budget.getMois(), budget.getAnnee());
		LigneDepense ligneTransfert = new LigneDepense(ligneDepense.getSsCategorie(), "[de "+budget.getCompteBancaire().getLibelle()+"] " + ligneDepense.getLibelle(), TypeDepenseEnum.CREDIT, ligneDepense.getValeur(), EtatLigneDepenseEnum.PREVUE, ligneDepense.isPeriodique());
		ajoutLigneDepense(budgetTransfert, ligneTransfert, utilisateur.getLibelle());
		calculBudgetEtSauvegarde(budgetTransfert);
		/**
		 *  Ajout de la ligne dans le budget courant
		 */
		ligneDepense.setLibelle("[vers "+budgetTransfert.getCompteBancaire().getLibelle()+"] " + ligneDepense.getLibelle());
		ajoutLigneDepenseEtCalcul(budget, ligneDepense, utilisateur.getLibelle());
	}



	/**
	 * Ajout d'une ligne de dépense
	 * @param ligneDepense ligne de dépense
	 * @param auteur auteur de l'action
	 */
	private void ajoutLigneDepense(BudgetMensuel budget, LigneDepense ligneDepense, String auteur){
		LOGGER.info("Ajout d'une ligne de dépense : {}", ligneDepense);
		ligneDepense.setAuteur(auteur);
		ligneDepense.setDateMaj(Calendar.getInstance().getTime());
		budget.getListeDepenses().add(ligneDepense);
	}


	/**
	 * Ajout d'une ligne de dépense
	 * @param ligneDepense ligne de dépense
	 * @param auteur auteur de l'action 
	 */
	public void ajoutLigneDepenseEtCalcul(BudgetMensuel budget, LigneDepense ligneDepense, String auteur){
		ajoutLigneDepense(budget, ligneDepense, auteur);
		// Résultat mensuel
		calculBudgetEtSauvegarde(budget);
	}

	/**
	 * @param ligneId
	 * @return {@link LigneDepense} correspondance
	 */
	private LigneDepense getLigneDepense(BudgetMensuel budget, String ligneId){
		// Recherche de la ligne
		LigneDepense ligneDepense = null;
		for (LigneDepense ligne : budget.getListeDepenses()) {
			if(ligne.getId().equals(ligneId)){
				ligneDepense = ligne;
				break;
			}
		}
		return ligneDepense;
	}

	/**
	 * Mise à jour des lignes de dépenses
	 * 
	 * Attention : Pas d'appel à CalculBudget() car c'est fait seulement à la fin de toute la liste
	 * 
	 * @param ligneId  id de la ligne de dépense
	 * @param propertyId id de la propriété
	 * @param propClass classe de la propriété
	 * @param value nouvelle valeur
	 */
	public void majLigneDepense(BudgetMensuel budget, String ligneId, String propertyId, @SuppressWarnings("rawtypes") Class propClass, Object value, String auteur){
		// Recherche de la ligne
		LigneDepense ligneDepense = getLigneDepense(budget, ligneId);
		boolean ligneUpdated = false;
		// Maj du modele (sauf pour Etat=null car cela signifie suppression de la ligne)
		if(ligneDepense != null && propertyId.equals("Etat") && value == null){
			ligneUpdated = budget.getListeDepenses().remove(ligneDepense);
		}
		// Maj du modele (sauf pour CATEGORIE)
		else if(ligneDepense != null && !propertyId.equals(EntetesTableSuiviDepenseEnum.CATEGORIE.getId())){
			ligneUpdated = ligneDepense.updateProperty(propertyId, propClass, value);
		}

		if(ligneUpdated){
			ligneDepense.setDateMaj(Calendar.getInstance().getTime());
			ligneDepense.setAuteur(auteur);
		}
		// Mise à jour du budget
		budget.setDateMiseAJour(Calendar.getInstance());
	}	



	/**
	 * Mise à jour des lignes de dépenses
	 * @param ligneId
	 * @param etat
	 */
	public void majEtatLigneDepense(BudgetMensuel budget, String ligneId, EtatLigneDepenseEnum etat, String auteur){
		LigneDepense ligneDepense = getLigneDepense(budget, ligneId);
		// Mise à jour de l'état
		if(ligneDepense != null && etat != null){
			ligneDepense.setEtat(etat);
			if(EtatLigneDepenseEnum.REALISEE.equals(etat)){
				ligneDepense.setDateOperation(Calendar.getInstance().getTime());
			}
			else{
				ligneDepense.setDateOperation(null);
			}
			// Mise à jour de la ligne
			ligneDepense.setDateMaj(Calendar.getInstance().getTime());
			ligneDepense.setAuteur(auteur);
		}
		majLigneDepense(budget, ligneId, "Etat", EtatLigneDepenseEnum.class, etat, auteur);

		// Mise à jour du budget
		budget.setDateMiseAJour(Calendar.getInstance());
		// Budget
		calculBudgetEtSauvegarde(budget);
	}	


	/**
	 * Mise à jour de la ligne comme dernière opération
	 * @param ligneId
	 */
	public void setLigneDepenseAsDerniereOperation(BudgetMensuel budget, String ligneId){
		for (LigneDepense ligne : budget.getListeDepenses()) {
			if(ligne.getId().equals(ligneId)){
				LOGGER.info("Tag de la ligne {} comme dernière opération", ligne);
				ligne.setDerniereOperation(true);
			}
			else{
				ligne.setDerniereOperation(false);
			}
		}
		// Mise à jour du budget
		budget.setDateMiseAJour(Calendar.getInstance());
		dataDepenses.sauvegardeBudgetMensuel(budget);
	}

	/**
	 * Calcul du budget Courant et sauvegarde
	 * @param budget budget à sauvegarder
	 */
	public void calculBudgetEtSauvegarde(BudgetMensuel budget){
		calculBudget(budget);
		dataDepenses.sauvegardeBudgetMensuel(budget);
	}

	/**
	 * Calcul du résumé
	 * @param budgetMensuelCourant
	 */
	private void calculBudget(BudgetMensuel budget){

		LOGGER.info("(Re)Calcul du budget : {}", budget);
		budget.razCalculs();

		for (LigneDepense depense : budget.getListeDepenses()) {
			LOGGER.trace("     > {}", depense);
			int sens = depense.getTypeDepense().equals(TypeDepenseEnum.CREDIT) ? 1 : -1;
			budget.getSetLibellesDepensesForAutocomplete().add(depense.getLibelle());
			/**
			 *  Calcul par catégorie
			 */
			Double[] valeursCat = {0D,0D};
			if(budget.getTotalParCategories().get(depense.getCategorie()) != null){
				valeursCat = budget.getTotalParCategories().get(depense.getCategorie());
			}
			if(depense.getEtat().equals(EtatLigneDepenseEnum.REALISEE)){
				valeursCat[0] = valeursCat[0] + sens * depense.getValeur();
				valeursCat[1] = valeursCat[1] + sens * depense.getValeur();
			}
			else if(depense.getEtat().equals(EtatLigneDepenseEnum.PREVUE)){
				valeursCat[1] = valeursCat[1] + sens * depense.getValeur();
			}
			budget.getTotalParCategories().put(depense.getCategorie(), valeursCat);

			/**
			 *  Calcul par sous catégorie
			 */
			Double[] valeurSsCat = {0D,0D};
			if( budget.getTotalParSSCategories().get(depense.getSsCategorie()) != null){
				valeurSsCat = budget.getTotalParSSCategories().get(depense.getSsCategorie());
			}
			if(depense.getEtat().equals(EtatLigneDepenseEnum.REALISEE)){
				valeurSsCat[0] = valeurSsCat[0] + sens * depense.getValeur();
				valeurSsCat[1] = valeurSsCat[1] + sens * depense.getValeur();
			}
			if(depense.getEtat().equals(EtatLigneDepenseEnum.PREVUE)){
				valeurSsCat[1] = valeurSsCat[1] + sens * depense.getValeur();
			}
			budget.getTotalParSSCategories().put(depense.getSsCategorie(), valeurSsCat);



			// Si réserve : ajout dans le calcul fin de mois
			// Pour taper dans la réserve : inverser le type de dépense

			if(ID_SS_CAT_RESERVE.equals(depense.getSsCategorie().getId())){
				budget.setMargeSecuriteFinMois(budget.getMargeSecurite() + Double.valueOf(depense.getValeur()));
				sens = - sens;
			}

			/**
			 * Calcul des totaux
			 */

			if(depense.getEtat().equals(EtatLigneDepenseEnum.REALISEE)){
				budget.ajouteANowArgentAvance(sens * depense.getValeur());
				budget.ajouteANowCompteReel(sens * depense.getValeur());
				budget.ajouteAFinArgentAvance(sens * depense.getValeur());
				budget.ajouteAFinCompteReel(sens * depense.getValeur());				
			}
			else if(depense.getEtat().equals(EtatLigneDepenseEnum.PREVUE)){
				budget.ajouteAFinArgentAvance(sens * depense.getValeur());
				budget.ajouteAFinCompteReel(sens * depense.getValeur());
			}
		}
		LOGGER.debug("Argent avancé : {}  :   {}", budget.getNowArgentAvance(), budget.getFinArgentAvance());
		LOGGER.debug("Solde réel    : {}  :   {}", budget.getNowCompteReel(), budget.getFinCompteReel());
	}

	/**
	 * Lock/unlock d'un budget
	 * @param budgetActif
	 */
	public void setBudgetActif(BudgetMensuel budgetMensuel, boolean budgetActif){
		LOGGER.info("{} du budget {}/{} de {}", budgetActif ? "Réouverture" : "Fermeture", budgetMensuel.getMois(), budgetMensuel.getAnnee(), budgetMensuel.getCompteBancaire().getLibelle());
		budgetMensuel.setActif(budgetActif);
		calculBudgetEtSauvegarde(budgetMensuel);
	}
}