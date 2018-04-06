(ns blocks.block)

(defprotocol Block
  ;; Attempt to push the block in a certain direction.
  ;; Returns false if it refuses to move
  ;; Otherwise returns the new full list of blocks.
  (push [block dir blocks]))
