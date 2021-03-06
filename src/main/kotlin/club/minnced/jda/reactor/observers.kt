/*
 * Copyright 2019  Florian Spieß and the contributors of jda-reactor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.minnced.jda.reactor

import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent
import net.dv8tion.jda.api.events.channel.text.update.GenericTextChannelUpdateEvent
import net.dv8tion.jda.api.events.channel.voice.update.GenericVoiceChannelUpdateEvent
import net.dv8tion.jda.api.events.guild.GenericGuildEvent
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent
import net.dv8tion.jda.api.events.message.GenericMessageEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.user.GenericUserEvent
import reactor.core.publisher.Flux

// Guild events

inline fun <reified T : GenericGuildEvent> Guild.on() = on(T::class.java)

fun <T : GenericGuildEvent> Guild.on(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.guild.idLong == id }
}

inline fun <reified T : GenericGuildUpdateEvent<*>> Guild.onUpdate() = onUpdate(T::class.java)

fun <T : GenericGuildUpdateEvent<*>> Guild.onUpdate(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.guild.idLong == id }
}

 // User events

inline fun <reified T : GenericUserEvent> User.on() = on(T::class.java)

fun <T : GenericUserEvent> User.on(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.user.idLong == id }
}

inline fun <reified T : GenericGuildMemberEvent> Member.on() = on(T::class.java)

fun <T : GenericGuildMemberEvent> Member.on(type: Class<T>): Flux<T> {
    val guildId = this.guild.idLong
    val userId = this.idLong
    return guild.on(type).filter { it.guild.idLong == guildId } .filter { it.user.idLong == userId }
}

 // Channel Updates

inline fun <reified T : GenericTextChannelUpdateEvent<*>> TextChannel.onUpdate() = onUpdate(T::class.java)

fun <T : GenericTextChannelUpdateEvent<*>> TextChannel.onUpdate(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.channel.idLong == id }
}

inline fun <reified T : GenericVoiceChannelUpdateEvent<*>> VoiceChannel.onUpdate() = onUpdate(T::class.java)

fun <T : GenericVoiceChannelUpdateEvent<*>> VoiceChannel.onUpdate(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.channel.idLong == id }
}

inline fun <reified T : GenericCategoryUpdateEvent<*>> Category.onUpdate() = onUpdate(T::class.java)

fun <T : GenericCategoryUpdateEvent<*>> Category.onUpdate(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.category.idLong == id }
}

 // Message events

inline fun <reified T : GenericMessageEvent> Message.on() = on(T::class.java)

fun <T : GenericMessageEvent> Message.on(type: Class<T>): Flux<T> {
    val id = this.idLong
    return jda.on(type).filter { it.messageIdLong == id }
}

fun MessageChannel.onMessage(): Flux<MessageReceivedEvent> {
    val id = this.idLong
    return jda.on(MessageReceivedEvent::class.java).filter { it.channel.idLong == id }
}