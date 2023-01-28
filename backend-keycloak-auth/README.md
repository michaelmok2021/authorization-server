### To enable self signed SSL in Keycloak Quarkus 20.+ 
Login to linux terminal

Create a certs folder in the folder where the docker-compose.yml file is located eg
```shell
mkdir /home/dockeruser/keycloak-20.0.3/certs
```
Run the following command
```shell
openssl req -x509 -out localhostcert.pem -keyout localhostkey.pem \
  -newkey rsa:2048 -nodes -sha256 \
  -subj '/CN=localhost' -extensions EXT -config <( \
   printf "[dn]\nCN=localhost\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:localhost\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")

```
Copy the two .pem files to certs
