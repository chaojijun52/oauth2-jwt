# oauth2-jwt
Demo of springboot+oauth2+jwt

keytool -genkeypair -alias mytestkey -keyalg RSA -dname "CN=Web Server,OU=Unit,O=Organization,L=City,S=State,C=CN" -keypass changeme -keystore server.jks -storepass letmein

keytool -export -keystore server.jks -alias mytestkey -file example.cer

openssl x509 -inform der -in example.cer -pubkey -noout

http://localhost.:9090/oauth/token
Basic auth:
	Username:testjwtclientid
	Password:jwtpass
Headers:
	Content-Type:application/x-www-form-urlencoded
Body:
	grant_type:password
	username:admin
	password:jwtpass
	
http://localhost.:9080/hello
Bear token:
	Token:eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1NzkwMTk1NjIsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiNWFkMzFiNjgtNmE1MC00NjMwLWIyYmQtYjVmMjEzZTIxYWJlIiwiY2xpZW50X2lkIjoidGVzdGp3dGNsaWVudGlkIiwidGltZXN0YW1wIjoxNTc5MDE4NjYyNzYzfQ.bo2Z5szINQyIONPUm9j5UO5DGphqnRzkgNSLYINZHx6A9ZircWwFmcu0Xa_n9kxsGC8zmCpAwbNzjrVpIFlxk1WMAoCxVTSUhloQyEKudVFFGfb0m9BiqNWyEVEiS5UJ_ZKomn27s4NAnYs96ZNpfajCamIEzHUiyh4dX32PwirehrvhpOEa77_pcJTjintY3pkIG8Vr5SyWBvzYbJEpRNrUDOt_o7TlmO_zIDX3a34dPZGoOuLF82_XnulIcKs-jjiiWRBgv3ibHxlMXlaDrH-hBvTe8R3mTXE3wJ6OMPCc2cg6f590ISIxG_HH7R0QgnfEhkq8hmepbR4CkgC1Og
Headers:
	Content-Type:text/plain
Body:
	Text:Wu Jun