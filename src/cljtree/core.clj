(ns cljtree.core
  (:import [java.io File]))

(defrecord Options [max-depth directories-only? all-files?])

(defn- list-files
  [file-path directories-only?]
  (cond->> (.listFiles (File. ^String file-path))
           directories-only? (filter #(.isDirectory %))
           :always (sort-by #(.toString %))))

(defn- dotfile?
  [file]
  (re-matches #"^\..*$" (.getName file)))

(defn- display-lines
  [current-file last-file]
  (if (= current-file last-file)
    ["  " "└── "]
    ["│  " "├── "]))

(defn tree
  [root prefix {:keys [max-depth directories-only? all-files?] :as options}]
  (when (> max-depth 0)
    (let [files (list-files root directories-only?)
          last-file (last files)]
      (doseq [file files
              :when (or all-files? (not (dotfile? file)))
              :let [[indent-line ruled-line] (display-lines file last-file)
                    file-name (.getName file)
                    file-path (.getPath file)]]
        (if (.isDirectory file)
          (do (println (str prefix ruled-line file-name "/"))
              (tree file-path
                    (str prefix indent-line)
                    (update options :max-depth dec)))
          (println (str prefix ruled-line file-name)))))))
