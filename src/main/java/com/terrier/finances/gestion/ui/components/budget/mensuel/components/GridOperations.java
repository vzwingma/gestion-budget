package com.terrier.finances.gestion.ui.components.budget.mensuel.components;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.terrier.finances.gestion.model.business.budget.LigneDepense;
import com.terrier.finances.gestion.model.business.parametrage.CategorieDepense;
import com.terrier.finances.gestion.model.enums.EntetesTableSuiviDepenseEnum;
import com.terrier.finances.gestion.model.enums.TypeDepenseEnum;
import com.terrier.finances.gestion.ui.components.abstrait.AbstractUIGridComponent;
import com.terrier.finances.gestion.ui.components.budget.mensuel.ActionsLigneBudget;
import com.terrier.finances.gestion.ui.components.budget.mensuel.binder.LigneOperationEditorBinder;
import com.terrier.finances.gestion.ui.controler.budget.mensuel.liste.operations.GridOperationsController;
import com.terrier.finances.gestion.ui.listener.budget.mensuel.editor.GridEditorListener;
import com.terrier.finances.gestion.ui.styles.operations.OperationBudgetTypeRenderer;
import com.vaadin.ui.renderers.ComponentRenderer;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.renderers.TextRenderer;

/**
 * Tableau de suivi des opérations
 * @author vzwingma
 *
 */
public class GridOperations extends AbstractUIGridComponent<GridOperationsController, LigneDepense> {

	//
	private static final long serialVersionUID = -7187184070043964584L;

	public static final int TAILLE_COLONNE_DATE = 95;
	public static final int TAILLE_COLONNE_CATEGORIE = 150;
	public static final int TAILLE_COLONNE_AUTEUR = 100;
	public static final int TAILLE_COLONNE_DATE_EDITEE = 150;
	public static final int TAILLE_COLONNE_ACTIONS = 110;
	public static final int TAILLE_COLONNE_TYPE_MENSUEL = 65;
	public static final int TAILLE_COLONNE_VALEUR = 100;
	
	private final SimpleDateFormat dateFormatMaj = new
			SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
	private final SimpleDateFormat dateFormatOperations = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
	private final TimeZone tzParis = TimeZone.getTimeZone("Europe/Paris");

