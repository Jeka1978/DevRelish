package com.epam.devrelish

class TestClassWithDependency {
    @InjectByType
    val testDependency: TestDependency? = null
}
