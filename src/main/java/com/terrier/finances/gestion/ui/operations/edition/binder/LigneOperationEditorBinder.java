/**
 * 
 */
package com.terrier.finances.gestion.ui.operations.edition.binder;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.terrier.finances.gestion.communs.operations.model.enums.TypeOperationEnum;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation;
import com.terrier.finances.gestion.communs.operations.model.v12.LigneOperation.Categorie;
import com.terrier.finances.gestion.communs.parametrages.model.enums.IdsCategoriesEnum;
import com.terrier.finances.gestion.communs.parametrages.model.v12.CategorieOperation;
import com.terrier.finances.gestion.communs.utils.data.BudgetDataUtils;
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

	
	private static List<CategorieOperation> setCategories ;

	/**
	 * Constructeur
	 * @param serviceParam
	 */
	public LigneOperationEditorBinder(List<CategorieOperation> setCategories){
		LigneOperationEditorBinder.setCategories = setCategories;
	}


	private ComboBox<CategorieOperation> cCategories = new  ComboBox<>();
	private ComboBox<CategorieOperation> ssCategories = new  ComboBox<>();
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
				.withValidator(v -> expectedType == null || expectedType.equals(v), "L'opération est une "+expectedType.getId()+". La valeur doit être " + expectedType.getLibelle())
				.bind(LigneOperation::getTypeOperation, LigneOperation::setTypeOperation);
	}

	/**
	 * @return binding de la valeur
	 */
	public Binding<LigneOperation, Double> bindValeur(){
		TextField tValeur = new TextField();
		// Validation de la valeur
		return this.forField(tValeur)
				.withConverter(BudgetDataUtils::getValueFromString, String::valueOf)
				.withValidator(Objects::nonNull, "La valeur ne doit pas être nulle ou incorrecte")
                .withValidator(v -> (!Double.isInfinite(Double.valueOf(v)) && !Double.isNaN(Double.valueOf(v))), "La valeur est incorrecte")
				.bind(LigneOperation::getValeurToSaisie, LigneOperation::setValeurFromSaisie);
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
//	public Binding<LigneOperation.AddInfos, LocalDateTime> bindDate(){
//		TextField valeurDate = new TextField();
//		valeurDate.setEnabled(false);
//		// Pas de validateur. Valeur en readonly
//		return this.forField(valeurDate).withConverter(new DateOperationEditorConverter()).bind(LigneOperation.AddInfos::getDateMaj, LigneOperation.AddInfos::setDateMaj);
//	}


	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, Categorie> bindCategories(){
		cCategories.setEnabled(false);
		return this.forField(cCategories)
				.withConverter(LigneOperationEditorBinder::categorieReferentielToCategorie, LigneOperationEditorBinder::categorieToCategorieReferentiel)
				.withValidator(Objects::nonNull, "La catégorie est obligatoire")
				.bind(LigneOperation::getCategorie, LigneOperation::setCategorie);
	}
	
	
	private static Categorie categorieReferentielToCategorie(CategorieOperation categorieReferentiel) {
		Categorie categorieInOperation = new LigneOperation().new Categorie();
		if(categorieReferentiel != null) {
			categorieInOperation.setId(categorieReferentiel.getId());
			categorieInOperation.setLibelle(categorieReferentiel.getLibelle());
		}
		return categorieInOperation;
	}
	
	private static CategorieOperation categorieToCategorieReferentiel(Categorie categorieOperation) {
		return BudgetDataUtils.getCategorieById(categorieOperation.getId(), setCategories);
	}
	
	
	
	/**
	 * @return binding périodique
	 */
	public Binding<LigneOperation, Categorie> bindSSCategories(){
		// Liste des sous catégories 
		Set<CategorieOperation> sousCategories = setCategories.stream()
				.map(CategorieOperation::getListeSSCategories)
				.flatMap(Set::stream)
				// Sauf transfert intercompte et réservice
				.filter(c -> 
						!IdsCategoriesEnum.TRANSFERT_INTERCOMPTE.getId().equals(c.getId()))
				.sorted((c1, c2) -> c1.getLibelle().compareTo(c2.getLibelle()))
				.collect(Collectors.toSet());
		ssCategories.setItems(sousCategories);
		ssCategories.setEmptySelectionAllowed(false);
		ssCategories.setTextInputAllowed(false);
		ssCategories.setEnabled(true);
		// Update auto de la catégorie et du type
		ssCategories.addSelectionListener(event -> {
			cCategories.setValue(event.getSelectedItem().get().getCategorieParente());
			if(IdsCategoriesEnum.SALAIRE.getId().equals(this.ssCategories.getSelectedItem().get().getId()) 
					|| IdsCategoriesEnum.REMBOURSEMENT.getId().equals(this.ssCategories.getSelectedItem().get().getId())
			){
				expectedType = TypeOperationEnum.CREDIT;
			}
			else if(IdsCategoriesEnum.TRANSFERT_INTERCOMPTE.getId().equals(this.ssCategories.getSelectedItem().get().getId())) {
				expectedType = null;
			}
			else{
				expectedType = TypeOperationEnum.DEPENSE;
			}
			cTypes.setValue(expectedType);
			
		});
		return this.forField(ssCategories)
				.withConverter(LigneOperationEditorBinder::categorieReferentielToCategorie, LigneOperationEditorBinder::categorieToCategorieReferentiel)
				.withValidator(Objects::nonNull, "La sous catégorie est obligatoire")
				.bind(LigneOperation::getSsCategorie, LigneOperation::setSsCategorie);
	}
}