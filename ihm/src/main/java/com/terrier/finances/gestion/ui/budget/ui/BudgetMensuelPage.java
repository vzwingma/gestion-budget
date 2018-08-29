package com.terrier.finances.gestion.ui.budget.ui;

import com.terrier.finances.gestion.communs.comptes.model.CompteBancaire;
import com.terrier.finances.gestion.ui.communs.abstrait.ui.AbstractUIComponent;
import com.terrier.finances.gestion.ui.operations.ui.GridOperations;
import com.terrier.finances.gestion.ui.resume.categories.ui.TreeGridResumeCategories;
import com.terrier.finances.gestion.ui.resume.totaux.ui.GridResumeTotaux;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Page de gestion d'un budet
 * @author vzwingma
 *
 */
public class BudgetMensuelPage extends AbstractUIComponent<BudgetMensuelController> {

	@AutoGenerated
	private VerticalLayout mainLayout;


	@AutoGenerated
	private AbsoluteLayout absoluteLayout1;


	@AutoGenerated
	private HorizontalLayout budgetMensuel;


	@AutoGenerated
	private VerticalLayout verticalLayoutDepenses;


	@AutoGenerated
	private HorizontalLayout horizontalLayout2;


	@AutoGenerated
	private Button buttonLock;


	@AutoGenerated
	private Button buttonRefreshMonth;


	@AutoGenerated
	private HorizontalLayout horizontalLayoutActions;


	@AutoGenerated
	private Button buttonCreate;

	@AutoGenerated
	private GridOperations gridOperations;


	@AutoGenerated
	private VerticalLayout verticalLayoutResume;


	@AutoGenerated
	private GridResumeTotaux tableTotalResume;


	@AutoGenerated
	private TreeGridResumeCategories treeResume;


	@AutoGenerated
	private HorizontalLayout menu;


	@AutoGenerated
	private HorizontalLayout horizontalLayout1;


	@AutoGenerated
	private Button buttonDeconnexion;


	@AutoGenerated
	private Label labelLastConnected;


	@AutoGenerated
	private InlineDateField mois;


	@AutoGenerated
	private ComboBox<CompteBancaire> comboBoxComptes;

	//@AutoGenerated
	//private Button buttonStatis;


