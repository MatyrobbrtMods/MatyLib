# MatyLib
A minecraft forge lib that is used by most of my mods.

## Installing (for modders)
In order to install the lib, you need to generate a GitHub PTA (Personal Access Token). Use this article for help on how to create it: https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token
We will start by adding our PTA and GitHub username in a `.env` file. (<b>Make sure to not share this file with anyone</b>). Create a file named `.env` in the root directory of your project. Add the following lines in it, but replace the username and the token with your username and PTA (token):
```java
GITHUB_USERNAME = yourGitHubUsername
GITHUB_TOKEN = yourPTA
```
Now go at the top of your `build.gradle`, and just under the last plugin add the following lines:
```java
def props = new Properties()
file(".env").withInputStream { props.load(it) }
```
Now, let's add the repository for the library. In the `repositories` block add:
```java
maven {
  url = uri("https://maven.pkg.github.com/Matyrobbrt/MatyLib")
  credentials {
    username = project.findProperty("gpr.user") ?: props.getProperty("GITHUB_USERNAME")
    password = project.findProperty("gpr.key") ?: props.getProperty("GITHUB_TOKEN")
  }
}
```
From here, things are pretty straightforward. We can define the dependency, in the `dependencies` block using:
```java
implementation fg.deobf("com.matyrobbrt:matylib-${mc_version}:${matylib_version}") // Make sure to define these values in your build.gradle, and make sure that the version you are targetting exists!
```
