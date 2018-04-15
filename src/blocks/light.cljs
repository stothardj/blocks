(ns blocks.light
  (:require [blocks.grid :as grid]
            [blocks.drawable :as drawable]
            [blocks.direction :as direction]
            [blocks.block :as block]))

(defrecord Light [pos grid]
  block/Block
  (push [block dir bs]
    (let [pos (:pos block)
          new-pos (merge-with + pos dir)
          new-block (assoc-in block [:pos] new-pos)
          same-pos? (partial = new-pos)
          split-blocks (group-by (comp same-pos? :pos) bs)
          pushed-block (get split-blocks true)
          other-blocks (get split-blocks false)]
      (cond
        ; Pushing would go out of bounds.
        (not (grid/in-bounds? grid new-pos))
        false

        ; There's no block there, go ahead.
        (not pushed-block)
        (conj bs new-block)

        ; We try pushing the block. This has a side-effect and also
        ; returns other moved blocks if it managed to move.
        :else
        (let [new-blocks (block/push (first pushed-block) dir other-blocks)]
          (when new-blocks
            (conj new-blocks new-block))))))
  drawable/Drawable
  (draw [block]
    (drawable/draw-rect (:pos block) (:grid block) "#a05")))
