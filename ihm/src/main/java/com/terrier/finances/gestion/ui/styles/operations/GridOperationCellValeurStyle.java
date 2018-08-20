/**
 * 
 */
package com.terrier.finances.gestion.ui.styles.operations;

import com.terrier.finances.gestion.model.enums.TypeDepenseEnum;
import com.terrier.finances.gestion.model.ui.budget.LigneOperationVO;

/**
 * Style des colonnes Valeurs du tableau des opérations
 * @author vzwingma
 */
public class GridOperationCellValeurStyle extends GridOperationCellStyle {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1551761447370771079L;

	@Override
	public String apply(LigneOperationVO depense) {
		
		StringBuilder style = new StringBuilder();
		style.append(super.apply(depense));
		
		// valeurs : (rouge pour négatif)
		style.append(" v-grid-cell-valeur");
		if(TypeDepenseEnum.DEPENSE.equals(depense.getTypeDepense())){
			style.append("_rouge");
		}
		return style.toString();
	}
}
