package DOCodingChallenge;

/***
 * Create an interface for the indexer functions.
 * This way we can have different 
 * implementations for these functions.
 * We can have a memory implementation like the one
 * I have implemented. Similarly, we can also have 
 * a persistent indexer implementation in a file, or in
 * a db.
 * @author abc
 *
 */
public interface DataLayerInterface {

	public String IndexPackage(String ParentPkg, String[] ChildPkgs);
	
	public String RemovePackage(String PkgName);
	
	public String QueryPackage(String PkgName);

}
