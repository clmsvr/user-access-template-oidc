package cms.api.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cms.domain.repository.UserRepository;
import cms.security.SecurityConfig;

@ControllerAdvice
public class ModelControllerAdvice {

    @Autowired
    OAuth2AuthorizedClientService authorizedClientService;
    @Autowired
    UserRepository userRep;

    public static final String DOMAIN_USER    = "domainUser";
    public static final String ACCESS_TOKEN   = "accessToken";
    public static final String STATES         = "states";
    
    private static HashMap<String, String> states = new HashMap<>();
    static{
        states.put("AC","Acre");
        states.put("AL","Alagoas");
        states.put("AP","Amapá");
        states.put("AM","Amazonas");
        states.put("BA","Bahia");
        states.put("CE","Ceará");
        states.put("DF","Distrito Federal");
        states.put("ES","Espirito Santo");
        states.put("GO","Goiás");
        states.put("MA","Maranhão");
        states.put("MS","Mato Grosso do Sul");
        states.put("MT","Mato Grosso");
        states.put("MG","Minas Gerais");
        states.put("PA","Pará");
        states.put("PB","Paraíba");
        states.put("PR","Paraná");
        states.put("PE","Pernambuco");
        states.put("PI","Piauí");
        states.put("RJ","Rio de Janeiro");
        states.put("RN","Rio Grande do Norte");
        states.put("RS","Rio Grande do Sul");
        states.put("RO","Rondônia");
        states.put("RR","Roraima");
        states.put("SC","Santa Catarina");
        states.put("SP","São Paulo");
        states.put("SE","Sergipe");
        states.put("TO","Tocantins");
    }

	@ModelAttribute
	public void addAttributes(Model model, OAuth2AuthenticationToken principal) 
	{
		model.addAttribute(STATES, states);
		
		if(principal != null) {
			OAuth2User oauthUser = principal.getPrincipal();
			
		    model.addAttribute(DOMAIN_USER, userRep.findByOidcId(oauthUser.getName()));
		    
			var oAuth2AuthorizedClient = authorizedClientService.loadAuthorizedClient(
					principal.getAuthorizedClientRegistrationId(), 
					oauthUser.getName());
			
			if (oAuth2AuthorizedClient != null) {//condições de problemas com sessão,restarts,etc
				String accessToken = oAuth2AuthorizedClient.getAccessToken().getTokenValue();
		    	model.addAttribute(ACCESS_TOKEN, accessToken );
			}
		}
	}
	
}
