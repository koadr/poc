package com.trovimap.domain

import java.net.URL
import java.util.Currency

import scala.util.Random

object TestHelpers extends TestHelpers

trait TestHelpers {
  private def randomAlphanumeric = scala.util.Random.alphanumeric

  private def randomInt: Int = Random.nextInt(Int.MaxValue) + 1

  private def randomLong = randomInt.toLong

  def arbBoolean = false

  def arbString = randomAlphanumeric.take(10).mkString

  def arbPropertyId = PropertyId(arbString)

  def arbBrokerId = BrokerId(arbString)

  def arbLocation = Location(randomLong, randomLong)

  def arbCurrency = Currency.getInstance("USD")

  def arbPrice = Price(randomInt, arbCurrency)

  def arbURL = new URL("http://localhost")

  def arbProperty =
    Property(arbPropertyId,
             arbBrokerId,
             Some(arbLocation),
             randomInt,
             randomInt,
             Some(arbString),
             arbPrice,
             Some(arbURL))

}
