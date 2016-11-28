(defproject crawlr "0.1.0-SNAPSHOT"
  :aliases {
    "feed" ["run" "-m" "crawlr.cmdline.feed"]
    "migrate-up" ["run" "-m" "crawlr.cmdline.migrate/up"]
    "migrate-down" ["run" "-m" "crawlr.cmdline.migrate/down"]
    "try" ["run" "-m" "crawlr.cmdline.try"]
    "worker" ["run" "-m" "crawlr.cmdline.worker"]
  }
  :dependencies [
    [org.apache.logging.log4j/log4j-core "2.7"]
    [org.clojure/clojure "1.8.0"]
    [org.clojure/data.json "0.2.6"]
    [org.clojure/java.jdbc "0.6.1"]
    [org.clojure/tools.logging "0.3.1"]
    [org.jsoup/jsoup "1.9.2"]
    [org.postgresql/postgresql "9.4.1211"]
    [org.slf4j/slf4j-log4j12 "1.7.21"]
    [postgre-types "0.0.4"]
  ]
  :jar-name "crawlr-%s.jar"
  :main nil
  :min-lein-version "2.6.1"
  :profiles {
    :uberjar {:aot :all}
  }
  :repl-options {
    :init (do
            (use 'crawlr.crawler)
            (use 'crawlr.database))
  }
  :target-path "target/%s"
  :uberjar-name "crawlr-%s-standalone.jar")
