package com.terrier.finances.gestion.ui.communs.aspects;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.terrier.finances.gestion.communs.api.security.ApiHeaderIdEnum;

/**
 * Configuration des aspects
 * @author vzwingma
 *
 */
@Component
@Aspect
public class AspectsConfig {


	private static final Logger LOGGER = LoggerFactory.getLogger( AspectsConfig.class );

	public AspectsConfig() {
		LOGGER.info("[INIT] Configuration des aspects");
	}
	
	@AfterReturning("execution(* com.terrier.finances.gestion.services.abstrait.api.AbstractHTTPReactiveClient+.*(..))")
	public void clearAPICorrIdLogger() {
		org.slf4j.MDC.remove(ApiHeaderIdEnum.HEADER_CORRELATION_ID);
		org.slf4j.MDC.remove(ApiHeaderIdEnum.HEADER_API_CORRELATION_ID);
	}


}
