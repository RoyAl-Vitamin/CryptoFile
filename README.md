# CryptoFile

## Разработка

Скачать JavaFX с [Gluon](https://gluonhq.com/products/javafx/) 21 версию и распаковать. В Idea IDE установить "VM option":

```
--module-path "/home/$USER/.jdks/javafx-sdk-21/lib" --add-modules javafx.controls,javafx.fxml
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

Установить переменные окружения:

- `$USER` - имя текущего пользователя, в Linux уже установлен;
- `$JAVA_HOME` - путь до Java 21 (Пример: /home/$USER/.jdks/openjdk-21.0.7);
- `$PATH_TO_FX` - путь до Java FX SDK (Пример: /home/$USER/.jdks/javafx-sdk-21.0.7/lib);
- `$PATH_TO_FX_MODS` - путь до Java FX jmods (Пример: /home/$USER/.jdks/javafx-jmods-21.0.7).

Получить зависимости проекта без учёта зависимостей не из JDK:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX --ignore-missing-deps --multi-release=21 --print-module-deps target/classes/vi/al/ro/Main.class
```

Для модульного проекта:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX --multi-release=21 --print-module-deps target/CryptoFile.jar
```

* Собрать jar-файл:

```bash
mvn clean package -DskipTests
```

* Скопировать немодульные либы из репозитория Мавен для превращения их копии в модульные можно, выполнив файл `./libForModularize/copyFromM2.sh`;

* Генерация module-info.java для reactive-streams-1.0.3.jar:

```bash
$JAVA_HOME/bin/jdeps --ignore-missing-deps --multi-release=21 --generate-module-info libForModularize libForModularize/reactive-streams-1.0.3.jar
```

* Компиляция module-info.java в module-info.class для org.reactivestreams:

```bash
$JAVA_HOME/bin/javac --patch-module org.reactivestreams=libForModularize/reactive-streams-1.0.3.jar libForModularize/org.reactivestreams/versions/21/module-info.java
```

* Вытащить файл `./libForModularize/org.reactivestreams/versions/21/module-info.class` в папку `./libForModularize`:

```bash
mv ./libForModularize/org.reactivestreams/versions/21/module-info.class ./libForModularize
```

* Обновить reactive-streams-1.0.3.jar, внедрив в него module-info.class:

```bash
$JAVA_HOME/bin/jar uf libForModularize/reactive-streams-1.0.3.jar -C libForModularize module-info.class
```

* Удалить лишнюю папку и module-info.class:

```bash
rm -rf ./libForModularize/org.reactivestreams && \
rm -f ./libForModularize/module-info.class
```

* Генерация module-info.java для r2dbc-spi-1.0.0.RELEASE.jar:

```bash
$JAVA_HOME/bin/jdeps --ignore-missing-deps --multi-release=21 --generate-module-info libForModularize libForModularize/r2dbc-spi-1.0.0.RELEASE.jar
```

* Компиляция module-info.java в module-info.class для r2dbc.spi:

```bash
$JAVA_HOME/bin/javac --patch-module r2dbc.spi=libForModularize/r2dbc-spi-1.0.0.RELEASE.jar libForModularize/r2dbc.spi/versions/21/module-info.java
```

* Вытащить файл `./libForModularize/r2dbc.spi/versions/21/module-info.class` в папку `./libForModularize`:

```bash
mv ./libForModularize/r2dbc.spi/versions/21/module-info.class ./libForModularize
```

* Обновить r2dbc-spi-1.0.0.RELEASE.jar, внедрив в него module-info.class:

```bash
$JAVA_HOME/bin/jar uf libForModularize/r2dbc-spi-1.0.0.RELEASE.jar -C libForModularize module-info.class
```

* Удалить лишнюю папку и module-info.class:

```bash
rm -rf ./libForModularize/r2dbc.spi && \
rm -f ./libForModularize/module-info.class
```

* Генерация module-info.java для h2-2.2.224.jar:

```bash
$JAVA_HOME/bin/jdeps --ignore-missing-deps --multi-release=21 --generate-module-info libForModularize libForModularize/h2-2.2.224.jar
```

* Компиляция module-info.java в module-info.class для com.h2database:

```bash
$JAVA_HOME/bin/javac --patch-module com.h2database=libForModularize/h2-2.2.224.jar libForModularize/com.h2database/versions/21/module-info.java
```

* Вытащить файл `./libForModularize/com.h2database/versions/21/module-info.class` в папку `./libForModularize`:

```bash
mv ./libForModularize/com.h2database/versions/21/module-info.class ./libForModularize
```

* Обновить h2-2.2.224.jar, внедрив в него module-info.class:

```bash
$JAVA_HOME/bin/jar uf libForModularize/h2-2.2.224.jar -C libForModularize module-info.class
```

* Удалить лишнюю папку и module-info.class:

```bash
rm -rf ./libForModularize/com.h2database && \
rm -f ./libForModularize/module-info.class
```

Все необходимые немодульные зависимости стали модульными в папке ./libForModularize, можно собирать окружение JRE для приложения.

Получить зависимости проекта без учёта зависимостей не из JDK:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX:"/home/alex/.m2/repository/org/jooq/jooq/3.19.7/jooq-3.19.7.jar":"./libForModularize/reactive-streams-1.0.3.jar":"./libForModularize/r2dbc-spi-1.0.0.RELEASE.jar":"/home/alex/.m2/repository/org/bouncycastle/bcpkix-jdk15on/1.70/bcpkix-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcutil-jdk15on/1.70/bcutil-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar" --add-modules org.reactivestreams,r2dbc.spi,org.bouncycastle.pkix,org.bouncycastle.util,org.bouncycastle.provider,org.apache.logging.log4j --multi-release=21 --print-module-deps target/CryptoFile.jar
```

Для создания дистрибутива из чистого Java приложения нужно указать в параметре `module-path` значение
`$JAVA_HOME/jmods`, для создания дистрибутива на основе Java FX - `$PATH_TO_FX_MODS`. Создать окружение JRE в папке `./target/java-runtime`:

```bash
$JAVA_HOME/bin/jlink --output target/java-runtime --module-path $PATH_TO_FX_MODS:"/home/alex/.m2/repository/org/jooq/jooq/3.19.7/jooq-3.19.7.jar":"./libForModularize/reactive-streams-1.0.3.jar":"./libForModularize/r2dbc-spi-1.0.0.RELEASE.jar":"./libForModularize/h2-2.2.224.jar":"/home/alex/.m2/repository/org/bouncycastle/bcpkix-jdk15on/1.70/bcpkix-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcutil-jdk15on/1.70/bcutil-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar":"/home/alex/.m2/repository/org/projectlombok/lombok/1.18.32/lombok-1.18.32.jar" --add-modules "java.base,jdk.localedata,javafx.controls,javafx.fxml,javafx.graphics,lombok,org.bouncycastle.pkix,org.bouncycastle.provider,com.h2database,org.jooq,org.reactivestreams,r2dbc.spi,org.bouncycastle.pkix,org.bouncycastle.util,org.bouncycastle.provider,org.apache.logging.log4j" --ignore-signing-information
```

Копирование собранного CryptoFile.jar в папку /lib для выполнения последующей генерации:

```bash
mkdir -p target/lib && cp target/CryptoFile.jar target/lib/CryptoFile.jar
```

Сборка исполняемого файла:

```bash
$JAVA_HOME/bin/jpackage --type app-image --name CryptoFile --input target/lib --main-jar CryptoFile.jar --runtime-image target/java-runtime --main-class vi.al.ro.Main --dest target/installer --java-options "-Dprism.order=sw,j2d -Dprism.verbose=true -Xmx2048m" --app-version 1.0-SNAPSHOT --vendor "RoyalVitamin" --copyright "Copyright © 2025 RAV"
```

Теперь приложение (`./target/installer/CryptoFile/bin/CryptoFile`) можно запустить!

## Ссылки

1. Build native exec [1](https://inside.java/2023/11/14/package-javafx-native-exec/);
2. jPackage [1](https://docs.oracle.com/en/java/javase/21/jpackage/packaging-overview.html);
3. Automatic modules [1](https://stackoverflow.com/questions/46741907/what-is-an-automatic-module);
4. Что делать с немодульными зависимостями [1](https://stackoverflow.com/a/77656893/9401964), [2](https://medium.com/azulsystems/using-jlink-to-build-java-runtimes-for-non-modular-applications-9568c5e70ef4), [3](https://youtu.be/bO6f3U4i0A0?si=kyoaCPCzwUPzCLVW).