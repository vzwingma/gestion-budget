/**
 * 
 */
package com.terrier.finances.gestion.ui.components.budget.mensuel.binder;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.terrier.finances.gestion.budget.business.OperationsService;
import com.terrier.finances.gestion.model.business.parametrage.CategorieDepense;
import com.terrier.finances.gestion.model.data.DataUtils;
import com.terrier.finances.gestion.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.operations.model.LigneOperation;
import com.vaadin.data.Binder;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;

/**
 * Binder des champs de LigneOperations dans le cas du mode éditable
 * @author vzwingma
 *
 */
public class LigneOperationEditorBinder extends Binder<LigneOperation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8849882826136832053L;

	
	private List<CategorieDepense> setCategories ;

	/**
	 * Constructeur
	 * @param serviceParam
	 */
	public LigneOperationEditorBinder(List<CategorieDepense> setCategories){
		this.setCategories = setCategories;
	}


	private ComboBox<CategorieDepense> cCategories = new  ComboBox<>();
	private ComboBox<CategorieDepense> ssCategories = new  ComboBox<>();
	private ComboBox<TypeOperationEnum> cTypes = new ComboBox<>();
	private TypeOperationEnum expectedType = TypeOperationEnum.DEPENSE;

	/**
	 * @return binding du libellé
	 */
	public Binding<LigneOperation, String> bindLibelle(){
		TextField tLibelle = new TextField();
		return this.forField(tLibelle)
				.withValidator(v -> v != null && v.length() > 0, "Le libellé ne doit pas être nul")
				.bind(LigneOperation::getLibelle, LigneOperation::setLibelle);
	}


	/**
	 * @return binding du type dépense
	 */
	public Binding<LigneOperation, TypeOperationEnum> bindTypeDepense(){
		cTypes = new ComboBox<>();
		cTypes.setTextInputAllowed(false);
		cTypes.setEmptySelectionAllowed(false);
		cTypes.setItems(TypeOperationEnum.values());
		return this.forField(cTypes)
				.withValidator(Objects::nonNull, "Le Type de dépense ne peut pas être nul")
				.withValidator(v -> expectedType.equals(v), "L'opération est une "+expectedType.getId()+". La valeur doit être " + expectedType.getLibelle())
				.bind(LigneOperation::getTypeDepense, LigneOperation::setTypeDepense);
	}

	/**
	 * @return binding de la valeur
	 */
	public Binding<LigneOperation, String> bindValeur(){
		TextField tValeur = new TextField();
		// Validation de la valeur
		return this.forField(tValeur)
				.withConverter(DataUtils::getValueFromString, String::valueOf)
				.withValidator(v -> v != null, "La valeur ne doit pas être nulle ou incorrecte")
                .withValidator(v -> {
                    return (!Double.isInfinite(Double.valueOf(v)) && !Double.isNaN(Double.valueOf(v)));
                }, "La valeur est incorrecte")
				.bind(LigneOperation::getValeurAbsStringFromDouble, LigneOperation::setValeurAbsStringToDouble);
	}


	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, Boolean> bindPeriodique(){
		return this.bind(new CheckBox(), LigneOperation::isPeriodique, LigneOperation::setPeriodique);
	}


	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, Date> bindDate(){
		TextField valeurDate = new TextField();
		valeurDate.setEnabled(false);
		// Pas de validateur. Valeur en readonly
		return this.forField(valeurDate).withConverter(new DateOperationEditorConverter()).bind(LigneOperation::getDateMaj, LigneOperation::setDateMaj);
	}


	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, CategorieDepense> bindCategories(){
		cCategories.setEnabled(false);
		return this.forField(cCategories).withValidator(Objects::nonNull, "La catégorie est obligatoire").bind(LigneOperation::getCategorie, LigneOperation::setCategorie);
	}
	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, CategorieDepense> bindSSCategories(){


		// Liste des sous catégories 
		Set<CategorieDepense> sousCategories = setCategories.stream()
				.map(CategorieDepense::getListeSSCategories)
				.flatMap(Set::stream)
				// Sauf transfert intercompte et réservice
				.filter(c -> 
						!OperationsService.ID_SS_CAT_TRANSFERT_INTERCOMPTE.equals(c.getId()) 
						&&	! OperationsService.ID_SS_CAT_RESERVE.equals(c.getId()))
				.sorted((c1, c2) -> c1.getLibelle().compareTo(c2.getLibelle()))
				.collect(Collectors.toSet());
		ssCategories.setItems(sousCategories);
		ssCategories.setEmptySelectionAllowed(false);
		ssCategories.setTextInputAllowed(false);
		ssCategories.setEnabled(true);
		// Update auto de la catégorie et du type
		ssCategories.addSelectionListener(event -> {
			cCategories.setValue(event.getSelectedItem().get().getCategorieParente());
			if((OperationsService.ID_SS_CAT_SALAIRE.equals(this.ssCategories.getSelectedItem().get().getId()) 
					|| OperationsService.ID_SS_CAT_REMBOURSEMENT.equals(this.ssCategories.getSelectedItem().get().getId()))){
				expectedType = TypeOperationEnum.CREDIT;
			}
			else{
				expectedType = TypeOperationEnum.DEPENSE;
			}
			cTypes.setValue(expectedType);
			
		});
		return this.forField(ssCategories)
				.withValidator(Objects::nonNull, "La sous catégorie est obligatoire")
				.bind(LigneOperation::getSsCategorie, LigneOperation::setSsCategorie);
	}
}