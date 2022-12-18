package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission

object CommandItemhunt {
    val main: CommandAPICommand = CommandAPICommand("itemhunt")
        .withSubcommands(
            ItemhuntStart.main,
            ItemhuntImport.main,
            ItemhuntNewWorld.main,
            ItemhuntSettings.main
        )
        .withPermission(CommandPermission.OP)
        .withAliases("ih")
}
