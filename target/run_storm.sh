nohup storm nimbus &
nohup storm supervisor &
nohup storm ui &

storm jar parallelfsdiagnosis-0.0.1-SNAPSHOT-jar-with-dependencies.jar storm.PfsdTopology
