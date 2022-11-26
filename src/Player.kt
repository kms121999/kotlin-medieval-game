import kotlin.math.pow

class Player(val Name : String) {
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


    fun spend(amount : Int) : Boolean {
        if (amount <= silver) {
            silver -= amount
            return true
        }

        println("\nInsufficient funds")
        waitForEnter()
        return false
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

    fun getPower(inTown : Boolean = false) : Double {
        var powerAccumulator = 0.0

        for (item in inventory) {
            powerAccumulator += item.getPower()
        }

        if (inTown) {
            for (building in buildings) {
                powerAccumulator += building.getPower()
            }
        }

        return powerAccumulator
    }

    fun getEnemyPower() : Double {
        return (level.toDouble().pow(2.0) * 9) + 200
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

            if (amountLost > 0) println("You've lost $amountLost ${item.name}s")
        }

        // Silver
        val silverLost = loseSilver((Math.random() * amount).toInt())

        if (silverLost > 0) println("You've lost $silverLost silver")

        // Buildings
        if (townDamage && lossTier > 1) {
            for (building in buildings) {
                var levelsLost : Int
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
        println("\nSTATS")
        println("Day ${getDays()}")
        println("Silver ${getSilver()}")
        for (building in buildings) {
            building.display()
        }
        for (item in inventory) {
            item.display()
        }
        waitForEnter()
    }

    fun startNewDay() {
        println()

        daysPlayed += 1

        // 25% of the time, have bandits attack
        if (Math.random() < 0.25) {
            val possibleDamage = getEnemyPower()
            println("Oh, no! Bandits are attacking!")
            takeDamage((possibleDamage - getPower(inTown = true)).toInt(), townDamage = true)
            waitForEnter()
        }

        val salaryAmount = collectSalary()
        println("You've collected $salaryAmount in taxes")

        waitForEnter()
    }

    fun obtainLoot(victimStrength : Int) {
        addSilver(victimStrength)
    }

    private fun collectSalary() : Int {
        val salaryAmount = getLevel() * 1000

        addSilver(salaryAmount)

        return salaryAmount
    }
}