	/**
	 * Constructure : démarrage du controleur
	 */
	public GridOperations(){
		
		dateFormatMaj.setTimeZone(tzParis);
		dateFormatOperations.setTimeZone(tzParis);
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
	@Override
	public void paramComponentsOnGrid() {
		
		LigneOperationEditorBinder binderLD = new LigneOperationEditorBinder(getControleur().getServiceParams().getCategories());
		getEditor().setBinder(binderLD);
		
		GridEditorListener editorListener = new GridEditorListener(getControleur());
		getEditor().addOpenListener(editorListener);
		getEditor().addSaveListener(editorListener);
		getEditor().addCancelListener(editorListener);
		setSelectionMode(SelectionMode.SINGLE);
		
		Column<LigneDepense, Date> c = addColumn(LigneDepense::getDateOperation);
		c.setId(EntetesTableSuiviDepenseEnum.DATE_OPERATION.name())
			.setCaption(EntetesTableSuiviDepenseEnum.DATE_OPERATION.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE)
			.setHidable(true)
			.setResizable(false);
		c.setRenderer(new DateRenderer(dateFormatMaj));
		// Pas éditable

		Column<LigneDepense, CategorieDepense> c2 = addColumn(LigneDepense::getCategorie);
		c2.setId(EntetesTableSuiviDepenseEnum.CATEGORIE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.CATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c2.setRenderer(new TextRenderer(""));
		// Pas éditable
		c2.setEditorBinding(binderLD.bindCategories());
		
		Column<LigneDepense, CategorieDepense> c3 = addColumn(LigneDepense::getSsCategorie);
		c3.setId(EntetesTableSuiviDepenseEnum.SSCATEGORIE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.SSCATEGORIE.getLibelle())
			.setWidth(TAILLE_COLONNE_CATEGORIE)
			.setHidable(true)
			.setResizable(false);
		c3.setRenderer(new TextRenderer(""));
		c3.setEditorBinding(binderLD.bindSSCategories());
		
		Column<LigneDepense, String> c4 = addColumn(LigneDepense::getLibelle);
		c4.setId(EntetesTableSuiviDepenseEnum.LIBELLE_VIEW.name())
			.setCaption(EntetesTableSuiviDepenseEnum.LIBELLE_VIEW.getLibelle())
			.setHidable(true)
			.setResizable(false);
		c4.setRenderer(new TextRenderer(""));
		// Binding Edition
		c4.setEditorBinding(binderLD.bindLibelle());
		
		Column<LigneDepense, String> c5 = addColumn(LigneDepense::getLibelle);
		c5.setId(EntetesTableSuiviDepenseEnum.LIBELLE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.LIBELLE.getLibelle())
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c5.setRenderer(new TextRenderer(""));
		// Binding Edition
		c5.setEditorBinding(binderLD.bindLibelle());
			
		Column<LigneDepense, TypeDepenseEnum> c6 = addColumn(LigneDepense::getTypeDepense);
		c6.setId(EntetesTableSuiviDepenseEnum.TYPE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.TYPE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c6.setRenderer(new OperationBudgetTypeRenderer());
		// Binding Edition	
		c6.setEditorBinding(binderLD.bindTypeDepense());
		
		Column<LigneDepense, Float> c7 = addColumn(LigneDepense::getValeur);
		c7.setId(EntetesTableSuiviDepenseEnum.VALEUR.name())
			.setCaption(EntetesTableSuiviDepenseEnum.VALEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_VALEUR)
			.setHidable(true)
			.setResizable(false);
		c7.setRenderer(new OperationBudgetTypeRenderer());
		//		c7.setColumnAlignment(EntetesTableSuiviDepenseEnum.VALEUR.name(), Align.RIGHT);
		c7.setEditorBinding(binderLD.bindValeur());
		
		Column<LigneDepense, Boolean> c8 = addColumn(LigneDepense::isPeriodique);
		c8.setId(EntetesTableSuiviDepenseEnum.PERIODIQUE.name())
			.setCaption(EntetesTableSuiviDepenseEnum.PERIODIQUE.getLibelle())
			.setWidth(TAILLE_COLONNE_TYPE_MENSUEL)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c8.setRenderer(new OperationBudgetTypeRenderer());
		c8.setEditorBinding(binderLD.bindPeriodique());

		Column<LigneDepense, ActionsLigneBudget> c9 = addColumn(LigneDepense::getActionsOperation);
		c9.setId(EntetesTableSuiviDepenseEnum.ACTIONS.name())
			.setCaption(EntetesTableSuiviDepenseEnum.ACTIONS.getLibelle())
			.setWidth(TAILLE_COLONNE_ACTIONS)
			.setHidable(true)
			.setResizable(false);
		c9.setRenderer(new ComponentRenderer());
		
		Column<LigneDepense, Date> c10 = addColumn(LigneDepense::getDateMaj);
		c10.setId(EntetesTableSuiviDepenseEnum.DATE_MAJ.name())
			.setCaption(EntetesTableSuiviDepenseEnum.DATE_MAJ.getLibelle())
			.setWidth(TAILLE_COLONNE_DATE + 10D)
			.setHidable(true)
			.setResizable(false);
		c10.setRenderer(new DateRenderer(dateFormatMaj));
		// Not editable
		
		Column<LigneDepense, String> c11 = addColumn(LigneDepense::getAuteur);
		c11.setId(EntetesTableSuiviDepenseEnum.AUTEUR.name())
			.setCaption(EntetesTableSuiviDepenseEnum.AUTEUR.getLibelle())
			.setWidth(TAILLE_COLONNE_AUTEUR)
			.setHidden(true)
			.setHidable(true)
			.setResizable(false);
		c11.setRenderer(new TextRenderer(""));
		// Not editable		

		getEditor().setEnabled(true);
	}
}