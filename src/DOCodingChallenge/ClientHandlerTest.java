package DOCodingChallenge;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClientHandlerTest {

	PackageIndexer pckIndexerObj = new PackageIndexer();
	ClientHandler chObj = new ClientHandler(null,pckIndexerObj);
	
	@Test
	void testParseAndExecuteIndexCommand()
	{
		String command = "INDEX|cloog|gmp,isl,pkg-config";
		String result = chObj.ParseAndExecute(command);
		assertEquals("FAIL\n", result);
		
		command = "BLINDEX|cloog|gmp,isl,pkg-config";
		result = chObj.ParseAndExecute(command);
		assertEquals("ERROR\n", result);
		
		command = "INDEXFFFF|muttils|";
		result = chObj.ParseAndExecute(command);
		assertEquals("ERROR\n", result);
		
		command = "INDEX|xyz|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|muttils|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|xvf|muttils,xyz,abc";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|PDD|muttils,xyz,abc,123,456";
		result = chObj.ParseAndExecute(command);
		assertEquals("FAIL\n", result);
		
		command = "INDEX|socketLib|xvf,muttils,abc,xyz";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
	}

	@Test
	void testParseAndExecuteRemoveCommand()
	{
		String command = "REMOVE|cloog|";
		String result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|muttils|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "REMOVE|cloog|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "INDEX|xyz|abc";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "REMOVE|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("FAIL\n", result);
		
		command = "REMOVE|xyz|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "REMOVE|xyz|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "REMOVE|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "XXXXREMOVE|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("ERROR\n", result);	
		
		command = "REMOVEXXX|abc|";
		result = chObj.ParseAndExecute(command);
		assertEquals("ERROR\n", result);
	}
	
	@Test
	void testParseAndExecuteQueryCommand()
	{
		String command = "QUERY|cloog|";
		String result = chObj.ParseAndExecute(command);
		assertEquals("FAIL\n", result);
		
		command = "INDEX|cloog|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
		
		command = "QUERY|cloog|";
		result = chObj.ParseAndExecute(command);
		assertEquals("OK\n", result);
	}

}
