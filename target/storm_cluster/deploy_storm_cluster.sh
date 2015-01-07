tar zxvf zeromq-2.2.0.tar.gz
unzip jzmq-master.zip
cd zeromq-2.2.0
./configure
make
make install
cd ../jzmq-master
./autogen.sh
./configure
make
make install
tar zxvf apache-storm-0.9.1-incubating.tar.gz
cp -R apache-storm-0.9.1-incubating /opt
chmod 775 /opt/apache-storm-0.9.1-incubating/bin/storm
CLASSPATH=$CLASSPATH:/opt/apache-storm-0.9.1-incubating/*
CLASSPATH=$CLASSPATH:/opt/apache-storm-0.9.1-incubating/conf/
CLASSPATH=$CLASSPATH:/opt/apache-storm-0.9.1-incubating/lib/*
export CLASSPATH
PATH=$PATH:/opt/apache-storm-0.9.1-incubating/bin/
export PATH
