# CryptoFile

## Разработка

Скачать JavaFX с [Gluon](https://gluonhq.com/products/javafx/) 25 версию и распаковать. В Idea IDE установить "VM option":

```
--module-path "/home/$USER/.jdks/javafx-sdk-25.0.2/lib" --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics
```

## Планы

1. ~~Поднять версию Java, JavaFx~~;
2. Сделать чтение публичного и приватного ключа для шифрования/дешифрования из pfx-хранилища;
3. Сделать шифрование/дешифрования и имени файла;
4. Реализовать отображение процесса шифрования/дешифрования.

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
4. [Create](https://stackoverflow.com/questions/68871952/how-to-use-jpackage-to-make-a-distribution-format-for-javafx-applications) distribution file.

## Сборка дистрибутива

Установить переменные окружения:

- `$USER` - имя текущего пользователя, в Linux уже установлен;
- `$JAVA_HOME` - путь до Java 25 (Пример: /home/$USER/.jdks/openjdk-25.0.2);
- `$PATH_TO_FX` - путь до Java FX SDK (Пример: /home/$USER/.jdks/javafx-sdk-25.0.2/lib);
- `$PATH_TO_FX_MODS` - путь до Java FX jmods (Пример: /home/$USER/.jdks/javafx-jmods-25.0.2).

Получить зависимости проекта без учёта зависимостей не из JDK:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX --ignore-missing-deps --multi-release=25 --print-module-deps target/classes/vi/al/ro/Main.class
```

Для модульного проекта:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX --multi-release=25 --print-module-deps target/CryptoFile.jar
```

* Собрать jar-файл:

```bash
mvn clean package
```

Получить зависимости проекта без учёта зависимостей не из JDK:

```bash
$JAVA_HOME/bin/jdeps --module-path $PATH_TO_FX:"/home/alex/.m2/repository/org/bouncycastle/bcpkix-jdk15on/1.70/bcpkix-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcutil-jdk15on/1.70/bcutil-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar" --add-modules org.bouncycastle.pkix,org.bouncycastle.util,org.bouncycastle.provider,org.apache.logging.log4j --multi-release=25 --print-module-deps target/CryptoFile.jar
```

Для создания дистрибутива из чистого Java приложения нужно указать в параметре `module-path` значение
`$JAVA_HOME/jmods`, для создания дистрибутива на основе Java FX - `$PATH_TO_FX_MODS`. Создать окружение JRE в папке `./target/java-runtime`:

```bash
$JAVA_HOME/bin/jlink --output target/java-runtime --module-path $PATH_TO_FX_MODS:"/home/alex/.m2/repository/org/bouncycastle/bcpkix-jdk15on/1.70/bcpkix-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcutil-jdk15on/1.70/bcutil-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.70/bcprov-jdk15on-1.70.jar":"/home/alex/.m2/repository/org/apache/logging/log4j/log4j-api/2.23.1/log4j-api-2.23.1.jar":"/home/alex/.m2/repository/org/projectlombok/lombok/1.18.32/lombok-1.18.32.jar" --add-modules "java.base,jdk.localedata,javafx.controls,javafx.fxml,javafx.graphics,lombok,org.bouncycastle.pkix,org.bouncycastle.provider,org.bouncycastle.pkix,org.bouncycastle.util,org.bouncycastle.provider,org.apache.logging.log4j" --ignore-signing-information
```

Копирование собранного CryptoFile.jar в папку /lib для выполнения последующей генерации:

```bash
mkdir -p target/lib && cp target/CryptoFile.jar target/lib/CryptoFile.jar
```

Сборка исполняемого файла:

```bash
$JAVA_HOME/bin/jpackage --type app-image --name CryptoFile --input target/lib --main-jar CryptoFile.jar --runtime-image target/java-runtime --main-class vi.al.ro.Main --dest target/installer --java-options "-Dprism.order=sw,j2d -Dprism.verbose=true -Xmx2048m" --app-version 1.0-SNAPSHOT --vendor "RoyalVitamin" --copyright "Copyright © 2026 RAV"
```

Теперь приложение (`./target/installer/CryptoFile/bin/CryptoFile`) можно запустить!

## Ссылки

1. Build native exec [1](https://inside.java/2023/11/14/package-javafx-native-exec/);
2. jPackage [1](https://docs.oracle.com/en/java/javase/25/jpackage/packaging-overview.html);
3. Automatic modules [1](https://stackoverflow.com/questions/46741907/what-is-an-automatic-module);
4. Что делать с немодульными зависимостями [1](https://stackoverflow.com/a/77656893/9401964), [2](https://medium.com/azulsystems/using-jlink-to-build-java-runtimes-for-non-modular-applications-9568c5e70ef4), [3](https://youtu.be/bO6f3U4i0A0?si=kyoaCPCzwUPzCLVW).