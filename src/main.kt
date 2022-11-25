import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

/**
 * Gets player name and begins the game
 */
fun main(args : Array<String>) {
    // Prompt for name
    println("Please enter your character's name:")
    val name = readLine()!!

    // Create the player object
    val player = Player(name)

    // Start the game
    showMainMenu(player)
}

/**
 * Displays the main menu and calls the appropriate funcitons
 */
fun showMainMenu(player : Player) {
    // Initialize the option selection
    var option : Int = -1

    // Loop until quit option [0] is selected
    while (option != 0) {
        // Display menu
        println("Day ${player.getDays()}")
        println()
        println("Do you wish to...")
        println("[1] Go to shop menu")
        println("[2] Go to construction menu")
        println("[3] Attack bandit camp")
        println("[4] View your stats")
        println("[5] End the day")
        println("[0] Quit the game")

        option = getIntInput()

        // Execute option
        when (option) {
            1 -> showShopMenu(player)
            2 -> showBuildMenu(player)
            3 -> executeAttack(player)
            4 -> player.displayStats()
            5 -> endDay(player)
            0 -> null
            else -> println("Invalid option\n")
        }
    }
}

/**
 * Displays the shop menu and handles shop transactions
 */
fun showShopMenu(player : Player) {
    var option : Int = -1

    // White space to buffer from previous menu
    println()

    while (option != 0) {
        // Display menu
        println("You have ${player.getSilver()} silver")
        println("[1] Buy 50 swords for 100 silver\tYou have ${player.getItem("sword")?.getQuantity() ?: 0} swords")
        println("[2] Buy 50 bows for 200 silver\tYou have ${player.getItem("bow")?.getQuantity() ?: 0} bows")
        println("[3] Buy 50 armour for 200 silver\tYou have ${player.getItem("armour")?.getQuantity() ?: 0} armour")
        println("[4] Buy 50 horses for 400 silver\tYou have ${player.getItem("horse")?.getQuantity() ?: 0} horses")
        println("[0] Return to main menu")

        option = getIntInput()

        // Execute option choice
        when (option) {
            1 -> {
                if (player.spend(100)) {
                    player.addItem(Item("sword", 50))
                }
            }
            2 -> {
                if (player.spend(200)) {
                    player.addItem(Item("bow", 50))
                }
            }
            3 -> {
                if (player.spend(200)) {
                    player.addItem(Item("armour", 50))
                }
            }
            4 -> {
                if (player.spend(400)) {
                    player.addItem(Item("horse", 50))
                }
            }
            0 -> null
            else -> println("Invalid option")
        }

        // White space before displaying the next menu
        println()
    }
}

/**
 * Display build menu and execute option selections
 */
fun showBuildMenu(player : Player) {
    var option : Int = -1

    // White space after previous menu
    println()

    // Loop
    while (option != 0) {
        // Display menu
        println("You have ${player.getSilver()} silver")
        println("[1] Upgrade castle for ${player.getBuilding("castle").getUpgradeCost()}")
        println("[2] Upgrade barracks for ${player.getBuilding("barracks").getUpgradeCost()}")
        println("[3] Upgrade farm for ${player.getBuilding("farm").getUpgradeCost()}")
        println("[4] Upgrade town for ${player.getBuilding("town").getUpgradeCost()}")
        println("[0] Return to main menu")

        option = getIntInput()

        // Execute selection
        when (option) {
            1 -> {
                if (player.spend(player.getBuilding("castle").getUpgradeCost())) {
                    player.getBuilding("castle").upgrade()
                }
            }
            2 -> {
                if (player.spend(player.getBuilding("barracks").getUpgradeCost())) {
                    player.getBuilding("barracks").upgrade()
                }
            }
            3 -> {
                if (player.spend(player.getBuilding("farm").getUpgradeCost())) {
                    player.getBuilding("farm").upgrade()
                }
            }
            4 -> {
                if (player.spend(player.getBuilding("town").getUpgradeCost())) {
                    player.getBuilding("town").upgrade()
                }
            }
            0 -> null
            else -> println("Invalid option")
        }

        // White space to buffer next menu
        println()
    }
}

/**
 * Attempts an attack against the bandits
 */
fun executeAttack(player : Player) {
    // Get the player's level
    val x = player.getLevel().toDouble()

    // Calculate the enemies power based on player's level
    val enemyPower = abs(100 * (-log10((x + 1).pow(2.0))) + (3 * (x + 1.7)).pow(2.0)) / (3 * log10(x+6)) + 100

    // Get the player's power
    val playerPower = player.getPower()

    println()
    // Test if more powerful
    if (playerPower > enemyPower) {
        println("You've scattered the bandits and stolen their loot!")
        player.obtainLoot(enemyPower.toInt())
        player.levelUp()
    } else {
        println("Run! There's too many of them!")
        player.takeDamage((playerPower - enemyPower).toInt())
    }
}

/**
 * End the day
 */
fun endDay(player : Player) {
    // Get the player's level
    val x = player.getLevel().toDouble()

    // Calculate the enemies power based on player's level
    val enemyPower = (abs(100 * (-log10((x + 1).pow(2.0))) + (3 * (x + 1.7)).pow(2.0)) / (3 * log10(x+6)) + 100)/100

    player.startNewDay(enemyPower.toInt())
}

/**
 * Gets an integer from the user and returns it
 */
fun getIntInput() : Int {
    // Initialize the input
    var input : String

    // Loop until valid integer is given
    while (true) {
        try {
            // Get and return an int
            return readLine()!!.toInt()
        } catch (e : NumberFormatException) {
            // Prompt for an integer
            println("Please input an integer")
        }
    }
}