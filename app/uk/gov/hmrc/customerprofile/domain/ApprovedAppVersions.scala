/*
 * Copyright 2016 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.customerprofile.domain

import net.ceedubs.ficus.readers.ValueReader
import play.api.libs.json.{JsError, Writes, _}

import scala.concurrent.Future


case class NativeVersion(ios: VersionRange, android: VersionRange)

trait LoadConfig {

  import com.typesafe.config.Config

  def config: Config
}

trait ApprovedAppVersions extends LoadConfig {
  import net.ceedubs.ficus.Ficus._

  private implicit val nativeVersionReader: ValueReader[NativeVersion] = ValueReader.relative { nativeVersion =>
    NativeVersion(
      VersionRange(config.as[String]("approvedAppVersions.ios")),
      VersionRange(config.as[String]("approvedAppVersions.android"))
    )
  }

  val appVersion: NativeVersion = config.as[NativeVersion]("approvedAppVersions")
}

object ValidateAppVersion extends ApprovedAppVersions {

  import com.typesafe.config.{Config, ConfigFactory}

  lazy val config: Config = ConfigFactory.load()

  def apply(deviceVersion: DeviceVersion) : Future[Boolean] = {
    //TODO add comparison base it off https://github.com/hmrc/sbt-bobby/blob/master/src/main/scala/uk/gov/hmrc/bobby/domain/DependencyChecker.scala#L34
//    !appVersion.ios.includes(Version(deviceVersion.version))
//    !appVersion.android.includes(Version(deviceVersion.version))

    Future.successful(false)
  }
}


trait NativeOS

object NativeOS {
  case object iOS extends NativeOS {
    override def toString: String = "ios"
  }
  case object Android extends NativeOS {
    override def toString: String = "android"
  }

  val reads: Reads[NativeOS] = new Reads[NativeOS] {
    override def reads(json: JsValue): JsResult[NativeOS] = json match {
      case JsString("ios") => JsSuccess(iOS)
      case JsString("android") => JsSuccess(Android)
      case _ => JsError()
    }
  }

  val writes: Writes[NativeOS] = new Writes[NativeOS] {
    override def writes(os: NativeOS) = os match {
      case `iOS` => JsString("ios")
      case Android => JsString("android")
    }
  }

  implicit val formats = Format(reads, writes)
}

case class DeviceVersion(os : NativeOS, version : String)

object DeviceVersion {
  import NativeOS.formats

  implicit val formats = Json.format[DeviceVersion]
}