# CryptoFile

## How to build

Run in terminal via Maven

```termional
mvn clean javafx:run
```

## How to generate keypair

```terminal
keytool -genkey -alias exampleKey -keyalg RSA -keysize 4096 -validity 365 -keystore keystore.pfx -storetype PKCS12
```