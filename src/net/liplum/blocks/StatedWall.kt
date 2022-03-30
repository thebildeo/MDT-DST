package net.liplum.blocks

import arc.Core
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import mindustry.graphics.Drawf
import mindustry.world.blocks.defense.Wall
import net.liplum.components.ComponentBase

open class StatedWall(name: String) : Wall(name) {
    @JvmField var states: Array<TextureRegion> = arrayOf()
    @JvmField var shadowRadius = 20f
    @JvmField var stateNumber = 0
    @JvmField var components = ArrayList<ComponentBase<StatedWallBuild>>()
    override fun load() {
        super.load()
        states = Array(stateNumber) {
            Core.atlas.find("$name-$it")
        }
    }

    inner class StatedWallBuild : WallBuild() {
        override fun updateTile() {
            for (component in components) {
                component.onUpdate(this)
            }
        }

        override fun drawCracks() {}
        override fun draw() {
            if (shadowRadius > 0f) {
                Drawf.shadow(x, y, shadowRadius)
            }
            var curIndex = (lostHealthPct() * stateNumber).toInt()
            curIndex = curIndex.coerceAtMost(stateNumber - 1)
            Draw.rect(states[curIndex], x, y)
            drawTeamTop()
        }

        fun lostHealthPct(): Float {
            return 1f - health / maxHealth
        }
    }
}