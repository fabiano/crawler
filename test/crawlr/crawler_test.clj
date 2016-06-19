(ns crawlr.crawler-test
  (:require [clojure.test :refer :all])
  (:require [crawlr.crawler :refer :all]))

(deftest test-sanitize-price
  (is (= (sanitize-price "R$ 10,00") 1000)))
