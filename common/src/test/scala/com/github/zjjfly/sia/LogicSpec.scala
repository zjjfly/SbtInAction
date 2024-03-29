package com.github.zjjfly.sia

import org.specs2.mutable.Specification

/**
  * @author zjjfly[https://github.com/zjjfly] on 2020/4/5
  */
class LogicSpec extends Specification {
  "The 'matchLikelihood' method" should {
    "be 100% when all attributes match" in {
      val tabby = Kitten(1, List("male", "tabby"))
      val prefs = BuyerPreferences(List("male", "tabby"))
      val result = Logic.matchLikelihood(tabby, prefs)
      result must beGreaterThan(.999)
    }
  }

  "The 'matchLikelihood' method" should {
    "be 0% when no attributes match" in {
      val tabby = Kitten(1, List("male", "tabby"))
      val prefs = BuyerPreferences(List("female", "calico"))
      val result = Logic.matchLikelihood(tabby, prefs)
      result must beLessThan(.001)
    }
  }
}
