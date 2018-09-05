# sharewood-oauth2-jwt
I present here a different implementation of the previous project https://github.com/dubersfeld/sharewood-boot-oauth2-upgrade. It uses the Spring Boot version 2.0.4.RELEASE. The main difference is the use of JWT instead of basic tokens. Moreover the JWT are signed using an asymetric key.

Here is the technology stack needed for running the complete application:

Any Linux platform (I use Ubuntu 16.04)

MySQL server installed

A recent Apache Maven version installed (I used 3.3.9)

In addition I used Spring Tool Suite for developing this demo but it is not required for running the application.

The complete application is comprised of an OAuth2 authorization server and an OAuth2 resource server. A standalone OAuth2 client is also used.

The authorization server and the resource server connect to two separate databases. All user passwords and client secret are explicitly stored as BCrypt hashes.

Project name         | Port | Database                 | Persists
---------------------|------|--------------------------|----------
authorization-server | 8080 | sharewood\_jwt\_users    | users,client
sharewood            | 8081 | sharewood\_photos        | photos


Here are the steps to run the application.

## 1. Database creation

In MySQL client run the commands:
```
SOURCE sharewoodJwtDB.sql
SOURCE sharewoodPhotoDB.sql 
```

This creates the two databases named sharewood\_jwt\_users and sharewood\_photos.

## 2. Key pair creation

Run the command:

keytool -genkeypair -alias mytest -keyalg RSA -keypass mypassword -keystore mytest.jks -storepass mypassword

This creates a binary keystore file named mytest.jks

Run the command:

keytool -list -rfc --keystore mytest.jks | openssl x509 -inform pem -pubkey > publicKey.txt

This creates a text file named public.txt that looks like this:

```
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkyfN9+50eUlOAGQ4zNJe
8FhGEGYhtGPtuJiTROiZd+1akGO3QcfZc659543NiMAs3LAoHCmoukQxAbLmJ372
+h4o/CBydsX3c2HD/4GntUEjxfSSuwJLl4O5LTtqFUm5K5E9bMxygUvpX10fjBso
uf7nOzglYOc0ukJTYYGjDRBbaXYL/M2YeBH+ivh8MhwFq+RpgiEbYyuIRnlcO3Ap
HWqIyfVDB/pTjCUYSbZLz16/8L9kTYV2F0z5U8nuwhBUkJJB6Q4PWIp5+kxeGcDp
YOQi5BZvhayGKIy04hKrIZO2xn3fta/msjoDQCWXwUgSLkKmXw+sEoUNd2B+AOow
+wIDAQAB
-----END PUBLIC KEY-----
-----BEGIN CERTIFICATE-----
MIIDZzCCAk+gAwIBAgIEahT1nzANBgkqhkiG9w0BAQsFADBkMQswCQYDVQQGEwJG
UjEQMA4GA1UECBMHVW5rbm93bjEPMA0GA1UEBxMGQ2FjaGFuMQ4wDAYDVQQKEwVh
cmVwbzEOMAwGA1UECxMFc2F0b3IxEjAQBgNVBAMTCURvbWluaXF1ZTAeFw0xODA5
MDMxMDA3MDdaFw0xODEyMDIxMDA3MDdaMGQxCzAJBgNVBAYTAkZSMRAwDgYDVQQI
EwdVbmtub3duMQ8wDQYDVQQHEwZDYWNoYW4xDjAMBgNVBAoTBWFyZXBvMQ4wDAYD
VQQLEwVzYXRvcjESMBAGA1UEAxMJRG9taW5pcXVlMIIBIjANBgkqhkiG9w0BAQEF
AAOCAQ8AMIIBCgKCAQEAkyfN9+50eUlOAGQ4zNJe8FhGEGYhtGPtuJiTROiZd+1a
kGO3QcfZc659543NiMAs3LAoHCmoukQxAbLmJ372+h4o/CBydsX3c2HD/4GntUEj
xfSSuwJLl4O5LTtqFUm5K5E9bMxygUvpX10fjBsouf7nOzglYOc0ukJTYYGjDRBb
aXYL/M2YeBH+ivh8MhwFq+RpgiEbYyuIRnlcO3ApHWqIyfVDB/pTjCUYSbZLz16/
8L9kTYV2F0z5U8nuwhBUkJJB6Q4PWIp5+kxeGcDpYOQi5BZvhayGKIy04hKrIZO2
xn3fta/msjoDQCWXwUgSLkKmXw+sEoUNd2B+AOow+wIDAQABoyEwHzAdBgNVHQ4E
FgQUUJFz2MTM5GIGzcZ3poxbMeuo7PwwDQYJKoZIhvcNAQELBQADggEBAC048xgF
87T1/ZWPuRwDjdK/A7DFVk/soDTq9x67vO0BXLhJEBXY4wSjrOvXJsFR6QwWSZE5
Vw5RURcLtYjqCTrIlbleABiHLXWhnKXySipjwJmvkP+34Kt1H6OBKRtrkmSqpEy4
ZWAHuRFzs/IcKug0j85Xx/0Dc9IowfafixVlVUoJTWYe4M5DXDc2yrO3dedNckiG
UcuL2YfizhdcXer+2vx7EiKr17cp8qhyK9M/YDtCG/9ogscu2rMv6efH+k4UaTIC
IMvrhbm0xIbczDjDqrtb1fnDcvaBKfMEJamDL68rXNw0whQZHb48ELRDPGM21Xgs
bUhy2dX14Gst9aQ=
-----END CERTIFICATE-----

``` 

