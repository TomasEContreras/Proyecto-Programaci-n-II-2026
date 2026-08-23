Proyecto Java inicial (Maven)

Instrucciones rápidas:

1) Compilar y empaquetar

```bash
mvn package
```

2) Ejecutar el jar generado

```bash
java -jar target/proyecto-prog-ii-1.0-SNAPSHOT.jar
```

O ejecutar directamente con Maven (si tiene el plugin `exec`):

```bash
mvn exec:java -Dexec.mainClass="com.example.app.Main"
```

Archivos creados:
- pom.xml
- src/main/java/com/example/app/Main.java
