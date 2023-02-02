
## Generate self sign SSL for backend-resources
login to linux terminal and run this 
```shell
openssl req -x509 -out backend-gateway-clientcert.pem -keyout backend-gateway-clientkey.pem \
-newkey rsa:2048 -nodes -sha256 \
-subj '/CN=backend-gateway-client' -extensions EXT -config <( \
printf "[dn]\nCN=backend-gateway-client\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:backend-gateway-client\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
```
edit the application.yml and specify the pem certificate path as follows
```shell
server:
  port: 8543
  ssl:
    certificate: classpath:backend-gateway-clientcert.pem
    certificate-private-key: classpath:backend-gateway-clientkey.pem  
```

## Import the sso cert into the java cacerts file eg /etc/ssl/certs/java
Do the following to import the sso public cert into java cacerts file. The default keystore password is changeit

We also need to import the backend-resource self sign into the java cacerts

```shell
cd etc/ssl/certs/java
cp cacerts cacerts.ori
cp ~/Downloads/sso.billview.com.au.cer .
keytool -printcert -file sso.billview.com.au.cer
keytool -import -alias sso_billview -file sso.billview.com.au.cer -keystore cacerts

sudo cp ~/Downloads/backend-resources.cer .
sudo keytool -printcert -file backend-resources.cer
sudo keytool -import -alias local_backend-resources -file backend-resources.cer -keystore cacerts
```
Note we also need to change intellij to use external jdk

## Upload file to EC2 using private PEM key
```shell
scp -i /home/michael/CloudStation/security/michael-home-ec2-access.pem backend-gateway-client-0.0.3-SNAPSHOT.jar ubuntu@api.billview.com.au:/home/ubuntu/prod-apps/gateway-client.jar
```



| **ID**              	| **HTTP Method** 	| **Description**                                                             	|
|---------------------	|-----------------	|-----------------------------------------------------------------------------	|
| **_globalfilters_** 	|       GET       	| Displays the list of global filters applied to the routes.                  	|
| **_routefilters_**  	|       GET       	| Displays the list of GatewayFilter factories applied to a particular route. 	|
| **_refresh_**       	|       POST      	| Clears the routes cache.                                                    	|
| **_routes_**        	|       GET       	| Displays the list of routes defined in the gateway.                         	|
| **_routes/{id}_**   	|       GET       	| Displays information about a particular route.                              	|
| **_routes/{id}_**   	|       POST      	| Add a new route to the gateway.                                             	|
| **_routes/{id}_**   	|      DELETE     	| Remove an existing route from the gateway.                                  	|