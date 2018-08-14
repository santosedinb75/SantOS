class FCFSSchedulingService(passedQueue: DllQueue) {
    //First Come First Serve Scheduling Service
    private var fcfsQueue = passedQueue
    private var detailList = mutableListOf<DetailNode>()
    private var pcbList = mutableListOf<PCB>()

    fun startFirstComeFirstServeProcessing() {
        pcbList = fcfsQueue.getCopyOfQueueAsList().sortedWith(compareBy(PCB::arrivalTime, PCB::burstTime)).toMutableList()
        var totalTicks = 0

//        println("Scheduled PID order:")
//        for (pcb in pcbList) {
//            println("${pcb.pid}")
//        }
//        println("------")

        while (pcbList.size > 0) {
            if (pcbList.first().arrivalTime <= totalTicks) {
                val newDetails = DetailNode(pcbList.first().arrivalTime, totalTicks, pcbList.first().pid, pcbList.first().burstTime, (totalTicks + pcbList.first().burstTime))
                totalTicks = newDetails.stop
                pcbList.removeAt(0)
                detailList.add(newDetails)
            } else {
                totalTicks += 1
            }
        }
        println("Execution:")
        printFCFS()
    }

    private fun printFCFS() {
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

    private fun pressAnyKeyToContinue() {
        println("Press any key to continue...")
        readLine()
    }

    private fun generateStatistics() {
        var turnaroundTime = 0.0
        var waitTime = 0.0
        println("\nFirst Come First Serve Statistics:")
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
}