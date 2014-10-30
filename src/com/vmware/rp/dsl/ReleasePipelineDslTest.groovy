package com.vmware.rp.dsl

/**
 * Created by rsaraf on 10/30/2014.
 */
class ReleasePipelineDslTest extends GroovyTestCase {

    void testDslUsage_outputXml() {
        ReleasePipelineDsl.make {
            name "SampleRp"
            description "This is to test dsl"
            json
        }
    }
}
