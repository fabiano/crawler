(ns crawlr.cmdline.migrate
  (:require [crawlr.database :as database])
  (:gen-class))

(defn -main [& args]
  (database/migrate))
