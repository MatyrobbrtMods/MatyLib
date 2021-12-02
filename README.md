# MatyLib
A minecraft forge lib that is used by most of my mods.

## Installing (for modders)
MatyLib is hosted through https://cloudsmith.io/ <br>
We will add the repository for the library. In the `repositories` block add:
```java
maven {
  url "https://dl.cloudsmith.io/public/matyrobbrt/matylib/maven/"
}
```
From here, things are pretty straightforward. We can define the dependency, in the `dependencies` block using:
```java
implementation fg.deobf("com.matyrobbrt:matylib-${mc_version}:${matylib_version}") // Make sure to define these values in your build.gradle, and make sure that the version you are targetting exists!
```
