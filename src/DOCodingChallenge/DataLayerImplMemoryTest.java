package DOCodingChallenge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DataLayerImplMemoryTest {

	DataLayerImplMemory memObject = new DataLayerImplMemory();
	
	@Test
	void testIndexer() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "http";
		childPkgs[0] = "ftp";
		result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("FAIL\n", result);
	}
	
	@Test
	void testQuery() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = memObject.QueryPackage(parentPkg);
		assertEquals("FAIL\n", result);

	}
	
	@Test
	void testRemove() {
		String parentPkg = "tar";
		String childPkgs[] = {"null"};
		String result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "xtar";
		childPkgs[0] = "tar";
		result = memObject.IndexPackage(parentPkg, childPkgs);
		assertEquals("OK\n", result);
		
		parentPkg = "tar";
		result = memObject.RemovePackage(parentPkg);
		assertEquals("FAIL\n", result);

		parentPkg = "xtar";
		result = memObject.RemovePackage(parentPkg);
		assertEquals("OK\n", result);
		
		parentPkg = "tar";
		result = memObject.RemovePackage(parentPkg);
		assertEquals("OK\n", result);
		
		parentPkg = "http";
		result = memObject.RemovePackage(parentPkg);
		assertEquals("OK\n", result);		
	}

}
