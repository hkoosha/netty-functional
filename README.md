This library allows you to program netty handlers functionally instead of
implementing a full fledged channel handler or extending an adapter.


### How to Build

Put the jar of netty (netty 4.1.0 snapshot was tested) as netty.jar in libs
directory, or fix the build.gradle file to point to correct version of netty
on maven central.


### Example

```java


     channel -> channel
                .pipeline()
                .addLast(new HttpRequestEncoder())
                .addLast(new HttpResponseDecoder())
                .addLast(Inbound.hook(HttpResponse.class, (ctx, response) -> {
                    // response is of type HttpResponse.
                    // this is equivalent to implementing channelRead(...)
                    if(response.status().code != 200) {
                        log.error("did not get an OK reponse, closing {}", ctx.channel());
                        ctx.close();
                    }
                }))
                .addLast(Outbound.transform(String.class, (ctx, msg, p) -> {
                    // custom is of type String.
                    // this is equivalent to implementing write(...), processing
                    // message (transform it) and fire the result in outbound
                    // direction through pipeline.
                    return ctx.alloc()
                              .buffer(msg.length()) 
                              .writeBytes(msg.getBytes());
                }))
                .addLast(Event.rmSink(UserEvent.class, (ctx, event) -> {
                    // This handler catches user events of type UserEvent,
                    // but does NOT fire the event through the pipeline (sinks the event), 
                    // then removes itself (the rm part of the rmSink).
                    log.info("Hey! we got a: {}", event);
                }))
                ;
```


The code above is same as implementing three top level classes, or three verbose
inner anonymous classes.

Refactoring is easy, for instance Event.rmsink(...) can easily be written as a
top level class by extending RemovedEventSink:

```java

class MyEventHandler extends RemovedEventSink<T>(matcher) {

    @Override
    protected void event2(final ChannelHandlerContext ctx, final T event) throws Exception {

          log.info("Hey! we got a: {}", event);
    }
};
```


If later you decide you don't want to sink the event, or sink it conditionally,
and never remove your handler, going one step back again you can change 
`RemvoedEventSink` to `EventHook`, and implement the sink logic yourself. 
(Although you can sink conditionally already by extending `EventTransformer` and
return null as a flag to sink).



### Documentation? Unit Tests?

The project was accidentally removed by a `rm -rf ./*`, and the code is result
of decompiling and refactoring. I have to re-write unit tests and documentations
again.


### Warning:

Because of what was said above, this code is not tested. not even run once!
I just know it compiles, and right now I'm so depressed that I don't want to test
it anyway. But it should work. A compiled version (of the project that was removed)
is working already.
