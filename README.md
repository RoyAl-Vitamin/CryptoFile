# CryptoFile

## Разработка

Скачать JavaFX с [Gluon](https://gluonhq.com/products/javafx/) 21 версию и распаковать. В Idea IDE установить "VM option":

```
--module-path "/home/username/.jdks/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml
```

## Как сгенерировать классы

```shell
mvn jooq-codegen:generate
```

## Как собрать

```shell
mvn clean javafx:run
```
## Как сгенерировать keypair, используя java keytool

```shell
keytool -genkey -alias exampleKey -keyalg RSA -keysize 4096 -validity 365 -keystore keystore.pfx -storetype PKCS12
```

## Как дебажить приложение

Смотри инструкции:

1. [Debug](https://stackoverflow.com/a/62654500/9401964) javafx app 1;
2. [Debug](https://stackoverflow.com/a/61341407/9401964) javafx app 2;
3. [Debug](https://stackoverflow.com/a/61474494/9401964) javafx app 3;
4. [Create](https://stackoverflow.com/questions/68871952/how-to-use-jpackage-to-make-a-distribution-format-for-javafx-applications) distribution file;
5. Jooq [codegen](https://www.jooq.org/doc/latest/manual/code-generation/codegen-configuration/) configuration.

## Сборка дистрибутива

`$JAVA_HOME` - путь до Java 21 (Пример: /home/username/.jdks/openjdk-21.0.6).  
`$PATH_TO_FX` - путь до Java FX SDK (Пример: /home/username/.jdks/javafx-sdk-21/lib).  
`$PATH_TO_FX_MODS` - путь до Java FX jMods (Пример: /home/username/.jdks/javafx-jmods-21.0.6)

Получить зависимости проекта без учёта зависимостей не из JDK:

```bash
$JAVA_HOME/bin/jdeps --ignore-missing-deps --multi-release=21 --print-module-deps target/classes/vi/al/ro/Main.class
```

Для создания дистрибутива из чистого Java приложения нужно указать в параметре `module-path` значение
`$JAVA_HOME/jmods`, для создания дистрибутива на основе Java FX - `$PATH_TO_FX_MODS`. Create environment in dir: /target:

```bash
$JAVA_HOME/bin/jlink --output target/java-runtime --module-path $PATH_TO_FX_MODS --add-modules "java.base,jdk.localedata,javafx.controls,javafx.fxml,javafx.graphics,lombok,org.bouncycastle.pkix,org.bouncycastle.provider,org.jooq"
```

Для немодульных зависимостей:

```bash
$JAVA_HOME/bin/jlink --output target/java-runtime --module-path "$PATH_TO_FX_MODS":"/home/username/.m2/repository/org/jooq/jooq/3.19.7/jooq-3.19.7.jar":"/home/username/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar":"/home/username/.m2/repository/org/bouncycastle/bcpkix-jdk15on/1.70/bcpkix-jdk15on-1.70.jar":"/home/username/.m2/repository/org/projectlombok/lombok/1.18.32/lombok-1.18.32.jar":"/home/username/.m2/repository/org/bouncycastle/bcutil-jdk15on/1.70/bcutil-jdk15on-1.70.jar":"/home/username/.m2/repository/io/r2dbc/r2dbc-spi/1.0.0.RELEASE/r2dbc-spi-1.0.0.RELEASE.jar":"/home/username/.m2/repository/org/reactivestreams/reactive-streams/1.0.3/reactive-streams-1.0.3.jar" --add-modules "java.base,jdk.localedata,javafx.controls,javafx.fxml,javafx.graphics,lombok,org.bouncycastle.pkix,org.bouncycastle.provider,org.jooq"
```

Перед выполнением следующей команды необходимо положить собранный jar `CryptoFile.jar` в папку, указанную в параметре
`--input`:

```bash
mkdir -p target/lib && cp target/CryptoFile.jar target/lib/CryptoFile.jar
```

Команда по сборке приложения в формате `app-image`:

```bash
$JAVA_HOME/bin/jpackage --type app-image --name CryptoFile --input target/lib --main-jar CryptoFile.jar --runtime-image target/java-runtime --main-class vi.al.ro.Main --dest target/installer --java-options "-Dprism.order=sw,j2d -Dprism.verbose=true -Xmx2048m" --app-version 1.0-SNAPSHOT --vendor "RoyalVitamin" --copyright "Copyright © 2025 RAV"
```

## Ссылки

1. Build native exec [1](https://inside.java/2023/11/14/package-javafx-native-exec/);
2. jPackage [1](https://docs.oracle.com/en/java/javase/21/jpackage/packaging-overview.html).