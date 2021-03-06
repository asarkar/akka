package name.abhijitsarkar.scauth.api

import name.abhijitsarkar.scauth.model.OAuthRequestConfig
import org.slf4j.LoggerFactory

import scala.collection.immutable.ListMap

trait OAuthSignature {
  def newInstance: String

  def signingKey: String

  val baseString: String
}
/**
 * @param consumerSecret The value which identifies your application.
 * @param tokenSecret The value which identifies the account your application is acting on behalf of;
 *   may not be known yet if obtaining a request token.
 */
abstract class OAuth10Signature(oAuthRequestConfig: OAuthRequestConfig,
    consumerSecret: String, tokenSecret: Option[String], oAuthEncoder: OAuthEncoder) extends OAuthSignature {
  private val log = LoggerFactory.getLogger(getClass())

  override val baseString = {
    // view allows for traversal only when necessary, doesn't create intermediate collection
    val queryUrl = ListMap(oAuthRequestConfig.queryParams.toSeq.sortBy(_._1): _*).view.zipWithIndex.foldLeft("") {
      case (acc, ((key, value), index)) =>
        s"${acc}${oAuthEncoder.encode(key)}=${oAuthEncoder.encode(value)}${ampersandOrEmpty(index)}"
    }

    val encodedBaseString = s"${oAuthRequestConfig.requestMethod.name.toUpperCase}&${oAuthEncoder.encode(oAuthRequestConfig.baseUrl)}&${oAuthEncoder.encode(queryUrl)}"
    
    log.debug("Base string: {}.", encodedBaseString)
    
    encodedBaseString
  }

  private def ampersandOrEmpty(index: Int) = {
    if (index != (oAuthRequestConfig.queryParams.size - 1)) "&" else ""
  }

  override def signingKey: String = {
    val encodedConsumerSecret = oAuthEncoder.encode(consumerSecret)
    val encodedTokenSecret = oAuthEncoder.encode(tokenSecret.getOrElse(""))

    s"${encodedConsumerSecret}&${encodedTokenSecret}"
  }
}