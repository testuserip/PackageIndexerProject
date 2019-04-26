package DOCodingChallenge;

import java.util.logging.Logger;

/***
 * This class calls the actual implementation of the
 * Indexer methods. This class is sort of an adapter layer,
 * where we can call different implementations of the
 * DataLayer Interface.
 * @author abc
 *
 */
public class PackageIndexer {
	
	private DataLayerInterface dataLayerObject;
    private Logger LOGGER = null;
	
	/**
	 * Constructor where we instantiate the object
	 * of the class that implements the DataLayer Interface.
	 */
	public PackageIndexer()
	{
		this.dataLayerObject = new DataLayerImplMemory();
		LOGGER = Logger.getLogger(Server.class.getName());
	}
	
	/**
	 * Method that calls the actual implementation 
	 * of Index() of DataLayer Interface
	 * @param parentPkg : Main package to index.
	 * @param childPkgs : List of child packages 
	 * @return Result String of the operation - OK or FAIL
	 */
	public String Index(String parentPkg, String[] childPkgs)
	{
		if(parentPkg == null)
		{
			LOGGER.warning("Returning error from Index function.");
			return ClientResponseCode.ERROR;
		}
		String result = dataLayerObject.IndexPackage(parentPkg, childPkgs);
		return(result);
	}
	
	/**
	 * Method that calls the actual implementation 
	 * of Query() of DataLayer Interface
	 * @param PkgName : Package to query.
	 * @return Result String of the operation - OK or FAIL
	 */
	public String Query(String PkgName)
	{	
		if(PkgName == null)
		{
			LOGGER.warning("Returning error from Query function.");
			return ClientResponseCode.ERROR;
		}
		String result = dataLayerObject.QueryPackage(PkgName);
		return(result);
	}
	
	/**
	 * Method that calls the actual implementation 
	 * of Remove() of DataLayer Interface
	 * @param PkgName : Package to query.
	 * @return Result String of the operation - OK or FAIL
	 */	
	public String Remove(String PkgName)
	{
		if(PkgName == null)
		{
			LOGGER.warning("Returning error from Remove function.");
			return ClientResponseCode.ERROR;
		}
		String result = dataLayerObject.RemovePackage(PkgName);
		return result;
	}
	
}