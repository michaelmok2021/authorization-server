### To enable self-signed SSL in Keycloak Quarkus 20.+ 
Login to linux terminal

Create a certs folder in the folder where the docker-compose.yml file is located e.g.
https://certbot.eff.org/docs/using.html#where-are-my-certificates
```shell
mkdir /home/dockeruser/keycloak-20.0.3/certs
```
Install certbot using snap ie


THe letsencrypt cert as located below
```shell
ls /etc/letsencrypt/live/sso.billview.com.au
```
Run the following command
```shell

openssl req -x509 -out localhostcert.pem -keyout localhostkey.pem \
  -newkey rsa:2048 -nodes -sha256 \
  -subj '/CN=localhost' -extensions EXT -config <( \
   printf "[dn]\nCN=localhost\n[req]\ndistinguished_name = dn\n[EXT]\nsubjectAltName=DNS:localhost\nkeyUsage=digitalSignature\nextendedKeyUsage=serverAuth")
sudo certbot certonly -a standalone -d backend-resources.com
```
Copy the two .pem files to certs folder

These 2 files need to be readable for all so the Keycloak runtime which runs as Keycloak user can read these files. 

The authorisation endpoint is 
```shell
https://backend-auth:8443/realms/myfirsttest/protocol/openid-connect/auth
```
Need to check this out to put keycloak behinf nginx

https://www.reddit.com/r/selfhosted/comments/zpj7ss/keycloak_nginx_oauth2proxy_with_dockercompose_an/
