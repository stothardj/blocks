(ns blocks.character
  (:require [blocks.grid :as grid]
            [blocks.drawable :as drawable]
            [blocks.direction :as direction]))

(defrecord Character [pos grid])

(defn draw [character]
  (drawable/draw-rect (:pos character) (:grid character) "#f00"))

(defn move [character dir]
  (let [pos (:pos character)
        new-pos (merge-with + pos dir)]
    (assoc-in character [:pos]
              (if (grid/in-bounds? (:grid character) new-pos)
                new-pos
                pos))))
