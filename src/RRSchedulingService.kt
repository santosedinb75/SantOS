class RRSchedulingService(passedQueue: DllQueue, passedQuantum: Int) {
    //Round Robin Scheduling
    private var fcfsQueue: DllQueue = passedQueue
    private var totalTicks: Int
    private var quantum: Int = passedQuantum
    private var pcbList: MutableList<PCB> = mutableListOf()
    private var toProcessList: MutableList<PCB> = mutableListOf()
    private var detailList: MutableList<DetailNode> = mutableListOf()

    init {
        totalTicks = 0
        pcbList = fcfsQueue.getCopyOfQueueAsList().sortedWith(compareBy(PCB::arrivalTime, PCB::burstTime)).toMutableList()
    }

    fun startRoundRobinProcessing() {
//        println("Scheduled PID order:")
//        for (pcb in pcbList) {
//            println("${pcb.pid}")
//        }
//        println("------")

        var doneProcessing = false
        val completedProcessingList = mutableListOf<PCB>()
        while (!doneProcessing) {
            if (!pcbList.isEmpty()) { //bring all pcbs into the toProcessList if they've arrived at or before this tick
                checkForNewArrivals()
            }
            if (!toProcessList.isEmpty()) {
                var tmpToProcessList = toProcessList.toMutableList()
                for (pcb in tmpToProcessList) {
                    if (pcb.burstTime > 0) {
                        var timeToProcess: Int = quantum
                        if (pcb.burstTime <= quantum) {
                            timeToProcess = pcb.burstTime
                        }
                        val newDetails = DetailNode(pcb.arrivalTime, totalTicks, pcb.pid, timeToProcess, (totalTicks + timeToProcess))
                        totalTicks = newDetails.stop
                        detailList.add(newDetails)
                        pcb.burstTime -= timeToProcess
                        if (!pcbList.isEmpty()) {
                            checkForNewArrivals()
                            if (!tmpToProcessList.containsAll(toProcessList)) { //Sync up toProcessList with tmpToProcessList
                                tmpToProcessList = toProcessList
                            }
                        }
                        if (pcb.burstTime <= 0) {
                            completedProcessingList.add(pcb)
                        }
                    } else if (pcb.burstTime <= 0) {
                        completedProcessingList.add(pcb)
                    }
                }
                toProcessList = tmpToProcessList
            } else {
                totalTicks += 1
            }
            for (pcb in completedProcessingList) {
                if (toProcessList.contains(pcb)) {
                    toProcessList.remove(pcb)
                }
            }
            if (pcbList.isEmpty() && toProcessList.isEmpty()) {
                doneProcessing = true
            }
        }

        println("Execution:")
        printRR()
    }

    private fun checkForNewArrivals() {
        val tmpList = pcbList.toMutableList()
        for (pcb in tmpList) {
            if (pcb.arrivalTime <= totalTicks) {
                toProcessList.add(pcb)
                pcbList.remove(pcb)
            }
        }
    }

    private fun printRR() {
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
        var avgTurnaroundTime = 0.0
        var avgWaitTime = 0.0
        val detailMap = mutableMapOf<Int, CalcTATandWT>()
        println("\nRound Robin Statistics:")
        for (pcb in fcfsQueue.getCopyOfQueueAsList()) {
            detailMap[pcb.pid] = CalcTATandWT()
        }
        for (deets in detailList) {
            //calculate wait time
            if (detailMap[deets.pid]!!.waitTime == 0) {
                detailMap[deets.pid]!!.waitTime = detailMap[deets.pid]!!.waitTime + (deets.start - deets.arrivalTime)
            } else {
                detailMap[deets.pid]!!.waitTime = detailMap[deets.pid]!!.waitTime + (deets.start - detailMap[deets.pid]!!.lastDetailNode.stop)
            }
            detailMap[deets.pid]!!.lastDetailNode = deets
        }

        for (x in detailMap) {
            x.value.turnAroundTime = x.value.lastDetailNode.stop - x.value.lastDetailNode.arrivalTime
            println("PID: ${x.value.lastDetailNode.pid}, Turnaround Time: ${x.value.turnAroundTime}, Wait Time: ${x.value.waitTime}")
            avgTurnaroundTime += x.value.turnAroundTime
            avgWaitTime += x.value.waitTime
        }

        avgTurnaroundTime /= fcfsQueue.getCopyOfQueueAsList().size
        avgWaitTime /= fcfsQueue.getCopyOfQueueAsList().size
        println("\nAverage Turnaround Time: $avgTurnaroundTime")
        println("Average Wait Time: $avgWaitTime\n")
    }

    private fun pressAnyKeyToContinue() {
        println("Press any key to continue...")
        readLine()
    }
}