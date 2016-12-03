package models

import play.api.data.Forms.{bigDecimal, mapping, nonEmptyText, number, optional, text}
import play.api.data._
import play.api.data.validation.Constraints.pattern

case class Characters(
        id: Option[String],
        name : String,
        className : String,
        race : String,
        strength_stat : Int,
        strength_mod : BigDecimal,
        dex_stat : Int,
        dex_mod : BigDecimal,
        con_stat : Int,
        con_mod : BigDecimal,
        intl_stat : Int,
        intl_mod : BigDecimal,
        wsdm_stat : Int,
        wsdm_mod : BigDecimal,
        chr_stat : Int,
        chr_mod : BigDecimal,
        ac : Int
    )

// Turn off your mind, relax, and float downstream
// It is not dying...
object Characters {
  import play.api.libs.json._

  implicit object CharactersWrites extends OWrites[Characters] {
    def writes(characters: Characters): JsObject = Json.obj(
      "_id" -> characters.id,
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
      "ac" -> characters.ac)
  }

  implicit object CharactersReads extends Reads[Characters] {
    def reads(json: JsValue): JsResult[Characters] = json match {
      case obj: JsObject => try {
        val id = (obj \ "_id").asOpt[String]
        val name = (obj \ "name").as[String]
        val className = (obj \ "className").as[String]
        val race = (obj \ "race").as[String]
        val strength_stat = (obj \ "strength_stat").as[Int]
        val strength_mod = (obj \ "strength_mod").as[BigDecimal]
        val dex_stat = (obj \ "dex_stat").as[Int]
        val dex_mod = (obj \ "dex_mod").as[BigDecimal]
        val con_stat = (obj \ "con_stat").as[Int]
        val con_mod = (obj \ "con_mod").as[BigDecimal]
        val intl_stat = (obj \ "intl_stat").as[Int]
        val intl_mod = (obj \ "intl_mod").as[BigDecimal]
        val wsdm_stat = (obj \ "wsdm_stat").as[Int]
        val wsdm_mod = (obj \ "wsdm_mod").as[BigDecimal]
        val chr_stat = (obj \ "chr_stat").as[Int]
        val chr_mod = (obj \ "chr_mod").as[BigDecimal]
        val ac = (obj \ "ac").as[Int]

        JsSuccess(Characters(id, name, className, race, strength_stat, strength_mod, dex_stat, dex_mod, con_stat, con_mod, intl_stat, intl_mod, wsdm_stat, wsdm_mod, chr_stat, chr_mod, ac))

      } catch {
        case cause: Throwable => JsError(cause.getMessage)
      }

      case _ => JsError("expected.jsobject")
    }
  }

  val form = Form(
    mapping(
      "_id" -> optional(text verifying pattern("""[a-fA-F0-9]{24}""".r, error = "error.objectId")),
      "name" -> nonEmptyText,
      "className" -> nonEmptyText,
      "race" -> nonEmptyText,
      "strength_stat" ->  number,
      "strength_mod" ->  bigDecimal,
      "dex_stat" -> number,
      "dex_mod" ->   bigDecimal,
      "con_stat" ->  number,
      "con_mod" -> bigDecimal,
      "intl_stat" -> number,
      "intl_mod" -> bigDecimal,
      "wsdm_stat" ->  number,
      "wsdm_mod" -> bigDecimal,
      "chr_stat" ->  number,
      "chr_mod" -> bigDecimal,
      "ac" -> number)
      {
      (id, name, className, race, strength_stat, strength_mod, dex_stat, dex_mod, con_stat, con_mod, intl_stat, intl_mod, wsdm_stat, wsdm_mod, chr_stat, chr_mod, ac) =>
        Characters(
          id,
          name,
          className,
          race,
          strength_stat,
          strength_mod,
          dex_stat,
          dex_mod,
          con_stat,
          con_mod,
          intl_stat,
          intl_mod,
          wsdm_stat,
          wsdm_mod,
          chr_stat,
          chr_mod,
          ac
        )
    } { characters =>
      Some(
        (characters.id,
          characters.name,
          characters.className,
          characters.race,
          characters.strength_stat,
          characters.strength_mod,
          characters.dex_stat,
          characters.dex_mod,
          characters.con_stat,
          characters.con_mod,
          characters.intl_stat,
          characters.intl_mod,
          characters.wsdm_stat,
          characters.wsdm_mod,
          characters.chr_stat,
          characters.chr_mod,
          characters.ac)
    )

  })
}
