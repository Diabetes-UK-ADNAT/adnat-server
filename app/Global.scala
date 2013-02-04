/*
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._

object GlobalScala extends GlobalSettings {

  def NoCache[A](action: Action[A]): Action[A] = Action(action.parser) { request =>
    action(request) match {
      case s: SimpleResult[_] => 
        s.withHeaders(
        "Access-Control-Allow-Origin" -> "*",
        "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
        "Access-Control-Allow-Headers" -> "Content-Type, X-Requested-With, Accept"
        )
      case result => result
    }
  }
//        response().setHeader("Access-Control-Allow-Origin", "*");
//        response().setHeader("Access-Control-Allow-Headers", "origin, X-Requested-With, x-requested-with, content-type");
//        response().setHeader("Access-Control-Allow-Methods", "PUT, GET, POST, DELETE, OPTIONS");
 
  override def onRouteRequest(request: RequestHeader): Option[Handler] = {
    //if (Play.isDev) {
      super.onRouteRequest(request).map {
        case action: Action[_] => NoCache(action)
        case other => other
      }
    //} else {
   //  super.onRouteRequest(request)
   // }
  }

}
*/