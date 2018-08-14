import java.lang.Integer.parseInt

class UserInterface {
    private var userContinue = true
    private val pcbQueue = DllQueue()

    fun start() {
        println("Welcome to..\n")
        printSantOS()
        pressAnyKeyToContinue()
        while (userContinue) {
            promptUser()
        }
    }

    private fun promptUser() {
        clearOutput()
        println("Please select the operation you would like to perform? (Number + Enter Key)")
        println("(0) Load professor provided sample data into queue.")
        println("(1) Print PCB queue.")
        println("(2) Push PCB with PID X to back of queue.")
        println("(3) Push PCB with PID X to front of queue.")
        println("(4) Push PCB with PID of X to queue after process with PID Y.")
        println("(5) Push PCB with PID of X to queue before process with PID Y.")
        println("(6) Pop PCB at the head of the queue.")
        println("(7) Delete PCB with PID X.")
        println("(8) Perform first come first serve scheduling service on current queue.")
        println("(9) Perform non-preemptive priority scheduling service on current queue.")
        println("(10) Perform round robin scheduling on current queue.")
        println("(11) Exit SantOS.")
        print("User: ")

        val userInput = readLine()

        clearOutput()

        when (userInput) {
            "0" -> loadSampleQueue()
            "1" -> printPCBQueue()
            "2" -> pushProcessToBackOfQueue()
            "3" -> pushProcessToFrontOfQueue()
            "4" -> pushProcessToQueueAfterPidX()
            "5" -> pushProcessToQueueBeforePidX()
            "6" -> popPCBQueue()
            "7" -> deleteProcessWithPidX()
            "8" -> performFCFS()
            "9" -> performNPP()
            "10" -> performRR()
            "11" -> exitSantOS()

            else -> {
                println(" \"$userInput\" is an invalid selection, please try again...")
                pressAnyKeyToContinue()
            }
        }
    }

    private fun pushProcessToBackOfQueue() {
        val arrivalTime: String
        val burstTime: String
        val priority: String
        val pid = getNewPidInput()
        if (!userCancelled(pid)) {
            arrivalTime = getNewArrivalTimeInput()
            if (!userCancelled(arrivalTime)) {
                burstTime = getNewBurstTimeInput()
                if (!userCancelled(burstTime)) {
                    priority = getNewPriorityInput()
                    if (!userCancelled(priority)) {
                        pcbQueue.push(PCB(pid.toInt(), arrivalTime.toInt(), burstTime.toInt(), priority.toInt()))
                    }
                }
            }
        }
    }

    private fun pushProcessToFrontOfQueue() {
        val arrivalTime: String
        val burstTime: String
        val priority: String
        val pid = getNewPidInput()
        if (!userCancelled(pid)) {
            arrivalTime = getNewArrivalTimeInput()
            if (!userCancelled(arrivalTime)) {
                burstTime = getNewBurstTimeInput()
                if (!userCancelled(burstTime)) {
                    priority = getNewPriorityInput()
                    if (!userCancelled(priority)) {
                        pcbQueue.pushToFront(PCB(pid.toInt(), arrivalTime.toInt(), burstTime.toInt(), priority.toInt()))
                    }
                }
            }
        }
    }

    private fun pushProcessToQueueAfterPidX() {
        val newPid: String
        val arrivalTime: String
        val burstTime: String
        val priority: String
        val targetPid = getExistingPidInput()
        if (!userCancelled(targetPid)) {
            newPid = getNewPidInput()
            if (!userCancelled(newPid)) {
                arrivalTime = getNewArrivalTimeInput()
                if (!userCancelled(arrivalTime)) {
                    burstTime = getNewBurstTimeInput()
                    if (!userCancelled(burstTime)) {
                        priority = getNewPriorityInput()
                        if (!userCancelled(priority)) {
                            pcbQueue.insertContentAfter(PCB(newPid.toInt(), arrivalTime.toInt(), burstTime.toInt(), priority.toInt()), targetPid.toInt())
                        }
                    }
                }
            }
        }
    }

    private fun pushProcessToQueueBeforePidX() {
        val newPid: String
        val arrivalTime: String
        val burstTime: String
        val priority: String
        val targetPid = getExistingPidInput()
        if (!userCancelled(targetPid)) {
            newPid = getNewPidInput()
            if (!userCancelled(newPid)) {
                arrivalTime = getNewArrivalTimeInput()
                if (!userCancelled(arrivalTime)) {
                    burstTime = getNewBurstTimeInput()
                    if (!userCancelled(burstTime)) {
                        priority = getNewPriorityInput()
                        if (!userCancelled(priority)) {
                            pcbQueue.insertContentBefore(PCB(newPid.toInt(), arrivalTime.toInt(), burstTime.toInt(), priority.toInt()), targetPid.toInt())
                        }
                    }
                }
            }
        }
    }

