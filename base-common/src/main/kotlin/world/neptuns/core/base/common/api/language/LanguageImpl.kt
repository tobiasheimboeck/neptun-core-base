package world.neptuns.core.base.common.api.language

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import world.neptuns.core.base.api.language.LangKey
import world.neptuns.core.base.api.language.Language
import world.neptuns.core.base.api.language.LineKey
import world.neptuns.core.base.api.language.properties.LanguageProperties
import java.text.MessageFormat
import java.util.*

class LanguageImpl(override val key: LangKey, override val messages: Map<LineKey, String>) : Language {

    private val defaultResolvers: MutableSet<TagResolver> = mutableSetOf(
        StandardTags.gradient(),
        StandardTags.color(),
        StandardTags.decorations(),
        StandardTags.clickEvent(),
        StandardTags.hoverEvent()
    )

    override fun lineAsString(properties: LanguageProperties, key: LineKey, vararg toReplace: Any): String {
        val content: String = this.messages[key] ?: return "$key not found..."
        return MessageFormat.format(content, *toReplace)
    }

    override fun line(properties: LanguageProperties, key: LineKey, vararg toReplace: TagResolver): Component {
        val builder = MiniMessage.builder()
        val message = this.messages[key] ?: return errorResult(builder)

        val tagResolver = TagResolver.builder()
            .resolvers(this.defaultResolvers)
            .resolvers(replacePlayerColors(properties))
            .resolvers(*toReplace)

        if (message.contains("<prefix")) tagResolver.resolver(replacePrefix(properties, message))

        return builder.tags(tagResolver.build()).build().deserialize(message)
    }

    override fun lines(properties: LanguageProperties, key: LineKey, vararg toReplace: TagResolver): List<Component> {
        val builder = MiniMessage.builder()
        val message = this.messages[key] ?: return listOf(errorResult(builder))
        if (!message.contains("\n")) return listOf(Component.text("$key is not a multiline message!", NamedTextColor.RED))

        val lines = message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val components: MutableList<Component> = ArrayList()

        for (line in lines) {
            val tagResolver = TagResolver.builder()
                .resolvers(this.defaultResolvers)
                .resolvers(replacePlayerColors(properties))
                .resolvers(*toReplace)

            if (line.contains("<prefix")) tagResolver.resolver(replacePrefix(properties, line))
            components.add(builder.tags(tagResolver.build()).build().deserialize(line))
        }

        return components
    }

    override fun displayName(properties: LanguageProperties, key: LineKey, vararg toReplace: TagResolver): Component {
        val builder = MiniMessage.builder()
        val message = this.messages[key] ?: return errorResult(builder)

        val tagResolver = TagResolver.builder()
            .resolvers(this.defaultResolvers)
            .resolvers(replacePlayerColors(properties))
            .resolvers(*toReplace)

        if (message.contains("<prefix")) tagResolver.resolver(replacePrefix(properties, message))
        return builder.tags(tagResolver.build()).build().deserialize("<!li>$message")
    }

    override fun lore(properties: LanguageProperties, key: LineKey, vararg toReplace: TagResolver): List<Component> {
        val builder = MiniMessage.builder()
        val message = this.messages[key] ?: return listOf(errorResult(builder))

        val components: MutableList<Component> = ArrayList()
        val tagResolver = TagResolver.builder()
            .resolvers(this.defaultResolvers)
            .resolvers(replacePlayerColors(properties))
            .resolvers(*toReplace)

        if (!message.contains("\n")) {
            if (message.contains("<prefix")) tagResolver.resolver(replacePrefix(properties, message))
            components.add(builder.tags(tagResolver.build()).build().deserialize("<!i>$message"))
        } else {
            for (line: String in message.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
                if (line.contains("<prefix")) tagResolver.resolver(replacePrefix(properties, line))
                components.add(builder.tags(tagResolver.build()).build().deserialize("<!i>$line"))
            }
        }

        return components
    }

    override fun hasMultipleLines(lineKey: LineKey): Boolean{
        return this.messages[lineKey]?.contains("\n") ?: false
    }

    private fun errorResult(builder: MiniMessage.Builder): Component {
        return builder.tags(TagResolver.builder().resolver(StandardTags.color()).build()).build()
            .deserialize("<red>$key not found...")
    }

    private fun replacePrefix(properties: LanguageProperties, content: String): TagResolver.Single {
        return if (content.contains("<prefix_")) {
            val unformattedPrefixName = content.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0].split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            val prefixName = unformattedPrefixName.substring(0, unformattedPrefixName.length - 1)
            val validPrefixName = prefixName.substring(0, 1).uppercase(Locale.getDefault()) + prefixName.substring(1)

            createPlaceholder(properties, prefixName, validPrefixName)
        } else if (content.contains("<prefix>")) {
            createPlaceholder(properties, null, null)
        } else {
            Placeholder.parsed("", "")
        }
    }

    private fun createPlaceholder(properties: LanguageProperties, prefixName: String?, validPrefixName: String?): TagResolver.Single {
        val lineKey = LineKey.coreKey(if (prefixName == null) "prefix.global" else "prefix")

        val prefixText = this.messages[lineKey]
            ?: throw NullPointerException("Prefix with key ${lineKey.asString()} not found!")
        if (prefixName != null) prefixText.replace("<prefix_text>", validPrefixName!!)

        return Placeholder.component(if (prefixName == null) "prefix " else "prefix_$prefixName", MiniMessage.builder()
            .tags(TagResolver.builder()
                .resolvers(replacePlayerColors(properties))
                .resolvers(this.defaultResolvers)
                .build())
            .build()
            .deserialize(prefixText))
    }

    private fun replacePlayerColors(properties: LanguageProperties): List<TagResolver.Single> {
        return listOf(
            Placeholder.parsed("pc", "<${properties.primaryColor.asHexString()}>"),
            Placeholder.parsed("sc", "<${properties.secondaryColor.asHexString()}>"),
            Placeholder.parsed("spc", "<${properties.separatorColor.asHexString()}>")
        )
    }

}