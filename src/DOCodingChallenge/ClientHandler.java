package DOCodingChallenge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a class that acts as a client handler thread.
 * It parses the commands that are received on the server.
 * And sends a response back to the client.
 * @author abc
 *
 */
public class ClientHandler implements Runnable{

	private Socket clientSocket;
    private String packageName = "";
    private String[] dependencyPackageNames = null;
    private PackageIndexer pckIndexerObj = null;
    private Logger LOGGER = null;
    
    /**
     * ClientHandler constructor
     * @param clientSocket : ClientSocket which is passed in from the Main Server class
     * @param pckIndexerObj : 
     */
	ClientHandler(Socket clientSocket, PackageIndexer pckIndexerObj) {
		this.packageName = "";
	    this.clientSocket = clientSocket;
	    this.pckIndexerObj = pckIndexerObj;
	    LOGGER = Logger.getLogger(Server.class.getName());
	}
	
	/**
	 * This method will parse the function name
	 * @param command : Command to be parsed which contains the package name
	 * @return
	 */
    public String getMainPackageName(String command)
    {
    	try
		{
    		// Extract pattern between |xxx| from the command
    		// as the package name
    		Pattern p = Pattern.compile("\\|(.*?)\\|");
    		Matcher m = p.matcher(command);
    		// If we find some characters in between |xxx|
    		if(m.find())
    		{
    			packageName = m.group(1);
     		}
    		else
    		{
    			// Did not find a valid package name after INDEX command
    			return ClientResponseCode.ERROR;
    		}
    		
			return packageName;
		}
		catch(Exception e)
		{
			// Log that it is an invalid command format
			LOGGER.warning("Exception occurred during extracting packagename : "+packageName);
			return ClientResponseCode.ERROR;
		}
    }

    /**
     * This method parses the commands, checks if it is valid
     * and if valid, calls the package indexer methods. If not 
     * valid, returns 'ERROR' to the client.
     * @param command : Command from the client to be parsed and executed.
     * @return String : Result code from the execution of the command.
     */
    public String ParseAndExecute(String command)
    {	
		// Examples :
    	// INDEX|cloog|gmp,isl,pkg-config
		// INDEX|ceylon|
		if (command.startsWith("INDEX|"))
		{
			// Extract the main package name
			packageName = getMainPackageName(command);
			
			// If we get a 'good' package name
			if(!(packageName.equals(ClientResponseCode.ERROR)))
			{
				try {
					String dependencyList = null;

					// When package dependency list is not present
					if(command.lastIndexOf('|') == command.length()-1)
					{
						//System.out.println("We are adding null to denote empty dependency list.");
						dependencyPackageNames = new String[1];
						dependencyPackageNames[0] = "null";
					}
					else
					{
						dependencyList = command.substring(command.lastIndexOf('|')+1, command.length());
						//System.out.println("Dependency List : "+dependencyList);
						if(dependencyList.length()!=0)
						{
							dependencyPackageNames  = dependencyList.split(",");
						}
					}
				}
				catch(Exception e)
				{
					LOGGER.warning("Exception occurred while parsing the INDEX command. " + e.toString());
					// Log that there is an error in getting the dependency list
					return ClientResponseCode.ERROR;
				}
			}
			// If we did not get a good package name, then return ERROR
			else
			{
				LOGGER.warning("We could not get a good package name. ");
				return ClientResponseCode.ERROR;
			}

        	String result = pckIndexerObj.Index(this.packageName, this.dependencyPackageNames);
        	return result;
		}
		
		// Example :  
		// REMOVE|cloog|\n
		if (command.startsWith("REMOVE|"))
		{	
			// Extract the main package name
			packageName = getMainPackageName(command);
						
			// If we get a 'good' package name
			if(!(packageName.equals(ClientResponseCode.ERROR)))
			{
				String result = pckIndexerObj.Remove(this.packageName);
				return result;
			}
			// If package name is invalid return ERROR
			LOGGER.warning("We could not get a good package name.");
			return ClientResponseCode.ERROR;
		}
		
		// Example :
		// QUERY|cloog|\n
		if (command.startsWith("QUERY"))
		{
			// Extract the main package name
			packageName = getMainPackageName(command);
			
			// If we get a 'good' package name
			if(!(packageName.contentEquals(ClientResponseCode.ERROR)))
			{
				String result = pckIndexerObj.Query(this.packageName);
				return result;
			}
			// If package name is invalid return ERROR
			LOGGER.warning("We could not get a good package name.");
			return ClientResponseCode.ERROR;
		}
		
		// Any other command is not supported yet
		LOGGER.warning("This is an invalid command.");
		return ClientResponseCode.ERROR;
    }
    
	/***
	 * Overridden method from the runnable interface, 
	 * which runs on thread start
	 * @Override
	 */
	public void run() {		
        try {
        	// Open input and output buffered streams for client socket
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			// Read data from client
			while(true) {
			    String input = in.readLine();
			    if (input!=null)
			    {
			    	// Process client's request
			        String resultCode = ParseAndExecute(input);
			        // Send the result back to the client
			        out.println(resultCode);
			    }
			}
	    } catch (IOException e) {
			LOGGER.severe("Accept failed.");
	        System.err.println("Accept failed.");
	        System.exit(1);
	    }
	}
	
}// end class
