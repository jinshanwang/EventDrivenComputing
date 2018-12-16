public class MapFormatException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int lineNr;
    
    public MapFormatException(int lineNr, String msg)
    {
	super(msg);
	this.lineNr= lineNr;
    }
    
    
    public String toString()
    {
	return lineNr+":0:"+super.toString();
    }
}
