package de.codepitbull.threaddumps

/**
 * @author Jochen Mader.
 */
@groovy.transform.Immutable class ThreadDumpElement {
    String name
    Boolean daemon
    Integer prio
    String tid
    String nid
    String doing
    String state
    String stack
    String cleanStack
    String fileName

    @Override
    public String toString() {
        return "ThreadDumpElement{" +
                "name='" + name + '\'' +
                ", daemon=" + daemon +
                ", prio=" + prio +
                ", tid='" + tid + '\'' +
                ", nid='" + nid + '\'' +
                ", doing='" + doing + '\'' +
                ", state='" + state + '\'' +
                ", fileName='" + fileName + '\'' +
                ", stack='" + stack + '\'' +
                ", cleanStack='" + cleanStack + '\'' +
                '}';
    }
}
