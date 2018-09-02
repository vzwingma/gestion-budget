package com.terrier.finances.gestion.ui.statistiques.listeners;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.terrier.finances.gestion.ui.budget.ui.BudgetMensuelPage;
import com.terrier.finances.gestion.ui.communs.abstrait.listeners.AbstractComponentListener;
import com.terrier.finances.gestion.ui.statistiques.ui.StatistiquesPage;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * Listener de navigation entre les pages
 * @deprecated : Pas de changement de page dans que les statistiques ne sont pas initiées
 * @author vzwingma
 *
 */
@Deprecated
public class ChangePageListener extends AbstractComponentListener implements ClickListener{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6573682471450033530L;

	/**
	 * Destination 
	 */
	private Class<? extends Component> pageDestination;
	
	public ChangePageListener(Class<? extends Component> pageDestination){
		this.pageDestination = pageDestination;
	}
	
	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		Layout mainLayout = getUserSession().getMainLayout();
		
		mainLayout.removeAllComponents();
		if(this.pageDestination.equals(StatistiquesPage.class)){
			
			BudgetMensuelPage page = (BudgetMensuelPage)event.getButton().getParent().getParent().getParent().getParent();
			mainLayout.addComponent(new StatistiquesPage(page.getComboBoxComptes().getValue()));			
		}
		else if(this.pageDestination.equals(BudgetMensuelPage.class)){
			
			StatistiquesPage page = (StatistiquesPage)event.getButton().getParent().getParent().getParent().getParent();
			mainLayout.addComponent(new BudgetMensuelPage(page.getComboBoxComptes().getValue()));			
		}
	}
}
