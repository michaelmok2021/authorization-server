
## Generate self sign SSL for backend-resources
login to linux terminal and run this 
```shell
openssl req -x509 -out backend-gateway-clientcert.pem -keyout backend-gateway-clientkey.pem \
-newkey rsa:2048 -nodes -sha256 \
-subj '/CN=backend-gateway-client' -extensions EXT -config <( \
printf "[dn]\nCN=backend-gateway-client\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:backend-gateway-client\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
```
then convert the pem files to p12, when prompted enter password as password99
```shell
openssl pkcs12 -export -out backend-gateway-client-cert.p12 -in backend-gateway-clientcert.pem -inkey backend-gateway-clientskey.pem -name bootalias
```
edit the application.yml as follows
```shell
server:
  port: 8443
  http.port: 8080
  ssl:
    key-store: classpath:backend-gateway-client-cert.p12
    key-store-password: password99
    keyStoreType: PKCS12
    keyAlias: bootalias
```
## Import the backend-auth p12 cert into a Java truststore...
Login to a linux terminal and issue the following command
```shell
keytool -importkeystore -deststorepass password99 -destkeystore backend-gateway-client-keystore.jks -srckeystore backend-gateway-client-cert.p12 -srcstoretype PKCS12 -srcstorepass password99
```
