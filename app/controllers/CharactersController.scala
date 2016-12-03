package controllers

import javax.inject.Inject

import scala.concurrent.Future

import play.api.Logger
import play.api.Play.current
import play.api.i18n.{ I18nSupport, MessagesApi }
import play.api.mvc.{ Action, Controller, Request }
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{ Json, JsObject, JsString }

import reactivemongo.api.gridfs.{ GridFS, ReadFile }

import play.modules.reactivemongo.{
MongoController, ReactiveMongoApi, ReactiveMongoComponents
}

import play.modules.reactivemongo.json._, ImplicitBSONHandlers._
import play.modules.reactivemongo.json.collection._

import models.Article, Article._,models.Characters, Characters._

/**
  * Created by nandpa on 11/22/16.
  */
class CharactersController  @Inject()(
                                        val messagesApi: MessagesApi,
                                        val reactiveMongoApi: ReactiveMongoApi)
  extends Controller with MongoController with ReactiveMongoComponents {

  import java.util.UUID
  import MongoController.readFileReads
  type JSONReadFile = ReadFile[JSONSerializationPack.type, JsString]

  // get the collection 'articles'
  val collection = db[JSONCollection]("characters")

  // a GridFS store named 'attachments'
  //val gridFS = GridFS(db, "attachments")
  private val gridFS = reactiveMongoApi.gridFS

  // let's build an index on our gridfs chunks collection if none
  gridFS.ensureIndex().onComplete {
    case index =>
      Logger.info(s"Checked index, result is $index")
  }

  // list all articles and sort them
  def index = Action.async { implicit request =>
    // get a sort document (see getSort method for more information)
    val sort: Option[JsObject] = getSort(request)

    println("Nothing")
    
    // build a selection document with an empty query and a sort subdocument ('$orderby')
    val query = Json.obj(
      "$orderby" -> sort.fold[Json.JsValueWrapper](Json.obj())(identity),
      "$query" -> Json.obj())

    val activeSort = request.queryString.get("sort").
      flatMap(_.headOption).getOrElse("none")

    // the cursor of documents
    val found = collection.find(query).cursor[Characters]
    // build (asynchronously) a list containing all the articles

    found.collect[List]().map { articles =>
      Ok(views.html.characters(articles, activeSort))
    }.recover {
      case e =>
        e.printStackTrace()
        BadRequest(e.getMessage())
    }
  }

//  def showCreationForm = Action { request =>
//    implicit val messages = messagesApi.preferred(request)
//
//    Ok(views.html.characters(None, Characters.form, None))
//  }

  def showEditForm(id: String) = Action.async { request =>
    // get the documents having this id (there will be 0 or 1 result)
    val futureCharacters = collection.find(Json.obj("_id" -> id)).one[Characters]
    // ... so we get optionally the matching article, if any
    // let's use for-comprehensions to compose futures (see http://doc.akka.io/docs/akka/2.0.3/scala/futures.html#For_Comprehensions for more information)
    for {
    // get a future option of article
      maybeCharacters <- futureCharacters
      // if there is some article, return a future of result with the article and its attachments
      result <- maybeCharacters.map { characters =>
        // search for the matching attachments
        // find(...).toList returns a future list of documents (here, a future list of ReadFileEntry)
        //gridFS.find[JsObject, JSONReadFile](
//          Json.obj("characters" -> characters.id.get)).collect[List]().map { files =>
//          val filesWithId = files.map { file =>
//            file.id -> file
//          }
//
//          implicit val messages = messagesApi.preferred(request)
//
//          // Ok(views.html.editCharacters(Some(id), Characters.form.fill(characters), Some(filesWithId)))
//          //Ok(views.html.editCharacters(Some(id), characters, Some(filesWithId)))
//          Ok(views.html.editCharacters(characters))




          gridFS.find[JsObject, JSONReadFile](
            Json.obj("characters" -> characters.id.get)).collect[List]().map { files =>
            val filesWithId = files.map { file =>
              file.id -> file
            }

            implicit val messages = messagesApi.preferred(request)
            Ok(views.html.editCharacters(characters))


        }
      }.getOrElse(Future(NotFound))
    } yield result
  }

  def getCharacter(id: String) = Action.async { request =>
    // get the documents having this id (there will be 0 or 1 result)
    val futureCharacters = collection.find(Json.obj("_id" -> id)).one[Characters]
    // ... so we get optionally the matching article, if any
    // let's use for-comprehensions to compose futures (see http://doc.akka.io/docs/akka/2.0.3/scala/futures.html#For_Comprehensions for more information)
    for {
    // get a future option of article
      maybeCharacters <- futureCharacters
      // if there is some article, return a future of result with the article and its attachments
      result <- maybeCharacters.map { characters =>
        // search for the matching attachments
        // find(...).toList returns a future list of documents (here, a future list of ReadFileEntry)
        gridFS.find[JsObject, JSONReadFile](
          Json.obj("characters" -> characters.id.get)).collect[List]().map { files =>
          val filesWithId = files.map { file =>
            file.id -> file
          }

          implicit val messages = messagesApi.preferred(request)

          // Ok(views.html.editCharacters(Some(id), Characters.form.fill(characters), Some(filesWithId)))
          //Ok(views.html.editCharacters(Some(id), characters, Some(filesWithId)))
          Ok(views.html.viewCharacters(characters))

        }
      }.getOrElse(Future(NotFound))
    } yield result
  }


  def getCharacterJSON(id: String) = Action.async { request =>
    // get the documents having this id (there will be 0 or 1 result)
    val futureCharacters = collection.find(Json.obj("_id" -> id)).one[Characters]
    // ... so we get optionally the matching article, if any
    // let's use for-comprehensions to compose futures (see http://doc.akka.io/docs/akka/2.0.3/scala/futures.html#For_Comprehensions for more information)
    for {
    // get a future option of article
      maybeCharacters <- futureCharacters
      // if there is some article, return a future of result with the article and its attachments
      result <- maybeCharacters.map { characters =>
        // search for the matching attachments
        // find(...).toList returns a future list of documents (here, a future list of ReadFileEntry)
        gridFS.find[JsObject, JSONReadFile](
          Json.obj("characters" -> characters.id.get)).collect[List]().map { files =>
          val filesWithId = files.map { file =>
            file.id -> file
          }

          implicit val messages = messagesApi.preferred(request)

          // Ok(views.html.editCharacters(Some(id), Characters.form.fill(characters), Some(filesWithId)))
          //Ok(views.html.editCharacters(Some(id), characters, Some(filesWithId)))
          val json = Json.toJson(characters);

          Ok(json)

        }
      }.getOrElse(Future(NotFound))
    } yield result
  }

  def create = Action.async { implicit request =>
    implicit val messages = messagesApi.preferred(request)
    println("lolllerrrrrr")
    Characters.form.bindFromRequest.fold(
      errors => Future.successful(
        Ok(views.html.error())),

      // if no error, then insert the article into the 'articles' collection
      article => collection.insert(article.copy(
        id = article.id.orElse(Some(UUID.randomUUID().toString)))
      ).map(_ => Redirect(routes.CharactersController.index))
    )
  }

  def edit(id: String) = Action.async { implicit request =>
    implicit val messages = messagesApi.preferred(request)

    Characters.form.bindFromRequest.fold(
      errors => Future.successful(
        Ok(views.html.error())),

      characters => {
        // create a modifier document, ie a document that contains the update operations to run onto the documents matching the query
        val modifier = Json.obj(
          // this modifier will set the fields
          // 'updateDate', 'title', 'content', and 'publisher'
          "$set" -> Json.obj(
            "name" -> characters.name,
            "className" -> characters.className,
            "race" -> characters.race,
            "strength_stat" -> characters.strength_stat,
            "strength_mod"-> characters.strength_mod,
            "dex_stat" -> characters.dex_stat,
            "dex_mod" -> characters.dex_mod,
            "con_stat" -> characters.con_stat,
            "con_mod" -> characters.con_mod,
            "intl_stat" -> characters.intl_stat,
            "intl_mod" -> characters.intl_mod,
            "wsdm_stat" -> characters.wsdm_stat,
            "wsdm_mod" -> characters.wsdm_mod,
            "chr_stat" -> characters.chr_stat,
            "chr_mod" -> characters.chr_mod,
            "ac" -> characters.ac
          )
        )

        // ok, let's do the update
        collection.update(Json.obj("_id" -> id), modifier).
          map { _ => Redirect(routes.CharactersController.index) }
      })
  }

  def delete(id: String) = Action.async {
    gridFS.find[JsObject, JSONReadFile](Json.obj("article" -> id)).
      collect[List]().flatMap { files =>
      // for each attachment, delete their chunks and then their file entry
      val deletions = files.map(gridFS.remove(_))

      Future.sequence(deletions)
    }.flatMap { _ =>
      // now, the last operation: remove the article
      collection.remove(Json.obj("_id" -> id))
    }.map(_ => Ok).recover { case _ => InternalServerError }
  }

  private def getSort(request: Request[_]): Option[JsObject] =
    request.queryString.get("sort").map { fields =>
      val sortBy = for {
        order <- fields.map { field =>
          if (field.startsWith("-"))
            field.drop(1) -> -1
          else field -> 1
        }
        if order._1 == "title" || order._1 == "publisher" || order._1 == "creationDate" || order._1 == "updateDate"
      } yield order._1 -> implicitly[Json.JsValueWrapper](Json.toJson(order._2))

      Json.obj(sortBy: _*)
    }

  def createForm() =  Action { request =>
        implicit val messages = messagesApi.preferred(request)
        Ok(views.html.characterscreate(None, Characters.form, None))
   }
}
