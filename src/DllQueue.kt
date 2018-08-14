class DllQueue {
    private var tail = DllNode()
    private var head = DllNode()
    private var size = -1

    fun push(passedPCB: PCB) {
        if (tail.nextNode == null && head.nextNode == null) {
            tail.nextNode = DllNode(passedPCB)
            head.nextNode = tail.nextNode
        } else {
            tail.nextNode = DllNode(passedPCB, tail.nextNode)
            tail.nextNode?.nextNode?.prevNode = tail.nextNode
        }
        size++
    }

    fun pushToFront(passedPCB: PCB) {
        if (tail.nextNode == null && head.nextNode == null) {
            tail.nextNode = DllNode(passedPCB)
            head.nextNode = tail.nextNode
        } else {
            head.nextNode = DllNode(head.nextNode, passedPCB, null)
            head.nextNode?.prevNode?.nextNode = head.nextNode
        }
        size++
    }

    fun insertContentAfter(passedPCB: PCB, targetPid: Int) {
        if (tail.nextNode == null && head.nextNode == null) {
            println("Queue is empty, cannot locate target node.")
        } else if (tail.nextNode?.pcb?.pid == targetPid) {
            val newNode = DllNode(null, passedPCB, tail.nextNode)
            newNode.nextNode?.prevNode = newNode
            tail.nextNode = newNode
            size++
        } else {
            val seeker = DllNode(tail.nextNode?.nextNode)
            while (seeker.nextNode?.pcb?.pid != targetPid && seeker.nextNode != null) {
                seeker.nextNode = seeker.nextNode?.nextNode
            }
            if (seeker.nextNode == null) {
                println("Target node with $targetPid not found in queue.")
            } else {
                val newNode = DllNode(seeker.nextNode?.prevNode, passedPCB, seeker.nextNode)
                newNode.nextNode?.prevNode = newNode
                newNode.prevNode?.nextNode = newNode
                size++
            }
        }
    }

    fun insertContentBefore(passedPCB: PCB, targetPid: Int) {
        if (tail.nextNode == null && head.nextNode == null) {
            println("Queue is empty, cannot locate target node.")
        } else if (head.nextNode?.pcb?.pid == targetPid) {
            val newNode = DllNode(head.nextNode, passedPCB, null)
            newNode.prevNode?.nextNode = newNode
            head.nextNode = newNode
            size++
        } else {
            val seeker = DllNode(head.nextNode?.prevNode)
            while (seeker.nextNode?.pcb?.pid != targetPid && seeker.nextNode != null) {
                seeker.nextNode = seeker.nextNode?.prevNode
            }
            if (seeker.nextNode == null) {
                println("Target node with $targetPid not found in queue.")
            } else {
                val newNode = DllNode(seeker.nextNode, passedPCB, seeker.nextNode?.nextNode)
                newNode.nextNode?.prevNode = newNode
                newNode.prevNode?.nextNode = newNode
                size++
            }
        }
    }

    fun pop() {
        if (tail.nextNode == null && head.nextNode == null) {
            println("Queue is empty, nothing to pop.")
        } else if (tail.nextNode?.nextNode == null) {
            tail.nextNode = null
            head.nextNode = null
            size--
        } else {
            head.nextNode = head.nextNode?.prevNode
            head.nextNode?.nextNode = null
            size--
        }
    }

    fun deleteNodeWithPid(targetPid: Int) {
        if (tail.nextNode == null) {
            println("Queue empty, nothing to delete.")
        } else if (tail.nextNode?.pcb?.pid == targetPid && tail.nextNode == head.nextNode) {
            tail.nextNode = null
            head.nextNode = null
            size--
        } else if (tail.nextNode?.pcb?.pid == targetPid) {
            tail.nextNode = tail.nextNode?.nextNode
            tail.nextNode?.prevNode = null
            size--
        } else if (head.nextNode?.pcb?.pid == targetPid) {
            head.nextNode = head.nextNode?.prevNode
            head.nextNode?.nextNode = null
            size--
        } else {
            val seeker = DllNode(tail.nextNode?.nextNode)
            while (seeker.nextNode?.pcb?.pid != targetPid && seeker.nextNode != null) {
                seeker.nextNode = seeker.nextNode?.nextNode
            }
            if (seeker.nextNode == null) {
                println("Node with $targetPid not found in queue")
            } else {
                seeker.nextNode?.nextNode?.prevNode = seeker.nextNode?.prevNode
                seeker.nextNode?.prevNode?.nextNode = seeker.nextNode?.nextNode
                size--
            }
        }
    }

    fun printQueue() {
        print("null <- [PN|Tail|NN] -> ")
        var tmp = tail.nextNode
        while (tmp != null) {
            print(" <- [PN|PID:${tmp.pcb.pid}, AT: ${tmp.pcb.arrivalTime}, BT: ${tmp.pcb.burstTime}, Pr:${tmp.pcb.priority}|NN] -> ")
            tmp = tmp.nextNode
        }
        println(" <- [PN|Head|NN] -> null")
    }

    fun doesQueueContainPid(targetPid: Int): Boolean {
        var queueContainsContent = false
        var seeker = tail.nextNode
        while (seeker != null && !queueContainsContent) {
            if (seeker.pcb.pid == targetPid) {
                queueContainsContent = true
            }
            seeker = seeker.nextNode
        }
        return queueContainsContent
    }

    fun getCopyOfQueueAsList(): MutableList<PCB> {
        val list = mutableListOf<PCB>()
        var seeker = tail.nextNode
        while (seeker != null) {
            list.add(PCB(seeker.pcb.pid, seeker.pcb.arrivalTime, seeker.pcb.burstTime, seeker.pcb.priority))
            seeker = seeker.nextNode
        }
        return list
    }
}