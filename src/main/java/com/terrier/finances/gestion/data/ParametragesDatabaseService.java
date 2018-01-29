package com.terrier.finances.gestion.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.terrier.finances.gestion.model.business.parametrage.CategorieDepense;
import com.terrier.finances.gestion.model.business.parametrage.CompteBancaire;
import com.terrier.finances.gestion.model.business.parametrage.Utilisateur;
import com.terrier.finances.gestion.model.exception.DataNotFoundException;

/**
 * Service de données en MongoDB fournissant les paramètres
 * @author vzwingma
 *
 */
@Repository
public class ParametragesDatabaseService extends AbstractDatabaseService {

	/**
	 * Liste des catégories
	 */
	private Map<String, CategorieDepense> mapCategories = new HashMap<String, CategorieDepense>();
	/**
	 * Liste des sous catégories
	 */
	private Map<String, CategorieDepense> mapSSCategories = new HashMap<String, CategorieDepense>();	
	/**
	 * Liste des utilisateurs
	 */
	private List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ParametragesDatabaseService.class);

	/**
	 * @return la liste des catégories
	 */
	public List<CategorieDepense> chargeCategories() throws DataNotFoundException{
		if(mapCategories.size() == 0){
			try{
				List<CategorieDepense> listeAllCategories =  getMongoOperation().findAll(CategorieDepense.class);
				// Ajout des catégories
				for (CategorieDepense depenseCategorie : listeAllCategories) {
					if(depenseCategorie.isCategorie()){
						mapCategories.put(depenseCategorie.getId(), depenseCategorie);
					}
				}

				// Ajout des sous catégories
				for (CategorieDepense depenseSousCategorie : listeAllCategories) {
					if(!depenseSousCategorie.isCategorie()){
						CategorieDepense categorieParente = mapCategories.get(depenseSousCategorie.getIdCategorieParente());
						if(categorieParente != null){
							depenseSousCategorie.setCategorieParente(categorieParente);
							categorieParente.getListeSSCategories().add(depenseSousCategorie);
						}
						mapSSCategories.put(depenseSousCategorie.getId(), depenseSousCategorie);
					}
				}
			}
			catch(Exception e){
				return new ArrayList<CategorieDepense>();
			}
		}
		return new ArrayList<CategorieDepense>(mapCategories.values());
	}



	/**
	 * @return la liste des catégories
	 */
	public CategorieDepense chargeCategorieParId(String id) throws DataNotFoundException{

		if(mapCategories.size() == 0){
			chargeCategories();
		}
		// Recherche parmi les catégories
		CategorieDepense categorie = mapCategories.get(id);

		// Sinon les sous catégories
		if(categorie == null){
			categorie = mapSSCategories.get(id);
		}
		return categorie;
	}




	/**
	 * @return la liste des catégories
	 */
	public Utilisateur chargeUtilisateur(String login) throws DataNotFoundException{
		try{
			LOGGER.info("Recherche de l'utilisateur {}", login);
			Query queryUser = new Query();
			queryUser.addCriteria(Criteria.where("login").is(login));
			return getMongoOperation().findOne(queryUser, Utilisateur.class);
		}
		catch(Exception e){
			LOGGER.error("Erreur lors de la recherche de l'utilisateur {}", login, e);
			throw new DataNotFoundException("Erreur lors de la recherche d'utilisateur " + login);
		}
	}


	/**
	 * @return la liste des catégories
	 */
	public void majUtilisateur(Utilisateur utilisateur){
		try{
			getMongoOperation().save(utilisateur);
		}
		catch(Exception e){
			LOGGER.error("Erreur lors de la sauvegarde de l'utilisateur", e);
		}
	}



	/**
	 * Chargement des comptes
	 * @param utilisateur utilisateur 
	 * @return liste des comptes associés
	 * @throws DataNotFoundException erreur dans la connexion
	 */
	public List<CompteBancaire> chargeComptes(Utilisateur utilisateur) throws DataNotFoundException{
		List<CompteBancaire>  listeComptes = new ArrayList<CompteBancaire>();
		try{
			LOGGER.info("Chargement des comptes de {} [_id={}]", utilisateur, utilisateur.getId());
			Query queryBudget = new Query().addCriteria(Criteria.where("listeProprietaires").elemMatch(Criteria.where("_id").is(utilisateur.getId())));

			listeComptes = getMongoOperation().find(queryBudget, CompteBancaire.class)
					.stream()
					.sorted((compte1, compte2) -> Integer.compare(compte1.getOrdre(), compte2.getOrdre()))
					.collect(Collectors.toList());
			LOGGER.info(" {} comptes chargés : {} ", listeComptes.size(), listeComptes);
		}
		catch(Exception e){
			LOGGER.error("Erreur lors du chargement des comptes de {}", utilisateur, e);
			throw new DataNotFoundException("Erreur lors de la recherche des comptes");
		}
		return listeComptes;
	}


	/**
	 * Chargement d'un compte par un id
	 * @param idCompte id du compte
	 * @param utilisateur utilisateur associé
	 * @return compte
	 * @throws DataNotFoundException
	 */
	public CompteBancaire chargeCompteParId(String idCompte) throws DataNotFoundException{
		try{
			LOGGER.info("Chargement du compte {}", idCompte);
			Query queryBudget = new Query();
			queryBudget.addCriteria(Criteria.where("id").is(idCompte));
			return getMongoOperation().findOne(queryBudget, CompteBancaire.class);
		}
		catch(Exception e){
			LOGGER.error("Erreur lors du chargement du compte {}", idCompte, e);
			throw new DataNotFoundException("Erreur lors de la recherche de compte");
		}
	}


	/**
	 * Suppression des données en mémoire
	 */
	public void resetData(){
		listeUtilisateurs.clear();
		mapCategories.clear();
		mapSSCategories.clear();
	}
}
