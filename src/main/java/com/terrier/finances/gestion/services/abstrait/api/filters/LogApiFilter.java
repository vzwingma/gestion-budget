package com.terrier.finances.gestion.services.abstrait.api.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.terrier.finances.gestion.communs.api.security.ApiHeaderIdEnum;

import reactor.core.publisher.Mono;

/**
 * Logger des API
 * @author vzwingma
 *
 */
@Service
public class LogApiFilter implements ExchangeFilterFunction, WebFilter {

	

	public static final Logger LOGGER = LoggerFactory.getLogger( LogApiFilter.class );

	/**
	 * Log de la requête
	 */
	@Override
	public Mono<ClientResponse> filter(ClientRequest requestContext, ExchangeFunction next) {
		String apiCorrID = requestContext.headers().getFirst(ApiHeaderIdEnum.HEADER_API_CORRELATION_ID);
		org.slf4j.MDC.put(ApiHeaderIdEnum.HEADER_API_CORRELATION_ID, "[API="+apiCorrID+"]");
		LOGGER.info("{} :: {}", requestContext.method(), requestContext.url());
		return next.exchange(requestContext);
	}


	/**
	 * Log de la réponse
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		// TODO : Activer le webfilter
		LOGGER.info("Statut HTTP : [{}]", exchange.getResponse().getStatusCode());
		return chain.filter(exchange);
	}

}