Copy the file mytest.jks into the folder src/main/resources of authorization-server.

Create a text file public.txt in the folder src/main/resource of sharewood and paste into this file the PUBLIC KEY part of publicKey.txt.  

## 3. JAR files creation

In each project directory:
1. authorization-server
1. sharewood
1. fleetwood

run the Maven command:
```
mvn clean package
```
 
## 4. Launching the application

In each project directory
1. authorization-server
1. sharewood
1. fleetwood

run the Maven command:
```
mvn spring-boot:run
```

Now you can login to the fleetwood client on port 9090. Once logged the user can execute all RESTful request after authenticating to the authorization server and granting to fleetwood the required scope.

The users server is populated with two users Alice and Carol who have the role USER. Their passwords are:
Alice: o8p7e6r5a
Carol: s1a2t3o4r

Now the user is presented the authentication page and approval page shown below:

![alt text](images/authenticationPage.png "Authentication page")
![alt text](images/approvalPage.png "Approval page")

Note that with this approach there is no way to choose between several scopes. For this reason only one scope TRUST is used.

A debugging filter InspectHeaderFilter is inserted in the filter chain of sharewood server. It only displays the encoded JWT extracted from the incoming Authorization header. The encoded JWT looks like this:

```
bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiU0hBUkVXT09EIl0sInVzZXJfbmFtZSI6IkNhcm9sIiwic2NvcGUiOlsiVFJVU1QiXSwiZXhwIjoxNTM2MTM4NTQ1LCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImp0aSI6IjU5MDgwYTBlLWM5MGYtNDIwNi05NzllLWI0ZjFmNDVlNmQ1YiIsImNsaWVudF9pZCI6IkZsZWV0d29vZCJ9.NNO4zi55IpDU7AemvRrrqJpW92C5yp2oyR8YLz5J5ymNGxRgI3Ap1-Eex5Niqmvun1QHMth-8amsGLTT9596L8wAo-hsBL2kpxqy59McR7W9yMOH77VAvTLog1iUht5Xr2vW5e6zn7bNXk-MsqS7zTMnrJxfJYHD7V2yCzmgrUFzus3UjOeVyn5i-5h6JohW3k9H3D-isxcgJyPJrr3jfEwag1tZSuuk06VM5rZv1N9Lr_wqyq4PnzlyHjY3_U43Q7HOUZm4_j6H9htMPUfHdRHeZclwSMbRvZQLltJVWY4ES9lr8NCqksdhmeSwr2SEzu3ydTCW7Aicro0bS8UQ1w
```  

The payload can be extracted using any online tool like https://www.jsonwebtoken.io/

The result looks like:

```
{
 "aud": [
  "SHAREWOOD"
 ],
 "user_name": "Carol",
 "scope": [
  "TRUST"
 ],
 "exp": 1536137761,
 "authorities": [
  "USER"
 ],
 "jti": "59080a0e-c90f-4206-979e-b4f1f45e6d5b",
 "client_id": "Fleetwood",
 "iat": 1536134161
}

```




