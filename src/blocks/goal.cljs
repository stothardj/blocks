(ns blocks.goal
  (:require [blocks.drawable :as drawable]))

(defrecord Goal [pos grid])

(defn draw [goal]
  (drawable/draw-rect (:pos goal) (:grid goal) "#ff0"))
