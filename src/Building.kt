// Set the upgrade costs for each building
val upgradeCosts = mapOf(
    "castle" to listOf(500, 1000, 2000, 3000, 5000, 7000, 10000),
    "barracks" to listOf(100, 100, 300, 500, 500, 800, 1000, 1000, 1000),
    "farm" to listOf(50, 50, 50, 50, 100, 100, 100, 200, 200, 300, 300, 300, 300, 500, 500, 500, 500),
    "town" to listOf(1000, 3000, 5000, 10000, 20000)
)

// Set the defense points added on for each level
val defensePoints = mapOf(
    "castle" to listOf(500, 1000, 2000, 3000, 5000, 7000, 10000),
    "barracks" to listOf(100, 200, 300, 400, 500, 600, 700, 800, 900),
    "farm" to listOf(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10),
    "town" to listOf(500, 500, 500, 500, 1000)
)

class Building(private val name : String) {
    private var level : Int = 0

    /**
     * Increase level by one
     */
    fun upgrade() {
        level += 1
    }

    /**
     * Safely reduce the level
     */
    fun destroy(levels : Int = 1) : Int {
        val destroyCount = levels.coerceAtMost(level)
        level -= destroyCount

        return destroyCount
    }

    /**
     * Get the cost to upgrade to next level. -1 if no next level
     */
    fun getUpgradeCost() : Int {
        // Check if current level has an upgrade option
        return if (level < upgradeCosts[name]!!.size) {
            // Returns the upgrade cost from the current level
            upgradeCosts[name]!![level]
        } else {
            // -1 means there is no next level to upgrade to
            -1
        }
    }

    /**
     * Get the accumulative power of all levels of the building
     */
    fun getPower() : Int {
        var powerAccumulator = 0

        // For each level, add its defense power
        for (i in 0 until level) {
            powerAccumulator += defensePoints[name]!![i]
        }

        return powerAccumulator
    }

    /**
     * Get the current level
     */
    fun getLevel() : Int {
        return level
    }

    /**
     * Get the building name
     */
    fun getName() : String {
        return name
    }

    /**
     * Displays the building in format of "$name is at level $level"
     */
    fun display() {
        println("${getName()} is at level ${getLevel()}")
    }
}