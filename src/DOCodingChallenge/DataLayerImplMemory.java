package DOCodingChallenge;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/***put
 * This class is an implementation of the
 * DataLayer Interface. It gives an in-memory
 * implementation for the Indexer.
 * @author abc
 *
 */
public class DataLayerImplMemory implements DataLayerInterface {

	private ConcurrentHashMap<String,String[]> IndexDB;
	private ConcurrentHashMap<String,Integer> EdgeCount;
    private Logger LOGGER = null;
	
	/**
	 * This function creates and initializes
	 * the Hash Map in memory.
	 */
	public DataLayerImplMemory()
	{
	    LOGGER = Logger.getLogger(Server.class.getName());
		InitializeHashmaps();
	}

	/**
	 * This method implements the in-memory implementation of
	 * indexing a package with or without it's dependencies and
	 * returns the resulting String code. We use hashmaps to do this.
	 */
	public String IndexPackage(String parentPkg, String[] childPkgs)
	{
		int i = 0;
		String result = ClientResponseCode.FAIL;

		if (parentPkg != null)
		{
			synchronized(this)
			{
				// If no child packages go ahead and index the package
				if(childPkgs[0].contentEquals("null"))
				{
					result = InsertPackage(parentPkg, childPkgs);
					return result;
				}
				
				// If child packages are present, enumerate to ensure all child packages are indexed
				for (i=0; i<childPkgs.length; i++)
				{
					if (!IndexDB.containsKey(childPkgs[i]))
					{
						// Dependency is not indexed
						LOGGER.warning("Package Indexing for Package :" + parentPkg + " was not successful as dependencies are not indexed.");
						return ClientResponseCode.FAIL;
					}
				}
				
				// If all child pkgs indexed, go ahead and insert
				if (i == childPkgs.length )
				{
					result = InsertPackage(parentPkg, childPkgs);
					return result;
				}
			
			} // end of synchronized block
		}
		
		LOGGER.warning("Returning an error while inserting package : " + parentPkg + ".");
		return ClientResponseCode.ERROR;
	}
	
	/**
	 * This method implements the in-memory implementation of
	 * removing a package with or without it's dependencies and
	 * returns the resulting String code. We use hashmaps to do this.
	 */
	public synchronized String RemovePackage(String pkgName)
	{
		// Package found in index
		if(IndexDB.containsKey(pkgName))
		{
			// Check if we can delete this package
			if(!EdgeCount.containsKey(pkgName))
			{
				String[] childPkgs = IndexDB.get(pkgName);
				SubtractEdgeCount(childPkgs);
				IndexDB.remove(pkgName);
				LOGGER.info("Package Removal for Package :" + pkgName + " was successful.");
				return ClientResponseCode.OK;
			}
			else
			{
				// Package cannot be removed as it has dependencies
				LOGGER.info("Package Removal for Package :" + pkgName + " was not successful. It has dependencies.");
				return ClientResponseCode.FAIL;
			}
		}
		// Package not indexed
		LOGGER.info("Package Removal for Package :" + pkgName + " is not indexed.");
		return ClientResponseCode.OK;
	}
	
	/**
	 * This method implements the in-memory implementation of
	 * querying a package with or without it's dependencies and
	 * returns the resulting String code. We use hashmaps to do this.
	 */
	public synchronized String QueryPackage(String pkgName)
	{
		synchronized(this)
		{
			if(IndexDB.containsKey(pkgName))
			{
				LOGGER.info("Package Query for Package :" + pkgName + " was successful.");
				return ClientResponseCode.OK;
			}
		} // end of sync block
		LOGGER.severe("Package Query for Package :" + pkgName + " was not successful.");
		return ClientResponseCode.FAIL;
	}
	
