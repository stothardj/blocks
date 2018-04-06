(ns blocks.drawable
  (:require [blocks.grid :as grid]))

(defprotocol Drawable
  (draw [drawable]))

(defn draw-rect [pos grid color]
  (let [{:keys [row col]} pos
        cell (grid/cell-bounds grid row col)]
    [:fill {:color color}
     [:rect cell]]))
