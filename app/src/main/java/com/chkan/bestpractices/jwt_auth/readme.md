AuthInterceptor - added "Bearer + token" in Header for authentication

TokenAuthenticator - when we get error 401 "Unauthorized", Authenticator catch it and do refresh token. Can suspend and continue tasks, 
what started after expired token. Worked in work thread, so runBlocking it is safety. If couldn't refresh token and return null - pushed error 401.