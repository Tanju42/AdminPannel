cd target
copy /Y %1 C:\IxD\Server\plugins
cd C:\IxD\Server
java -Xms2G -Xmx2G -jar paper.jar nogui