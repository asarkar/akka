package org.abhijitsarkar.moviedb

import java.text.NumberFormat.getNumberInstance
import java.util.Locale.US

import spray.json.{DefaultJsonProtocol, DeserializationException, JsArray, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

import scala.util.Try

/**
  * @author Abhijit Sarkar
  */
case class Movie(
                  title: String,
                  year: Int,
                  genres: Seq[String],
                  mpaaRating: String,
                  runtime: String,
                  directors: Seq[String],
                  actors: Seq[String],
                  languages: Seq[String],
                  countries: Seq[String],
                  `type`: String,
                  plot: String,
                  imdbVotes: Long,
                  imdbRating: Double,
                  imdbId: String
                )

object MovieProtocol extends DefaultJsonProtocol {

  implicit object MovieJsonFormat extends RootJsonFormat[Movie] {
    def write(m: Movie) = JsObject(
      "title" -> JsString(m.title),
      "year" -> JsNumber(m.year),
      "genres" -> JsArray(m.genres.map(JsString(_)).toVector),
      "mpaaRating" -> JsString(m.mpaaRating),
      "runtime" -> JsString(m.runtime),
      "directors" -> JsArray(m.directors.map(JsString(_)).toVector),
      "actors" -> JsArray(m.actors.map(JsString(_)).toVector),
      "languages" -> JsArray(m.languages.map(JsString(_)).toVector),
      "countries" -> JsArray(m.countries.map(JsString(_)).toVector),
      "type" -> JsString(m.`type`),
      "plot" -> JsString(m.plot),
      "imdbVotes" -> JsNumber(m.imdbVotes),
      "imdbRating" -> JsNumber(m.imdbRating),
      "imdbId" -> JsString(m.imdbId)
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields("Title", "Year", "Genre", "Rated", "Runtime", "Director", "Actors", "Language",
        "Country", "Type", "Plot", "imdbVotes", "imdbRating", "imdbID") match {
        case Seq(
        JsString(title),
        JsString(year),
        JsString(genres),
        JsString(mpaaRating),
        JsString(runtime),
        JsString(directors),
        JsString(actors),
        JsString(languages),
        JsString(countries),
        JsString(typ),
        JsString(plot),
        JsString(imdbVotes),
        JsString(imdbRating),
        JsString(imdbId)
        ) => new Movie(
          title,
          Try(year.toInt).getOrElse(-1),
          genres.split(","),
          mpaaRating,
          runtime,
          directors.split(","),
          actors.split(","),
          languages.split(","),
          countries.split(","),
          typ,
          plot,
          Try(getNumberInstance(US).parse(imdbVotes).longValue).getOrElse(-1),
          Try(imdbRating.toDouble).getOrElse(-1.0),
          imdbId
        )
        case _ => throw new DeserializationException("Failed to unmarshal movie")
      }
    }
  }

}
