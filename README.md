# Spring-Boot-OAuth2-Examples -Auth-SERVER

## STEPS:

1. To get the access_token from this Auth server, client app will 1st call below API. So Go to POSTMAN and hit below URL as we don't have client project here
   
   ```
   -URL: localhost:8185/oauth/token
   -REQUEST TYPE : POST
   -REQUEST BODY: x-www-form-urlencoded --> grant_type = password, username=abhi, password=abhi123
   -AUTH: BASIC -->username=mobile, password=pin
   ```
   
   Above request will return the response in below format,
   
   ```
   {
      "access_token": "8edc557b-53a7-4c7c-9d74-425119539a31",
      "token_type": "bearer",
      "refresh_token": "eaa511d5-226b-4ddc-9ce7-5042f0ecbc84",
      "expires_in": 3599,
      "scope": "READ,WRITE"
   }
  
2. Then client app will call below API with access token which was provided by Auth server in earllier request. So in POSTMAN hit below request.

   ```
   -URL: localhost:8282/oauth/check_token?token=8edc557b-53a7-4c7c-9d74-425119539a31
   -REQUEST TYPE : GET
   -AUTH: BASIC -->username=mobile, password=pin
   ```
   
It will return Authorization details and user info back. Based on which we can restrict some resource on resource server.
