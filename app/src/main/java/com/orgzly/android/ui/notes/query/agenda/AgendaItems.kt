package com.orgzly.android.ui.notes.query.agenda

import com.orgzly.android.db.entity.NoteView
import com.orgzly.android.query.Query
import com.orgzly.android.query.user.InternalQueryParser
import com.orgzly.android.util.AgendaUtils
import org.joda.time.DateTime
import java.util.Collections
import kotlin.Comparator
import kotlin.collections.HashSet

object AgendaItems {
    data class ExpandableOrgRange(val range: String?, val overdueToday: Boolean)

    fun getList(
            notes: List<NoteView>, queryString: String?, idMap: MutableMap<Long, Long>
    ): List<AgendaItem> {

        return if (queryString != null) {
            val parser = InternalQueryParser()

            val query = parser.parse(queryString)

            getList(notes, query, idMap)

        } else {
            listOf()
        }
    }

    fun getList(
            notes: List<NoteView>, query: Query, item2databaseIds: MutableMap<Long, Long>
    ): List<AgendaItem> {

        return getList(notes, item2databaseIds, query.options.agendaDays)
    }

    private fun getList(
            notes: List<NoteView>,
            item2databaseIds: MutableMap<Long, Long>,
            agendaDays: Int
    ): List<AgendaItem> {

        item2databaseIds.clear()

        var index = 1L

        val now = DateTime.now().withTimeAtStartOfDay()

        // Create day buckets
        val dayBuckets = (0 until agendaDays)
                .map { i -> now.plusDays(i) }
                .associateBy(
                        { it.millis },
                        { mutableListOf<AgendaItem>(AgendaItem.Divider(index++, it)) })

        val addedPlanningTimes = HashSet<Long>()

        notes.forEach { note ->
            fun addInstances(timeType: Int, timeString: String?, overdueToday: Boolean) {
                // Expand each note if it has a repeater or is a range
                val times = AgendaUtils.expandOrgDateTime(
                        arrayOf(ExpandableOrgRange(timeString, overdueToday)),
                        now,
                        agendaDays)

                // Add each note instance to its day bucket
                times.forEach { time ->
                    val bucketKey = time.withTimeAtStartOfDay().millis

                    dayBuckets[bucketKey]?.let {
                        it.add(AgendaItem.Note(index, note, timeType))
                        item2databaseIds[index] = note.note.id
                        index++
                    }
                }
            }

            // Add planning times for a note only once
            if (!addedPlanningTimes.contains(note.note.id)) {
                addInstances(1, note.scheduledRangeString, true)
                addInstances(2, note.deadlineRangeString, true)

                addedPlanningTimes.add(note.note.id)
            }

            // Add each note's event
            addInstances(3, note.eventString, false)
        }

        sortAgendaItemsByTimestamp(dayBuckets)
        return dayBuckets.values.flatten() // FIXME
    }

    private fun sortAgendaItemsByTimestamp(dayBuckets: Map<Long, MutableList<AgendaItem>>) {
        for ((_, value) in dayBuckets) {
            val ff = Comparator<AgendaItem> { o1, o2 ->
                val firstIsDivider = o1 is AgendaItem.Divider
                val secondIsDivider = o2 is AgendaItem.Divider
                if (firstIsDivider || secondIsDivider) {
                    return@Comparator if (firstIsDivider && secondIsDivider) 0 else -1
                }

                val firstTimestamp = (o1 as AgendaItem.Note).note.scheduledTimeTimestamp
                val secondTimestamp = (o2 as AgendaItem.Note).note.scheduledTimeTimestamp
                val missingFirstTimestamp = firstTimestamp == null
                val missingSecondTimestamp = secondTimestamp == null
                if (missingFirstTimestamp || missingSecondTimestamp) {
                    return@Comparator if (missingFirstTimestamp && missingSecondTimestamp) 0 else -1
                }
                if (firstTimestamp == secondTimestamp) {
                    0
                } else {
                    if (firstTimestamp!! < secondTimestamp!!) -1 else 1
                }
            }
            Collections.sort(value, ff)
        }
    }
}
