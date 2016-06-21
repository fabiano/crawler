(ns crawlr.crawler-test
  (:require [clojure.test :refer :all])
  (:require [crawlr.crawler :refer :all])
  (:import [org.jsoup Jsoup]))

(deftest test-attr
  (let [element (.getElementsByTag (Jsoup/parseBodyFragment "<a href='/'>Lorem Ipsum</a>") "a")]
    (is (= (attr element "href") "/")))

  (let [element (.getElementsByTag (Jsoup/parseBodyFragment "<a href=''>Lorem Ipsum</a>") "a")]
    (is (nil? (attr element "href"))))

  (let [element (.getElementsByTag (Jsoup/parseBodyFragment "<a>Lorem Ipsum</a>") "a")]
    (is (nil? (attr element "href")))))

(deftest test-text
  (let [element (.getElementsByTag (Jsoup/parseBodyFragment "<div>Lorem Ipsum</div>") "div")]
    (is (= (text element) "Lorem Ipsum")))

  (let [element (.getElementsByTag (Jsoup/parseBodyFragment "<div></div>") "div")]
    (is (nil? (text element)))))

(deftest test-sanitize-price
  (is (= (sanitize-price "R$ 10,00") 1000))
  (is (= (sanitize-price "10,00") 1000))
  (is (= (sanitize-price "R$ 10,00 (Lorem Ipsum)") 1000)))

(deftest test-products
  (let [parent (Jsoup/parseBodyFragment "<div class='single-product'></div>")]
    (is (not (empty? (products parent)))))

  (let [parent (Jsoup/parseBodyFragment "<div class='hproduct'></div>")]
    (is (not (empty? (products parent)))))

  (let [parent (Jsoup/parseBodyFragment "<div class='product'></div>")]
    (is (not (empty? (products parent)))))

  (let [parent (Jsoup/parseBodyFragment "<div class='unmapped'></div>")]
    (is (empty? (products parent))))

  (let [parent (Jsoup/parseBodyFragment "<div></div>")]
    (is (empty? (products parent)))))

(deftest test-title
  (let [parent (Jsoup/parseBodyFragment "<a class='url' title='Lorem Ipsum'></a>")]
    (is (= (title parent) "Lorem Ipsum")))

  (let [parent (Jsoup/parseBodyFragment "<a class='url' title=''></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='url'></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url' title='Lorem Ipsum'></a>")]
    (is (= (title parent) "Lorem Ipsum")))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url' title=''></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url'></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li' title='Lorem Ipsum'></a>")]
    (is (= (title parent) "Lorem Ipsum")))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li' title=''></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li'></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='unmapped'></a>")]
    (is (nil? (title parent))))

  (let [parent (Jsoup/parseBodyFragment "<a></a>")]
    (is (nil? (title parent)))))

(deftest test-url
  (let [parent (Jsoup/parseBodyFragment "<a class='url' href='http://loremipsum.com'></a>")]
    (is (= (url parent) "http://loremipsum.com")))

  (let [parent (Jsoup/parseBodyFragment "<a class='url' href=''></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='url'></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url' href='http://loremipsum.com'></a>")]
    (is (= (url parent) "http://loremipsum.com")))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url' href=''></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='link url'></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li' href='http://loremipsum.com'></a>")]
    (is (= (url parent) "http://loremipsum.com")))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li' href=''></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='product-li'></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a class='unmapped'></a>")]
    (is (nil? (url parent))))

  (let [parent (Jsoup/parseBodyFragment "<a></a>")]
    (is (nil? (url parent)))))

(deftest test-price
  (let [parent (Jsoup/parseBodyFragment "<div class='price sale'>R$ 123.45</div>")]
    (is (= (price parent) 12345)))

  (let [parent (Jsoup/parseBodyFragment "<div class='price sale'></div>")]
    (is (nil? (price parent))))

  (let [parent (Jsoup/parseBodyFragment "<div class='price'>R$ 123.45</div>")]
    (is (= (price parent) 12345)))

  (let [parent (Jsoup/parseBodyFragment "<div class='price'></div>")]
    (is (nil? (price parent))))

  (let [parent (Jsoup/parseBodyFragment "<div class='unmapped'></div>")]
    (is (nil? (price parent))))

  (let [parent (Jsoup/parseBodyFragment "<div></div>")]
    (is (nil? (price parent)))))

(deftest test-next-page
  (let [parent (Jsoup/parse "<html><head><link href='http://www.loremipsum.com/2' rel='next' /><title>Lorem Ipsum</title></head><body>Lorem Ipsum</body></html>")]
    (is (= (next-page parent) "http://www.loremipsum.com/2")))

  (let [parent (Jsoup/parse "<html><head><link href='' rel='next' /><title>Lorem Ipsum</title></head><body>Lorem Ipsum</body></html>")]
    (is (nil? (next-page parent))))

  (let [parent (Jsoup/parse "<html><head><title>Lorem Ipsum</title></head><body>Lorem Ipsum</body></html>")]
    (is (nil? (next-page parent)))))
