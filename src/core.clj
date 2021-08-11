(ns core
  (:require [cljtree.core :refer [->Options tree]]))

(defn -main [& args]
  (println ".")
  (tree "." "" (->Options 10 false false)))
