jar=$(find ./target -name "carrot-*-standalone.jar" | tail -1)
echo $jar

java -jar $jar \
     -DAggressiveOpts=true \
     -DUseCompressedOops=true \
     -server \
     -XX:NewSize=12m -XX:MaxNewSize=12m -XX:SurvivorRatio=8 -Xms24m -Xmx24m  -Xgc:genpar  -XXfullCompaction
