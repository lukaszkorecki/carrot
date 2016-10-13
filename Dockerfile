FROM java:openjdk-8-jre-alpine

ENV JAVA_HOME=/usr/lib/jvm/default-jvm
ENV PATH /usr/local/bin:$PATH

ADD ./target/uberjar/carrot-0.1.0-SNAPSHOT-standalone.jar /app/carrot.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar",  "/app/carrot.jar", "-DAggressiveOpts=true", "-DUseCompressedOops=true", "-server", "-XX:NewSize=12m", "-XX:MaxNewSize=12m", "-XX:SurvivorRatio=8", "-Xms24m", "f-Xmx24m", "-Xgc:genpar",     "-XXfullCompaction" ]
