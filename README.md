# CUSTOMER-PROFILE

[![Build Status](https://travis-ci.org/hmrc/customer-profile.svg?branch=master)](https://travis-ci.org/hmrc/customer-profile) [ ![Download](https://api.bintray.com/packages/hmrc/releases/customer-profile/images/download.svg) ](https://bintray.com/hmrc/releases/customer-profile/_latestVersion)


# API

## GET     /profile/accounts

Look up for the current user's tax account identifiers, e.g. National Insurance Number (NINO), Self Assessment UTR.

**Response Status Codes**

| StatusCode | Description                                                                                                       |
|------------|-------------------------------------------------------------------------------------------------------------------|
| 200        | Record found                                                                                                      |
| 404        | Record not found                                                                                                  |
| 500        | There was an unrecoverable error, possibly bad data received from the upstream API

**Example Response Body**

TODO


## GET     /profile/personal-details/:nino

Finds a user's designatory details

**Response Status Codes**

| StatusCode | Description                                                                                                       |
|------------|-------------------------------------------------------------------------------------------------------------------|
| 200        | Record found                                                                                                      |
| 404        | Record not found                                                                                                  |
| 423        | Record was hidden, due to manual correspondence indicator flag being set                                          |
| 500        | There was an unrecoverable error, possibly bad data received from the upstream API

**Example Response Body**

TODO



### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html")