	private CompteBancaire compteSelectionne;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5059425148801750290L;

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public BudgetMensuelPage() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// Démarrage
		startControleur();
	}

	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public BudgetMensuelPage(CompteBancaire compteSelectionne) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
		this.compteSelectionne = compteSelectionne;
		// Démarrage
		startControleur();
	}

	
	/**
	 * @return the buttonLock
	 */
	public Button getButtonLock() {
		return buttonLock;
	}



	/**
	 * @return the buttonRefreshMonth
	 */
	public Button getButtonRefreshMonth() {
		return buttonRefreshMonth;
	}



	/**
	 * @param buttonLock the buttonLock to set
	 */
	public void setButtonLock(Button buttonLock) {
		this.buttonLock = buttonLock;
	}



	/**
	 * @return the tableSuiviDepense
	 */
	public GridOperations getGridOperations() {
		return gridOperations;
	}

	/**
	 * @return the treeResume
	 */
	public TreeGridResumeCategories getTreeResume() {
		return treeResume;
	}

	/**
	 * @return the inlineDateField_1
	 */
	public InlineDateField getMois() {
		return mois;
	}
	
	
	/**
	 * @return the tableTotalResume
	 */
	public GridResumeTotaux getGridResumeTotaux() {
		return tableTotalResume;
	}

	/**
	 * @return the buttonCreate
	 */
	public Button getButtonCreate() {
		return buttonCreate;
	}

	/**
	 * @return the button_2
	
	public Button getButtonStatistique() {
		return buttonStatis;
	}
	 */
	
	/**
	 * @return the comboBoxComptes
	 */
	public ComboBox<CompteBancaire> getComboBoxComptes() {
		return comboBoxComptes;
	}
	
	
	/**
	 * @return the buttonDeconnexion
	 */
	public Button getButtonDeconnexion() {
		return buttonDeconnexion;
	}

	@Override
	public BudgetMensuelController createControleur() {
		return new BudgetMensuelController(this);
	}

	/**
	 * @return the idCompteSelectionne
	 */
	public CompteBancaire getCompteSelectionne() {
		return compteSelectionne;
	}

	/**
	 * @return the labelLastConnected
	 */
	public Label getLabelLastConnected() {
		return labelLastConnected;
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		mainLayout.setMargin(true);
		
		// top-level component properties
		setSizeFull();
		
		// menu
		menu = buildMenu();
		mainLayout.addComponent(menu);
		mainLayout.setComponentAlignment(menu, new Alignment(20));
		
		// budgetMensuel
		budgetMensuel = buildBudgetMensuel();
		mainLayout.addComponent(budgetMensuel);
		mainLayout.setExpandRatio(budgetMensuel, 1.0f);
		mainLayout.setComponentAlignment(budgetMensuel, new Alignment(48));
		
		// absoluteLayout_1
		absoluteLayout1 = new AbsoluteLayout();
		absoluteLayout1.setSizeUndefined();
		mainLayout.addComponent(absoluteLayout1);
		
		return mainLayout;
	}

	@AutoGenerated
	private HorizontalLayout buildMenu() {
		// common part: create layout
		menu = new HorizontalLayout();
		menu.setSizeFull();
		menu.setHeight("40px");
		menu.setMargin(false);
		
		// comboBoxComptes
		comboBoxComptes = new ComboBox<>();
		comboBoxComptes.setWidth("300px");
		comboBoxComptes.setHeight("26px");
		menu.addComponent(comboBoxComptes);
		menu.setComponentAlignment(comboBoxComptes, new Alignment(33));
		
		// mois
		mois = new InlineDateField();
		mois.setSizeUndefined();
		menu.addComponent(mois);
		menu.setComponentAlignment(mois, new Alignment(48));
		
		// horizontalLayout_1
		horizontalLayout1 = buildHorizontalLayout1();
		menu.addComponent(horizontalLayout1);
		menu.setComponentAlignment(horizontalLayout1, new Alignment(34));
		
		return menu;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout1() {
		// common part: create layout
		horizontalLayout1 = new HorizontalLayout();
		horizontalLayout1.setSizeUndefined();
		horizontalLayout1.setMargin(false);
		horizontalLayout1.setSpacing(true);
		
		// labelLastConnected
		labelLastConnected = new Label();
		labelLastConnected.setSizeUndefined();
		labelLastConnected.setValue("Label");
		horizontalLayout1.addComponent(labelLastConnected);
		horizontalLayout1.setComponentAlignment(labelLastConnected,
				new Alignment(34));
		
		// buttonDeconnexion
		buttonDeconnexion = new Button();
		buttonDeconnexion.setStyleName("logout");
		buttonDeconnexion.setCaption("Button");
		buttonDeconnexion.setWidth("30px");
		buttonDeconnexion.setHeight("30px");
		horizontalLayout1.addComponent(buttonDeconnexion);
		horizontalLayout1.setComponentAlignment(buttonDeconnexion,
				new Alignment(33));
		
		return horizontalLayout1;
	}

	@AutoGenerated
	private HorizontalLayout buildBudgetMensuel() {
		// common part: create layout
		budgetMensuel = new HorizontalLayout();
		budgetMensuel.setSizeFull();
		budgetMensuel.setMargin(false);
		
		// verticalLayoutResume
		verticalLayoutResume = buildVerticalLayoutResume();
		budgetMensuel.addComponent(verticalLayoutResume);
		budgetMensuel.setExpandRatio(verticalLayoutResume, 1.0f);
		budgetMensuel.setComponentAlignment(verticalLayoutResume,
				new Alignment(33));
		
		// verticalLayoutDepenses
		verticalLayoutDepenses = buildVerticalLayoutDepenses();
		budgetMensuel.addComponent(verticalLayoutDepenses);
		budgetMensuel.setExpandRatio(verticalLayoutDepenses, 2.5f);
		
		return budgetMensuel;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayoutResume() {
		// common part: create layout
		verticalLayoutResume = new VerticalLayout();
		verticalLayoutResume.setSizeFull();
		verticalLayoutResume.setWidth("95.0%");
		verticalLayoutResume.setMargin(false);
		
		// treeResume
		treeResume = new TreeGridResumeCategories();
		treeResume.setSizeFull();
		verticalLayoutResume.addComponent(treeResume);
		verticalLayoutResume.setExpandRatio(treeResume, 1.0f);
		
		// tableTotalResume
		tableTotalResume = new GridResumeTotaux();
		tableTotalResume.setSizeFull();
		tableTotalResume.setHeight("100px");
		verticalLayoutResume.addComponent(tableTotalResume);
		
		return verticalLayoutResume;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayoutDepenses() {
		// common part: create layout
		verticalLayoutDepenses = new VerticalLayout();
		verticalLayoutDepenses.setSizeFull();
		verticalLayoutDepenses.setMargin(false);
		
		// tableSuiviDepense
		gridOperations = new GridOperations();
		gridOperations.setSizeFull();
		verticalLayoutDepenses.addComponent(gridOperations);
		verticalLayoutDepenses.setExpandRatio(gridOperations, 40.0f);
		verticalLayoutDepenses.setComponentAlignment(gridOperations,
				new Alignment(20));
		
		// horizontalLayout_2
		horizontalLayout2 = buildHorizontalLayout2();
		verticalLayoutDepenses.addComponent(horizontalLayout2);
		
		return verticalLayoutDepenses;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout2() {
		// common part: create layout
		horizontalLayout2 = new HorizontalLayout();
		horizontalLayout2.setSizeFull();
		horizontalLayout2.setHeightUndefined();
		horizontalLayout2.setMargin(false);
		
		// horizontalLayoutActions
		horizontalLayoutActions = buildHorizontalLayoutActions();
		horizontalLayout2.addComponent(horizontalLayoutActions);
		horizontalLayout2.setExpandRatio(horizontalLayoutActions, 200.0f);
		horizontalLayout2.setComponentAlignment(horizontalLayoutActions,
				new Alignment(48));
		
		// buttonRefreshMonth
		buttonRefreshMonth = new Button();
		buttonRefreshMonth.setStyleName("reinit");
		buttonRefreshMonth.setCaption(" ");
		buttonRefreshMonth.setDescription("Réinitialiser le mois courant");
		buttonRefreshMonth.setWidth("30px");
		buttonRefreshMonth.setHeight("30px");
		horizontalLayout2.addComponent(buttonRefreshMonth);
		horizontalLayout2.setComponentAlignment(buttonRefreshMonth,
				new Alignment(34));
		
		// buttonLock
		buttonLock = new Button();
		buttonLock.setCaption(" ");
		buttonLock.setWidth("30px");
		buttonLock.setHeight("30px");
		horizontalLayout2.addComponent(buttonLock);
		horizontalLayout2.setComponentAlignment(buttonLock, new Alignment(34));
		
		return horizontalLayout2;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayoutActions() {
		// common part: create layout
		horizontalLayoutActions = new HorizontalLayout();
		horizontalLayoutActions.setWidth("40.0%");
		horizontalLayoutActions.setHeight("40px");
		horizontalLayoutActions.setMargin(false);
		
		// buttonCreate
		buttonCreate = new Button();
		buttonCreate.setStyleName("friendly");
		buttonCreate.setCaption("Créer une nouvelle opération");
		buttonCreate.setSizeUndefined();
		buttonCreate.setTabIndex(2);
		horizontalLayoutActions.addComponent(buttonCreate);
		horizontalLayoutActions.setComponentAlignment(buttonCreate,
				new Alignment(48));
		
		return horizontalLayoutActions;
	}
}
