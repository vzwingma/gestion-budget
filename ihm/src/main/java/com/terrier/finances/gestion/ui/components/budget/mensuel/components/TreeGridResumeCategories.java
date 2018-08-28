package com.terrier.finances.gestion.ui.components.budget.mensuel.components;

import com.terrier.finances.gestion.model.business.budget.ResumeTotalCategories;
import com.terrier.finances.gestion.operations.model.enums.EntetesGridResumeOperationsEnum;
import com.terrier.finances.gestion.ui.components.abstrait.AbstractUITreeGridComponent;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.resume.TreeGridResumeCategoriesController;
import com.terrier.finances.gestion.ui.styles.operations.OperationBudgetTypeRenderer;
import com.terrier.finances.gestion.ui.styles.resume.TreeGridResumeValeurCellStyle;
import com.vaadin.shared.MouseEventDetails.MouseButton;

/**
 * Tableau de suivi des dépenses
 * @author vzwingma
 *
 */
public class TreeGridResumeCategories extends AbstractUITreeGridComponent<TreeGridResumeCategoriesController, ResumeTotalCategories> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7187184070043964584L;

	public static final int TAILLE_COLONNE_VALEUR = 90;
	
	
	public TreeGridResumeCategories(){
		// Start controleur
		startControleur();
	}

	
	@Override
	public void paramComponentsOnTreeGrid() {
		
		addColumn(ResumeTotalCategories::getTypeTotal)
		.setId(EntetesGridResumeOperationsEnum.CATEGORIE.getId())
		.setCaption(EntetesGridResumeOperationsEnum.CATEGORIE.getLibelle());
		
		addColumn(ResumeTotalCategories::getTotalADate)
		.setId(EntetesGridResumeOperationsEnum.VALEUR_NOW.getId())
		.setCaption(EntetesGridResumeOperationsEnum.VALEUR_NOW.getLibelle())
		.setWidth(TAILLE_COLONNE_VALEUR)
		.setRenderer(new OperationBudgetTypeRenderer())
		.setStyleGenerator(new TreeGridResumeValeurCellStyle(EntetesGridResumeOperationsEnum.VALEUR_NOW));
		
		addColumn(ResumeTotalCategories::getTotalFinMois)
		.setId(EntetesGridResumeOperationsEnum.VALEUR_FIN.getId())
		.setCaption(EntetesGridResumeOperationsEnum.VALEUR_FIN.getLibelle())
		.setWidth(TAILLE_COLONNE_VALEUR)
		.setRenderer(new OperationBudgetTypeRenderer())
		.setStyleGenerator(new TreeGridResumeValeurCellStyle(EntetesGridResumeOperationsEnum.VALEUR_FIN));
		
		// Pas de sélection de lignes
		setSelectionMode(SelectionMode.NONE);

		addItemClickListener(event -> {
			// Double click / collapse/expend all
			if(MouseButton.LEFT.equals(event.getMouseEventDetails().getButton()) && event.getMouseEventDetails().isDoubleClick()){
				getControleur().collapseExpendTreeGrid();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUITreeTableComponent#createControleur()
	 */
	@Override
	public TreeGridResumeCategoriesController createControleurTreeGrid() {
		return new TreeGridResumeCategoriesController(this);
	}
}
