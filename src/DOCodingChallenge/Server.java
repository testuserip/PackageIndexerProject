package DOCodingChallenge;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.logging.Logger;

public class Server {

    public static String packageName = "";
    public static String[] dependencyPackageNames = {};
    private static ClientHandler clientHandler;
    private static ServerSocket serverSocket = null;
    private static PackageIndexer pckIndexerObj = null;
    private static int THREAD_POOL_SIZE = 200;
    private static Logger LOGGER = null;

    public static void main(String[] args) throws IOException {
    	
        try
        {
        	// Create a Package Indexer object
        	pckIndexerObj = new PackageIndexer();
        	// Create a logger 
        	// Using the default logger, but a better library
        	// like log4j, etc. can be used here too.
        	LOGGER = Logger.getLogger(Server.class.getName());
        }
        catch(Exception e)
        {
        	LOGGER.warning("Encountered a Null Pointer Exception. Message : " + e.getMessage());
        }
    	
    	// Start the server. Listening on port 8080
        try {
            serverSocket = new ServerSocket(8080);
            System.out.println(" ***** Package Indexer Server started. It is listening on port 8080... *****\n");
            LOGGER.info(" ***** Package Indexer Server started. It is listening on port 8080... *****\n");
        } catch (IOException e) {
        	LOGGER.warning("Could not listen on port 8080. Message : " + e.getMessage());
            System.err.println("Could not listen on port: 8080.");
            System.exit(1);
        }
        
        // Create a thread pool
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Keep waiting for clients to connect
        while (true) {
            clientHandler = new ClientHandler(serverSocket.accept(), pckIndexerObj);
            executorService.submit(clientHandler);
        } 
    }
    
    protected void finalize() throws IOException {
    	LOGGER.warning("We want to close this socket.");
        serverSocket.close();
    }

}