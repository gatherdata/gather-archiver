gather-archiver - safe storage for data
=======================================

The gather-archiver feature provides an archival storage of raw data, timestamped 
and sealed with a calculated digest.

Running
-------
There are various dao implementations available. To select one, specify the
corresponding maven profile. For instance:

    mvn pax:provision -Pneo4j

Current implementation profiles include:

* jpa - common java persistence ORM
* vfs - a partial implementation which uses the file system
* neo4j - the [Neo4j][3] graph-database
* db4o - [db4objects][4] object database

[3]: http://neo4j.org/              "Neo4j"
[4]: http://developer.db4o.com/     "db4objects"
