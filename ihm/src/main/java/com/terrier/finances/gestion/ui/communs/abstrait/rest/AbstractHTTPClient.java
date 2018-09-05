/**
 * 
 */
package com.terrier.finances.gestion.ui.communs.abstrait.rest;



import java.security.NoSuchAlgorithmException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe d'un client HTTP
 * @author vzwingma
 *
 */
public abstract class AbstractHTTPClient {


	private static final Logger LOGGER = LoggerFactory.getLogger( AbstractHTTPClient.class );
	// Tout est en JSON
	private static final MediaType JSON_MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;


	private Client clientHTTP;

	/**
	 * @return client HTTP
	 */
	private Client getClient(){
		if(clientHTTP == null){
			this.clientHTTP = getClient(null);
		}
		return this.clientHTTP;
	}

	/**
	 * Créé un client HTTP 
	 * (dans une méthode séparée pour pouvoir être mocké facilement)
	 * @return client HTTP
	 * @throws NoSuchAlgorithmException 
	 */
	public Client getClient(HttpAuthenticationFeature feature) {

		ClientConfig clientConfig = new ClientConfig();
		if(feature != null){
			clientConfig.register(feature);
		}

		try {
			//			// Install the all-trusting trust manager
			//			SSLContext sslcontext = SSLContext.getInstance("TLS");
			//
			//			sslcontext.init(null,  new TrustManager[] { new ClientHTTPTrustManager() }, new java.security.SecureRandom());
			//			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			return ClientBuilder.newBuilder()
					//					.sslContext(sslcontext)
					.withConfig(clientConfig)
					.build();
		}
		catch(Exception e){
			LOGGER.error("Erreur envoi : Erreur lors de la création du Client HTTP {}", e.getMessage());
			return ClientBuilder.newClient(clientConfig);
		}
	}


	/**
	 * @param clientHTTP
	 * @param url
	 * @param path
	 * @param type
	 * @return
	 */
	public Invocation.Builder getInvocation(String url, String path){
		LOGGER.debug("[HTTP] Appel de l'URI [{}{}]", url, path);
		WebTarget wt = getClient().target(url);
		if(path != null){
			wt = wt.path(path);
		}
		return wt.request(JSON_MEDIA_TYPE);
	}



	/**
	 * Appel POST 
	 * @param clientHTTP client utilisé
	 * @param url url appelée
	 * @param formData data envoyées
	 * @return
	 */
	public Object callHTTPPost(Invocation.Builder invocation, Entity<?> entityData){
		LOGGER.debug("[HTTP POST] Appel du service");
		try{
			invocation.header("Content-type", JSON_MEDIA_TYPE.getType()+"/"+JSON_MEDIA_TYPE.getSubtype());
			Response response = invocation.post(entityData);
			LOGGER.debug("[HTTP POST] Resultat : {}", response);
			if(response != null){
				return response.getEntity();
			}
		}
		catch(Exception e){
			LOGGER.error("> Resultat : Erreur lors de l'appel HTTP POST", e);
		}
		return null;
	}


	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 */
	public boolean callHTTPGet(Invocation.Builder invocation){
		LOGGER.debug("[HTTP GET] Appel du service");
		boolean resultat = false;
		try{

			Response response = invocation.get();
			if(response != null){
				LOGGER.debug("[HTTP GET] Resultat : {}", response.getStatus());
				resultat = response.getStatus() == 200;
			}
		}
		catch(Exception e){
			LOGGER.error("> Resultat : Erreur lors de l'appel HTTP GET", e);
			resultat = false;
		}
		return resultat;
	}


	/**
	 * Appel HTTP GET
	 * @param clientHTTP client HTTP
	 * @param url racine de l'URL
	 * @param urlParams paramètres de l'URL (à part pour ne pas les tracer)
	 * @return résultat de l'appel
	 */
	public Response callHTTPGetData(Invocation.Builder invocation){
		LOGGER.debug("[HTTP GET] Appel du service");
		try{

			Response response = invocation.get();
			if(response != null){
				LOGGER.debug("[HTTP GET] Resultat : {} / MediaType {}", response, response.getMediaType());
			}
			return response;
		}
		catch(Exception e){
			LOGGER.error("> Resultat : Erreur lors de l'appel HTTP GET", e);
		}
		return null;
	}

}