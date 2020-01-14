# oauth2-jwt
Demo of springboot+oauth2+jwt

keytool -genkeypair -alias mytestkey -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=CN" -keypass changeme -keystore server.jks -storepass letmein

keytool -export -keystore server.jks -alias mytestkey -file example.cer

openssl x509 -inform der -in example.cer -pubkey -noout