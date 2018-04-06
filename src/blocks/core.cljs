(ns blocks.core
  (:require [play-cljs.core :as p]
            [goog.events :as events]
            [blocks.grid :as grid]
            [blocks.rect :as rect]
            [blocks.block :as block]
            [blocks.goal :as goal]
            [blocks.character :as character]
            [blocks.position :as pos]
            [blocks.drawable :as drawable]
            [blocks.levels :as levels]
            [blocks.direction :as direction]))

(defonce game (p/create-game (.-innerWidth js/window) (.-innerHeight js/window)))
(defonce state (atom {}))

(def main-screen
  (reify p/Screen
    (on-show [this]
      (let [width (.-innerWidth js/window)
            height (.-innerHeight js/window)
            border-size 20
            game-width (- width (* 2 border-size))
            game-height (- height (* 2 border-size))
            game-area (rect/Rect. border-size border-size game-width game-height)]
        (reset! state (levels/level1 game-area))))
    (on-hide [this])
    (on-render [this]
      (let [width (.-innerWidth js/window)
            height (.-innerHeight js/window)
            {:keys [grid goal player blocks]} @state]
        (p/render game
                  [[:fill {:color "#f90"}
                    [:rect {:x 0 :y 0 :width width :height height}]]
                   [:fill {:color "#fff"}
                    [:rect (:bounds grid)]]
                   (grid/draw grid)
                   (goal/draw goal)
                   (character/draw player)
                   (for [b blocks]
                     (drawable/draw b))
                   ])))))

(events/listen js/window "resize"
  (fn [event]
    (p/set-size game js/window.innerWidth js/window.innerHeight)))


(defn try-move [dir state]
  (let [player (:player state)
        new-player (character/move player dir)
        new-pos (:pos new-player)
        bs (:blocks state)
        same-pos? (partial = new-pos)
        split-bs (group-by (comp same-pos? :pos) bs)
        block (get split-bs true)]
    (if block
      (let [new-bs (block/push (first block) dir (get split-bs false))]
        (if new-bs
          (-> state
              (assoc-in [:player] new-player)
              (assoc-in [:blocks] new-bs))
          state))
      (assoc-in state [:player] new-player))))

(events/listen js/window "keydown"
               (fn [event]
                 (case (.-key event)
                   "ArrowUp" (swap! state (partial try-move direction/up))
                   "ArrowDown" (swap! state (partial try-move direction/down))
                   "ArrowLeft" (swap! state (partial try-move direction/left))
                   "ArrowRight" (swap! state (partial try-move direction/right))
                   nil
                   )
                 ))

(doto game
  (p/start)
  (p/set-screen main-screen))
