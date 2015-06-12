(ns orgref-api.util)

(defn parse-int
  "Parse a string into an integer"
  [s]
  (if (number? s)
    s
    (try
      (Integer/parseInt s)
      (catch NumberFormatException e
        nil))))

(defn assoc-if
  "assoc a value into a map only if it is not nil"
  [m k value]
  ; (if (and value (not (empty? value)))
  (if value
    (assoc m k value)
    m))
