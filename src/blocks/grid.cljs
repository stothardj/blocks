(ns blocks.grid
  (:require [blocks.rect :as rect]))

(defrecord Grid [rows cols bounds])

(defn cell-width [grid]
  (/ (:width (:bounds grid)) (:cols grid)))

(defn cell-height [grid]
  (/ (:height (:bounds grid)) (:rows grid)))

(defn cell-bounds [grid row col]
  (let [w (cell-width grid)
        h (cell-height grid)
        bounds (:bounds grid)]
    (rect/Rect.
     (+ (:x bounds) (* col w))
     (+ (:y bounds) (* row h))
     w h)))

(defn in-bounds? [grid pos]
  (let [{:keys [row col]} pos]
    (and (>= row 0)
               (>= col 0)
               (< row (:rows grid))
               (< col (:cols grid)))))

(defn draw [grid]
  (for [r (range (:rows grid))
        c (range (:cols grid))]
    (let [cell (cell-bounds grid r c)]
      [:rect cell])))

