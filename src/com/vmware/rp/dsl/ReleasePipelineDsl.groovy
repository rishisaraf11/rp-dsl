package com.vmware.rp.dsl

import groovy.xml.MarkupBuilder
import org.apache.tools.ant.taskdefs.Manifest

/**
 * Created by rsaraf on 10/30/2014.
 */
class ReleasePipelineDsl {
    String name
    String description
    def sections = []

    def static make(closure) {
        ReleasePipelineDsl rp = new ReleasePipelineDsl()
        closure.delegate = rp
        closure()
    }

    def name(String name) {
        this.name = name
    }

    def  description(String desc) {
        this.description = desc
    }

    /**
     * When a method is not recognized, assume it is a title for a new section. Create a simple
     * object that contains the method name and the parameter which is the body.
     */
    def methodMissing(String methodName, args) {
        def section = new Manifest.Section(title: methodName, body: args[0])
        sections << section
    }

    /**
     * 'get' methods get called from the dsl by convention. Due to groovy closure delegation,
     * we had to place MarkUpBuilder and StringWrite code in a static method as the delegate of the closure
     * did not have access to the system.out
     */
    def getXml() {
        doXml(this)
    }


/**
 * Use markupBuilder to create a customer xml output
 */
    private static doXml(ReleasePipelineDsl rp) {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.rp() {
            name(rp.name)
            description(rp.description)
            // cycle through the stored section objects to create an xml tag
            for (s in rp.sections) {
                "$s.title"(s.body)
            }
        }
        println writer
    }

    def getJson() {
        doJson(this)
    }

    private static doJson(ReleasePipelineDsl rp) {
        def jsonBuilder = new groovy.json.JsonBuilder()
        jsonBuilder.rp(
                name: rp.name,
                description: rp.description
        )
        println(jsonBuilder.toPrettyString())
    }


}
