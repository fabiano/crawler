(ns crawlr.cmdline.migrate
  (:require [clojure.tools.logging :as logging])
  (:require [crawlr.database :as database])
  (:gen-class))

(defn up [& args]
  (logging/info "Running migrations")

  (database/migrate-up)

  (logging/info "Database updated"))

(defn down [& args]
  (logging/info "Running migrations")

  (database/migrate-down)

  (logging/info "Database updated"))
