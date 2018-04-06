(ns blocks.sliding
  (:require [blocks.grid :as grid]
            [blocks.drawable :as drawable]
            [blocks.direction :as direction]
            [blocks.block :as block]))

(defrecord Sliding [pos grid]
  block/Block
  (push [block dir bs]
    (let [pos (:pos block)
          new-pos (merge-with + pos dir)
          new-block (assoc-in block [:pos] new-pos)]
      (if (and (grid/in-bounds? grid new-pos) (not-any? #(= new-pos (:pos %)) bs))
        (conj bs new-block)
        false)))
  drawable/Drawable
  (draw [block]
    (drawable/draw-rect (:pos block) (:grid block) "#0a5")))
