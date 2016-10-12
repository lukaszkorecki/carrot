jar=$(find ./target -name "carrot-*-standalone.jar" | tail -1)
echo $jar
m=12m
echo java -jar $jar \
     -DAggressiveOpts=true \
     -DUseCompressedOops=true \
     -server \
     -Xmx$m -Xms$m

java -jar $jar \
     -DAggressiveOpts=true \
     -DUseCompressedOops=true \
     -server \
     -Xmx$m -Xms$m
