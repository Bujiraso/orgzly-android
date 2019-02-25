package com.orgzly.android.ui.notes.query.agenda

import com.orgzly.android.db.entity.Note
import com.orgzly.android.db.entity.NotePosition
import com.orgzly.android.db.entity.NoteView
import org.joda.time.Instant
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class AgendaItemsTest {

    fun makeNoteView(schedTime: Long): NoteView {
        val notePosition = NotePosition(123, 0, 0, 0, 0, 0, false, 0)
        val note = Note(1, 0, null, "", null, null, null, null, 0, 0, null, null, null, notePosition)
        return NoteView(note, null, null, null, null, schedTime, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, " <2015-03-13 Fri 13:15 ++1y --2d>", null, 0, 0, "MyBook")
    }

    @Before
    fun setup() {
        //TODO: Extract common portion of the various tests
    }

    @Test
    fun sevenDayQuery_itemsOutOfOrder_agendaSortedByScheduledTime() {
        val baseTime = System.nanoTime()
        val noteView1 = makeNoteView(baseTime)
        val noteView2 = makeNoteView(baseTime - 100)
        val noteView3 = makeNoteView(baseTime + 1000)

        val notes = listOf(noteView1, noteView2, noteView3)

        val queryString = ".it.done ad.7"

        val idMap = mutableMapOf<Long, Long>()
        val list = AgendaItems.getList(notes, queryString, idMap)

        assertFalse(list.isEmpty())
        var lastDate: Instant = when (val first = list.get(0)) {
            is AgendaItem.Note -> {
                Instant.ofEpochMilli(first.note.scheduledTimeTimestamp!!)
            }
            is AgendaItem.Divider -> first.day.toInstant()
        }

        for (agendaItem: AgendaItem in list) {
            when (agendaItem) {
                is AgendaItem.Note -> {
                    val currentDate = Instant.ofEpochMilli(agendaItem.note.scheduledTimeTimestamp!!)
                    Assert.assertTrue(!currentDate.isBefore(lastDate))
                    lastDate = currentDate
                }
            }
        }
    }
}