(ns blocks.stationary
  (:require [blocks.drawable :as drawable]
            [blocks.block :as block]))

(defrecord Stationary [pos grid]
  block/Block
  (push [block dir blocks] false)
  drawable/Drawable
  (draw [block]
    (drawable/draw-rect (:pos block) (:grid block) "#05a")))
