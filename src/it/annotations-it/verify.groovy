/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Verify the OSGI-INF xml files generated by the bnd plugin based
// on our annotations

def INVALID_CONFIG = 'Configuration is invalid'

// Utility functions
def getXml(filename) {
    return new XmlSlurper().parse(new File(basedir, "target/classes/OSGI-INF/" + filename))
}

def assertTypedAttribute(xml, name, type, value) {
    def errorMsg = name + " " + type + " attribute does not match " + value
    assert(xml.property.find{ it.@name == name && it.@type == type && it.@value == value }) : errorMsg
}

def assertProperty(xml, name, type, String [] expected) {
    def linesFromXml = xml.property.find{ it.@name == name && it.@type == type }.toString().split('\n')
    expected.each {
        def errorMsg = name + " " + type + " does not contain " + it
        assert(linesFromXml.contains(it)) : errorMsg
    }
    assert(linesFromXml.length == expected.length)
}

def assertNoProperty(xml, String [] names) {
    names.each {
        def name = it
        xml.property.find{ it.@name == name }.each {
            assert(false) : "did not expect " + it + " for " + name
        }
    }
}

// The actual tests
{
    def xml=getXml("org.apache.sling.servlets.annotations.services.PathBoundService.xml")
    assertProperty(xml, 'sling.servlet.paths', 'String', 'PathBoundServicePath')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths.strict',
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.services.PathBoundStrictAllOptionsService.xml")
    assertProperty(xml, 'sling.servlet.paths', 'String', 'P1', 'P2')
    assertProperty(xml, 'sling.servlet.selectors', 'String', 'S1', 'S2')
    assertProperty(xml, 'sling.servlet.extensions', 'String', 'E1', 'E2')
    assertProperty(xml, 'sling.servlet.methods', 'String', 'M1', 'M2')
    assertTypedAttribute(xml, 'sling.servlet.paths.strict', 'Boolean', 'true')
    assertNoProperty(xml,
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.services.PathBoundStrictJustPathService.xml")
    assertProperty(xml, 'sling.servlet.paths', 'String', 'JustThePath')
    assertTypedAttribute(xml, 'sling.servlet.paths.strict', 'Boolean', 'true')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservletfilters.SimpleServletFilter.xml")
    assertProperty(xml, 'sling.filter.scope', 'String', 'REQUEST')
    assertProperty(xml, 'sling.filter.pattern', 'String', '')
    assertProperty(xml, 'sling.filter.resource.pattern', 'String', '')
    assertProperty(xml, 'sling.filter.request.pattern', 'String', '')
    assertProperty(xml, 'sling.filter.suffix.pattern', 'String', '')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.servlet.selectors',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.BaseResourceTypeBoundServletWithSelectors.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', 'sling/testservlets/BaseResourceTypeBoundServletWithSelectors')
    assertProperty(xml, 'sling.servlet.selectors', 'String', 'sel1', 'sel2')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.InheritingResourceTypeBoundServlet.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', 'sling/testservlets/InheritingResourceTypeBoundServlet')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/testservlets/BaseResourceTypeBoundServletWithSelectors')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.PathBoundServlet.xml")
    assertProperty(xml, 'sling.servlet.paths', 'String', '/bin/PathBoundServlet')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths.strict',
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.PathBoundServletWithPrefix.xml")
    assertProperty(xml, 'sling.servlet.paths', 'String', 'PathBoundServletWithPrefix')
    assertTypedAttribute(xml, 'sling.servlet.prefix', 'String', '/bin/')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths.strict',
        'sling.servlet.resourceTypes',
        'sling.servlet.resourceSuperType',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.ResourceTypeBoundServlet.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', '/apps/sling/testservlets/ResourceTypeBoundServlet')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.ResourceTypeBoundServletWithExtension.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', '/apps/sling/testservlets/ResourceTypeBoundServletWithExtension')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertProperty(xml, 'sling.servlet.extensions', 'String', 'ext1', 'ext2')
    assertProperty(xml, 'sling.servlet.methods', 'String', 'PUT', 'GET')
    assertNoProperty(xml,
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.ResourceTypeBoundServletWithMethods.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', '/apps/sling/testservlets/ResourceTypeBoundServletWithMethods')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertProperty(xml, 'sling.servlet.methods', 'String', 'PUT', 'POST')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.ResourceTypeBoundServletWithPrefix.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', 'ResourceTypeBoundServletWithPrefix')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertTypedAttribute(xml, 'sling.servlet.prefix', 'String', '/apps/sling/testservlets/')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.servlet.selectors',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',

    )
}

{
    def xml=getXml("org.apache.sling.servlets.annotations.testservlets.ResourceTypeBoundServletWithSelectors.xml")
    assertProperty(xml, 'sling.servlet.resourceTypes', 'String', '/apps/sling/testservlets/ResourceTypeBoundServletWithSelectors')
    assertTypedAttribute(xml, 'sling.servlet.resourceSuperType', 'String', 'sling/bundle/resource')
    assertProperty(xml, 'sling.servlet.selectors', 'String', 'selector1.selector2','selector3')
    assertNoProperty(xml,
        'sling.servlet.extensions',
        'sling.servlet.methods',
        'sling.servlet.paths',
        'sling.servlet.paths.strict',
        'sling.filter.scope',
        'sling.filter.pattern',
        'sling.filter.resource.pattern',
        'sling.filter.request.pattern',
        'sling.filter.suffix.pattern',
        'sling.servlet.prefix',
    )
}

return true
