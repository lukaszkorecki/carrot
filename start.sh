jar=$(find ./target -name "carrot-*-standalone.jar" | tail -1)
echo $jar

java -jar $jar \
     -DAggressiveOpts=true \
     -DUseCompressedOops=true \
     -server \
     -Xmx20m -Xms20m -XX:NewSize=256k -mx20m -XshowSettings:vm
