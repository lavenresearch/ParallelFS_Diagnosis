# INSTALL JAVA 6 in SUSE 11
Download Java SE 6 "jdk-6u45-linux-x64.bin[^account]" from Oracle's website (http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase6-419409.html) and install it as follows:

    chmod 775 jdk-6u45-linux-x64.bin
    yes | jdk-6u35-linux-x64.bin
    mv jdk1.6.0_35 /opt
    ln -s /opt/jdk1.6.0_35/bin/java /usr/bin
    ln -s /opt/jdk1.6.0_35/bin/javac /usr/bin
    JAVA_HOME=/opt/jdk1.6.0_35
    export JAVA_HOME
    PATH=$PATH:$JAVA_HOME/bin
    export PATH

[^account]: email:opencourses@yahoo.com, password:Oj123456
