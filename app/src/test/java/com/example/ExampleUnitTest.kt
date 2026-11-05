package com.example

import com.example.data.model.NicheType
import com.example.data.model.SubscriberStatus
import com.example.data.repository.SaaSRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRepositoryInitialization() {
        val repo = SaaSRepository()
        assertNotNull(repo.currentNiche.value)
        assertEquals(NicheType.AGENCY_RETAINER, repo.currentNiche.value)
        assertTrue(repo.plans.value.isNotEmpty())
        assertTrue(repo.subscribers.value.isNotEmpty())
        assertTrue(repo.auditCriteria.value.isNotEmpty())
    }

    @Test
    fun testAddSubscriberAndInvoicing() {
        val repo = SaaSRepository()
        val initialCount = repo.subscribers.value.size
        val plan = repo.plans.value.first()

        repo.addSubscriber("Test User", "Test Co", "test@test.com", plan)

        assertEquals(initialCount + 1, repo.subscribers.value.size)
        val added = repo.subscribers.value.first()
        assertEquals("Test User", added.name)
        assertEquals(SubscriberStatus.ACTIVE, added.status)
        assertEquals(plan.monthlyPrice, added.mrrAmount, 0.01)
    }

    @Test
    fun testSwitchNiche() {
        val repo = SaaSRepository()
        repo.switchNiche(NicheType.AI_MICRO_TOOL)
        assertEquals(NicheType.AI_MICRO_TOOL, repo.currentNiche.value)
        assertTrue(repo.plans.value.any { it.name.contains("AI") || it.name.contains("Workflow") || it.name.contains("Hobbyist") })
    }

    @Test
    fun testAuditCriteriaScores() {
        val repo = SaaSRepository()
        val criteria = repo.auditCriteria.value
        assertEquals(6, criteria.size)
        criteria.forEach { crit ->
            assertTrue(crit.score >= 95)
            assertEquals("PASSED", crit.statusBadge)
        }
    }
}
