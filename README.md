# Authorization Server with Spring Security

The content of this repository is explained in my YouTube channel.

https://youtube.com/playlist?list=PLab_if3UBk9-AArufc8CryyhSDVqkNT-U

## Run on Localhost

To be able to run the project on localhost, make sure you've followed the next steps.

### Backend alias

The following lines must be added in ```/etc/hosts``` to avoid having the browser create the cookies for the same 
URL overriding values from the different backends.
```
127.0.0.1       backend-auth
127.0.0.1       backend-gateway-client
127.0.0.1       backend-resources
```

### Local Database

The backend-auth needs a database where to store the credentials. The following docker command will create it.

```
docker run -d -e POSTGRES_HOST_AUTH_METHOD=trust -e POSTGRES_USER=auth_usr -e POSTGRES_PASSWORD=pwd -e POSTGRES_DB=authdb -p 5434:5432 postgres:13
```

## Useful links

Follow this link to see how to set up the claims in keycloak

Ty to inport this realm into keycloak

https://github.com/Baeldung/spring-security-oauth/blob/master/oauth-jwt/jwt-auth-server/src/main/resources/baeldung-realm.json

https://www.baeldung.com/keycloak-custom-user-attributes
