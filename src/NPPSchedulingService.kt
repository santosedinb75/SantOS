class NPPSchedulingService(passedQueue: DllQueue) {
    //Non-Preemptive Priority Scheduling Service
    private var fcfsQueue = passedQueue
    private var detailList = mutableListOf<DetailNode>()

    fun startNonPreemptivePriorityProcessing() {
        val pcbList = fcfsQueue.getCopyOfQueueAsList().sortedWith(compareBy(PCB::arrivalTime, PCB::priority, PCB::burstTime)).toMutableList()
        var totalTicks = 0

//        println("Scheduled PID order:")
//        for (pcb in pcbList) {
//            println("${pcb.pid}")
//        }
//        println("------")

        var currentNext: PCB
        while (pcbList.size > 0) {
            if (pcbList.first().arrivalTime <= totalTicks) {
                currentNext = pcbList.first()
                for (i in 1 until pcbList.size) {
                    if (pcbList[i].arrivalTime <= totalTicks) {
                        if (pcbList[i].priority < currentNext.priority) {
                            currentNext = pcbList[i]
                        } else if (pcbList[i].priority == currentNext.priority) {
                            if (pcbList[i].arrivalTime < currentNext.arrivalTime) {
                                currentNext = pcbList[i]
                            }
                        }
                    }
                }
                val newDetails = DetailNode(currentNext.arrivalTime, totalTicks, currentNext.pid, currentNext.burstTime, (totalTicks + currentNext.burstTime))
                totalTicks = newDetails.stop
                pcbList.remove(currentNext)
                detailList.add(newDetails)
            } else {
                totalTicks += 1
            }
        }
        println("Execution:")
        printNPP()
    }

    private fun printNPP() {
        for (deets in detailList) {
            if (detailList.last() == deets) {
                println("T:${deets.start} [${deets.pid}(${deets.burst})] T:${deets.stop}")
            } else {
                print("T:${deets.start} [${deets.pid}(${deets.burst})] ")
            }
        }
        generateStatistics()
        pressAnyKeyToContinue()
    }

    private fun generateStatistics() {
        var turnaroundTime = 0.0
        var waitTime = 0.0
        println("\nNon Preemptive Priority Statistics:")
        for (pcb in detailList) {
            turnaroundTime += (pcb.stop - pcb.arrivalTime)
            waitTime += (pcb.start - pcb.arrivalTime)
            println("PID: ${pcb.pid}, Turnaround Time: ${pcb.stop - pcb.arrivalTime}, Wait Time: ${pcb.start - pcb.arrivalTime}")

        }
        turnaroundTime = (turnaroundTime / fcfsQueue.getCopyOfQueueAsList().size)
        waitTime = (waitTime / fcfsQueue.getCopyOfQueueAsList().size)
        println("\nAverage Turnaround Time: $turnaroundTime")
        println("Average Wait Time: $waitTime\n")
    }

    private fun pressAnyKeyToContinue() {
        println("Press any key to continue...")
        readLine()
    }
}