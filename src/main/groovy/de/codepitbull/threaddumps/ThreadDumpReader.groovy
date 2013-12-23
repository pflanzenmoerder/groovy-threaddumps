package de.codepitbull.threaddumps

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import static groovyx.gpars.GParsPool.*;


dir = new File("/Users/jmader/Documents/3_development/playground/groovy-threaddumps/src/main/resources/")

def pars = { file ->
    def ret = new ArrayList<ThreadDumpElement>()
    try {
        def matcher = file.text =~ /(?ms)^".*?^$/

        matcher.each() {
            def matched = it =~ /(?ms)^"(.*?)" (daemon )?prio=([0-9]*) tid=(.*?) nid=(.*?) (.*?)\n(   java\.lang\.Thread\.State: (.*?)\n)?(.*)$/
            matched.matches()
            ThreadDumpElement threadDumpElement = new ThreadDumpElement(
                    fileName: file.getName(),
                    name: matched.group(1),
                    daemon: matched.group(2)!=null,
                    prio: matched.group(3).toInteger(),
                    tid: matched.group(4),
                    nid: matched.group(5),
                    doing: matched.group(6),
                    state: matched.group(8),
                    stack: matched.group(9),
                    cleanStack: (matched.group(9)=~ /<(0x.*)>/).replaceAll("REMOVED"))
            ret.add(threadDumpElement)
        };
    }
    catch (Exception e ) {
        println(file.getName())
        e.printStackTrace()
    }

    return ret
}

pool = Executors.newFixedThreadPool(5)

defer = { c -> pool.submit(c as Callable) }

futureList = new ArrayList<Future>()
dir.eachFile { file ->
    futureList.add(defer{ pars(file) })
}

List<ThreadDumpElement> elements = new ArrayList<ThreadDumpElement>()
futureList.each {elements.addAll(it.get())}
pool.shutdown()

withPool(20) {
    return elements
            .parallel
            .map{[it.cleanStack,1]}
            .combine(0) {sum, value -> sum + value}
}.each {entry -> println(entry.getValue()+" => "+entry.getKey())}