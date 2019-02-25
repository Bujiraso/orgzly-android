package com.orgzly.android

import com.orgzly.android.espresso.*
import com.orgzly.android.misc.*
import com.orgzly.android.query.*
import com.orgzly.android.reminders.*
import com.orgzly.android.repos.*
import com.orgzly.android.ui.*
import com.orgzly.android.ui.notes.query.agenda.AgendaItemsTest
import com.orgzly.android.uiautomator.ListWidgetTest
import com.orgzly.android.usecase.*
import com.orgzly.android.util.*
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.Suite

@Ignore("Test suite")
@RunWith(Suite::class)
@Suite.SuiteClasses(
        // tests for com.orgzly.android.espresso
        ActionModeTest::class,
        AgendaFragmentTest::class,
        BookChooserActivityTest::class,
        BookPrefaceTest::class,
        BooksSortOrderTest::class,
        BooksTest::class,
        BookTest::class,
        CreatedAtPropertyTest::class,
        ExternalLinksTest::class,
        InternalLinksTest::class,
        MiscTest::class,
        NewNoteTest::class,
        NoteEventsTest::class,
        NoteFragmentTest::class,
        QueryFragmentTest::class,
        ReposActivityTest::class,
        SavedSearchesFragmentTest::class,
        SettingsChangeTest::class,
        SettingsFragmentTest::class,
        ShareActivityTest::class,
        SyncingTest::class,

        // tests for com.orgzly.android.misc
        BookNameTest::class,
        BookParsingTest::class,
        CreatedAtPropertyTest::class,
        DataTest::class,
        SettingsTest::class,
        StateChangeTest::class,
        StructureTest::class,
        UriTest::class,

        // tests for com.orgzly.android.query
        QueryTest::class,
        QueryTokenizerTest::class,
        QueryUtilsTest::class,

        // tests for com.orgzly.android.reminders
        ReminderServiceTest::class,

        // tests for com.orgzly.android.repos
        DataRepositoryTest::class,
        DirectoryRepoTest::class,
        DropboxRepoTest::class,
        LocalDbRepoTest::class,
        RepoFactoryTest::class,
        SyncTest::class,

        // tests for com.orgzly.android.ui
        ImageLoaderTest::class,

        // tests for com.orgzly.android.uiautomator
        ListWidgetTest::class,

        // tests for com.orgzly.android.usecase
        NoteUpdateDeadlineTimeTest::class,
        NoteUpdateScheduledTimeTest::class,

        // tests for com.orgzly.android.util
        AgendaUtilsTest::class,
        EncodingDetectTest::class,
        MiscUtilsTest::class,
        OrgFormatterLinkTest::class,
        OrgFormatterMiscTest::class,
        OrgFormatterSpeedTest::class,
        OrgFormatterStyleTextTest::class,
        UriUtilsTest::class,

        // tests for com.orgzly.android.ui.notes.query.agenda
        AgendaItemsTest::class
)
class AllTestSuite