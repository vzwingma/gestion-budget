function addBasicAuth(e){e.setRequestHeader("Authorization","Basic "+btoa($.session.get("loginUser")+":"+$.session.get("mdpUser")))}function getLabelMois(e){return 0==e?"Janvier":1==e?"Février":2==e?"Mars":3==e?"Avril":4==e?"Mai":5==e?"Juin":6==e?"Juillet":7==e?"Aout":8==e?"Septembre":9==e?"Octobre":10==e?"Novembre":11==e?"Décembre":void 0}var rootServer="http://localhost:8080/gestion-budget/rest/v2",serverPingUrl=rootServer+"/ping",serverCategorieUrl=rootServer+"/categories/depenses",serverUtilisateurUrl=rootServer+"/utilisateur",serverBudgetUrl=rootServer+"/budget/",serverDepensesUrl=rootServer+"/depenses/";