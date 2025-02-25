package cms.components;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentity.model.NotAuthorizedException;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ChangePasswordRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.InvalidPasswordException;
import software.amazon.awssdk.services.cognitoidentityprovider.model.PasswordResetRequiredException;

@Slf4j
@Component
public class Cognito {

	private CognitoProperties             properties;
	private CognitoIdentityProviderClient client;
	
	public Cognito(CognitoProperties securityProperties) {
		
		properties = securityProperties;
		client = CognitoIdentityProviderClient.builder()
				//https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/credentials.html
				// The default credential provider chain, implemented by the DefaultCredentialsProvider class, checks sequentially each of places where you can set default credentials and selects the ﬁrst one you set.
                //.credentialsProvider(ProfileCredentialsProvider.create()) 
				.credentialsProvider(
						StaticCredentialsProvider
						    .create(AwsBasicCredentials.create(
						    		properties.getIdChaveAcesso(),
						    		properties.getChaveAcessoSecreta())))
				.region(Region.of(properties.getRegiao()))				
                .build();		
	}
	
	public void close() {
		try {
			if (client != null) client.close();
		} catch (Exception e) {
		}
	}
	
	public void chantePassword(String accessToken, String senhaAntiga, String novaSenha) 
	throws NotAuthorizedException,
	       InvalidPasswordException, 
	       PasswordResetRequiredException,
	       CognitoIdentityProviderException  //base
	{
    	ChangePasswordRequest userRequest = ChangePasswordRequest.builder()
                .accessToken(accessToken)
                .previousPassword(senhaAntiga)
                .proposedPassword(novaSenha)
                .build() ; 
        
    	client.changePassword(userRequest);
	}
	
}
