/**
 * 
 */
package com.terrier.finances.gestion.communs.utils.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Budget non trouvé
 * @author vzwingma
 *
 */
public class BudgetNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 873736486722763673L;
	/**
	 * Logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BudgetNotFoundException.class);
	
	
	public BudgetNotFoundException(StringBuilder libelleErreur){
		LOGGER.error("{}", libelleErreur);
	}
}
