package cms.domain.exceptions;

@SuppressWarnings("serial")
public class InvalidProviderException extends Exception {

    public InvalidProviderException()
    {
    }
    
    public InvalidProviderException(Exception e)
    {
        super(e);
    }
    
    public InvalidProviderException(String msg) {
        super(msg);
    }
    
    public InvalidProviderException(String msg, Exception e)
    {
        super(msg,e);
    }
}