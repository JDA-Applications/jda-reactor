package club.minnced.jda.reactor;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.IEventManager;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.List;

public class ReactiveEventManager implements IEventManager {
    private final FluxProcessor<Event, ? super Event> processor;
    private final Scheduler scheduler;
    private final FluxSink<Event> eventSink;

    private boolean disposeOnShutdown = true;
    private boolean instance = true;

    public ReactiveEventManager() {
        this(FluxSink.OverflowStrategy.BUFFER);
    }

    public ReactiveEventManager(FluxSink.OverflowStrategy strategy) {
        this(EmitterProcessor.create(), Schedulers.newSingle("JDA-EventManager", true), strategy);
        scheduler.start();
    }

    public ReactiveEventManager(FluxProcessor<Event, ? super Event> processor, Scheduler scheduler, FluxSink.OverflowStrategy strategy) {
        this.processor = processor;
        this.scheduler = scheduler;
        this.eventSink = processor.sink(strategy);
    }

    public void setDisposeOnShutdown(boolean enabled) {
        this.disposeOnShutdown = enabled;
    }

    public void setInstance(boolean enabled) {
        this.instance = enabled;
    }

    @Override
    public void handle(Event event) {
        try {
            eventSink.next(event);
        } catch (Throwable t) {
            eventSink.next(new ExceptionEvent(event.getJDA(), t, false));
        }
        if (instance && event instanceof ShutdownEvent) {
            eventSink.complete();
            if (disposeOnShutdown)
                scheduler.dispose();
        }
    }

    public <T extends Event> Flux<T> on(Class<T> type) {
        return processor.publishOn(scheduler)
                .ofType(type);
    }

    @Override
    public void register(Object listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(Object listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Object> getRegisteredListeners() {
        throw new UnsupportedOperationException();
    }
}
