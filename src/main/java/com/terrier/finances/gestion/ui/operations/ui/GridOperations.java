package com.terrier.finances.gestion.ui.operations.ui;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Locale;

import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation.Categorie;
import com.terrier.finances.gestion.communs.utils.data.BudgetDateTimeUtils;
import com.terrier.finances.gestion.ui.communs.abstrait.AbstractUIGridComponent;
import com.terrier.finances.gestion.ui.operations.actions.ui.ActionsOperation;
import com.terrier.finances.gestion.ui.operations.actions.ui.renderers.ActionsOperationRenderer;
import com.terrier.finances.gestion.ui.operations.edition.binder.LigneOperationEditorBinder;
import com.terrier.finances.gestion.ui.operations.edition.listeners.GridOperationsEditorListener;
import com.terrier.finances.gestion.ui.operations.edition.listeners.GridOperationsRightClickListener;
import com.terrier.finances.gestion.ui.operations.model.enums.EntetesGridOperationsEnum;
import com.terrier.finances.gestion.ui.operations.ui.renderers.OperationBudgetTypeRenderer;
import com.terrier.finances.gestion.ui.operations.ui.styles.GridOperationCellActionsStyle;
import com.terrier.finances.gestion.ui.operations.ui.styles.GridOperationCellStyle;
import com.terrier.finances.gestion.ui.operations.ui.styles.GridOperationCellValeurStyle;
import com.vaadin.contextmenu.GridContextMenu;
import com.vaadin.ui.renderers.LocalDateTimeRenderer;
import com.vaadin.ui.renderers.TextRenderer;

/**
 * Tableau de suivi des opérations
 * @author vzwingma
 *
 */
public class GridOperations extends AbstractUIGridComponent<GridOperationsController, LigneOperation> {

	//
	private static final long serialVersionUID = -7187184070043964584L;

	public static final int TAILLE_COLONNE_DATE = 105;
	public static final int TAILLE_COLONNE_CATEGORIE = 150;
	public static final int TAILLE_COLONNE_AUTEUR = 100;
	public static final int TAILLE_COLONNE_DATE_EDITEE = 150;
	public static final int TAILLE_COLONNE_ACTIONS = 110;
	public static final int TAILLE_COLONNE_TYPE_MENSUEL = 100;
	public static final int TAILLE_COLONNE_VALEUR = 100;
	
	private final SimpleDateFormat dateFormatOperations = new SimpleDateFormat(BudgetDateTimeUtils.DATE_DAY_PATTERN, Locale.FRENCH);

