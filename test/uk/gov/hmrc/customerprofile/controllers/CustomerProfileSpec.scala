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

package uk.gov.hmrc.customerprofile.controllers

import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeApplication}
import uk.gov.hmrc.customerprofile.domain._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}


class TestCustomerProfileGetProfileSpec extends UnitSpec with WithFakeApplication with ScalaFutures with StubApplicationConfiguration {
  override lazy val fakeApplication = FakeApplication(additionalConfiguration = config)

  "getProfile sandbox controller " should {

    "return the profile response from a resource" in new SandboxSuccess {
      val result = await(controller.getProfile()(emptyRequestWithHeader))

      status(result) shouldBe 200

      contentAsJson(result) shouldBe Json.toJson(customerProfile)
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getProfile()(emptyRequest))

      status(result) shouldBe 406
    }
  }

  "getProfile live controller " should {

    "return the profile successfully" in new Success {

      val result = await(controller.getProfile()(emptyRequestWithHeader))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson(customerProfile)
    }

    "Return unauthorized when authority record does not contain a NINO" in new AuthWithoutNino {
      val result = await(controller.getProfile()(emptyRequestWithHeader))

      status(result) shouldBe 401
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getProfile()(emptyRequest))

      status(result) shouldBe 406
    }
  }
}

class TestCustomerProfileGetAccountSpec extends UnitSpec with WithFakeApplication with ScalaFutures with StubApplicationConfiguration {
  override lazy val fakeApplication = FakeApplication(additionalConfiguration = config)

  "getAccount sandbox controller " should {

    "return the accounts response from a resource" in new SandboxSuccess {
      val result = await(controller.getAccounts()(emptyRequestWithHeader))

      status(result) shouldBe 200

      contentAsJson(result) shouldBe Json.toJson(testAccount)
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getAccounts()(emptyRequest))

      status(result) shouldBe 406
    }
  }

  "getAccount live controller " should {

    "return the accounts successfully" in new Success {

      val result = await(controller.getAccounts()(emptyRequestWithHeader))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson(Accounts(Some(nino), None))
    }

    "Return unauthorized when authority record does not contain a NINO" in new AuthWithoutNino {
      val result = await(controller.getAccounts()(emptyRequestWithHeader))

      status(result) shouldBe 401
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getAccounts()(emptyRequest))

      status(result) shouldBe 406
    }
  }
}

class TestCustomerProfileGetPersonalDetailsSpec extends UnitSpec with WithFakeApplication with ScalaFutures with StubApplicationConfiguration {
  override lazy val fakeApplication = FakeApplication(additionalConfiguration = config)


  "getPersonalDetails sandbox controller " should {

    "return the PersonalDetails response from a resource" in new SandboxSuccess {
      val result = await(controller.getPersonalDetails(nino)(emptyRequestWithHeader))

      status(result) shouldBe 200

      contentAsJson(result) shouldBe Json.toJson(person)
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getPersonalDetails(nino)(emptyRequest))

      status(result) shouldBe 406
    }
  }

  "live controller " should {

    "return the PersonalDetails successfully" in new Success {
      val result = await(controller.getPersonalDetails(nino)(emptyRequestWithHeader))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson(person)
    }

    "Return unauthorized when authority record does not contain a NINO" in new AuthWithoutNino {
      val result = await(controller.getPersonalDetails(nino)(emptyRequestWithHeader))

      status(result) shouldBe 401
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.getPersonalDetails(nino)(emptyRequest))

      status(result) shouldBe 406
    }
  }
}

class TestCustomerProfilePaperlessSettingsSpec extends UnitSpec with WithFakeApplication with ScalaFutures with StubApplicationConfiguration {
  override lazy val fakeApplication = FakeApplication(additionalConfiguration = config)

  "paperlessSettings live" should {

    "update paperless settings and 200 response code" in new Success {
      val result = await(controller.paperlessSettings()(paperlessRequest))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson("The existing record has been updated")
    }

    "update paperless settings and 201 response code" in new SandboxPaperlessCreated {
      val result = await(controller.paperlessSettings()(paperlessRequest))

      status(result) shouldBe 201
    }

    "fail to update paperless settings and 500 response code" in new SandboxPaperlessFailed {
      val result = await(controller.paperlessSettings()(paperlessRequest))

      status(result) shouldBe 500
    }

    "return the summary response from a resource" in new Success {
      val result = await(controller.paperlessSettings()(paperlessRequest))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson("The existing record has been updated")
    }

    "return status code 406 when the headers are invalid" in new Success {
      val result = await(controller.paperlessSettings()(paperlessRequestNoAccept))

      status(result) shouldBe 406
    }
  }

  "paperlessSettings sandbox " should {

    "update paperless settings and 200 response code" in new SandboxSuccess {
      val result = await(controller.paperlessSettings()(paperlessRequest))

      status(result) shouldBe 200
      contentAsJson(result) shouldBe Json.toJson("The existing record has been updated")
    }

    "return status code 406 when the headers are invalid" in new SandboxSuccess {
      val result = await(controller.paperlessSettings()(paperlessRequestNoAccept))

      status(result) shouldBe 406
    }
  }

}