    private fun popPCBQueue() {
        pcbQueue.pop()
    }

    private fun deleteProcessWithPidX() {
        val targetPid = getExistingPidInput()
        if (!userCancelled(targetPid)) {
            pcbQueue.deleteNodeWithPid(targetPid.toInt())
        }
    }

    private fun printPCBQueue() {
        pcbQueue.printQueue()
        pressAnyKeyToContinue()
    }

    private fun performFCFS() {
        val fcfs = FCFSSchedulingService(pcbQueue)
        fcfs.startFirstComeFirstServeProcessing()
    }

    private fun performNPP() {
        val npp = NPPSchedulingService(pcbQueue)
        npp.startNonPreemptivePriorityProcessing()
    }

    private fun performRR() {
        println("Please provide a time quantum (Integer > 0)")
        print("User: ")
        var quantumInput: String = readLine()!!
        while (!isValidQuantum(quantumInput)) {
            clearOutput()
            println("Invalid quantum provided, please use an Integer.")
            print("User: ")
            quantumInput = readLine()!!
        }
        val rr = RRSchedulingService(pcbQueue, quantumInput.toInt())
        rr.startRoundRobinProcessing()
    }

    private fun isValidQuantum(passedQuantum: String): Boolean {
        var isValid = false
        if (isStringNumeric(passedQuantum) && passedQuantum.toInt() > 0) {
            isValid = true
        }
        return isValid
    }

    private fun exitSantOS() {
        userContinue = false
        println("Thank you for using SantOS, have a nice day!")
    }

    private fun clearOutput() {
        println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n")
    }

    private fun pressAnyKeyToContinue() {
        println("Press any key to continue...")
        readLine()
        clearOutput()
    }

    private fun isStringNumeric(userInput: String): Boolean {
        var isNumeric = true

        try {
            parseInt(userInput)
        } catch (e: NumberFormatException) {
            isNumeric = false
        }
        return isNumeric
    }

    private fun isValidPriority(userInput: String): Boolean {
        var isValid = false
        if (isStringNumeric(userInput)) {
            if (userInput.toInt() in 1..4) {
                isValid = true
            }
        }
        return isValid
    }

    private fun getExistingPidInput(): String {
        print("Enter PID for PCB or select \"c\" to cancel: ")
        var pid = readLine()
        while ((!isStringNumeric(pid!!) || !pcbQueue.doesQueueContainPid(pid.toInt())) && !userCancelled(pid)) {
            if (!isStringNumeric(pid)) {
                clearOutput()
                println("Invalid Number, please enter a valid PID (For example: 1 or 123 or 7543) or select \"c\" to cancel:")
            } else if (!pcbQueue.doesQueueContainPid(pid.toInt())) {
                clearOutput()
                println("Invalid Number, PID does not exist in the queue, please enter an existing PID or select \"c\" to cancel:")
            } else {
                clearOutput()
                println("Invalid Number, try again or select \"c\" to cancel:")
            }
            pid = readLine()
            clearOutput()
        }
        return pid
    }

    private fun getNewPidInput(): String {
        print("Enter PID for PCB or select \"c\" to cancel: ")
        var pid = readLine()
        while ((!isStringNumeric(pid!!) || pcbQueue.doesQueueContainPid(pid.toInt())) && !userCancelled(pid)) {
            if (!isStringNumeric(pid)) {
                clearOutput()
                println("Invalid number, please enter a valid PID (For example: 1 or 123 or 4567) or select \"c\" to cancel:")
            } else if (pcbQueue.doesQueueContainPid(pid.toInt())) {
                clearOutput()
                println("Invalid number, PID already exists in queue, please enter a PID or select \"c\" to cancel:")
            } else {
                clearOutput()
                println("Invalid number, try again or select \"c\" to cancel:")
            }
            pid = readLine()
            clearOutput()
        }
        return pid
    }

    private fun getNewArrivalTimeInput(): String {
        print("Enter arrival time for PCB or select \"c\" to cancel: ")
        var arrivalTime = readLine()!!
        while (!isStringNumeric(arrivalTime) && !userCancelled(arrivalTime)) {
            if (!isStringNumeric(arrivalTime)) {
                clearOutput()
                println("Invalid number, please enter a valid arrival time (For example: 1 or 123 or 4567) or select \"c\" to cancel:")
            } else {
                clearOutput()
                println("Invalid number, try again or select \"c\" to cancel:")
            }
            arrivalTime = readLine()!!
            clearOutput()
        }
        return arrivalTime
    }

