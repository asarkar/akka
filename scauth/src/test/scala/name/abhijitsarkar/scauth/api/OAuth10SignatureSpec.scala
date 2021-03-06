package name.abhijitsarkar.scauth.api

import akka.http.scaladsl.model.HttpMethods.POST
import name.abhijitsarkar.scauth.model.OAuthRequestConfig
import name.abhijitsarkar.scauth.model.OAuthSignatureMethod.HMacSHA1
import name.abhijitsarkar.scauth.model.OAuthVersion.OneOh
import name.abhijitsarkar.scauth.util.SimpleUrlEncoder
import org.scalatest.{FlatSpec, Matchers}

class OAuth10SignatureSpec extends FlatSpec with Matchers {
  val requestMethod = POST
  val baseUrl = "https://api.twitter.com/1/statuses/update.json"
  val queryParams: Map[String, String] = Map("status" -> "Hello Ladies + Gentlemen, a signed OAuth request!",
    "include_entities" -> "true",
    "oauth_consumer_key" -> "xvz1evFS4wEEPTGEFPHBog",
    "oauth_nonce" -> "kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg",
    "oauth_signature_method" -> HMacSHA1,
    "oauth_timestamp" -> "1318622958",
    "oauth_token" -> "370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
    "oauth_version" -> OneOh)

  private val anonSign = new {
    val oAuthRequestConfig = OAuthRequestConfig(requestMethod, baseUrl, queryParams)
    val consumerSecret = "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw"
    val tokenSecret = Some("LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")
    val oAuthEncoder = SimpleUrlEncoder
  } with OAuth10Signature(oAuthRequestConfig, consumerSecret, tokenSecret, oAuthEncoder) {
    override def newInstance = ""
  }

  it should "generate the base string as defined by the OAuth 1.0 spec" in {
    val expected = """POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json&
      include_entities%3Dtrue%26oauth_consumer_key%3Dxvz1evFS4wEEPTGEFPHBog%26
      oauth_nonce%3DkYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg%26
      oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1318622958%26
      oauth_token%3D370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb%26
      oauth_version%3D1.0%26
      status%3DHello%2520Ladies%2520%252B%2520Gentlemen%252C%2520a%2520signed%2520OAuth%2520request%2521"""
    anonSign.baseString shouldBe (expected.filterNot { c => c.isWhitespace || c.isControl })
  }

  it should "generate the signing key as defined by the OAuth 1.0 spec" in {
    anonSign.signingKey shouldBe ("kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw&LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE")
  }
}
