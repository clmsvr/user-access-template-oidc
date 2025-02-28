package cms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.Transactional;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;

import cms.components.CognitoLogoutSuccessHandler;
import cms.components.CognitoProperties;
import cms.domain.Consts;
import cms.domain.model.Authority;
import cms.domain.model.Role;
import cms.domain.model.UserRole;
import cms.repository.UserRoleRepository;

@Configuration //nao eh necessario pq a anotacao @EnableWebSecurity deriva de @Configuration
@EnableWebSecurity //permite que nossa configuracao substitua as configurações default de seguranca dos Starters do Spring Security - https://stackoverflow.com/questions/44671457/what-is-the-use-of-enablewebsecurity-in-spring
public class SecurityConfig {

	@Autowired
	UserRoleRepository userRepository;
	@Autowired
	CognitoProperties cognitoProperties;
	
	@Bean
	public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception 
	{
		http
			.authorizeHttpRequests((requests) -> 
				requests
					.requestMatchers("/","/bootstrap/**","/css/**","/fonts/**","/image/**","/js/**")
					.permitAll()
					.requestMatchers("/user/mng/**").authenticated()
					.requestMatchers("/admin/**").hasRole("Admin") //com "/admin/*" '/admin' NAO estará incluso, somente paths abaixo.
					.anyRequest().authenticated()
			)
			.oauth2Login(Customizer.withDefaults())
            .logout(logout -> 
  		    	logout.logoutSuccessHandler(new CognitoLogoutSuccessHandler(cognitoProperties))
  		    	//logout.logoutSuccessUrl("https://us-east-1fssjl3xir.auth.us-east-1.amazoncognito.com/logout?client_id=j5ifq77iqp38769eun48lat3&logout_uri=http://localhost:8080&a=b")           
			
//			.logout((logout) -> 
//				logout
//				    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//				    .logoutSuccessUrl("/")
//					.permitAll()
            
			)
		;
		return http.build();
	}

//	@Autowired
//	private ClientRegistrationRepository clientRegistrationRepository;
//    //Nao funciona com Cognito. Os parametros da url esperados pelo Cognito sao diferentes da Especificação: https://openid.net/specs/openid-connect-rpinitiated-1_0.html
//    //https://docs.spring.io/spring-security/reference/servlet/oauth2/login/logout.html#configure-client-initiated-oidc-logout
//	private LogoutSuccessHandler oidcLogoutSuccessHandler() 
//	{
//		// Este handler obtem a uri "end_session_endpoint" e passa os parametros conforme:
//		// https://docs.aws.amazon.com/cognito/latest/developerguide/logout-endpoint.html
//		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
//				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
//
//		// Sets the location that the End-User's User Agent will be redirected to
//		// after the logout has been performed at the Provider
//		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");
//
//		return oidcLogoutSuccessHandler;
//	}
	
	
	
    /**
     * Fluxo OAuth
     * Interceptar o usuario logado, validar/verificar base local, e adicionar authorities.
     */
    // https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-map-authorities-oauth2userservice
    // https://spring.io/guides/tutorials/spring-boot-oauth2
	@Bean
	public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() 
	{
		final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
		return request -> {
			
			System.out.println("### Building MY OAuth2User .....");
			
			OAuth2User oauthUser = delegate.loadUser(request);
			
			return updateUser(request, oauthUser);
		};
	}

    /**
     * Fluxo OIDC
     * Interceptar o usuario logado, validar/verificar base local, e adicionar authorities.
     */
	@Bean
	public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() 
	{
		final OidcUserService delegate = new OidcUserService();
		return request -> {
			
			System.out.println("### Building MY OidcUser .....");
			
			OidcUser oauthUser = delegate.loadUser(request);
			
			return (OidcUser) updateUser(request, oauthUser);
		};
	}

	@Transactional
	private DefaultOAuth2User updateUser(OAuth2UserRequest request, OAuth2User oauthUser) 
	{
		OAuth2AccessToken accessToken = request.getAccessToken();
		
		Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
		
		//Buscar ou Criar usuario no cadastrado no banco
		UserRole domainUser = findUserInDatabase(oauthUser);
		addAuthorities(domainUser.getRoles(), accessToken.getScopes(), mappedAuthorities);
		
		//executar ações com base no Client ID
		if ("github".equals(request.getClientRegistration().getRegistrationId())) {

		}
		
		// podemos gerar erro e abortara o login do usuario na aplicação.
		//throw new OAuth2AuthenticationException(new OAuth2Error("invalid_token", "Not in Spring Team", ""));
		
		//Criar copia do OAuth2User com "mappedAuthorities" .
		//Definimos que a claim "sub" vai nomear e identificar o OAuth2User (nosso oidcId)
		if (oauthUser instanceof OidcUser oidcuser) 
		{
			return new DefaultOidcUser(mappedAuthorities, oidcuser.getIdToken(), oidcuser.getUserInfo(), StandardClaimNames.SUB);
		}
		else 
		{
			return new DefaultOAuth2User(mappedAuthorities, oauthUser.getAttributes(),StandardClaimNames.SUB);
		}
	}
	
	@Transactional
	private UserRole findUserInDatabase(OAuth2User oauthUser) 
	{
		String oidcId = oauthUser.getName();  //getAttribute(IdTokenClaimNames.SUB);
		UserRole domainUser = userRepository.findByOidcId(oidcId);
		if (domainUser == null) {
			domainUser = new UserRole();
			domainUser.setOidcId(oidcId);
			domainUser.setName(oauthUser.getAttribute("name"));
			domainUser.setEmail(oauthUser.getAttribute("email"));
			domainUser.setProviderName(getProviderName(oauthUser));
			userRepository.save(domainUser);
			userRepository.flush();
		}
		return domainUser;
	}
	
	@Transactional
	private void addAuthorities(Set<Role> roles, Set<String> scopes,
			Collection<GrantedAuthority> mappedAuthorities) 
	{
		for (Role role : roles) {
			mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
			for (Authority p : role.getAuthorities()) {
				mappedAuthorities.add(new SimpleGrantedAuthority(p.getName()));
			}
		}
		
		scopes.stream().forEach(
				scope -> mappedAuthorities.add(new SimpleGrantedAuthority("SCOPE_"+scope)));

	}
	
	public static String getProviderName(OAuth2User oauthUser)
	{
		String providerName = "";
		if (oauthUser != null) {
			providerName = Consts.LOCAL_PROVIDER_NAME;
			
			Object identities = oauthUser.getAttribute("identities");
			if (identities instanceof ArrayList atts) 
			{
				Object object = atts.get(0);
				if(object instanceof LinkedTreeMap map) {
					providerName = map.get("providerName").toString();
				}
			}
		}
		return providerName;
	}
}