    private fun getNewBurstTimeInput(): String {
        print("Enter burst time for PCB or select \"c\" to cancel: ")
        var burstTime = readLine()!!
        while (!isStringNumeric(burstTime) && !userCancelled(burstTime)) {
            if (!isStringNumeric(burstTime)) {
                clearOutput()
                println("Invalid number, please enter a valid arrival time (For example: 1 or 123 or 4567) or select \"c\" to cancel:")
            } else {
                clearOutput()
                println("Invalid number, try again or select \"c\" to cancel:")
            }
            burstTime = readLine()!!
            clearOutput()
        }
        return burstTime
    }

    private fun getNewPriorityInput(): String {
        print("Enter priority for PCB or select \"c\" to cancel: ")
        var priority = readLine()!!
        while (!isValidPriority(priority) && !userCancelled(priority)) {
            if (!isValidPriority(priority)) {
                clearOutput()
                println("Invalid number, please enter a valid arrival time (1, 2, 3 or 4) or select \"c\" to cancel:")
            } else {
                clearOutput()
                println("Invalid number, try again or select \"c\" to cancel:")
            }
            priority = readLine()!!
            clearOutput()
        }
        return priority
    }


    private fun userCancelled(input: String): Boolean {
        var cancelled = false
        if (input == "c") {
            cancelled = true
        }
        return cancelled
    }

    private fun printSantOS() {
        println("   SSSSSSSSSSSSSSS                                             tttt               OOOOOOOOO        SSSSSSSSSSSSSSS \n" +
                " SS:::::::::::::::S                                         ttt:::t             OO:::::::::OO    SS:::::::::::::::S\n" +
                "S:::::SSSSSS::::::S                                         t:::::t           OO:::::::::::::OO S:::::SSSSSS::::::S\n" +
                "S:::::S     SSSSSSS                                         t:::::t          O:::::::OOO:::::::OS:::::S     SSSSSSS\n" +
                "S:::::S              aaaaaaaaaaaaa  nnnn  nnnnnnnn    ttttttt:::::ttttttt    O::::::O   O::::::OS:::::S            \n" +
                "S:::::S              a::::::::::::a n:::nn::::::::nn  t:::::::::::::::::t    O:::::O     O:::::OS:::::S            \n" +
                " S::::SSSS           aaaaaaaaa:::::an::::::::::::::nn t:::::::::::::::::t    O:::::O     O:::::O S::::SSSS         \n" +
                "  SS::::::SSSSS               a::::ann:::::::::::::::ntttttt:::::::tttttt    O:::::O     O:::::O  SS::::::SSSSS    \n" +
                "    SSS::::::::SS      aaaaaaa:::::a  n:::::nnnn:::::n      t:::::t          O:::::O     O:::::O    SSS::::::::SS  \n" +
                "       SSSSSS::::S   aa::::::::::::a  n::::n    n::::n      t:::::t          O:::::O     O:::::O       SSSSSS::::S \n" +
                "            S:::::S a::::aaaa::::::a  n::::n    n::::n      t:::::t          O:::::O     O:::::O            S:::::S\n" +
                "            S:::::Sa::::a    a:::::a  n::::n    n::::n      t:::::t    ttttttO::::::O   O::::::O            S:::::S\n" +
                "SSSSSSS     S:::::Sa::::a    a:::::a  n::::n    n::::n      t::::::tttt:::::tO:::::::OOO:::::::OSSSSSSS     S:::::S\n" +
                "S::::::SSSSSS:::::Sa:::::aaaa::::::a  n::::n    n::::n      tt::::::::::::::t OO:::::::::::::OO S::::::SSSSSS:::::S\n" +
                "S:::::::::::::::SS  a::::::::::aa:::a n::::n    n::::n        tt:::::::::::tt   OO:::::::::OO   S:::::::::::::::SS \n" +
                " SSSSSSSSSSSSSSS     aaaaaaaaaa  aaaa nnnnnn    nnnnnn          ttttttttttt       OOOOOOOOO      SSSSSSSSSSSSSSS   ")
    }

    private fun loadSampleQueue() {
        pcbQueue.push(PCB(2710, 8, 6, 2))
        pcbQueue.push(PCB(2720, 8, 2, 1))
        pcbQueue.push(PCB(2730, 0, 8, 1))
        pcbQueue.push(PCB(2740, 2, 5, 3))
        pcbQueue.push(PCB(2750, 6, 10, 4))
    }
}
