// Set the upgrade costs for each building
val upgradeCosts = mapOf(
    "castle" to listOf(500, 1000, 2000, 3000, 5000, 7000, 10000),
    "barracks" to listOf(100, 100, 300, 500, 500, 800, 1000, 1000, 1000),
    "farm" to listOf(50, 50, 50, 50, 100, 100, 100, 200, 200, 300, 300, 300, 300, 500, 500, 500, 500),
    "town" to listOf(1000, 3000, 5000, 10000, 20000)
)

val defensePoints = mapOf(
    "castle" to listOf(500, 1000, 2000, 3000, 5000, 7000, 10000),
    "barracks" to listOf(100, 200, 300, 400, 500, 600, 700, 800, 900),
    "farm" to listOf(10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10),
    "town" to listOf(500, 500, 500, 500, 1000)
)

class Building(private val name : String) {
    private var level : Int = 0

    fun getUpgradeCost() : Int {
        // Check if current level has an upgrade option
        if (level < upgradeCosts[name]!!.size)
        {
            // Returns the upgrade cost from the current level
            return upgradeCosts[name]!![level]
        } else {
            return 0
        }
        throw Exception("This building is not supported")
    }

    fun getPower() : Int {
        var powerAccumulator = 0
        for (i in 0 until level) {
            powerAccumulator += defensePoints[name]!![i]
        }
        return powerAccumulator
    }

    fun upgrade() {
        level += 1
    }

    fun destroy(levels : Int = 1) : Int {
        val destroyCount = levels.coerceAtMost(level)
        level -= destroyCount

        return destroyCount
    }

    fun getLevel() : Int {
        return level
    }

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