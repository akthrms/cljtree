(ns core
  (:require [clojure.tools.cli :as cli]
            [cljtree.core :refer [->Options tree]]))

(def options [["-a" "--all" "Show dotfiles" :default false]
              ["-d" "--dir" "Show only directories" :default false]
              ["-l" "--limit DEPTH" "Limit depth" :parse-fn #(Integer/parseInt %)]
              ["-h" "--help" "Show this help message"]])

(defn help-message
  [summary]
  (str "`tree` powered by Clojure" \newline
       \newline
       "USAGE" \newline
       "  cljtree [dirname] : Show children of dirname. default dirname is pwd." \newline
       \newline
       "OPTIONS" \newline
       summary))

(defn -main [& args]
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args options)]
    (cond
      errors (println errors)
      (:help options) (println (help-message summary))
      :else (let [root (if (empty? arguments) "." (first arguments))]
              (println root)
              (tree root "" (->Options (or (:limit options) 10)
                                       (:dir options)
                                       (:all options)))))))
