(defproject crawlr "0.1.0-SNAPSHOT"
  :aliases {
    "feed" ["run" "-m" "crawlr.cmdline.feed"]
    "migrate" ["run" "-m" "crawlr.cmdline.migrate"]
    "try" ["run" "-m" "crawlr.cmdline.try"]
    "worker" ["run" "-m" "crawlr.cmdline.worker"]
  }
  :dependencies [
    [org.apache.logging.log4j/log4j-core "2.7"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/java.jdbc "0.6.1"]
    [org.clojure/tools.logging "0.3.1"]
    [org.jsoup/jsoup "1.9.2"]
    [org.postgresql/postgresql "9.4.1211"]
    [org.slf4j/slf4j-log4j12 "1.7.21"]
  ]
  :jar-name "crawlr-%s.jar"
  :main ^:skip-aot crawlr.cmdline.try
  :min-lein-version "2.6.1"
  :profiles {
    :uberjar {:aot :all}
  }
  :target-path "target/%s"
  :uberjar-name "crawlr-%s-standalone.jar")
