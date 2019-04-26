package DOCodingChallenge;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PackageIndexerTest {
	
	PackageIndexer pkgIndexer = new PackageIndexer();
	
	@Test
	void testIndexer() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "http";
		childPkgs[0] = "ftp";
		result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("FAIL\n", result);
	}
	
	@Test
	void testQuery() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = pkgIndexer.Query(parentPkg);
		assertEquals("FAIL\n", result);

	}
	
	@Test
	void testRemove() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = pkgIndexer.Index(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "tar";
		result = pkgIndexer.Remove(parentPkg);
		assertEquals("FAIL\n", result);

		parentPkg = "xtar";
		result = pkgIndexer.Remove(parentPkg);
		assertEquals("OK\n", result);
		
		parentPkg = "tar";
		result = pkgIndexer.Remove(parentPkg);
		assertEquals("OK\n", result);
		
		parentPkg = "http";
		result = pkgIndexer.Remove(parentPkg);
		assertEquals("OK\n", result);		
	}
}
