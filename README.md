# CryptoFile

## Develop

Download JavaFX from [Gluon](https://gluonhq.com/products/javafx/) 17.0.2 version and unpack
In Idea IDE set as VM option:

```
--module-path "\javaFX\unpack\sdk\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml
```

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

1. [Debug](https://stackoverflow.com/a/62654500/9401964) javafx app 1;
2. [Debug](https://stackoverflow.com/a/61341407/9401964) javafx app 2;
3. [Debug](https://stackoverflow.com/a/61474494/9401964) javafx app 3;
4. [Create](https://stackoverflow.com/questions/68871952/how-to-use-jpackage-to-make-a-distribution-format-for-javafx-applications) distribution file.