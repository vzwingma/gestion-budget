/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.creation.listeners;

import java.util.Optional;
import java.util.stream.Collectors;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.parametrages.model.CategorieOperation;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.operations.creation.ui.CreerDepenseController;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.event.selection.SingleSelectionListener;

/**
 * Changement d'une ss catégorie dans le formulaire de création
 * Affichage du transfert intercompte
 * @author vzwingma
 *
 */
public class SelectionSousCategorieValueChangeListener extends AbstractComponentListener implements SingleSelectionListener<CategorieOperation>{

	// Controleur
	private CreerDepenseController controleur;

	public SelectionSousCategorieValueChangeListener(CreerDepenseController controleur){
		this.controleur = controleur;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7460353635366793837L;

	/**
	 * Si transfert intercompte affichage du choix du compte
	 * @see com.vaadin.data.Property.ValueChangeListener#valueChange(com.vaadin.data.Property.ValueChangeEvent)
	 */
	@Override
	public void selectionChange(SingleSelectionEvent<CategorieOperation> event) {
		Optional<CategorieOperation> catSelected = event.getFirstSelectedItem();
		if(catSelected.isPresent()){
			CategorieOperation ssCategorie = catSelected.get();	
			boolean interCompte = false;
			boolean reserve = false;
			if(ssCategorie != null){
				interCompte = IdsCategoriesEnum.TRANSFERT_INTERCOMPTE.getId().equals(ssCategorie.getId());
				reserve = IdsCategoriesEnum.RESERVE.getId().equals(ssCategorie.getId());
			}	

			/*
			 * Sélection d'un virement intercompte
			 */
			controleur.getComponent().getComboboxComptes().setVisible(interCompte);
			if(interCompte && controleur.getComponent().getComboboxComptes().isEmpty()){
				controleur.getComponent().getComboboxComptes().setItems(
						getServiceComptes().getComptes(getUserSession().getIdUtilisateur())
						.stream()
						.filter(CompteBancaire::isActif)
						.filter(c -> !c.getId().equals(getUserSession().getBudgetCourant().getCompteBancaire().getId()))
						.collect(Collectors.toList()));
			}
			controleur.getComponent().getLayoutCompte().setVisible(interCompte);
			controleur.getComponent().getLabelCompte().setVisible(interCompte);

			/*
			 * 	#121 sélection d'une réserve
			 */
			controleur.getComponent().getComboboxEtat().setVisible(!reserve);
			controleur.getComponent().getLabelEtat().setVisible(!reserve);

			/**
			 * Préparation du type de dépense
			 */
			if(ssCategorie != null){
				TypeOperationEnum typeAttendu = TypeOperationEnum.DEPENSE;
				if(IdsCategoriesEnum.SALAIRE.getId().equals(ssCategorie.getId()) || IdsCategoriesEnum.REMBOURSEMENT.getId().equals(ssCategorie.getId())){
					typeAttendu = TypeOperationEnum.CREDIT;
				}
				controleur.getComponent().getComboboxType().setSelectedItem(typeAttendu);
			}
		}
	}
}
