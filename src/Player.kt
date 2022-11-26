import kotlin.math.pow

class Player(val name : String) {
    // Initialize all buildings
    private val buildings = mutableListOf(
        Building("castle"),
        Building("barracks"),
        Building("farm"),
        Building("town")
    )

    private val inventory = mutableListOf<Item>()

    // Set initial silver
    private var silver = 500

    private var daysPlayed = 0
    private var level = 1

    /**
     * Attempts to spend, alerts if invalid cost or insufficient funds.
     */
    fun spend(amount : Int) : Boolean {
        // Check if amount signals an unavailable item
        if (amount == -1) {
            println("\nUnavailable for purchase")
            waitForEnter()
            return false
        }

        // If player has enough, spend the silver and signify success
        if (amount <= silver) {
            silver -= amount
            return true
        }

        // There were insufficient funds
        println("\nInsufficient funds")
        waitForEnter()
        return false
    }

    /**
     * Safely reduce money
     */
    private fun loseSilver(amount : Int) : Int {
        val lossAmount = amount.coerceAtMost(silver)
        silver -= lossAmount
        return lossAmount
    }

    /**
     * Increase silver
     */
    private fun addSilver(amount : Int) {
        silver += amount
    }

    /**
     * Add items to inventory
     */
    fun addItem(newItem : Item) {
        var exists = false

        // Check if item already exists
        for (item in inventory) {
            // If item exists, merge
            if (item.name == newItem.name) {
                item.add(newItem.getQuantity())
                exists = true
            }
        }

        // If item does not already exist, just add it to the inventory
        if (!exists) inventory.add(newItem)
    }

    /**
     * Increase level by one
     */
    fun levelUp() {
        level += 1
    }

    /**
     * Suffer losses to inventory, buildings, and wallet
     */
    fun takeDamage(amount : Int, townDamage : Boolean = false) {
        // If amount of damage is negative, take no damage
        if (amount < 0) {
            println("We had the upper hand and suffered no losses...")
            return
        }

        // If we are lucky, take no damage
        if (Math.random() < 0.10) {
            println("We were lucky and sustained no losses...")
            return
        }

        // Calculate the loss tier
        val lossTier : Int = if (amount < 500) 1
        else if (amount < 5000) 2
        else if (amount < 15000) 3
        else 4

        // Weapons
        for (item in inventory) {
            val amountLost = item.destroy((Math.random() * ((lossTier + 1.0).pow(6.0))).toInt())

            if (amountLost > 0) println("You've lost $amountLost ${item.name}s")
        }

        // Silver
        val silverLost = loseSilver((Math.random() * amount).toInt())

        if (silverLost > 0) println("You've lost $silverLost silver")

        // Buildings
        if (townDamage && lossTier > 1) {
            for (building in buildings) {
                var levelsLost : Int

                // Farms are a special case since they are cheap and are considered as individual buildings
                if (building.getName() == "farm") {
                    levelsLost = building.destroy((Math.random() * 15 * lossTier).toInt())

                    if (levelsLost > 0) println("You've lost $levelsLost farm${if (levelsLost == 1) "" else "s"}")
                } else {
                    if (Math.random() < ((0.20 * lossTier) - 0.05)) {
                        levelsLost = building.destroy()

                        if (levelsLost > 0) println("Your ${building.getName()} was damaged and has been downgraded " +
                                                    "$levelsLost levels to level ${building.getLevel()}")
                    }
                }
            }
        }
    }

    /**
     * Execute nighttime events
     */
    fun startNewDay() {
        // whitespace
        println()

        // Increase day count
        daysPlayed += 1

        // 25% of the time, have bandits attack
        if (Math.random() < 0.25) {
            val possibleDamage = getEnemyPower()
            println("Oh, no! Bandits are attacking!")
            takeDamage((possibleDamage - getPower(inTown = true)).toInt(), townDamage = true)
            waitForEnter()
        }

        // Collect taxes
        val salaryAmount = collectSalary()
        println("You've collected $salaryAmount in taxes")

        waitForEnter()
    }

    /**
     * Collect loot bonuses
     */
    fun obtainLoot(victimStrength : Int) {
        addSilver(victimStrength)
    }

    /**
     * Collect salary
     */
    private fun collectSalary() : Int {
        val salaryAmount = getLevel() * 1000

        addSilver(salaryAmount)

        return salaryAmount
    }

    /**
     * Get the accumulative power of the player
     */
    fun getPower(inTown : Boolean = false) : Double {
        var powerAccumulator = 0.0

        // Get the power of all items
        for (item in inventory) {
            powerAccumulator += item.getPower()
        }

        // Get the power of all buildings if inTown
        if (inTown) {
            for (building in buildings) {
                powerAccumulator += building.getPower()
            }
        }

        return powerAccumulator
    }

    /**
     * Calculate enemy power based on player level
     */
    fun getEnemyPower() : Double {
        // Exponential equation based on level
        return (level.toDouble().pow(2.0) * 9) + 200
    }

    /**
     * Get an item from player inventory
     */
    fun getItem(name : String) : Item? {
        // Search for item
        for (item in inventory) {
            if (item.name == name) {
                return item
            }
        }

        // Item is not in inventory
        return null
    }

    /**
     * Get building from player buildings
     */
    fun getBuilding(name : String) : Building {
        // Search for buildings
        for (building in buildings) {
            if (building.getName() == name) return building
        }

        // This should never occur
        throw Exception("Building does not exist")
    }

    /**
     * Get player silver
     */
    fun getSilver() : Int {
        return silver
    }

    /**
     * Get number of days played
     */
    fun getDays() : Int {
        return daysPlayed
    }

    /**
     * Get player level
     */
    private fun getLevel() : Int {
        return level
    }

    /**
     * Display player's current standing
     */
    fun displayStats() {
        println("\n$name's STATS")
        // Day
        println("Day ${getDays()}")

        // Money
        println("Silver ${getSilver()}")

        // Buildings
        for (building in buildings) {
            building.display()
        }

        // Items
        for (item in inventory) {
            item.display()
        }

        waitForEnter()
    }
}