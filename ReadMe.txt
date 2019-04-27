README.txt

Please see the 'Design Rationale' for more design details.

This project is a package indexing server. It waits for client's to send it indexing requests. Here is a description of how it works :

The system keeps track of package dependencies. Clients will connect to the server and inform which packages should be indexed, and which dependencies they might have on other packages. It keeps our index consistent, so your server must not index any package until all of its dependencies have been indexed first. The server does not remove a package if any other packages depend on it. The server opens a TCP socket on port 8080. It accepts connections from multiple clients at the same time, all trying to add and remove items to the index concurrently.

Messages from clients follow this pattern:
```
<command>|<package>|<dependencies>\n
```
Where:
* `<command>` is mandatory, and is either `INDEX`, `REMOVE`, or `QUERY`
* `<package>` is mandatory, the name of the package referred to by the command, e.g. `mysql`, `openssl`, `pkg-config`, `postgresql`, etc.
* `<dependencies>` is optional, and if present it will be a comma-delimited list of packages that need to be present before `<package>` is installed. e.g. `cmake,sphinx-doc,xz`
* The message always ends with the character `\n`

Here are some sample messages:
```
INDEX|cloog|gmp,isl,pkg-config\n
INDEX|ceylon|\n
REMOVE|cloog|\n
QUERY|cloog|\n
```
Possible response codes are `OK\n`, `FAIL\n`, or `ERROR\n`. After receiving the response code, the client can send more messages.
The response code returned should is as follows:
* For `INDEX` commands, the server returns `OK\n` if the package can be indexed. It returns `FAIL\n` if the package cannot be indexed because some of its dependencies aren't indexed yet and need to be installed first. If a package already exists, then its list of dependencies is updated to the one provided with the latest command.
* For `REMOVE` commands, the server returns `OK\n` if the package could be removed from the index. It returns `FAIL\n` if the package could not be removed from the index because some other indexed package depends on it. It returns `OK\n` if the package wasn't indexed.
* For `QUERY` commands, the server returns `OK\n` if the package is indexed. It returns `FAIL\n` if the package isn't indexed.
* If the server doesn't recognize the command or if there's any problem with the message sent by the client it should return `ERROR\n`.

***************************************************

Classes :
1. Server - This class handles the requests from clients who call this server with commands.

2. ClientHandler - This class extends the Runnable interface. It parses and verifies the requests from the client 
and then pass it on to the Package Indexer implementation.

3. DataLayerInterface - This is an interface which declares the methods which need to be implemented for the 
Package Indexer to work. This is to make the design extendable. This way, we can later add some on disk or 
Database implementations of the package indexer as well.

4. DataLayerImplMemory - This is an actual in-memory implementation of the Package Indexer using Hashmaps.

5. PackageIndexer - This is an adapter layer class which instantiates the actual implementation of the DataLayerInterface.

6. ClientResponseCode - This is a class which shares the common return codes for the client.

****************************************************

How to run :
Open the project in Eclipse and run from program.
Else you can also run the attached jar file as follows :
java -jar PackageIndexer.jar

****************************************************

Docker :

To run docker please run 'docker build -t package_indexer_image .'
This will start a docker image with the server.

****************************************************

Testing :

Please see the result for concurrency of 100 clients in the file : TestOutputForConcurrency100.txt

*****************************************************

Git Repo :

git clone https://github.com/testuserip/PackageIndexerProject.git

Link to repo : https://github.com/testuserip/PackageIndexerProject
