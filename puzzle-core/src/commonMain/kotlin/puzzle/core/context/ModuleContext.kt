package puzzle.core.context

class ModuleContext : Context() {

    override lateinit var parent: ProjectContext

    lateinit var name: String

    lateinit var files: List<FileContext>

    lateinit var group: List<String>
}