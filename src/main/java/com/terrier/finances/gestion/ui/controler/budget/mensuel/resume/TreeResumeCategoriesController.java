/**
 * 
 */
package com.terrier.finances.gestion.ui.controler.budget.mensuel.resume;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.terrier.finances.gestion.model.business.budget.BudgetMensuel;
import com.terrier.finances.gestion.model.data.DataUtils;
import com.terrier.finances.gestion.ui.components.budget.mensuel.components.TreeResumeCategories;
import com.terrier.finances.gestion.ui.controler.common.AbstractUIController;

/**
 * Controleur du tableau des résumés
 * @author vzwingma
 *
 */
public class TreeResumeCategoriesController extends AbstractUIController<TreeResumeCategories>{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5190668755144306669L;
	
	private final SimpleDateFormat auDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
	private final SimpleDateFormat finDateFormat = new SimpleDateFormat("MMM yyyy", Locale.FRENCH);

	public static final int TAILLE_COLONNE_VALEUR = 90;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TreeResumeCategoriesController.class);
	
	/**
	 * @param composant
	 */
	public TreeResumeCategoriesController(TreeResumeCategories composant) {
		super(composant);
	}


  
	@Deprecated
	public void initDynamicComponentsOnPage() {
		/**
		 * Total resume
		
		getComponent().addContainerProperty(EntetesTreeResumeDepenseEnum.CATEGORIE.getId(), String.class, null);
		getComponent().addContainerProperty(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId(), Double.class, null);
		getComponent().setColumnWidth(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId(), TAILLE_COLONNE_VALEUR);
		getComponent().setColumnAlignment(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId(), Align.RIGHT);
		
		getComponent().addContainerProperty(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId(), Double.class, null);
		getComponent().setColumnWidth(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId(), TAILLE_COLONNE_VALEUR);
		getComponent().setColumnAlignment(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId(), Align.RIGHT);
		
		getComponent().setColumnHeader(EntetesTreeResumeDepenseEnum.CATEGORIE.getId(), EntetesTreeResumeDepenseEnum.CATEGORIE.getLibelle());		
		 */
	}
	
	
	

	@Override
	public void miseAJourVueDonnees() { 
		// Style
		/*
		getComponent().setCellStyleGenerator(new TreeResumeCellStyle());
		getComponent().addActionHandler(new TreeResumeCategoriesActionMenuHandler());
		*/
	}
	
	/**
	 * Effacement des données
	 */
	public void resetVueDonnees(){
		/*
		getComponent().removeAllItems();
		getComponent().refreshRowCache();
		*/
	}
	
	/**
	 * Mise à jour des données suite au budget
	 * @param refreshAllTable refresh total en cas de changemetn de mois ou de compte
	 * @param budget budget à jour
	 * @param dateBudget date du budget
	 */
	public void miseAJourVueDonnees(BudgetMensuel budget){
		

		LocalDate dateDerniereOperation = DataUtils.getMaxDateListeOperations(budget.getListeDepenses());
		/*
		getComponent().setColumnHeader(EntetesTreeResumeDepenseEnum.VALEUR_NOW.getId(), 
				EntetesTreeResumeDepenseEnum.VALEUR_NOW.getLibelle()+ auDateFormat.format(dateBudget.getTime()));
		getComponent().setColumnHeader(EntetesTreeResumeDepenseEnum.VALEUR_FIN.getId(), 
				EntetesTreeResumeDepenseEnum.VALEUR_FIN.getLibelle() +finDateFormat.format(dateBudget.getTime()));
		// Mise à jour
		int i=1;
		int j=1;
		getComponent().removeAllItems();
		getComponent().refreshRowCache();

		// Tri des catégories
		List<CategorieDepense> listeCategories;
		try {
			listeCategories = getServiceParams().getCategories();
		} catch (DataNotFoundException e) {
			Notification.show("Erreur grave : Impossible de charger les données", Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		Collections.sort(listeCategories, new CategorieDepenseComparator());
		
		for (CategorieDepense categorie : listeCategories) {

			// LOGGER.trace("[{}]  >  Categorie : {} : {}" , i * 100, categorie, budget.getTotalParCategories().get(categorie));
			if(categorie != null && budget.getTotalParCategories().get(categorie) != null){
				
				getComponent().addItem(new Object[]{
						categorie.getLibelle(), 
						budget.getTotalParCategories().get(categorie)[0], 
						budget.getTotalParCategories().get(categorie)[1]}, 
						i * 100);


				for (CategorieDepense ssCategorie : categorie.getListeSSCategories()) {

					// LOGGER.trace("[{}]      > SsCategorie : {} : {}" , j, ssCategorie, budget.getTotalParSSCategories().get(ssCategorie));
					if(budget.getTotalParSSCategories().get(ssCategorie) == null){
						getComponent().addItem(new Object[]{ssCategorie, new double[]{0,0}}, j);
					}
					else{
						getComponent().addItem(new Object[]{ssCategorie.getLibelle(), 
								budget.getTotalParSSCategories().get(ssCategorie)[0], 
								budget.getTotalParSSCategories().get(ssCategorie)[1]}, 
								j);
					}
					// Organisation de l'arbre
					getComponent().setParent(j, i * 100);
					getComponent().setChildrenAllowed(j, false);
					j++;
				}
				i++;
			}
			else{
				LOGGER.warn("Attention : Catégorie vide");
			}
			
			for (Object objItemId : getComponent().getItemIds()) {
				Integer itemId = (Integer)objItemId;
				getComponent().setCollapsed(itemId, false);
			}
		}
		*/
	}
}