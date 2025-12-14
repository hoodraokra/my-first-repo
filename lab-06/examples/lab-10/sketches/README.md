# Using JavaFX

## Upgrade Java
**MacOS and Unix/Linux**
```
# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Upgrade Java SDK
sdk install java 22-tem
sdk use java 22-tem
```

## Download JavaFX Library and Add it to your Project
1. Download JavaFX [JavaFX Download](https://gluonhq.com/products/javafx/)
1. Extract the file to your project directory
1. Reference the library in your code and compile and run commands
1. Alternatively, you can install it in a central place somewhere rather than placing it directly in the project


## Compiling and Running JavaFX programs

1. **Compile**
`javac --module-path javafx-sdk-24/lib --add-modules javafx.controls HelloJavaFX.java`

1. **Run**
`java --module-path javafx-sdk-24/lib --add-modules javafx.controls HelloJavaFX`


## Compiling and Running Other JavaFX programs

1. **VideoPlayer.java**
```
javac --module-path javafx-sdk-24/lib --add-modules javafx.controls,javafx.media VideoPlayer.java
java --module-path javafx-sdk-24/lib --add-modules javafx.controls,javafx.media VideoPlayer
```

1.**Scatter3DPlotWithTimeExport.java**
```
javac --module-path javafx-sdk-24/lib --add-modules javafx.controls,javafx.media,javafx.swing -cp .:jcodec-0.2.5.jar:jcodec-javase-0.2.5.jar Scatter3DPlotWithTimeExport.java
java --module-path javafx-sdk-24/lib --add-modules javafx.controls,javafx.media,javafx.swing -cp .:jcodec-0.2.5.jar:jcodec-javase-0.2.5.jar Scatter3DPlotWithTimeExport
```

1. **PostgresConnectionExample.java**
`java -cp .:postgresql-42.7.5.jar PostgresConnectionExample`

1. **BombermanGame.java**
```
javac BombermanGame.java
java OptionsScreen
```

*If you are having trouble figuring out what modules to include you can always import everything just to get your programming running, but remember this will make the program needlessly larger*
`--add-modules ALL-MODULE-PATH`
