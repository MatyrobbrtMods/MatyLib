# MatyLib
A minecraft forge lib that is used by most of my mods.

## Installing (for modders)
We will add the repository for the library. In the `repositories` block add:
```java
maven {
  url "https://dl.cloudsmith.io/public/matyrobbrt/matylib/maven/"
}
```
From here, things are pretty straightforward. We can define the dependency, in the `dependencies` block using:
```java
implementation fg.deobf("com.matyrobbrt:matylib-${mc_version}:${matylib_version}") // Make sure to define these values in your gradle.properties, and make sure that the version you are targetting exists!
```


[![Hosted By: Cloudsmith](https://img.shields.io/badge/OSS%20hosting%20by-cloudsmith-blue?logo=cloudsmith&style=for-the-badge)](https://cloudsmith.com)

Package repository hosting is graciously provided by  [Cloudsmith](https://cloudsmith.com).
Cloudsmith is the only fully hosted, cloud-native, universal package management solution, that
enables your organization to create, store and share packages in any format, to any place, with total
confidence.
