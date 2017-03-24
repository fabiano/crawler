(ns crawlr.selectors-test
  (:require [clojure.test :refer :all])
  (:require [crawlr.selectors :refer :all]))

(deftest test-parse-price
  (is (= (parse-price "R$ 10,00") 1000))
  (is (= (parse-price "10,00") 1000))
  (is (= (parse-price "R$ 10,00 (Lorem Ipsum)") 1000)))
