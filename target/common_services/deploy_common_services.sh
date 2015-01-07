chmod 775 jdk-6u45-linux-x64.bin
yes | jdk-6u45-linux-x64.bin
mv jdk1.6.0_45 /opt
ln -s /opt/jdk1.6.0_45/bin/java /usr/bin
ln -s /opt/jdk1.6.0_45/bin/javac /usr/bin
JAVA_HOME=/opt/jdk1.6.0_45
export JAVA_HOME
PATH=$PATH:$JAVA_HOME/bin
export PATH
tar xzvf kafka_2.10-0.8.1.1.tgz
tar xzvf zookeeper-3.4.6.tar.gz
cp -R kafka_2.10-0.8.1.1 /opt
cp -R zookeeper-3.4.6 /opt
PATH=$PATH:/opt/kafka_2.10-0.8.1.1/bin/
PATH=$PATH:/opt/zookeeper-3.4.6/bin/
export PATH
CLASSPATH=$CLASSPATH:/opt/kafka_2.10-0.8.1.1/libs/*
export CLASSPATH
cp profile.local /etc
