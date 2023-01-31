
## Generate self sign SSL for backend-resources
login to linux terminal and run this 
```shell
openssl req -x509 -out backend-resourcescert.pem -keyout backend-resourceskey.pem \
-newkey rsa:2048 -nodes -sha256 \
-subj '/CN=backend-resources' -extensions EXT -config <( \
printf "[dn]\nCN=backend-resources\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:backend-resources\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
```
then convert the pem files to p12, when prompted enter password as password99
```shell
openssl pkcs12 -export -out backend-resources-cert.p12 -in backend-resourcescert.pem -inkey backend-resourceskey.pem -name bootalias
```
edit the application.yml as follows
```shell
server:
  port: 8443
  http.port: 8080
  ssl:
    key-store: classpath:backend-resources-cert.p12
    key-store-password: password99
    keyStoreType: PKCS12
    keyAlias: bootalias
```