	// Private methods
	/**
	 * This method inserts the package name and the package dependency 
	 * list into the hashmap.
	 * @param parentPkg : Main package name
	 * @param childPkgs : Dependency package list
	 * @return : String result
	 */
	private String InsertPackage(String parentPkg, String[] childPkgs)
	{
		if(IndexDB.containsKey(parentPkg))
		{
			OverwritePackage(parentPkg);
		}
		
		try
		{
			IndexDB.put(parentPkg, childPkgs);
		}
		catch(NullPointerException npe)
		{
			LOGGER.severe("Null Pointer Exception while indexing package : "+ parentPkg);
		}
		
		if(!(childPkgs[0].equals("null")))
		{
			AddEdgeCount(childPkgs);
		}
		
		LOGGER.info("Package : " + parentPkg + " insertion was successful.");
		return ClientResponseCode.OK;
	}
	
	/**
	 * This method is used to reduce the incoming edge counts
	 * for all the child packages for the package are being 
	 * overwritten.
	 */
	private String OverwritePackage(String pkgName)
	{
		synchronized(this)
		{
			// Package found in index
			if(IndexDB.containsKey(pkgName))
			{
				String[] childPkgs = IndexDB.get(pkgName);
				SubtractEdgeCount(childPkgs);
				LOGGER.info("Overwrite was successful.");
				return ClientResponseCode.OK;
			}
		} // end of sync block
		LOGGER.warning("Overwrite function failed.");
		return ClientResponseCode.FAIL;
	}
	
	/**
	 * This is a method that initializes hashmap.
	 * We are using a concurrent hashmap.
	 * @return boolean result indicating if 
	 * initializing hashmaps was possible.
	 */
	private boolean InitializeHashmaps()
	{
		try
		{
			synchronized(this)
			{
				if (IndexDB == null)
				{
					// Create Hashmap with initial size: 1000, Load Factor: 0.75, Concurrency level: 100
					IndexDB = new ConcurrentHashMap<String,String[]>(10000,(float)0.75,100);
				}
				
				if (EdgeCount == null)
				{
					EdgeCount = new ConcurrentHashMap<String,Integer>(5000,(float)0.75,100);
				}
			} // end of sync block
			LOGGER.info("Both the Hashmaps are initialized.");
		}
		catch(Exception e)
		{
			LOGGER.severe("Exception while initializing Hashmaps. Message : " + e.toString());
			return false;
		}
		return true;
	}
	
	/**
	 * This method adds incoming edge counts to the 
	 * child packages when called.
	 * @param PkgNodes : This is the list of child packages.
	 */
	private synchronized void AddEdgeCount(String[] PkgNodes)
	{
		for (int i=0; i< PkgNodes.length; i++)
		{
			String pkgName = PkgNodes[i];
			if (EdgeCount.containsKey(pkgName))
			{
				int edgeCount = EdgeCount.get(pkgName);
				try
				{
					EdgeCount.put(pkgName, edgeCount+1);
				}
				catch(NullPointerException npe)
				{
					LOGGER.severe("Exception encountered while adding edgecount. Message : "+npe.toString());
				}
			}
			else
			{
				try
				{
					EdgeCount.put(pkgName, 1);
				}
				catch(NullPointerException npe)
				{
					LOGGER.severe("Exception encountered while adding edgecount. Message : "+npe.toString());
				}
			}
		}
	}
	
	/**
	 * This method subtracts incoming edge counts to the 
	 * child packages when called.
	 * @param PkgNodes : This is the list of child packages.
	 */
	private synchronized void SubtractEdgeCount(String[] PkgNodes)
	{
		for (int i=0; i<PkgNodes.length; i++)
		{
			String pkgName = PkgNodes[i];
			if (EdgeCount.containsKey(pkgName))
			{
				int edgeCount = EdgeCount.get(pkgName);
				if(edgeCount > 1)
				{
					try
					{
						EdgeCount.put(pkgName, edgeCount-1);
					}
					catch(NullPointerException npe)
					{
						LOGGER.severe("Exception encountered while subtracting edgecount. Message : "+npe.toString());
					}
				}
				else
				{
					EdgeCount.remove(pkgName);	
				}
			}
		}// end for
	}
}
