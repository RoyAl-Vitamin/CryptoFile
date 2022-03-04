# CryptoFile

## How to build

Run in terminal via Maven

```termional
mvn clean javafx:run
```

## How to generate keypair using java keytool

```terminal
keytool -genkey -alias exampleKey -keyalg RSA -keysize 4096 -validity 365 -keystore keystore.pfx -storetype PKCS12
```

## How to debug app

See instruction:
1. [link1](https://stackoverflow.com/a/62654500/9401964)
2. [link2](https://stackoverflow.com/a/61341407/9401964)
3. [link3](https://stackoverflow.com/a/61474494/9401964)