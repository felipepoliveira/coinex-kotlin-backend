package io.felipepoliveira.coinex.test.unit.mocks.dao

class MockedObject<T>(
    private val seeder: () -> T,
) {
    /**
     * Store a reference that will be used to compare the values
     */
    val reference: T = seeder()

    /**
     * Create a new instance of the mocked object
     */
    fun mock(): T = seeder()
}