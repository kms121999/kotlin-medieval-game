import kotlin.math.pow

class Player(val Name : String) {
    // Initialize all buildings
    private val buildings = mutableListOf<Building>(
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


    fun spend(amount : Int) : Boolean {
        if (amount <= silver) {
            silver -= amount
            return true
        }

        println("Insufficient funds")
        return false;
    }

    private fun loseSilver(amount : Int) : Int {
        val lossAmount = amount.coerceAtMost(silver)
        silver -= lossAmount
        return lossAmount
    }

    private fun addSilver(amount : Int) {
        silver += amount
    }

    fun addItem(newItem : Item) {
        var exists = false
        for (item in inventory) {
            if (item.name == newItem.name) {
                item.add(newItem.getQuantity())
                exists = true
            }
        }

        if (!exists) inventory.add(newItem)
    }

    fun getItem(name : String) : Item? {
        for (item in inventory) {
            if (item.name == name) {
                return item
            }
        }

        return null
    }

    fun getBuilding(name : String) : Building {
        for (building in buildings) {
            if (building.getName() == name) return building
        }
        throw Exception("Building does not exist")
    }

    fun getSilver() : Int {
        return silver
    }

    fun getDays() : Int {
        return daysPlayed
    }

    fun getLevel() : Int {
        return level
    }

    fun getPower() : Double {
        return 0.0
    }

    fun levelUp() {
        level += 1
    }

    fun takeDamage(amount : Int, townDamage : Boolean = false) {
        if (amount < 0) {
            println("We had the upper hand and suffered no losses...")
            return
        }

        if (Math.random() < 0.10) {
            println("We were lucky and sustained no losses...")
            return
        }

        val lossTier : Int = if (amount < 500) 1
        else if (amount < 5000) 2
        else if (amount < 15000) 3
        else 4

        // Weapons
        for (item in inventory) {
            val amountLost = item.destroy((Math.random() * ((lossTier + 1.0).pow(6.0))).toInt())

            if (amountLost > 0) println("You've lost $amountLost ${item.name}")
        }

        // Silver
        val silverLost = loseSilver((Math.random() * amount).toInt())

        if (silverLost > 0) println("You've lost $silverLost silver")

        // Buildings
        if (townDamage && lossTier > 1) {
            for (building in buildings) {
                var levelsLost = 0
                if (building.getName() == "farm") {
                    levelsLost = building.destroy((Math.random() * 15 * lossTier).toInt())

                    if (levelsLost > 0) println("You've lost $levelsLost farm${if (levelsLost == 1) "" else "s"}")
                } else {
                    if (Math.random() < ((0.20 * lossTier) - 0.05)) {
                        levelsLost = building.destroy()

                        if (levelsLost > 0) println("Your ${building.getName()} was damaged and has been downgraded $levelsLost levels to level ${building.getLevel()}")
                    }
                }
            }
        }
    }

    fun displayStats() {
        println("Day ${getDays()}")
        println("Silver ${getSilver()}")
        for (building in buildings) {
            building.display()
        }
        for (item in inventory) {
            item.display()
        }
        println()
    }

    fun startNewDay(possibleDamage : Int) {
        daysPlayed += 1

        // 25% of the time, have bandits attack
        if (Math.random() < 0.25) {
            println("Oh, no! Bandits are attacking!")
            takeDamage((possibleDamage - getPower()).toInt(), townDamage = true)
        }

        collectSalary()
    }

    fun obtainLoot(victimStrength : Int) {
        // TODO
    }

    private fun collectSalary() {
        addSilver(getLevel() * 1000)
    }

    fun getEffects(attribute : String) {
        // TODO
    }
}