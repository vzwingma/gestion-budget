/*
 * Classe commune à toutes les pages
 */
var rootServer = "http://192.168.59.103:18080/gestion-budget/rest"
// var rootServer = "http://localhost:8080/gestion-budget/rest"
// var rootServer = "https://budget-tushkanyogik.rhcloud.com/rest"

// Ping
var serverPingUrl = rootServer + "/ping";
// Catégories dépenses
var serverCategorieUrl = rootServer + "/categories/depenses";
// Contexte utilisateur
var serverUtilisateurUrl = rootServer + "/utilisateur";



// Ajout de la BasicAuthentication à la requête
function addBasicAuth(req){
	req.setRequestHeader('Authorization', 'Basic ' + btoa($.session.get('loginUser') + ":" + $.session.get('mdpUser')));
}