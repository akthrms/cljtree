(ns core
  (:require [clojure.tools.cli :as cli]
            [cljtree.core :refer [->Options tree]]))

(def options [["-a" "--all" "Show dotfiles"
               :default false]
              ["-d" "--dir" "Show only directories"
               :default false]
              ["-l" "--limit DEPTH" "Limit depth (default : 5)"
               :default 5
               :parse-fn #(Integer/parseInt %)
               :validate [#(pos-int? %) "Must be a positive number"]]
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

(defn -main
  [& args]
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args options)]
    (cond
      errors (println errors)
      (:help options) (println (help-message summary))
      :else (let [root (if (empty? arguments) "." (first arguments))]
              (println root)
              (tree root "" (->Options (:limit options)
                                       (:dir options)
                                       (:all options)))))))
