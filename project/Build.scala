import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "adnat"
  val appVersion      = "0.10"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    //mongo
    "com.google.code.morphia" % "morphia" % "0.99",
    "org.mongodb" % "mongo-java-driver" % "2.7.3",
    "com.google.code.morphia" % "morphia-logging-slf4j" % "0.99",
	//mysql
    "mysql" % "mysql-connector-java" % "5.1.18",
	//mail
	"com.typesafe" %% "play-plugins-mailer" % "2.1.0" , 
    // play authenticate
    "be.objectify"  %%  "deadbolt-java"     % "2.1-SNAPSHOT",
    "com.feth" %% "play-authenticate" % "0.2.5-SNAPSHOT"
  )

 val main = play.Project(appName, appVersion, appDependencies).settings(
    // https://groups.google.com/forum/#!msg/play-framework/Lfa6VzNqOVE/9XvxLyfTILUJ
    ebeanEnabled := true, 

    //mongo
    resolvers += "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
    resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/",
    // play authenticate
    resolvers += Resolver.url("play-easymail (release)", url("http://joscha.github.com/play-easymail/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-easymail (snapshot)", url("http://joscha.github.com/play-easymail/repo/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (release)", url("http://joscha.github.com/play-authenticate/repo/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("play-authenticate (snapshot)", url("http://joscha.github.com/play-authenticate/repo/snapshots/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Repository", url("http://schaloner.github.com/releases/"))(Resolver.ivyStylePatterns),
    resolvers += Resolver.url("Objectify Play Repository - snapshots", url("http://schaloner.github.com/snapshots/"))(Resolver.ivyStylePatterns)
  )
}
