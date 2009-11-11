gather-archiver - safe storage for data
=======================================

The gather-archiver feature provides an archival storage of raw data, timestamped 
and sealed with a calculated digest.

Building
--------

    cd gather-archiver
    mvn clean install

Running
-------
There are various dao implementations available. To select one, specify the
corresponding maven profile. For instance:

    mvn pax:provision -Pjpa

Current implementation profiles include:

* jpa - java persistence ORM using [EclipseLink][1]
* neo4j - the [Neo4j][2] graph-database
* db4o - [db4objects][3] object database

A shell command is available for working with the archiver feature. 
Try `archive help` to see what it can do. 

Next berries
------------

* [gather-alert](http://github.com/gatherdata/gather-alert)
* [gather-data](http://github.com/gatherdata/gather-data)


Dependencies
------------

* [gather-commons](http://github.com/gatherdata/gather-commons)

[1]: http://www.eclipse.org/eclipselink/    "EclipseLink"
[2]: http://neo4j.org/                      "Neo4j"
[3]: http://developer.db4o.com/             "db4objects"
