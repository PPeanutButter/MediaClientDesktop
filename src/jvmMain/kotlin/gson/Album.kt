package gson

import java.util.regex.Pattern

data class Album (
    val name: String,// "NAS600/Friends"
    val length: Int,// 0
    val lasts: Int,// -1
    val bitrate: String,// ""
    val desc: String,// "Fri Sep  2 14:09:23 2022"
    val type: String,// "Directory"
    val mime_type: String,// "application/octet-stream"
    val watched: String,// "watched"
    val score: Int,// 0
    val title: String,// "老友记(1994)"
    val bookmark_state: String// "bookmark_add"
){
    val displayTitle: String get() = this.title.getDisplayTitle()
    val relativePath: String get() = this.name
    val hashKey: Int get() = this.name.hashCode()

    companion object {
        val Example = Album(
            name = "NAS600/Friends",
            length = 0,
            lasts = -1,
            bitrate = "",
            desc = "Fri Sep  2 14:09:23 2022",
            type = "Directory",
            mime_type = "application/octet-stream",
            watched = "watched",
            score = 0,
            title = "老友记(1994)",
            bookmark_state = "bookmark_add"
        )
    }

    private fun String.getDisplayTitle(): String{
        Pattern.compile("(.*)\\(\\d{4}\\)", Pattern.MULTILINE).matcher(this).apply { this.find() }.also {
            return if (it.groupCount() == 1)
                it.group(1)?:""
            else this
        }
    }

    override fun toString(): String {
        return "Album(title=$displayTitle, path=${relativePath})"
    }

    constructor() : this(
        name = "NAS600/Friends",
        length = 0,
        lasts = -1,
        bitrate = "",
        desc = "Fri Sep  2 14:09:23 2022",
        type = "Directory",
        mime_type = "application/octet-stream",
        watched = "watched",
        score = 0,
        title = "老友记(1994)",
        bookmark_state = "bookmark_add"
    )
}
