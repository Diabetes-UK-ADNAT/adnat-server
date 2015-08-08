# ADNAT REST API and Auth Endpoints

The ADNAT REST API is built with Java on the Play Framework

## Java Source

### Controllers
	./app/controllers/Account.java
	./app/controllers/Application.java
	./app/controllers/AssessmentController.java
	./app/controllers/BaseController.java
	./app/controllers/ContactRequestController.java
	./app/controllers/ContentController.java
	./app/controllers/FaqController.java
	./app/controllers/GroupController.java
	./app/controllers/PersonController.java
	./app/controllers/Signup.java

### Configuration
	./app/Global.java
	./app/Global.scala

### Models
	./app/models
	./app/models/Assessment.java
	./app/models/auth
	./app/models/auth/LinkedAccount.java
	./app/models/auth/SecurityRole.java
	./app/models/auth/TokenAction.java
	./app/models/auth/User.java
	./app/models/auth/UserPermission.java
	./app/models/BaseModel.java
	./app/models/ContactRequest.java
	./app/models/Content.java
	./app/models/Faq.java
	./app/models/Group.java
	./app/models/MorphiaObject.java
	./app/models/PassTest.java
	./app/models/Person.java
	./app/models/Ping.java
	./app/models/Response.java
	./app/models/Score.java


### Authentication Providers
	./app/providers/MyLoginUsernamePasswordAuthUser.java
	./app/providers/MyUsernamePasswordAuthProvider.java
	./app/providers/MyUsernamePasswordAuthUser.java

### Security
	./app/security/MyDeadboltHandler.java

### User Service
	./app/service/MyUserServicePlugin.java

### Views
	./app/views/_emailPartial.scala.html
	./app/views/_passwordPartial.scala.html
	./app/views/_providerIcon.scala.html
	./app/views/_providerPartial.scala.html
	./app/views/account
	./app/views/account/ask_link.scala.html
	./app/views/account/ask_merge.scala.html
	./app/views/account/email
	./app/views/account/email/password_reset_de.scala.html
	./app/views/account/email/password_reset_en.scala.html
	./app/views/account/email/password_reset_pl.scala.html
	./app/views/account/email/verify_email_de.scala.html
	./app/views/account/email/verify_email_en.scala.html
	./app/views/account/email/verify_email_pl.scala.html
	./app/views/account/link.scala.html
	./app/views/account/password_change.scala.html
	./app/views/account/signup
	./app/views/account/signup/email
	./app/views/account/signup/email/verify_email_de.scala.html
	./app/views/account/signup/email/verify_email_en.scala.html
	./app/views/account/signup/email/verify_email_pl.scala.html
	./app/views/account/signup/exists.scala.html
	./app/views/account/signup/no_token_or_invalid.scala.html
	./app/views/account/signup/oAuthDenied.scala.html
	./app/views/account/signup/password_forgot.scala.html
	./app/views/account/signup/password_reset.scala.html
	./app/views/account/signup/unverified.scala.html
	./app/views/account/unverified.scala.html
	./app/views/email
	./app/views/email/email_assessment_notification.scala.html
	./app/views/email/email_contact.scala.html
	./app/views/index.scala.html
	./app/views/login.scala.html
	./app/views/main.scala.html
	./app/views/ping.scala.html
	./app/views/profile.scala.html
	./app/views/restricted.scala.html
	./app/views/signup.scala.html

## Configuration
	./conf/application.conf
	./conf/logger.xml
	./conf/messages
	./conf/messages.en
	./conf/nginx.conf.prod
	./conf/nginx.conf.sample
	./conf/play-authenticate
	./conf/play-authenticate/deadbolt.conf
	./conf/play-authenticate/mine.conf
	./conf/play.plugins
	./conf/prod.conf
	./conf/routes

## SQL
	./conf/evolutions/default/1.sql

## Distribution
	./dist/adnat-api.jar

## NetBeans Project
	./nbproject/build-impl.xml
	./nbproject/genfiles.properties
	./nbproject/private
	./nbproject/private/private.properties
	./nbproject/private/private.xml
	./nbproject/project.properties
	./nbproject/project.xml
## Unit Tests
	./test/ApplicationTest.java
	./test/FaqControllerTest.java
	./test/IntegrationTest.java
	./test/PersonControllerTest.java
