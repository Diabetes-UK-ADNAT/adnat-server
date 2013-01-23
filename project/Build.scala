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
	"com.google.code.morphia" % "morphia-logging-slf4j" % "0.99"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
	//mongo
    resolvers += "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
	resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/"
  )




}