	/**
	 * Constructure : démarrage du controleur
	 */
	public GridOperations(){
		
		dateFormatOperations.setTimeZone(BudgetDateTimeUtils.getTzParis());
		// Start controleur
		startControleur();
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.AbstractUITableComponent#getControleur()
	 */
	@Override
	public GridOperationsController createControleurGrid() {
		return new GridOperationsController(this);
	}


	/* (non-Javadoc)
	 * @see com.terrier.finances.gestion.ui.components.abstrait.AbstractUIGridComponent#paramComponentsOnPage()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void paramComponentsOnGrid() {
		
		/**
		 *  Editor
		 */
		LigneOperationEditorBinder binderLD = new LigneOperationEditorBinder(getControleur().getServiceParams().getCategories());
		getEditor().setBinder(binderLD);
		
		GridOperationsEditorListener editorListener = new GridOperationsEditorListener(getControleur());
		getEditor().addOpenListener(editorListener);
		getEditor().addSaveListener(editorListener);
		getEditor().addCancelListener(editorListener);
		setSelectionMode(SelectionMode.SINGLE);
		
		/**
		 * Columns
		 */		
		Column<LigneOperation, LocalDateTime> c = addColumn(LigneOperation::getDateOperation);
		c.setId(EntetesGridOperationsEnum.DATE_OPERATION.name())
			.setCaption(EntetesGridOperationsEnum.DATE_OPERATION.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE)
			.setHidable(true)
			.setResizable(false);
		c.setRenderer(new LocalDateTimeRenderer(BudgetDateTimeUtils.DATE_DAY_HOUR_PATTERN));
		c.setStyleGenerator(new GridOperationCellStyle());
		// Pas éditable

		Column<LigneOperation, Categorie> c2 = addColumn(LigneOperation::getCategorie);
		c2.setId(EntetesGridOperationsEnum.CATEGORIE.name())
			.setCaption(EntetesGridOperationsEnum.CATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c2.setRenderer(new TextRenderer(""));
		c2.setStyleGenerator(new GridOperationCellStyle());
		// Pas éditable
		c2.setEditorBinding(binderLD.bindCategories());
		
		Column<LigneOperation, Categorie> c3 = addColumn(LigneOperation::getSsCategorie);
		c3.setId(EntetesGridOperationsEnum.SSCATEGORIE.name())
			.setCaption(EntetesGridOperationsEnum.SSCATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c3.setRenderer(new TextRenderer(""));
		c3.setStyleGenerator(new GridOperationCellStyle());
		c3.setEditorBinding(binderLD.bindSSCategories());
		
		Column<LigneOperation, String> c5 = addColumn(LigneOperation::getLibelle);
		c5.setId(EntetesGridOperationsEnum.LIBELLE.name())
			.setCaption(EntetesGridOperationsEnum.LIBELLE.getLibelle())
			.setHidable(true)
			.setResizable(false);
		c5.setRenderer(new TextRenderer(""));
		c5.setStyleGenerator(new GridOperationCellStyle());
		// Binding Edition
		c5.setEditorBinding(binderLD.bindLibelle());
			
		Column<LigneOperation, TypeOperationEnum> c6 = addColumn(LigneOperation::getTypeOperation);
		c6.setId(EntetesGridOperationsEnum.TYPE.name())
			.setCaption(EntetesGridOperationsEnum.TYPE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c6.setRenderer(new OperationBudgetTypeRenderer());
		c6.setStyleGenerator(new GridOperationCellStyle());
		// Binding Edition	
		c6.setEditorBinding(binderLD.bindTypeDepense());
		
		Column<LigneOperation, Double> c7 = addColumn(LigneOperation::getValeur);
		c7.setId(EntetesGridOperationsEnum.VALEUR.name())
			.setCaption(EntetesGridOperationsEnum.VALEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_VALEUR)
			.setHidable(true)
			.setResizable(false);
		c7.setRenderer(new OperationBudgetTypeRenderer());
		c7.setStyleGenerator(new GridOperationCellValeurStyle());
		c7.setEditorBinding(binderLD.bindValeur());
		
		Column<LigneOperation, Boolean> c8 = addColumn(LigneOperation::isPeriodique);
		c8.setId(EntetesGridOperationsEnum.PERIODIQUE.name())
			.setCaption(EntetesGridOperationsEnum.PERIODIQUE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c8.setRenderer(new OperationBudgetTypeRenderer());
		c8.setStyleGenerator(new GridOperationCellStyle());
		c8.setEditorBinding(binderLD.bindPeriodique());

		@SuppressWarnings("rawtypes")
		Column c9 = addColumn(ActionsOperation::new, new ActionsOperationRenderer());
		c9.setId(EntetesGridOperationsEnum.ACTIONS.name())
			.setCaption(EntetesGridOperationsEnum.ACTIONS.getLibelle())
			.setWidth(TAILLE_COLONNE_ACTIONS)
			.setHidable(true)
			.setResizable(false);
		c9.setStyleGenerator(new GridOperationCellActionsStyle());
		
		Column<LigneOperation, LocalDateTime> c10 = addColumn(LigneOperation::getDateMaj);
		c10.setId(EntetesGridOperationsEnum.DATE_MAJ.name())
			.setCaption(EntetesGridOperationsEnum.DATE_MAJ.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE)
			.setHidable(true)
			.setResizable(false);
		c10.setStyleGenerator(new GridOperationCellStyle());
		c10.setRenderer(new LocalDateTimeRenderer(BudgetDateTimeUtils.DATE_DAY_HOUR_PATTERN));
		// Not editable
		
		Column<LigneOperation, String> c11 = addColumn(LigneOperation::getAuteur);
		c11.setId(EntetesGridOperationsEnum.AUTEUR.name())
			.setCaption(EntetesGridOperationsEnum.AUTEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_AUTEUR)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c11.setRenderer(new TextRenderer(""));
		c11.setStyleGenerator(new GridOperationCellStyle());

		
		/**
		 * Context Menu
		 */
		GridContextMenu<LigneOperation> contextMenu = new GridContextMenu<>(this);
		GridOperationsRightClickListener menuCommand = new GridOperationsRightClickListener(getControleur());
		contextMenu.addGridBodyContextMenuListener(menuCommand);
	}
	
	
	/**
	 * Nom de la colonne
	 * @param nomColonne
	 * @return la colonne correspondante
	 */
	public Column<LigneOperation, ?> getColumn(EntetesGridOperationsEnum nomColonne){
		return super.getColumn(nomColonne.name());
	}
}
