package cms.domain.exceptions;

@SuppressWarnings("serial")
public class CognitoException extends Exception {

    public CognitoException()
    {
    }
    
    public CognitoException(Exception e)
    {
        super(e);
    }
    
    public CognitoException(String msg) {
        super(msg);
    }
    
    public CognitoException(String msg, Exception e)
    {
        super(msg,e);
    }
}