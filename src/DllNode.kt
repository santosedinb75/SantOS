class DllNode {
    var pcb: PCB = PCB(-1, -1, -1, -1)
    var prevNode: DllNode? = null
    var nextNode: DllNode? = null

    constructor()

    constructor(passedPcb: PCB) {
        this.pcb = passedPcb
        this.nextNode = null
    }

    constructor(p_NextNode: DllNode?) {
        this.nextNode = p_NextNode
    }

    constructor(passedPcb: PCB, p_NextNode: DllNode?) {
        this.pcb = passedPcb
        this.nextNode = p_NextNode
    }

    constructor(p_PrevNode: DllNode?, passedPcb: PCB, p_NextNode: DllNode?) {
        this.pcb = passedPcb
        this.prevNode = p_PrevNode
        this.nextNode = p_NextNode
    }
}