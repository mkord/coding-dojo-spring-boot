# Run PostgreSQL in docker
```
docker run -d --name dev-postgres -e POSTGRES_USER=weather-db -e POSTGRES_PASSWORD=Ather2021# -e POSTGRES_DB=weather-db -v ${HOME}/postgres-data/:/var/lib/postgresql/data -p 5432:5432 postgres
```
## Create schema
(Execute next command from project root)
```
cat src/main/resources/data/schema.sql | docker exec -i dev-postgres psql -U weather-db -d weather-db
```

# To run application with profile `prod` 
Create environment variable
```
export JASYPT_ENCRYPTOR_PASSWORD=secret-weather-app-data-prod
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

# To re-encrypt with new `some-new-secret-password`
```
download https://github.com/jasypt/jasypt/releases/tag/jasypt-1.9.3
unzip jasypt-1.9.3-dist.zip
cd jasypt-1.9.3
chmod +x bin/*
``` 

for datasource password 
```
./encrypt.sh input=Ather2021# password=[some-new-secret-password] algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
```
and change application-prod.yaml with new encrypted password

for OpenWeather app id
```
./encrypt.sh input=c95bcc176fcd165de1d38eed87d52037 password=[some-new-secret-password] algorithm=PBEWITHHMACSHA512ANDAES_256 ivGeneratorClassName=org.jasypt.iv.RandomIvGenerator
``` 
and change application-prod.yaml with new encrypted appid
Do not forget set environment variable before start
```
export JASYPT_ENCRYPTOR_PASSWORD=some-new-secret-password
``` 
