javac dhcpPoolGenerator/*.java
jar cfm dhcpPoolGenerator.jar dhcpPoolGenerator/Manifest.txt dhcpPoolGenerator/*.class 
java -jar dhcpPoolGenerator.jar 172.16.0.0 16 501
mv dhcpPoolGenerator.jar dhcpPoolGenerator/dhcpPoolGenerator.jar


